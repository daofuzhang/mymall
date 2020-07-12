package com.zdf.demo.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
   /**
    * localhost:8080/oauth/token?client_id=client_id_1&client_secret=123456&grant_type=client_credentials
{
	access_token: "097db1fd-20a1-4a8f-a7a9-8d500309b12e",
	token_type: "bearer",
	expires_in: 3590,
	scope: "read write"
}
    * @return
    */

	
	@RequestMapping("/test")
	public String test() {
		 System.err.println(SecurityContextHolder.getContext().getAuthentication());
		return "test";
	}
}
