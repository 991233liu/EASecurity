/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.security.blowfish;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Blowfish的秘钥生产器
 *
 */
public class BlowfishKey {

    /**
     * @param args
     */
    public static void main(String[] args) {
//	Random random = new Random("test-key".hashCode());    // 可以推算出来的秘钥，不推荐使用
	Random random = new Random();
	// 生成秘钥
	Map<String, Object> keys = printAllKey(random);
	// 校验秘钥强度
	int[] pbox_init = (int[]) keys.get("pbox_init");
	int[][] sbox_init = (int[][]) keys.get("sbox_init");
	BlowfishECB be = new BlowfishECB("test-key", pbox_init, sbox_init);
	System.out.println("\n秘钥的强度很好：" + !be.weakKeyCheck());
	// 验证秘钥可用性
	be.encrypt("1234567890abcdefghijklmnopqrstuvwxyz");
	be.decrypt(be.encrypt("1234567890abcdefghijklmnopqrstuvwxyz"));
    }

    private static Map<String, Object> printAllKey(Random random) {
	Map<String, Object> keys = new HashMap<>();
	System.out.print("public static final int pbox_init[] = {");
	int[] pKey = printKey(random, 18);
	System.out.println("};");
	int[][] sKey = new int[4][];
	System.out.print("public static final int sbox_init[][] = { { ");
	sKey[0] = printKey(random, 256);
	System.out.println("},");
	System.out.print("{ ");
	sKey[1] = printKey(random, 256);
	System.out.println("},");
	System.out.print("{ ");
	sKey[2] = printKey(random, 256);
	System.out.println("},");
	System.out.print("{ ");
	sKey[3] = printKey(random, 256);
	System.out.println("} };");
	keys.put("pbox_init", pKey);
	keys.put("sbox_init", sKey);
	return keys;
    }

    private static int[] printKey(Random random, int kLen) {
	int[] keys = new int[kLen];
	for (int i = 0; i < kLen; i++) {
	    double a = random.nextDouble() * 10;
	    String strHexBytes = Double.toHexString(a);
	    strHexBytes = strHexBytes.substring(strHexBytes.indexOf(".") + 1);
	    if (strHexBytes.startsWith("0") || strHexBytes.length() < 8) {
		i--;
		continue;
	    } else
		strHexBytes = strHexBytes.substring(0, 8);
	    System.out.print("0x" + strHexBytes);
	    System.out.print(", ");
	    int b = new BigInteger(strHexBytes, 16).intValue();
	    keys[i] = b;
//	    System.out.println(b);
	}
	System.out.println("");
	return keys;
    }
}
