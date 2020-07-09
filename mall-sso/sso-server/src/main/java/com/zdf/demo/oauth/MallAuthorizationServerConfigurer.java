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
@EnableAuthorizationServer //注解用于配置 OAuth 2.0 授权服务器机制
public class MallAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private  DataSource  dataSource;
	@Autowired
	private AuthenticationManager  authenticationManager;
	@Autowired
	private MallUserDetailsService userDetailsService;
	
	/*
	 * 定义令牌端点上的安全约束
	 * /oauth/authorize（授权端点），/oauth/token（令牌端点），/oauth/confirm_access（用户在这里发布授权批准），/oauth/error（用于在授权服务器上渲染错误），/oauth/check_token（由资源服务器用来解码访问令牌）和/oauth/token_key（如果使用JWT令牌，公开密钥用于令牌验证）
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("isAuthenticated()");// 开启/oauth/check_token验证端口认证权限访问
		security.tokenKeyAccess("permitAll()");
		security.allowFormAuthenticationForClients();
		security.passwordEncoder(passwordEncoder());
		
	}

	/*
	 * 定义客户端详细信息服务的配置器。客户详细信息可以初始化，或者可以引用现有的 store
	 * clientId: （必须的）客户端 id
	 * secret: （要求用于受信任的客户端）客户端的机密，如果有的话
	 * scope: 客户范围限制。如果范围未定义或为空（默认），客户端将不受范围限制
	 * authorizedGrantTypes: 授权客户端使用的授予类型。默认值为空
	 * authorities: 授权给客户的认证（常规 Spring Security 认证）
	 * 通过直接访问底层存储（例如 JdbcClientDetailsService 用例中的数据库表）或者通过 ClientDetailsManager 接口（ClientDetailsService 也能实现这两种实现），可以在正在运行的应用程序中更新客户端详细信息。
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(new JdbcClientDetailsService(dataSource));
	}

	/*
	 * 定义授权和令牌端点以及令牌服务
	 * authenticationManager：通过注入 AuthenticationManager 来开启密码授权。
		userDetailsService：如果你注入一个 UserDetailsService，或者全局地配置了一个UserDetailsService（例如在 GlobalAuthenticationManagerConfigurer中），那么刷新令牌授权将包含对用户详细信息的检查，以确保该帐户仍然是活动的
		authorizationCodeServices：为授权代码授权定义授权代码服务（AuthorizationCodeServices 的实例）。
		
		implicitGrantService：在 imlpicit 授权期间管理状态。
		tokenGranter：TokenGranter（完全控制授予和忽略上面的其他属性）
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		endpoints.authenticationManager(authenticationManager);
		endpoints.userDetailsService(userDetailsService);
		endpoints.tokenStore(tokenStore());
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST) ;
		
		 // 配置tokenServices参数
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);//可以使用refresh_token
        tokenServices.setReuseRefreshToken(false);// 刷新token后，不复用refresh_token
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setAccessTokenValiditySeconds(24*30*30); // 30天
        tokenServices.setRefreshTokenValiditySeconds(24*30*30);
        endpoints.tokenServices(tokenServices);

	}
	
	 @Bean
	    public TokenStore tokenStore() {
	        //token保存在内存中（也可以保存在数据库、Redis中）。
	        //如果保存在中间件（数据库、Redis），那么资源服务器与认证服务器可以不在同一个工程中。
	        //注意：如果不保存access_token，则没法通过access_token取得用户信息
//	      return new InMemoryTokenStore();
	        return new JdbcTokenStore(dataSource); /// 使用Jdbctoken store
	    }
	 @Bean
	    PasswordEncoder passwordEncoder() {
	        // 加密方式
	        return new BCryptPasswordEncoder();
	    }

}
