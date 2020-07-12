package com.zdf.demo.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired 
	private AuthenticationManager authenticationManager;
	@Autowired 
	private MallUserDetailsService userDetailsService;
	
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
 
        clients.inMemory()
                .withClient("client-authorization_01") //client端唯一标识
                    .secret(passwordEncoder.encode("client-authorization-secret")) //客户端的密码，这里的密码应该是加密后的
                    .authorizedGrantTypes("authorization_code","refresh_token") //授权模式标识
                    .scopes("all", "read", "write") //作用域
                    .autoApprove(true)
                    .resourceIds("resource_authorization") //资源id
                    .redirectUris("http://localhost:8081/login") //回调地址
                .and()
                .withClient("client-authorization_02") //client端唯一标识
		        .secret(passwordEncoder.encode("client-authorization-secret")) //客户端的密码，这里的密码应该是加密后的
		        .authorizedGrantTypes("authorization_code","refresh_token") //授权模式标识
		        .scopes("all", "read", "write") //作用域
		        .autoApprove(true)
		        .resourceIds("resource_authorization") //资源id
		        .redirectUris("http://localhost:8082/login"); //回调地址
    }
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        
        security.allowFormAuthenticationForClients();//允许表单提交
        security.checkTokenAccess("isAuthenticated()");// 开启/oauth/check_token验证端口认证权限访问
        security.tokenKeyAccess("permitAll()");
		security.passwordEncoder(passwordEncoder);
    }
    
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager);
			endpoints.userDetailsService(userDetailsService);//若无，refresh_token会有UserDetailsService is required错误
	}
}
