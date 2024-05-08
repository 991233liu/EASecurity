/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.framework.authentication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.easecurity.core.authentication.JWT;
import com.easecurity.core.authentication.JWTExpirationException;
import com.easecurity.core.authentication.UserDetails;
import com.easecurity.framework.EaSecurityConfiguration;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.SignedJWT;

/**
 * 登录相关服务
 *
 */
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private EaSecurityConfiguration eaSecurityConfiguration;

    static ThreadLocal<UserDetails> userDetails = new ThreadLocal<UserDetails>();

    public LoginService(EaSecurityConfiguration eaSecurityConfiguration) {
	this.eaSecurityConfiguration = eaSecurityConfiguration;
    }

    /**
     * 获取当前登录人
     * 
     * @param request
     * @param publicKeyFile RSA公钥证书文件（Base64编码格式）
     * 
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws JWTExpirationException
     */
    public JWT getCurrentUserJWT(HttpServletRequest request, File publicKeyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JWTExpirationException {
	return getCurrentUserJWT(request, getRSAPublicKey(publicKeyFile));
    }

    /**
     * 获取当前登录人
     * 
     * @param request
     * @param publicKey RSA公钥证书（Base64编码格式）
     * 
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws JWTExpirationException
     */
    public JWT getCurrentUserJWT(HttpServletRequest request, String publicKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JWTExpirationException {
	return getCurrentUserJWT(request, getRSAPublicKey(publicKey));
    }

    /**
     * 获取当前登录人
     * 
     * @param request
     * @param publicKey RSA公钥证书
     * 
     * @return
     * @throws IOException
     * @throws JWTExpirationException
     */
    public JWT getCurrentUserJWT(HttpServletRequest request, RSAPublicKey publicKey) throws IOException, JWTExpirationException {
	String at = getAccessToken(request);
	if (at != null && !at.isEmpty()) { // 基于OAT的jwt
	    return getCurrentUserJWT(null, at, publicKey);
	} else { // 基于cookie的jwt
	    Cookie[] cookies = request.getCookies();
	    return getCurrentUserJWT(cookies, null, publicKey);
	}
    }

    /**
     * 获取当前登录人
     * 
     * @param cookies
     * @param publicKey RSA公钥证书
     * 
     * @return
     * @throws IOException
     * @throws JWTExpirationException
     */
    public JWT getCurrentUserJWT(Cookie[] cookies, String accessToken, RSAPublicKey publicKey) throws IOException, JWTExpirationException {
	if (cookies != null) {
	    for (Cookie cookie : cookies) {
		if (cookie.getName().equals("EASECURITY_S")) {
		    String uri = eaSecurityConfiguration.server.getUrl() + "/jwt/currentUserJWT";
		    return _getCurrentUserJWT(uri, cookie, null, publicKey);
		}
	    }
	}
	String uri = eaSecurityConfiguration.server.getUrl() + "/jwt/getJWT";
	return _getCurrentUserJWT(uri, null, accessToken, publicKey);
    }

    /**
     * 
     * 从文件中读取RSAPublicKey（Base64编码格式）
     * 
     * @param publicKeyFile
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public RSAPublicKey getRSAPublicKey(File publicKeyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	BufferedReader br = null;
	StringBuilder sb = new StringBuilder();
	try {
	    br = new BufferedReader(new InputStreamReader(new FileInputStream(publicKeyFile)));
	    String line;
	    while ((line = br.readLine()) != null) {
		if (line.indexOf("BEGIN PUBLIC KEY") > -1)
		    continue;
		if (line.indexOf("END PUBLIC KEY") > -1)
		    break;
		sb.append(line.trim());
	    }
	} finally {
	    if (br != null)
		try {
		    br.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	String str = sb.toString().trim();
	return getRSAPublicKey(str);
    }

    /**
     * 
     * 创建RSAPublicKey（Base64编码格式）
     * 
     * @param publicKey
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public RSAPublicKey getRSAPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
	X509EncodedKeySpec keySpec = new X509EncodedKeySpec(new Base64(publicKey).decode());
	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    /**
     * 从本地线程中获取当前登录人授权信息
     * 
     * @param session
     * @return
     */
    public UserDetails getLocalUserDetails(HttpSession session) {
	if (session != null) {
	    UserDetails userDetails = (UserDetails) session.getAttribute("userDetails");
	    if (userDetails != null)
		return userDetails;
	}
	return userDetails.get();
    }

    private JWT _getCurrentUserJWT(String uri, Cookie cookie, String accessToken, RSAPublicKey publicKey) throws IOException, JWTExpirationException {
	BufferedReader br = null;
	try {
	    HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
	    connection = eaSecurityConfiguration.setDefaultConfig(connection);
	    connection.setRequestMethod("GET");
	    if (cookie != null)
		connection.addRequestProperty("Cookie", cookie.getName() + "=" + cookie.getValue());
	    if (accessToken != null)
		connection.addRequestProperty("access_token", accessToken);
	    connection.connect();
	    int state = connection.getResponseCode();
	    if (state == 200) { // 已登录用户
		StringBuilder sb = new StringBuilder();
		String line;
		br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while ((line = br.readLine()) != null) {
		    sb.append(line);
		}
		String str = sb.toString().trim();
		return getUserJWT(str, publicKey);
	    } else if (state == 203) { // 匿名访问
		return null;
	    } else { // 服务器返回错误
		log.error("获取当前登录人时，服务器报错，getResponseCode:{}", state);
		return null;
	    }
	} catch (IOException e) {
	    log.error("获取当前登录人时，数据流读取异常:", e);
	    throw e;
	} finally {
	    if (br != null)
		try {
		    br.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
    }

    /**
     * 获取有效的用户认证信息
     * 
     * @param jwtStr    JWT密文
     * @param publicKey RSA公钥
     * @return
     * @throws JWTExpirationException
     * @throws IOException
     */
    public JWT getUserJWT(String jwtStr, RSAPublicKey publicKey) throws JWTExpirationException, IOException {
	try {
	    String str = decode(jwtStr, publicKey);
	    @SuppressWarnings("unchecked")
	    HashMap<String, Object> jwtMap = JSON.parseObject(str, HashMap.class);
	    JWT jwt = new JWT(jwtMap);
	    jwt.parsedStr = jwtStr;
	    if (jwt.verify()) {
		if (jwtMap.get("userDetails") != null) {
		    jwt.userDetails = (UserDetails) JSON.parseObject((String) jwtMap.get("userDetails"), UserDetails.class);
		    return jwt;
		}
	    } else {
		throw new JWTExpirationException(jwt.sub + " tocken has expiration, exp is:" + jwt.exp);
	    }
	} catch (ParseException | JOSEException e) {
	    log.error("JWT 解码异常，jwtStr=" + jwtStr, e);
	    throw new IOException("JWT 解码异常", e);
	}
	return null;
    }
    
    /**
     * 获取AccessToken（如果存在的话）
     * 
     * @param request
     * @return
     */
    public String getAccessToken(HttpServletRequest request) {
	String accessToken = request.getHeader("authorization");
	if (accessToken != null && accessToken.indexOf("Bearer") > -1) {
	    return accessToken.substring(accessToken.indexOf("Bearer") + 6).trim();
	} else if (request.getHeader("access_token") != null && !request.getHeader("access_token").trim().isEmpty()) {
	    return request.getHeader("access_token").trim();
	} else if (request.getParameter("access_token") != null && !request.getParameter("access_token").trim().isEmpty()) {
	    return request.getParameter("access_token").trim();
	}
	return null;
    }

    /**
     * 解码+验签
     */
    private String decode(String jwtStr, RSAPublicKey publicKey) throws ParseException, JOSEException {
	try {
	    SignedJWT sjwt = SignedJWT.parse(jwtStr);
	    JWSVerifier verifier = new RSASSAVerifier(publicKey);
	    if (sjwt.verify(verifier)) {
		return sjwt.getPayload().toString();
	    }
	} catch (ParseException e) {
	    throw e;
	} catch (JOSEException e) {
	    throw e;
	}
	return null;
    }
}
