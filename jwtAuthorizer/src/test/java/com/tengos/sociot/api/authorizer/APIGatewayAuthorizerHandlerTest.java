package com.tengos.sociot.api.authorizer;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.auth0.jwt.JWTSigner;
import com.tengos.sociot.api.authorizer.io.TokenAuthorizerContext;
import com.tengos.sociot.api.persistence.model.user.User;

public class APIGatewayAuthorizerHandlerTest {

	private APIGatewayAuthorizerHandler handler;
	private User user;

	@Before
	public void setUp() throws Exception {
		handler = new APIGatewayAuthorizerHandler();
		user = new User();
		user.setUserId("xxx");
		user.setName("XXX");
		user.setEmail("xxx@xxx.com");
		user.setToken("00000");

	}

	@Test
	public void testHandler() {
		TokenAuthorizerContext tokenContext = new TokenAuthorizerContext();
		tokenContext.setType("JWT");
		tokenContext.setMethodArn("arn:aws:execute-api:eu-west-1:873226932616:taabrh2kd8/*/POST/sensor/*");
		tokenContext.setAuthorizationToken(createJWTToken());
		handler.handleRequest(tokenContext, new Context() {

			@Override
			public int getRemainingTimeInMillis() {
				return 0;
			}

			@Override
			public int getMemoryLimitInMB() {
				return 0;
			}

			@Override
			public LambdaLogger getLogger() {
				return new LambdaLogger() {

					@Override
					public void log(String message) {
						System.err.println(message);

					}
				};
			}

			@Override
			public String getLogStreamName() {
				return null;
			}

			@Override
			public String getLogGroupName() {
				return null;
			}

			@Override
			public String getInvokedFunctionArn() {
				return null;
			}

			@Override
			public CognitoIdentity getIdentity() {
				return null;
			}

			@Override
			public String getFunctionVersion() {
				return null;
			}

			@Override
			public String getFunctionName() {
				return null;
			}

			@Override
			public ClientContext getClientContext() {
				return null;
			}

			@Override
			public String getAwsRequestId() {
				return null;
			}
		});
	}

	private String createJWTToken() {
		JWTSigner signer = new JWTSigner(user.getToken());
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("iss", "http://10gos.com");
		claims.put("aud", "http://api.sociot.10gos.com");
		claims.put("sub", user.getUserId());
		return signer.sign(claims);
	}

}
