package com.zdf.demo.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

   /**
    * 1、http://localhost:8080/login 输入账号密码

2、再次输入如下地址，获取code
http://localhost:8080/oauth/authorize?response_type=code&client_id=client_id_1&redirect_uri=http://example.com&scope=write
地址栏重定向如下
http://example.com/?code=n9WlYp 

3、http://localhost:8080/oauth/token?grant_type=authorization_code&client_id=client_id_1&client_secret=123456&redirect_uri=http://example.com&code=n9WlYp
{
    "access_token": "812403de-548d-4939-b0fa-57b1ec8abb11",
    "token_type": "bearer",
    "refresh_token": "8a8a7ebb-6f73-452d-9582-2fbf4ff80815",
    "expires_in": 99,
    "scope": "write"
}
    * @return
    */
	
	@RequestMapping("/test")
	public String test() {
		 System.err.println(SecurityContextHolder.getContext().getAuthentication());
		return "test";
	}
}
