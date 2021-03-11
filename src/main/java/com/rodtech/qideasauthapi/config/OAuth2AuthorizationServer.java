package com.rodtech.qideasauthapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * configuration of oauth2 clients
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                    .withClient("client")
                    .authorizedGrantTypes("password","refresh_token","client_credentials","authorization_code")
                    .scopes("read")
                    .secret(passwordEncoder.encode("123456"))
                    .resourceIds("services")
                .and()
                    .withClient("register_service")
                    .authorizedGrantTypes("client_credentials","authorization_code")
                    .scopes("read","write")
                        .secret(passwordEncoder.encode("r3g!st3r_s3cre4"))
                    .resourceIds("services")
        ;
    }

    /**
     * oauth2 token generator configuration
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("KeyTokenSingning");
        return accessTokenConverter;
    }

    /**
     * Token Provider Configuration
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * Authorization server configuration
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
         endpoints.tokenStore(tokenStore())
                 .accessTokenConverter(accessTokenConverter())
                 .authenticationManager(authenticationManager)
                 .userDetailsService(userDetailsService);
    }
}
