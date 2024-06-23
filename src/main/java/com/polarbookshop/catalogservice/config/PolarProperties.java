package com.polarbookshop.catalogservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "polar")
public class PolarProperties {

    /**
     * 사용자 환영 메시지
     */
    private String greeting;

}
