package com.fdzang.cas.client.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * @author tanghu
 * @Date: 2020/3/27 10:33
 */
@Data
@ConfigurationProperties(prefix = "cas", ignoreUnknownFields = false)
public class CasClientConfigurationProperties {
    /**
     * CAS 服务端 url 不能为空
     */
    @NotNull
    private String serverUrlPrefix;

    /**
     * CAS 服务端登录地址  上面的连接 加上/login 该参数不能为空
     */
    @NotNull
    private String serverLoginUrl;

    /**
     * 当前客户端的地址
     */
    @NotNull
    private String serverName;

    /**
     * 忽略规则,访问那些地址 不需要登录
     */
    private String ignorePattern;

    /**
     * 认证的URL
     */
    private String authrizationUrl;
}