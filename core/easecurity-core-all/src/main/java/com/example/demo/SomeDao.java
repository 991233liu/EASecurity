package com.example.demo;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.easecurity.core.basis.User;

@Service
public class SomeDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @SuppressWarnings("unchecked")
	public <T> List<T> queryForList(String sql){
    	List<T> re = null;
    	re = (List<T>) jdbcTemplate
                .query("SELECT * FROM b_user", new BeanPropertyRowMapper<>(User.class));
//    	re = (List<T>) jdbcTemplate
//    			.queryForList("SELECT user FROM b_user", String.class);
		return re;
    }
}
