package com.totorotec.rbac.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@Import({
    RepositoryRestMvcConfiguration.class
})
@EnableSpringDataWebSupport
public class RepositoryRestConfig extends RepositoryRestConfigurerAdapter {
//    @Autowired(required = false)
//    List<ResourceProcessor<?>> resourceProcessors = Collections.emptyList();
//
//    @Autowired
//    ListableBeanFactory beanFactory;
//
////    public RepositoryRestConfig(ApplicationContext context, ObjectFactory<ConversionService> conversionService) {
////        super(context, conversionService);
////    }
//
//    @Bean
//    public PluginRegistry<BackendIdConverter, Class<?>> backendIdConverterRegistry() {
//
//        List<BackendIdConverter> converters = new ArrayList<BackendIdConverter>(3);
//        converters.add(new CustomBackendIdConverter());
//        converters.add(BackendIdConverter.DefaultIdConverter.INSTANCE);
//
//        return OrderAwarePluginRegistry.create(converters);
//    }

    /**
     * Spring data rest 自定义配置
     *
     * @param config
     */
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

//        config.exposeIdsFor(
//            com.totorotec.rbac.entity.Resource.class
//        );
//        config.setDefaultMediaType(MediaType.APPLICATION_JSON_UTF8);
        config.setBasePath("/rbac");
        config.getCorsRegistry()
            .addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowCredentials(true)
            .maxAge(0);
    }

    /**
     * 自定义JSON输出
     * https://docs.spring.io/spring-data/rest/docs/current/reference/html/#customizing-sdr.customizing-json-output
     *
     * @return
     */
//    @Bean
//    public ResourceProcessor<Resource<com.totorotec.rbac.entity.Resource>> resourceProcessor() {
//        return new ResourceProcessor<Resource<com.totorotec.rbac.entity.Resource>>() {
//            @Override
//            public Resource<com.totorotec.rbac.entity.Resource> process(Resource<com.totorotec.rbac.entity.Resource> resource) {
//                resource.removeLinks();
//                return resource;
//            }
//        };
//    }

    /**
     * https://stackoverflow.com/questions/43087647/spring-data-rest-resourceprocessor-for-projection-exception/43482131
     * @return
     */
//    @Bean
//    public ResourceProcessor<ResourceSupport> organisationProcessor() {
//        return resource -> {
//            resource.removeLinks();
//            return resource;
//        };
//    }

}
