package com.zdf.demo.oauth;

import javax.servlet.http.HttpServletResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * �û���ϢҲ����Դ,����Ҳ��Ҫ��Դ����
 * @author 00291315
 *
 */
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Override
    public void configure(HttpSecurity http) throws Exception {
	    	http
	        .csrf().disable()
	        .exceptionHandling()
	        .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
	        .and()
		        .authorizeRequests()
		        .anyRequest().authenticated()
	        .and()
	        	.httpBasic();
    }
}