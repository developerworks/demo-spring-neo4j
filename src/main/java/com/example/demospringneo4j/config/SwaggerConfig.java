package com.example.demospringneo4j.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//@ComponentScan(basePackageClasses = {
//    FindController.class
//})
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .tags(new Tag("EnvironmentRepository", "环境管理"))
            .tags(new Tag("ProjectRepository", "项目管理"))
            .tags(new Tag("PropertySourceRepository", "属性管理"))
            .tags(new Tag("SourceConfigurationRepository", "资源管理"))
            .tags(new Tag("profile-controller", "用不到的控制器"))
            .tags(new Tag("Find", "根据不同的 ID 查询其下所有数据"))
            .tags(new Tag("Save", "单独添加或修改 module 的数据"))
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(doFilteringRules())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("龙猫游戏RBAC权限管理系统")
            .description("如果有什么不会的地方请：http://rbac.totoro.com/index.html")
            .termsOfServiceUrl("http://rbac.totoro.com/tos.html")
            .contact(new Contact("何智强", "http://rbac.totoro.com", "developerworks@163.com"))
            .version("1.0")
            .build();
    }

    /**
     * 设置过滤规则 这里的过滤规则支持正则匹配    //若有静态方法在此之前加载就会报集合相关的错误.
     *
     * @return
     */
    private Predicate<String> doFilteringRules() {
//		return Predicates.not(PathSelectors.regex("/error.*"));
//		return or(regex("/*"), regex("/*"));//success
        return PathSelectors.regex("/info.*");
    }
}
