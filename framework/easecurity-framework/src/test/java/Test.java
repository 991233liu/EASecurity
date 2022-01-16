import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Enumeration;

import javax.crypto.Cipher;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class Test {

    @SuppressWarnings({ "rawtypes", "unused" })
    public static void main(String[] args) throws Exception {
	
	System.out.println(new Date());
	System.out.println(new Date(1642349663));
	System.out.println(Instant.now());
	System.out.println(Instant.ofEpochMilli(1642349663));
	System.out.println(Instant.ofEpochSecond(1642349663));
	System.out.println(Instant.ofEpochSecond(1642349663).getEpochSecond());

	FileInputStream in = new FileInputStream("D:/jwt.keystore");
	KeyStore keyStore = KeyStore.getInstance("JKS"); // JKS???
	keyStore.load(in, "SecurityCentre".toCharArray());
	Enumeration aliasEnum = keyStore.aliases();
	String keyAlias = "";
	while (aliasEnum.hasMoreElements()) {
	    keyAlias = (String) aliasEnum.nextElement();
	    System.out.println("别名:" + keyAlias);
	}
	Certificate ce = keyStore.getCertificate(keyAlias);
	PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();
	System.out.println("publicKey:" + publicKey);
	Base64 publick = Base64.encode(publicKey.getEncoded());
	System.out.println("public:" + publick);
	// 加载私钥,这里填私钥密码
	PrivateKey privateKey = ((KeyStore.PrivateKeyEntry) keyStore.getEntry(keyAlias, new KeyStore.PasswordProtection("SecurityCentre".toCharArray()))).getPrivateKey();
	// 加载私钥另一写法
	// PrivateKey privateKey = (PrivateKey)
	// keyStore.getKey(keyAlias，"123456".toCharArray());
	// base64输出私钥
	Base64 strKey = Base64.encode(privateKey.getEncoded());
	System.out.println("privateKey:" + strKey);
	// 测试签名
//	String sign = Base64.encodeBase64String(sign("测试msg".getBytes(),privateKey, "SHAL1withRSA",null));
	byte[] jiami = encrypt("01510136|15000364728".getBytes(), publicKey);
//	System.out.println("加密后"+Base64.encodeBase64String(jiami));
	// 测试验签
//	boolean verfi = verify("01510136|1508364728".getBytes(),Base64.decodeBase64(sign), publicKkey, "SHA1withRsA" ,null);
	// String s = privateDecrypt(我是一个人" , privateKey ) ;
	byte[] jiemi = decrypt(jiami, privateKey);
	System.out.println("解密后:" + new String(jiemi));

	String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3FlqJr5TRskIQIgdE3Dd\n" + "7D9lboWdcTUT8a+fJR7MAvQm7XXNoYkm3v7MQL1NYtDvL2l8CAnc0WdSTINU6IRv\n"
		+ "c5Kqo2Q4csNX9SHOmEfzoROjQqahEcve1jBXluoCXdYuYpx4/1tfRgG6ii4Uhxh6\n" + "iI8qNMJQX+fLfqhbfYfxBQVRPywBkAbIP4x1EAsbC6FSNmkhCxiMNqEgxaIpY8C2\n"
		+ "kJdJ/ZIV+WW4noDdzpKqHcwmB8FsrumlVY/DNVvUSDIipiq9PbP4H99TXN1o746o\n" + "RaNa07rq1hoCgMSSy+85SagCoxlmyE+D+of9SsMY8Ol9t0rdzpobBuhyJ/o5dfvj\n" + "KwIDAQAB";
//	String key = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAoapulVMVY78PZ/pEsC3YMAEV1Wb93Q3JAF+mg8TMDdwTN5EUOAzErai4Xea7w8VuIBvvvHoYh8T6Kjl+LdozqHNlXiVGlc+y8EDYYl6DJKN3sm1LeBhVwlwvwbcPfmVNAMxCie8JTJf6Y78MISHkFsI6lhaiWYdyUfrBqaDPT/VXO5EeRclztBoSh0rHAeLdfJJ3/bW3thay3DQq7HQuYgjGRt8U+L6E6UT8WiqCL5NNu8Rul3gccDA3FJCFTS0QsPB9Bl9NP90pWlpBAUXoQ8uV5wvbwDWb3BeN0M5G6U1Cp9zIG6TwKr8UEMBO7QVWvCDrBfOkJ4buqEbaDmYN6iYRoIDwg+TUnyFnejV0yH0nf8p6t8tN6Sc09qqWbPG5UV+PNEtaqQCCxZgk3doig9QKxBrPTuY+Qid/4KNkWT4NqH9L1k3ifKS1jJebkJTZTT3eIiJCF8q1a9BTDWeNlhv0jEXGQ2E8DKcTUvfdoaOmit0kHQkX0y/ndjpoF0aOtq1RCyMDDzjsfjyGULYQpqShqP7HvusxvhWgSfJXSga5tgMWQQ5HLd8WEEIdNyh3R/sgsJUWbd2hzdN8Ldj9tejV25/yPsukOkJKLqb/H7AqXcd1FyLEOykDoI5RAuy+gQNs1RdaYJlS4xpPMhXHmewyZWFVEt77+haX+XqGUusCAwEAAQ==";
	byte[] keyBytes;
	keyBytes = new Base64(key).decode();
	X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	PublicKey publicKey3 = keyFactory.generatePublic(keySpec);

	String str = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJTZWN1cml0eUNlbnRyZSIsInN1YiI6ImxpdWx1ZmVuZyIsImV4cCI6MTY0MjMzMDk5NCwiaWF0IjoxNjQyMjk0OTk0LCJzY29wZSI6IntcImFjY291bnRcIjpcImxpdWx1ZmVuZ1wiLFwiaWRcIjpcImVmZDExMTFcIixcImlkZW50aXRpZXNcIjpcIntcXFwidXNlclxcXCI6XFxcImVmZDExMTFcXFwiLFxcXCJwb3N0c1xcXCI6XFxcIjEsMlxcXCIsXFxcIm9yZ1xcXCI6XFxcIjQsM1xcXCJ9XCJ9In0.e16RXq_kdOfYSuFwbcy5I0lkbxJI1ZI9aSTLLM_XzEtpVGvST90yIdFeWxn26lejlYWy1MndFISAW9759jMWY4ZWiL0nQIdOHOgCXgl8hZdlC2mq_-p3__jfpxj_7GOL3jy6E1smyhSPL55agw9YtBZh3Xn5ZGQL4Xqb2Er_zv3NuZ20cgHzVJUeH3TEZpnl3e3Gdymu5TL8z4203zBfIr9f7piaNdUzRhjvAl1AXHhIkv3sy0PSvzAmeZHXE_Vhq3QHCjmUqREhyULxfF7gCc4AEzfTQa0DcP4WtlLgWNErvReJCUga33FPh4zAYaLy7EC04_qIZwDYlBPVAIH2VEOMXalhgLukh_6vnb55H0X2BFs0Hk6pB2lVAz-vqVVBsvMxDf0NiphK2g14kfP0ewVsEs8P8GsqZEqBSxMILT4HHR_RWG3T1mkrzo7oTuNo7dAql5DgpOtdYR4VD8OeVBLDnUXWTLPkZVtKXht3QdAt_qG8io5RTKo7sFdYXBMeS9siO7VI5hPvM-hbvYBRI9ZBpVRL7SUXJAqTjU2QJvYrGw0LnNqCmO_y0QeAmvpPNS-7ENPVfT9uUY-O5sCj4A-3Ouo09AO96C9FULJgg1avmAAW0KOTNpG9Je2NzcWThEVj9noKUPYysAOP5h919mGo_BDLJ_fYpk020JyoBFo";
	SignedJWT sjwt;
	try {
	    sjwt = SignedJWT.parse(str);
	    JWTClaimsSet claims = sjwt.getJWTClaimsSet();
	    RSAPublicKey publicKey2 = (RSAPublicKey) publicKey3;
//	    RSAPublicKey publicKey2 = (RSAPublicKey) publicKey;
	    JWSVerifier verifier = new RSASSAVerifier(publicKey2);
	    sjwt.verify(verifier);
	    sjwt.getPayload().toJSONObject();
	    sjwt.getPayload().toString();
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	// 从指定流中获取数据并生成证书
	X509Certificate cert = null;
	try {
	    File file = new File("D:\\work\\code\\oschina\\EASecurity\\develop\\core\\easecurity-core-centre\\src\\main\\resources\\app.pub");
	    CertificateFactory cf = CertificateFactory.getInstance("X.509");
	    cert = (X509Certificate) cf.generateCertificate(new FileInputStream(file));
	    PublicKey publicKey4 = cert.getPublicKey();
	    System.out.println("-----------------获取的公钥--------------------");
	    System.out.println(publicKey);
//	publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
//	System.out.println(" -----------------Base64encode后的公钥--------------------");System.out.println(publicKeyString);
	} catch (CertificateException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

    }

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";

    /* 验签 */
    public static boolean verify(byte[] message, byte[] signMessage, PublicKey publicKey, String algorithm, String provider) throws Exception {
	Signature signature;
	if (null == provider || provider.length() == 0) {
	    signature = Signature.getInstance(algorithm);
	} else {
	    signature = Signature.getInstance(algorithm, provider);
	}
	signature.initVerify(publicKey);
	signature.update(message);
	return signature.verify(signMessage);
    }

    /*** 签名 */
    public static byte[] sign(byte[] message, PrivateKey privatekey, String algorithm, String provider) throws Exception {
	Signature signature;
	if (null == provider || provider.length() == 0) {
	    signature = Signature.getInstance(algorithm);
	} else {
	    signature = Signature.getInstance(algorithm, provider);
	}
	signature.initSign(privatekey);
	signature.update(message);
	return signature.sign();
    }

    // 公钥加密
    public static byte[] encrypt(byte[] content, PublicKey publicKey) throws Exception {
	Cipher cipher = Cipher.getInstance("RSA"); // java默认RSA""="RSA/ECB/PKCS1Padding"
	cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	return cipher.doFinal(content);
    }

    // 私钥解密
    public static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception {
	Cipher cipher = Cipher.getInstance("RSA");
	cipher.init(Cipher.DECRYPT_MODE, privateKey);
	return cipher.doFinal(content);
    }

}
