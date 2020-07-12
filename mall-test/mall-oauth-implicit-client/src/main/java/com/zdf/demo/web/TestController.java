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
	 * 
	 * 1、http://localhost:8080/login 输入账号密码

2、再次输入如下地址，获取token
http://localhost:8080/oauth/authorize?response_type=token&client_id=client_id_1&redirect_uri=http://example.com&scope=write 
地址栏重定向如下
http://example.com/#access_token=2820363f-2e2f-4485-8892-64683895675e&token_type=bearer&expires_in=3512
	 * 
	 */
	
	@RequestMapping("/test")
	public String test() {
		 System.err.println(SecurityContextHolder.getContext().getAuthentication());
		return "test";
	}
}
