package com.zdf.demo.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

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
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // @formatter: off
        clients.inMemory()
                .withClient("client-a") //client��Ψһ��ʶ
                    .secret(passwordEncoder.encode("client-a-secret")) //�ͻ��˵����룬���������Ӧ���Ǽ��ܺ��
                    .authorizedGrantTypes("authorization_code") //��Ȩģʽ��ʶ
                    .scopes("read_user_info") //������
                    .resourceIds("resource1") //��Դid
                    .redirectUris("http://localhost:9001/callback"); //�ص���ַ
        // @formatter: on
    }
}