package com.tengos.sociot.api;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AwsSignRequestTest {

	private AwsSignRequest signer;

	private String accessKeyId = "979uwkjoiuo";
	private String secretKey = "iuw7789898798q";
	private String region = "eu-west-1";
	private String service = "iam";
	private String method;
	private String uri;
	private String queryString;
	private Map<String, String> headers;
	private String signedHeaders;
	private String payload;
	private String url;

	@Before
	public void setUp() throws Exception {
		url = "https://iam.amazonaws.com/?Action=ListUsers&Version=2010-05-08";
		signer = new AwsSignRequest(accessKeyId, secretKey, region, service, url);
		method = "GET";
		uri = "/";
		queryString = "Action=ListUsers&Version=2010-05-08";
		headers = new HashMap<String, String>();
		headers.put("Host", "iam.amazonaws.com");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		headers.put("My-Header2", "    a   b   c  ");
		headers.put("X-Amz-Date", "20150830T123600Z");
		headers.put("My-Header1", "    \"a   b   c\"");
		payload = "{}";
		signedHeaders = "content-type;host;my-header1;my-header2;x-amz-date";
	}

	@Test
	public void testCreateCanonicalRequest() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, String> lowerAndSortedHeaders = signer.lowerAndSortedHeaders(headers);
		String signedHeaders = signer.signedHeaders(lowerAndSortedHeaders);
		String canonicalRequest = signer.createCanonicalRequest(method, uri, queryString, lowerAndSortedHeaders,
				signedHeaders, payload);
		Assert.assertNotNull(canonicalRequest);
		Assert.assertTrue(canonicalRequest.contains(signedHeaders));
		System.out.println(canonicalRequest);
	}

	@Test
	public void testGetAuthorizationHeader() throws Exception {
		String authorization = signer.getAuthorizationHeader(method, uri, queryString, headers, payload);

		Assert.assertNotNull(authorization);
		Assert.assertTrue(authorization.contains(signedHeaders));
		System.out.println(authorization);

	}

}
