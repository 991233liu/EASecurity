/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.security.blowfish;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author liulf
 *
 */
public class BlowfishTest {

//    private static Base64.Decoder b64_d = Base64.getDecoder();
//    private static Base64.Encoder b64_e = Base64.getEncoder();

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	System.out.println(BlowfishBox.pbox_init[0]);
	System.out.println(BlowfishBox.pbox_init.length);
	System.out.println(BlowfishBox.sbox_init.length);
	System.out.println(BlowfishBox.sbox_init[0].length);
	System.out.println(BlowfishBox2.pbox_init[0]);
	System.out.println(BlowfishBox2.pbox_init.length);
	System.out.println(BlowfishBox2.sbox_init.length);
	System.out.println(BlowfishBox2.sbox_init[0].length);
//	for (int a : BlowfishBox2.sbox_init[0]) {
//	    System.out.println(a);
//	}

	String key = "testKey111111111";
	BlowfishECB be = new BlowfishECB(key, BlowfishBox2.pbox_init, BlowfishBox2.sbox_init);
	String buffer = "1234567890abcdefghijklmnopqrstuvwxyz1231234567890abcdefghijklmnopqrstuvwxyz1231234567890abcdefghijklmnopqrstuvwxyz1231234567890abcdefghijklmnopqrstuvwxy"
		+ "z1231234567890abcdefghijklmnopqrstuvwxyz1231234567890abcdefghijklmnopqrstuvwxyz1231234567890abcdefghijklmnopqrstuvwxyz1231234567890abcdefghijklmnopqrstuvwxyz123";
	String str = be.encrypt(buffer);
	System.out.println("明文：" + buffer);
	System.out.println("密文：" + str);
	System.out.println("---大字符串10000万次加密耗时：");
	long start = System.currentTimeMillis();
	for (int i = 0; i < 10000; i++) {
	    be.encrypt(buffer);
	}
	System.out.println(System.currentTimeMillis() - start);
	start = System.currentTimeMillis();
	for (int i = 0; i < 10000; i++) {
	    be.decrypt(str);
	}
	System.out.println("---大字符串10000万次解码耗时：");
	System.out.println(System.currentTimeMillis() - start);
	System.out.println(be.weakKeyCheck());
//	System.out.println("---1");
//	long start = System.currentTimeMillis();
//	for (int i = 0; i < 10000; i++) {
//	    be.encrypt(buffer);
//	}
//	System.out.println(System.currentTimeMillis() - start);
//	String str = be.encrypt(buffer);
//	System.out.println(buffer);
//	System.out.println(str);
//	start = System.currentTimeMillis();
//	for (int i = 0; i < 10000; i++) {
//	    be.decrypt(str);
//	}
//	System.out.println(System.currentTimeMillis() - start);
//
//	// create a key generator based upon the Blowfish cipher
//	KeyGenerator keygenerator = KeyGenerator.getInstance("Blowfish");
//	SecureRandom random = new SecureRandom();
//	Random random2 = new Random();
//	keygenerator.init(256, random);
//
//	// create a key
//	SecretKey secretkey = keygenerator.generateKey();
//	byte bytes[] = secretkey.getEncoded();
//	System.out.println(Arrays.toString(bytes));
////	String strHexBytes =  Integer.toHexString(268435456);
////	String strHexBytes = DatatypeConverter.printInt(0x10000000);
////	double a=Math.random()*10;
//	for (int i = 0; i < 100; i++) {
//	    double a = random2.nextDouble() * 10;
////	    System.out.println(a);
//	    String strHexBytes = Double.toHexString(a);
//	    strHexBytes = strHexBytes.substring(strHexBytes.indexOf(".") + 1);
//	    if (strHexBytes.startsWith("0") || strHexBytes.length() < 8)
//		System.out.println("----------");
//	    else
//		System.out.print("0x" + strHexBytes.substring(0, 8));
//	    System.out.print(", ");
//	}
//	System.out.println("");
//
//	// create a cipher based upon Blowfish
//	Cipher cipher = Cipher.getInstance("Blowfish");
//
//	// initialise cipher to with secret key
//	cipher.init(Cipher.ENCRYPT_MODE, secretkey);
//
//	// get the text to encrypt
//	String inputText = "MyTextToEncrypt";
//
//	// encrypt message
//	byte[] encrypted = cipher.doFinal(inputText.getBytes());
//
//	System.out.println(encrypted.length);
//
//	String Key = "Something";
//	byte[] KeyData = Key.getBytes();
//	SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
//	cipher = Cipher.getInstance("Blowfish");
//	cipher.init(Cipher.ENCRYPT_MODE, KS);
//	encrypted = cipher.doFinal(inputText.getBytes());
//
//	System.out.println(encrypted.length);
//
//	main2(args);
//	main3(args);

//	byte[] buf;
////	byte[] b = Base642.encode(buffer).getBytes();
//	byte[] b = buffer.getBytes();
//	int nLen = b.length % 8 == 0 ? 0 : 8 - b.length % 8;
//	System.out.println(nLen);
//	if (nLen > 0) {
//	    byte[] M0 = new byte[nLen];
//	    for (int i = 0; i < nLen; i++) {
//		M0[i] = 0x00;
//	    }
//	    buf = concat(b, M0);
//	} else {
//	    buf = b;
//	}
//	b = buf.clone();
//	buf = new byte[b.length];
//	System.out.println("---1");
//	System.out.println(new String(b));
//	System.out.println(hexString(buf));
//	System.out.println(be.encrypt(b, 0, buf, 0, b.length));
//	System.out.println("---2");
//	System.out.println(new String(b));
//	System.out.println(hexString(buf));
//
//	buf = buf.clone();
//	byte[] b2 = new byte[buf.length];
//	System.out.println(be.decrypt(buf, 0, b2, 0, buf.length));
//	int nCount = 0;
//	for (int i = b2.length - 1; i >= 0 && b2[i] == 0x00; i--, nCount++)
//	    ;
//	byte[] b3 = new byte[b2.length - nCount];
//	System.out.println("---3," + (b2.length - nCount));
//	System.arraycopy(b2, 0, b3, 0, b2.length - nCount);
//	System.out.println("---3");
//	System.out.println(new String(b3));
//	System.out.println(new String(buf));
//	System.out.println(Base642.decode(new String(b2)));

//	System.out.println("---4");
//	BlowfishECB2 be2 = new BlowfishECB2(key, BlowfishBox.pbox_init, BlowfishBox.sbox_init);
//	System.out.println(be2.encrypt(buffer));
//	System.out.println(be2.decrypt(be2.encrypt(buffer)));
//	buffer = Base642.encode(buffer);
//	System.out.println(be2.encrypt(buffer));
//	System.out.println(be2.decrypt(be2.encrypt(buffer)));
//	System.out.println(Base642.decode(be2.decrypt(be2.encrypt(buffer))));
//	System.out.println(be.weakKeyCheck());
//	System.out.println(be2.weakKeyCheck());

//	int nCount = 0;
//	for (int i = b2.length - 1; i >= 0 && b2[i] == 0x00; i--, nCount++)
//	    ;
//	byte[] b3 = new byte[b2.length - nCount];
//	System.arraycopy(b2, 0, b3, 0, b2.length - nCount);
//	System.out.println(new String(b2));
//	System.out.println(new String(b3));

//	Base64.Decoder b64_d = Base64.getDecoder();
//	Base64.Encoder b64_e = Base64.getEncoder();
//	String str = "1234567890abcdefghijklmnopqrstuvwxyz";
//	System.out.println(b64_d.decode(str));
//	System.out.println(b64_e.encodeToString(b64_d.decode(str)));
////	b64_d = Base64.getMimeDecoder();
////	b64_e = Base64.getMimeEncoder();
//	str = "1234567890ab1";
//	System.out.println(b64_d.decode(str.getBytes(StandardCharsets.UTF_8)));
//	System.out.println(b64_e.encodeToString(b64_d.decode(str.getBytes(StandardCharsets.UTF_8))));
//	System.out.println(b64_e.encodeToString(b64_d.decode(str)));
    }

