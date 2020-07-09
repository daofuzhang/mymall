package com.zdf.demo.oauth;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer //ע���������� OAuth 2.0 ��Ȩ����������
public class MallAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private  DataSource  dataSource;
	@Autowired
	private AuthenticationManager  authenticationManager;
	@Autowired
	private MallUserDetailsService userDetailsService;
	
	/*
	 * �������ƶ˵��ϵİ�ȫԼ��
	 * /oauth/authorize����Ȩ�˵㣩��/oauth/token�����ƶ˵㣩��/oauth/confirm_access���û������﷢����Ȩ��׼����/oauth/error����������Ȩ����������Ⱦ���󣩣�/oauth/check_token������Դ��������������������ƣ���/oauth/token_key�����ʹ��JWT���ƣ�������Կ����������֤��
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("isAuthenticated()");// ����/oauth/check_token��֤�˿���֤Ȩ�޷���
		security.tokenKeyAccess("permitAll()");
		security.allowFormAuthenticationForClients();
		security.passwordEncoder(passwordEncoder());
		
	}

	/*
	 * ����ͻ�����ϸ��Ϣ��������������ͻ���ϸ��Ϣ���Գ�ʼ�������߿����������е� store
	 * clientId: ������ģ��ͻ��� id
	 * secret: ��Ҫ�����������εĿͻ��ˣ��ͻ��˵Ļ��ܣ�����еĻ�
	 * scope: �ͻ���Χ���ơ������Χδ�����Ϊ�գ�Ĭ�ϣ����ͻ��˽����ܷ�Χ����
	 * authorizedGrantTypes: ��Ȩ�ͻ���ʹ�õ��������͡�Ĭ��ֵΪ��
	 * authorities: ��Ȩ���ͻ�����֤������ Spring Security ��֤��
	 * ͨ��ֱ�ӷ��ʵײ�洢������ JdbcClientDetailsService �����е����ݿ��������ͨ�� ClientDetailsManager �ӿڣ�ClientDetailsService Ҳ��ʵ��������ʵ�֣����������������е�Ӧ�ó����и��¿ͻ�����ϸ��Ϣ��
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(new JdbcClientDetailsService(dataSource));
	}

	/*
	 * ������Ȩ�����ƶ˵��Լ����Ʒ���
	 * authenticationManager��ͨ��ע�� AuthenticationManager ������������Ȩ��
		userDetailsService�������ע��һ�� UserDetailsService������ȫ�ֵ�������һ��UserDetailsService�������� GlobalAuthenticationManagerConfigurer�У�����ôˢ��������Ȩ���������û���ϸ��Ϣ�ļ�飬��ȷ�����ʻ���Ȼ�ǻ��
		authorizationCodeServices��Ϊ��Ȩ������Ȩ������Ȩ�������AuthorizationCodeServices ��ʵ������
		
		implicitGrantService���� imlpicit ��Ȩ�ڼ����״̬��
		tokenGranter��TokenGranter����ȫ��������ͺ���������������ԣ�
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		endpoints.authenticationManager(authenticationManager);
		endpoints.userDetailsService(userDetailsService);
		endpoints.tokenStore(tokenStore());
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST) ;
		
		 // ����tokenServices����
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);//����ʹ��refresh_token
        tokenServices.setReuseRefreshToken(false);// ˢ��token�󣬲�����refresh_token
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setAccessTokenValiditySeconds(24*30*30); // 30��
        tokenServices.setRefreshTokenValiditySeconds(24*30*30);
        endpoints.tokenServices(tokenServices);

	}
	
	 @Bean
	    public TokenStore tokenStore() {
	        //token�������ڴ��У�Ҳ���Ա��������ݿ⡢Redis�У���
	        //����������м�������ݿ⡢Redis������ô��Դ����������֤���������Բ���ͬһ�������С�
	        //ע�⣺���������access_token����û��ͨ��access_tokenȡ���û���Ϣ
//	      return new InMemoryTokenStore();
	        return new JdbcTokenStore(dataSource); /// ʹ��Jdbctoken store
	    }
	 @Bean
	    PasswordEncoder passwordEncoder() {
	        // ���ܷ�ʽ
	        return new BCryptPasswordEncoder();
	    }

}