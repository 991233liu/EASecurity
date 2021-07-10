package com.example.demo;

import java.util.Map;

import com.easecurity.util.JsonUtils;

public class Test {

    public static void main(String[] args) {
	String jsonString = "{id:['1','4']}";
	Map map = (Map) JsonUtils.jsonToObject(jsonString);
	System.out.println(map);
    }
}
