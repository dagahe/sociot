package com.tengos.sociot.api;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.amazonaws.util.Base16Lower;

public class AwsSignRequest {

	private static final String HMAC_SHA256 = "HmacSHA256";
	private static final String AWS4_HMAC_SHA256 = "AWS4-HMAC-SHA256\n";
	private static final String AWS4_REQUEST = "/aws4_request";
	private static final String AWS4_HMAC_SHA256_CREDENTIAL = "AWS4-HMAC-SHA256 Credential=";
	private static final String SIGNED_HEADERS = ", SignedHeaders=";
	private static final String SIGNATURE = ", Signature=";
	private static final String SHA_256 = "SHA-256";
	private static final String AWS4 = "AWS4";

	private Calendar calendar;
	private DateFormat dfmT;
	private DateFormat dfmD;
	private String host;
	private String region;
	private String service;
	private String accessKey;
	private String secretKey;

	public AwsSignRequest(String accessKey, String secretKey, String region, String service, String url) {
		super();
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.region = region;
		this.service = service;
		this.calendar = Calendar.getInstance();
		this.dfmT = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
		this.dfmT.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.dfmD = new SimpleDateFormat("yyyyMMdd");
		this.dfmD.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.host = URI.create(url).getHost();

	}

	/**
	 * Create a canonical request from the request parameters following
	 * 
	 * http://docs.aws.amazon.com/general/latest/gr/sigv4-create-canonical-
	 * request.html
	 * 
	 * <pre>
	 * CanonicalRequest=HTTPRequestMethod+'\n'+CanonicalURI+'\n'+
	 * CanonicalQueryString+'\n'+CanonicalHeaders+'\n'+SignedHeaders+'\n'+
	 * HexEncode(Hash(RequestPayload))
	 * 
	 * <pre>
	 *
	 * @param method:
	 * @param CanonicalReequest
	 * @param uri
	 * @param queryString
	 * @param headers
	 * @param signedHeaders
	 * @param payload
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */

	public String createCanonicalRequest(String method, String uri, String queryString,
			Map<String, String> headersSorted, String signedHeaders, String payload)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// To hold appended headers key-values pair
		StringBuffer headers = new StringBuffer();
		for (Entry<String, String> entry : headersSorted.entrySet()) {
			headers.append(entry.getKey() + ":" + entry.getValue() + "\n");
		}
		String canonicalRequest = method + "\n" + uri + "\n" + queryString + "\n" + headers.toString() + "\n"
				+ signedHeaders + "\n" + toBase16(hash(payload));
		return canonicalRequest;

	}

	public String getAuthorizationHeader(String method, String uri, String queryString,
			Map<String, String> headersInput, String payload) throws Exception {
		Map<String, String> lowerAndSortedHeaders = lowerAndSortedHeaders(headersInput);
		String signedHeaders = signedHeaders(lowerAndSortedHeaders);
		String canonicalRequest = createCanonicalRequest(method, uri, queryString, lowerAndSortedHeaders, signedHeaders,
				payload);
		String stringToSign = createStringToSign(canonicalRequest);
		String signature = sign(stringToSign);
		String autorizationHeader = AWS4_HMAC_SHA256_CREDENTIAL + this.accessKey + "/" + getCredentialScope()
				+ SIGNED_HEADERS + signedHeaders + SIGNATURE + signature;
		return autorizationHeader;

	}

	public String signedHeaders(Map<String, String> headers) {
		// To hold appended headers key
		StringBuffer signedHeaders = new StringBuffer();
		for (Entry<String, String> entry : headers.entrySet()) {
			signedHeaders.append(entry.getKey() + ";");
		}
		if (signedHeaders.length() > 0)
			signedHeaders.deleteCharAt(signedHeaders.length() - 1);
		return signedHeaders.toString();
	}

	public Map<String, String> lowerAndSortedHeaders(Map<String, String> headersInput) {
		SortedMap<String, String> map = new TreeMap<String, String>();
		for (Entry<String, String> entry : headersInput.entrySet()) {
			map.put(entry.getKey().toLowerCase(), entry.getValue().trim());
		}
		return map;
	}

	private String getTimestamp() {
		return this.dfmT.format(calendar.getTime());
	}

	private String getDate() {
		return this.dfmD.format(calendar.getTime());
	}

	private String createStringToSign(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return AWS4_HMAC_SHA256 + getTimestamp() + "\n" + getCredentialScope() + "\n" + toBase16(hash(data));
	}

	private String getCredentialScope() {
		return getDate() + "/" + this.region + "/" + this.service + AWS4_REQUEST;
	}

	private byte[] getSignatureKey() throws Exception {
		byte[] kSecret = (AWS4 + secretKey).getBytes("UTF8");
		byte[] kDate = hmacSHA256(getDate(), kSecret);
		byte[] kRegion = hmacSHA256(region, kDate);
		byte[] kService = hmacSHA256(service, kRegion);
		byte[] kSigning = hmacSHA256(AWS4_REQUEST.substring(1), kService);
		return kSigning;
	}

	private String sign(String stringToSign) throws Exception {
		return Hex.encodeHexString(hmacSHA256(stringToSign, getSignatureKey()));
	}

	private static byte[] hmacSHA256(String data, byte[] key) throws Exception {
		String algorithm = HMAC_SHA256;
		Mac mac = Mac.getInstance(algorithm);
		mac.init(new SecretKeySpec(key, algorithm));
		return mac.doFinal(data.getBytes("UTF8"));
	}

	private String toBase16(byte[] data) {
		return Base16Lower.encodeAsString(data);
	}

	private byte[] hash(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance(SHA_256);
		md.update(data.getBytes("UTF-8"));
		byte[] digest = md.digest();
		return digest;
	}

}
