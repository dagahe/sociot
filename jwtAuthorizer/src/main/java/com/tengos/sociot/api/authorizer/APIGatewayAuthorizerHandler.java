/*
* Copyright 2015-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at
*
*     http://aws.amazon.com/apache2.0/
*
* or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package com.tengos.sociot.api.authorizer;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.json.Jackson;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tengos.sociot.api.authorizer.io.AuthPolicy;
import com.tengos.sociot.api.authorizer.io.TokenAuthorizerContext;
import com.tengos.sociot.api.persistence.model.DAOFactory;
import com.tengos.sociot.api.persistence.model.DAOFactory.DAOType;
import com.tengos.sociot.api.persistence.model.exception.DAOException;
import com.tengos.sociot.api.persistence.model.user.User;
import com.tengos.sociot.api.persistence.model.user.UserDAO;

/**
 * Handles IO for a Java Lambda function as a custom authorizer for API Gateway
 *
 * @author Jack Kohn
 *
 */
public class APIGatewayAuthorizerHandler implements RequestHandler<TokenAuthorizerContext, AuthPolicy> {

	private final UserDAO userDAO = DAOFactory.getUserDAO(DAOType.DynamoDB);
	private final Base64 decoder = new Base64(true);
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public AuthPolicy handleRequest(TokenAuthorizerContext input, Context context) {

		String token = input.getAuthorizationToken();
		context.getLogger().log("Token received: " + token);
		String principalId = null;
		try {
			principalId = getSubjectFromJwtClaims(token);
		} catch (IOException e) {
			throw new RuntimeException("Malformed token, a subject field is mandatory");
		}

		// subject
		context.getLogger().log("PrincipalId: " + principalId);

		// find the secret before
		User user = null;
		try {
			user = userDAO.getUserById(principalId);
		} catch (DAOException e) {
			throw new RuntimeException("Unable to recover a user");
		}

		String secretKey = user.getToken();
		context.getLogger().log("SecretKey: " + secretKey);

		// Verify the token received
		JWTVerifier verifier = new JWTVerifier(secretKey);
		try {
			Map<String, Object> map = verifier.verify(token);
		} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException
				| IOException | JWTVerifyException e) {

			throw new RuntimeException("Unathorized access due: " + e.getMessage());
		}

		context.getLogger().log("Token validated, generating policy on the fly");

		String methodArn = input.getMethodArn();
		String[] arnPartials = methodArn.split(":");
		String region = arnPartials[3];
		String awsAccountId = arnPartials[4];
		String[] apiGatewayArnPartials = arnPartials[5].split("/");
		String restApiId = apiGatewayArnPartials[0];
		String stage = apiGatewayArnPartials[1];
		String httpMethod = apiGatewayArnPartials[2];
		String resource = ""; // root resource
		if (apiGatewayArnPartials.length == 4) {
			resource = apiGatewayArnPartials[3];
		}

		AuthPolicy policy = new AuthPolicy(principalId,
				AuthPolicy.PolicyDocument.getAllowAllPolicy(region, awsAccountId, restApiId, stage));
		context.getLogger().log(Jackson.toJsonPrettyString(policy.getPolicyDocument()));
		return policy;
	}

	@SuppressWarnings("static-access")
	private String getSubjectFromJwtClaims(String token) throws JsonParseException, JsonMappingException, IOException {
		String sub = null;
		if (token != null || !"".equals(token)) {
			String[] pieces = token.split("\\.");
			if (pieces.length >= 2) {
				String jsonString = new String(decoder.decodeBase64(pieces[1]), "UTF-8");
				JsonNode payload = mapper.readValue(jsonString, JsonNode.class);
				sub = payload.findValue("sub").asText(null);
			}
		}
		return sub;
	}

}
