package com.service.image.configuration;

import com.service.image.entities.Image;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * https://www.baeldung.com/spring-data-rest-customize-http-endpoints
 * https://www.baeldung.com/rest-api-pagination-in-spring
 */
@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
        final HttpMethod[] unsupportedActions = {HttpMethod.PATCH, HttpMethod.PUT};

        disableHttpMethods(Image.class, config, unsupportedActions);
    }

    private void disableHttpMethods(Class typeClass, RepositoryRestConfiguration config,
                                    HttpMethod[] unsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(typeClass)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions));
    }

}