//    private static String hexString(byte[] b) {
//	StringBuffer d = new StringBuffer(b.length * 2);
//	for (int i = 0; i < b.length; i++) {
//	    char hi = Character.forDigit((b[i] >> 4) & 0x0F, 16);
//	    char lo = Character.forDigit(b[i] & 0x0F, 16);
//	    d.append(Character.toUpperCase(hi));
//	    d.append(Character.toUpperCase(lo));
//	}
//	return d.toString();
//    }
//
//    private static byte[] hex2byte(byte[] b, int offset, int len) {
//	byte[] d = new byte[len];
//	for (int i = 0; i < len * 2; i++) {
//	    int shift = i % 2 == 1 ? 0 : 4;
//	    d[i >> 1] |= Character.digit((char) b[offset + i], 16) << shift;
//	}
//	return d;
//    }
//
//    private static byte[] hex2byte(String s) {
//	if (s.length() % 2 == 0) {
//	    return hex2byte(s.getBytes(), 0, s.length() >> 1);
//	} else {
//	    throw new RuntimeException("Uneven number(" + s.length() + ") of hex digits passed to hex2byte.");
//	}
//    }
//
//    private static byte[] concat(byte[] array1, byte[] array2) {
//	byte[] concatArray = new byte[array1.length + array2.length];
//	System.arraycopy(array1, 0, concatArray, 0, array1.length);
//	System.arraycopy(array2, 0, concatArray, array1.length, array2.length);
//	return concatArray;
//    }

//    public static void main2(String[] args) {
//	MessageDigest messageDigest = null;
//	try {
//	    String str = "测试字符串";
//	    messageDigest = MessageDigest.getInstance("SHA1");
//	    messageDigest.update(str.getBytes("UTF-8"));
//	    byte bytes[] = messageDigest.digest();
//
//	    BigInteger bigInteger = new BigInteger(1, bytes);
//	    System.out.println(bigInteger.toString(16));
//
//	    String strHexBytes = DatatypeConverter.printHexBinary(bytes);
//	    System.out.println(strHexBytes);
//
//	    System.out.println(bytes2hex(bytes));
//	} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//	    e.printStackTrace();
//	}
//    }
//
//    public static String bytes2hex(byte[] bytes) {
//	StringBuilder sb = new StringBuilder();
//	String tmp = null;
//	for (byte b : bytes) {
//	    // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
//	    tmp = Integer.toHexString(0xFF & b);
//	    if (tmp.length() == 1) {
//		tmp = "0" + tmp;
//	    }
//	    sb.append(tmp);
//	}
//	return sb.toString();
//
//    }
//
//    public static void main3(String args[]) {
//	System.out.println(bytesToHexString("中华人民共和国".getBytes()));
//    }
//
//    public static String bytesToHexString(byte[] src) {
//	StringBuilder stringBuilder = new StringBuilder("");
//	if (src == null || src.length <= 0) {
//	    return null;
//	}
//	for (int i = 0; i < src.length; i++) {
//	    int v = src[i] & 0xFF;
//	    stringBuilder.append("0x");
//
//	    String hv = Integer.toHexString(v);
//	    if (hv.length() < 2) {
//		stringBuilder.append(0);
//	    }
//	    stringBuilder.append(hv);
//	    if (i != src.length - 1) {
//		stringBuilder.append(",");
//
//	    }
//	}
//	return stringBuilder.toString();
//    }

}
