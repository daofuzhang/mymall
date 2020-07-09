package com.zdf.demo.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	
	/* post���������� client-id ��client-secret
	 * http://localhost:8080/oauth/token?username=hellxz&password=xyz&scope=read_scope&grant_type=password
	 * http://localhost:9001/test?access_token=xxxxx
	 */
	@RequestMapping("/test")
	public String test() {
		 System.err.println(SecurityContextHolder.getContext().getAuthentication());
		return "test";
	}
}
