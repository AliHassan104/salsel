package com.salsel.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class PublicEndpointConfiguration  implements WebMvcConfigurer {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                // here you match any resource path from the URL
                .addResourceHandler("/**")
                // to search in the classpath:/static/ folder, which in your build
                // is "/target/static" and NOT "/src/main/resources/static"
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
}
