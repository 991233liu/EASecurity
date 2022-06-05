package com.example.demo;

import java.util.Base64;
import java.util.Map;

//import com.easecurity.core.basis.UserDo;
import com.easecurity.util.JsonUtils;

public class Test {

    public static void main(String[] args) {
	String jsonString = "{id:['1','4']}";
	@SuppressWarnings("rawtypes")
	Map map = (Map) JsonUtils.jsonToObject(jsonString);
	System.out.println(map);
	byte[] decodedCookieBytes = Base64.getDecoder().decode("MmM0MTYxMWQtYjBhZS00MTZmLWFkMjYtMDJhZDAyZWU5YTk0");
	System.out.println(new String(decodedCookieBytes));
	byte[] encodedCookieBytes = Base64.getEncoder().encode("2c41611d-b0ae-416f-ad26-02ad02ee9a94".getBytes());
	System.out.println(new String(encodedCookieBytes));
//	Object a = null;
//	UserDo userDo = (UserDo) a;
    }
}
