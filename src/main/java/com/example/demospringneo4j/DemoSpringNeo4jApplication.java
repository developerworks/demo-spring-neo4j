package com.example.demospringneo4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableTransactionManagement
@EnableNeo4jRepositories
@EnableSwagger2
@Import({
    SpringDataRestConfiguration.class,
    BeanValidatorPluginsConfiguration.class
})
public class DemoSpringNeo4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringNeo4jApplication.class, args);
    }
}
