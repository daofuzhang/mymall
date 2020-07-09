package com.zdf.demo.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	/**
	 * http://localhost:8080/oauth/authorize?client_id=client-a&redirect_uri=http://localhost:9001/callback&response_type=token&scope=read_user_info
	 * http://localhost:9001/callback#access_token=ccf5149a-a4b0-4f31-9f45-1fdbd989ae7a&token_type=bearer&expires_in=119
	 * http://localhost:9001/test?access_token=ccf5149a-a4b0-4f31-9f45-1fdbd989ae7a
	 * @return
	 */
	
	@RequestMapping("/test")
	public String test() {
		 System.err.println(SecurityContextHolder.getContext().getAuthentication());
		return "test";
	}
}
