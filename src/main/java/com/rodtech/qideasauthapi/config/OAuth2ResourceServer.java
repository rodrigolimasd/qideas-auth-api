package com.rodtech.qideasauthapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {

    /**
     * auth2 permission resource
     * @param resources
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("services");
    }

    /**
     * setting oauth2 permissions on the web layer
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers("/**")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }


}
