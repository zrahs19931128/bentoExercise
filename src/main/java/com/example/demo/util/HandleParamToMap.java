package com.example.demo.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public class HandleParamToMap {

	/**
	* 将请求对象的参数转换为HashMap
	* 
	* 
	*/
	public static Map<String, Object> handleParamToMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			String[] arr = entry.getValue();
			String result = "";
			if (null != arr && arr.length > 0) {
				for (int i = 0; i < arr.length; i++) {
					result += arr[i];
					if (i < arr.length - 1) {
					result += ",";
					}
				}
				map.put(entry.getKey(), result);
			}
		}
		return map;
	}
	
}
