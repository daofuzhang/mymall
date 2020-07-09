package com.zdf.demo.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

//��ʽ��ȨģʽҪ���û���¼���Ե�����Ӧ�ý�����Ȩ��ֱ�ӷ��ط���token��ͨ��token������Դ
//��Ȩ����������
@Configuration
@EnableAuthorizationServer //������Ȩ����
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    @Override
	    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
	        //���������ύ
	        security.allowFormAuthenticationForClients()
	                .checkTokenAccess("permitAll()"); //������security���ʿ���һ��
	    }

	    @Override
	    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
	        // @formatter: off
	        clients.inMemory()
	                .withClient("client-a") //client��Ψһ��ʶ
	                    .authorizedGrantTypes("implicit") //��Ȩģʽ��ʶ
	                    .accessTokenValiditySeconds(120) //�������Ƶ���Ч�ڣ���������120s
	                    .scopes("read_user_info") //������
	                    .resourceIds("resource1") //��Դid
	                    .redirectUris("http://localhost:9001/callback") //�ص���ַ
	                    .and()
	                .withClient("resource-server") //��Դ������У��tokenʱ�õĿͻ�����Ϣ������Ҫclient_id������
	                    .secret(passwordEncoder.encode("test"));
	        // @formatter: on
	    }
}