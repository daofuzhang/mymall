package com.zdf.demo.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	
	
	/* post请求并且设置 client-id 和client-secret
	 * http://localhost:8080/oauth/token?username=hellxz&password=xyz&scope=read_scope&grant_type=password
	 * http://localhost:9001/test?access_token=xxxxx
	 * 
	 * localhost:8080/oauth/token?client_id=client_id_1&client_secret=123456&grant_type=password&username=admin&password=123456
{
	access_token: "b909b1e6-bf10-4c13-bef7-bfca64211feb",
	token_type: "bearer",
	refresh_token: "521c491d-ef73-45af-aefb-36c5c66cbe89",
	expires_in: 3599,
	scope: "read write"
}
	 * 
	 * 
	 */
	@RequestMapping("/test")
	public String test() {
		 System.err.println(SecurityContextHolder.getContext().getAuthentication());
		return "test";
	}
}
