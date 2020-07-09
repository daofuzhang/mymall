package com.zdf.demo.oauth;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 我们认证之前需要先校验用户的账户密码是否正确，所以我们先配置WebSecurityConfig拦截
 * @author 00291315
 *
 */
@Resource
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MallSpringSecurityWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private MallUserDetailsService userDetailsService;
	
	 /*
    spring security 先通过ResourceSecurityConfiguration的配置先执行
    ，再执行webSecurityConfiguration的配置，所以要规划好不同断点对应的过滤链不冲突，需要清晰规划好。
     */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		http    // 配置登陆页/login并允许访问
                 .formLogin().permitAll()
		         // 登出页
		         .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
		         .and().authorizeRequests().antMatchers("/oauth/**").permitAll()
		         // 其余所有请求全部需要鉴权认证
		         .and().authorizeRequests().anyRequest().authenticated()
		         // 由于使用的是JWT，我们这里不需要csrf
		         .and().csrf().disable(); 
		         */
        http
        .antMatcher("/**")
        .authorizeRequests()
        .antMatchers("/oauth/**",
        		"/user",
                "/v1.1/**",
                "/v1.0/**",
                "/open/**") //
        .permitAll()//  在webSecurityConfiguration都不需要web的认证
        .and()
        .authorizeRequests()
        .anyRequest().authenticated()//除了上面的，任何请求都要认证
        .and()
        .csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable()
        .sessionManagement().maximumSessions(1);//  最大会话数设置
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	// 不定义没有password grant_type
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
