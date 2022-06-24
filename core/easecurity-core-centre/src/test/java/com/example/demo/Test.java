package com.example.demo;

import java.text.ParseException;
import java.util.Base64;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.easecurity.util.JsonUtils;

public class Test {

    public static void main(String[] args) throws ParseException {
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
	ParserConfig.getGlobalInstance().addAccept("com.easecurity.core.basis."); 
	System.out.println(JSON.toJSONString(map,SerializerFeature.WriteClassName));
	jsonString = "{\"@class\":\"java.util.HashMap\",\"/demo/queryData3\":{\"@class\":\"com.easecurity.core.basis.UriDo\",\"uri\":{\"@class\":\"com.easecurity.core.basis.re.Uri\",\"id\":1,\"uri\":\"/demo/queryData3\",\"classFullName\":\"com.easecurity.demo.DemoController\",\"methodName\":\"queryData3\",\"methodSignature\":\"public com.easecurity.core.authentication.UserDetails com.easecurity.demo.DemoController.queryData3(javax.servlet.http.HttpServletRequest)\",\"easType\":\"SOURCE_ONLY\",\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},\"uriIp\":null,\"uriOrg\":[\"java.util.ArrayList\",[{\"@class\":\"com.easecurity.core.basis.au.UriOrg\",\"id\":1,\"uriId\":1,\"orgId\":3,\"code\":null,\"name\":null,\"fullCode\":null,\"fullName\":null,\"group1\":1,\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},{\"@class\":\"com.easecurity.core.basis.au.UriOrg\",\"id\":2,\"uriId\":1,\"orgId\":4,\"code\":null,\"name\":null,\"fullCode\":null,\"fullName\":null,\"group1\":1,\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"}]],\"maxGroup\":1,\"_auOrgIdStr\":null,\"_auOrgCodeStr\":null,\"_auOrgNameStr\":null,\"_auOrgFullCodeStr\":null,\"_auOrgFullNameStr\":null}}";
	System.out.println(JSON.parse(jsonString));
	System.out.println(JSON.toJSONString(JSON.parse(jsonString),SerializerFeature.WriteClassName));
	System.out.println(JsonUtils.jsonToObject(jsonString));
	System.out.println(JSON.toJSONString(JsonUtils.jsonToObject(jsonString),SerializerFeature.WriteClassName));
    }
}
