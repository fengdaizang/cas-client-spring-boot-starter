package com.fdzang.cas.client.configuration;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(CasClientConfigurationProperties.class)
public class CasClientConfiguration {

    @Autowired
    CasClientConfigurationProperties configProps;

    @Bean
    public FilterRegistrationBean filterSingleRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SingleSignOutFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);

        Map<String, String> params = new HashMap<String, String>();
        params.put("casServerUrlPrefix", configProps.getServerUrlPrefix());
        params.put("serverName", configProps.getServerName());
        registration.setInitParameters(params);

        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration() {
        ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<EventListener>();
        registrationBean.setListener(new SingleSignOutHttpSessionListener());
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(2);

        Map<String, String> params = new HashMap<String, String>();
        params.put("casServerLoginUrl", configProps.getServerLoginUrl());
        params.put("serverName", configProps.getServerName());
        if (!StringUtils.isEmpty(configProps.getIgnorePattern())) {
            params.put("ignorePattern", configProps.getIgnorePattern());
        }
        registration.setInitParameters(params);

        return registration;
    }

    @Bean
    public FilterRegistrationBean filterValidationRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(3);

        Map<String, String> params = new HashMap<String, String>();
        params.put("casServerUrlPrefix", configProps.getServerUrlPrefix());
        params.put("serverName", configProps.getServerName());
        params.put("useSession", "true");
        registration.setInitParameters(params);

        return registration;
    }

    @Bean
    public FilterRegistrationBean filterWrapperRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestWrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(4);

        return registration;
    }

    @Bean
    public FilterRegistrationBean casAssertionThreadLocalFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AssertionThreadLocalFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(5);

        return registration;
    }
}