package com.zdf.demo.oauth;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class MallResServerConfig extends ResourceServerConfigurerAdapter {

	/*
	
	 @Autowired
	  private TokenStore tokenStore;

	    @Override
	    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
	        resources
	                .tokenStore(tokenStore)
	                .resourceId("resource_id")//    resourceId要跟认证服务器客户端配置的resourceId一致
	                .stateless(true);
	    }
	    */

	    /*
	    spring security 先通过ResourceSecurityConfiguration的配置先执行
	    ，再执行webSecurityConfiguration的配置，所以要规划好不同断点对应的过滤链不冲突，需要清晰规划好。
	     */
	
	
	    @Override
	    public void configure(HttpSecurity http) throws Exception {
	    	 http
             .csrf().disable()
             .exceptionHandling()
             .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
             .and()
             .authorizeRequests()
             .antMatchers("/user", "/oauth/**","/token/**").permitAll()
             .anyRequest().authenticated()
             .and()
             .formLogin().permitAll();

	    }
	    
	}

