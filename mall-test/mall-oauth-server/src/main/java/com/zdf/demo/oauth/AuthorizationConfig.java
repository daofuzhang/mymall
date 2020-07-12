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
                .withClient("client-authorization_01") //client��Ψһ��ʶ
                    .secret(passwordEncoder.encode("client-authorization-secret")) //�ͻ��˵����룬���������Ӧ���Ǽ��ܺ��
                    .authorizedGrantTypes("authorization_code","refresh_token") //��Ȩģʽ��ʶ
                    .scopes("all", "read", "write") //������
                    .autoApprove(true)
                    .resourceIds("resource_authorization") //��Դid
                    .redirectUris("http://localhost:8081/login") //�ص���ַ
                .and()
                .withClient("client-authorization_02") //client��Ψһ��ʶ
		        .secret(passwordEncoder.encode("client-authorization-secret")) //�ͻ��˵����룬���������Ӧ���Ǽ��ܺ��
		        .authorizedGrantTypes("authorization_code","refresh_token") //��Ȩģʽ��ʶ
		        .scopes("all", "read", "write") //������
		        .autoApprove(true)
		        .resourceIds("resource_authorization") //��Դid
		        .redirectUris("http://localhost:8082/login"); //�ص���ַ
    }
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        
        security.allowFormAuthenticationForClients();//���������ύ
        security.checkTokenAccess("isAuthenticated()");// ����/oauth/check_token��֤�˿���֤Ȩ�޷���
        security.tokenKeyAccess("permitAll()");
		security.passwordEncoder(passwordEncoder);
    }
    
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager);
			endpoints.userDetailsService(userDetailsService);//���ޣ�refresh_token����UserDetailsService is required����
	}
}