package com.example.demospringneo4j.config;

import com.example.demospringneo4j.rbac.entity.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.MediaType;

@Configuration
class RepositoryRestConfig extends RepositoryRestConfigurerAdapter {

    /**
     * Spring data rest 自定义配置
     *
     * @param config
     */
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        super.configureRepositoryRestConfiguration(config);
        config.exposeIdsFor(
            Resource.class
        );
        config.setBasePath("/rbac");
        config.setDefaultMediaType(MediaType.APPLICATION_JSON_UTF8);

        config.getCorsRegistry()
            .addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowCredentials(true)
            .maxAge(0);
    }
}
