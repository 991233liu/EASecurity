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
        System.out.println(JSON.toJSONString(map, SerializerFeature.WriteClassName));
        jsonString = "{\"@class\":\"java.util.HashMap\",\"/demo/queryData3\":{\"@class\":\"com.easecurity.core.basis.UriDo\",\"uri\":{\"@class\":\"com.easecurity.core.basis.re.Uri\",\"id\":1,\"uri\":\"/demo/queryData3\",\"classFullName\":\"com.easecurity.demo.DemoController\",\"methodName\":\"queryData3\",\"methodSignature\":\"public com.easecurity.core.authentication.UserDetails com.easecurity.demo.DemoController.queryData3(javax.servlet.http.HttpServletRequest)\",\"easType\":\"SOURCE_ONLY\",\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},\"uriIp\":null,\"uriOrg\":[\"java.util.ArrayList\",[{\"@class\":\"com.easecurity.core.basis.au.UriOrg\",\"id\":1,\"uriId\":1,\"orgId\":3,\"code\":null,\"name\":null,\"fullCode\":null,\"fullName\":null,\"group1\":1,\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},{\"@class\":\"com.easecurity.core.basis.au.UriOrg\",\"id\":2,\"uriId\":1,\"orgId\":4,\"code\":null,\"name\":null,\"fullCode\":null,\"fullName\":null,\"group1\":1,\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"}]],\"maxGroup\":1,\"_auOrgIdStr\":null,\"_auOrgCodeStr\":null,\"_auOrgNameStr\":null,\"_auOrgFullCodeStr\":null,\"_auOrgFullNameStr\":null}}";
        System.out.println(JSON.parse(jsonString));
        System.out.println(JSON.toJSONString(JSON.parse(jsonString), SerializerFeature.WriteClassName));
        System.out.println(JsonUtils.jsonToObject(jsonString));
        System.out.println(JSON.toJSONString(JsonUtils.jsonToObject(jsonString), SerializerFeature.WriteClassName));
        jsonString = "{\"@class\":\"java.util.HashMap\",\"lastModified\":\"402060034\",\"allUriDos\":{\"@class\":\"java.util.HashMap\",\"/demo/queryData1\":{\"@class\":\"com.easecurity.core.basis.UriDo\",\"uri\":{\"@class\":\"com.easecurity.core.basis.re.Uri\",\"id\":11,\"uri\":\"/demo/queryData1\",\"classFullName\":\"com.easecurity.demo.DemoController\",\"methodName\":\"queryData1\",\"methodSignature\":\"public com.easecurity.core.authentication.UserDetails com.easecurity.demo.DemoController.queryData1(javax.servlet.http.HttpServletRequest)\",\"easType\":\"DEFAULT\",\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},\"uriIp\":{\"@class\":\"com.easecurity.core.basis.au.UriIp\",\"id\":3,\"uriId\":11,\"ips\":\"128.0.0.1\",\"group1\":1,\"fromTo\":\"MANUALLY\",\"status\":\"DISABLED\"},\"uriOrg\":[\"java.util.ArrayList\",[{\"@class\":\"com.easecurity.core.basis.au.UriOrg\",\"id\":15,\"uriId\":11,\"orgId\":1,\"group1\":1,\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},{\"@class\":\"com.easecurity.core.basis.au.UriOrg\",\"id\":16,\"uriId\":11,\"orgId\":4,\"group1\":1,\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"}]],\"uriRole\":[\"java.util.ArrayList\",[]],\"uriRoleGroup\":[\"java.util.ArrayList\",[]],\"maxGroup\":1},\"/demo/queryData2\":{\"@class\":\"com.easecurity.core.basis.UriDo\",\"uri\":{\"@class\":\"com.easecurity.core.basis.re.Uri\",\"id\":12,\"uri\":\"/demo/queryData2\",\"classFullName\":\"com.easecurity.demo.DemoController\",\"methodName\":\"queryData2\",\"methodSignature\":\"public com.easecurity.core.authentication.UserDetails com.easecurity.demo.DemoController.queryData2(javax.servlet.http.HttpServletRequest)\",\"easType\":\"DEFAULT\",\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},\"uriOrg\":[\"java.util.ArrayList\",[{\"@class\":\"com.easecurity.core.basis.au.UriOrg\",\"id\":17,\"uriId\":12,\"orgId\":1,\"group1\":2,\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},{\"@class\":\"com.easecurity.core.basis.au.UriOrg\",\"id\":18,\"uriId\":12,\"orgId\":4,\"group1\":2,\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"}]],\"uriRole\":[\"java.util.ArrayList\",[]],\"uriRoleGroup\":[\"java.util.ArrayList\",[]],\"maxGroup\":2},\"/demo/queryData3\":{\"@class\":\"com.easecurity.core.basis.UriDo\",\"uri\":{\"@class\":\"com.easecurity.core.basis.re.Uri\",\"id\":9,\"uri\":\"/demo/queryData3\",\"classFullName\":\"com.easecurity.demo.DemoController\",\"methodName\":\"queryData3\",\"methodSignature\":\"public com.easecurity.core.authentication.UserDetails com.easecurity.demo.DemoController.queryData3(javax.servlet.http.HttpServletRequest)\",\"easType\":\"DEFAULT\",\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},\"uriOrg\":[\"java.util.ArrayList\",[{\"@class\":\"com.easecurity.core.basis.au.UriOrg\",\"id\":13,\"uriId\":9,\"orgId\":3,\"group1\":1,\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},{\"@class\":\"com.easecurity.core.basis.au.UriOrg\",\"id\":14,\"uriId\":9,\"orgId\":4,\"group1\":1,\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"}]],\"uriRole\":[\"java.util.ArrayList\",[]],\"uriRoleGroup\":[\"java.util.ArrayList\",[]],\"maxGroup\":1},\"/demo/queryData4\":{\"@class\":\"com.easecurity.core.basis.UriDo\",\"uri\":{\"@class\":\"com.easecurity.core.basis.re.Uri\",\"id\":10,\"uri\":\"/demo/queryData4\",\"classFullName\":\"com.easecurity.demo.DemoController\",\"methodName\":\"queryData4\",\"methodSignature\":\"public com.easecurity.core.authentication.UserDetails com.easecurity.demo.DemoController.queryData4(javax.servlet.http.HttpServletRequest)\",\"easType\":\"DEFAULT\",\"fromTo\":\"SOURCECODE\",\"status\":\"ENABLED\"},\"uriOrg\":[\"java.util.ArrayList\",[]],\"uriRole\":[\"java.util.ArrayList\",[]],\"uriRoleGroup\":[\"java.util.ArrayList\",[{\"@class\":\"com.easecurity.core.basis.au.UriRoleGroup\",\"id\":1,\"uriId\":10,\"annotation\":\"'code':['anonymous']}\",\"group1\":1,\"fromTo\":\"MANUALLY\",\"status\":\"DISABLED\"}]],\"maxGroup\":-1}}}";
        System.out.println(JsonUtils.jsonToObject(jsonString));
    }
}
