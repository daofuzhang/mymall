package com.zdf.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MallApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallApplication.class, args);
		String finalSecret = "++++++" + new BCryptPasswordEncoder().encode("123456");
        System.out.println(finalSecret);
	}
}
