package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DataOperationController {

	@Autowired
	SomeDao service;

	@RequestMapping("/queryData")
    public List<Object> queryData() {
        return service.queryForList(null);
    }

//    @PostMapping("/api/addData")
//    public String addData() {
//        try {
//            service.addData();
//            return "add data success";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "add data fail";
//        }
//    }
}
