package com.sahibinden.base;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtilities extends BaseTest{
	public Map<String,Object> getData(){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			result = new ObjectMapper().readValue(new File("src/main/resources/data.json"), HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
