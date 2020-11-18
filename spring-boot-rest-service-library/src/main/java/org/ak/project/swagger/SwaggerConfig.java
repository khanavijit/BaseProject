package org.ak.project.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.ant;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${swagger.application.description}")
    String swaggerApplicationDescription;

    @Autowired(required = false)
    BuildProperties buildProperties;

    @Value("${swagger.application.topbar.color:#2d80d3}")
    String swaggerTopbarColor;

    @Value("classpath:META-INF/resources/webjars/springfox-swagger-ui/swagger-ui.css")
    private Resource swaggerUiCssFile;


    @Value("${jwks-resources.ignored:@null}")
    private String ignoredPaths;


    @Autowired(required = false)
    private org.ak.project.swagger.SecuredPaths securedPaths;

    @Controller
    class SwaggerWelcome {
        @GetMapping("/")
        public String redirectToUi() {
            return "redirect:/swagger-ui.html";
        }

        @ApiIgnore
        @GetMapping(value = "webjars/springfox-swagger-ui/swagger-ui.css", produces = "text/css")
        public String redirectSwaggerUiCss(ModelMap model) {
            model.addAttribute("topbarColor", swaggerTopbarColor);
            return "swagger-ui";
        }

        @ApiIgnore
        @GetMapping(value = "swagger/webjars/springfox-swagger-ui/swagger-ui.css", produces = "text/css")
        public @ResponseBody
        byte[] getSwaggerUiCss() throws IOException {
            return IOUtils.toByteArray(swaggerUiCssFile.getInputStream());
        }

    }

    @Bean(name = "viewResolver")
    public ViewResolver getViewResolver() {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
        viewResolver.setCache(true);
        viewResolver.setSuffix(".ftl");
        viewResolver.setOrder(1);
        viewResolver.setContentType("text/css;charset=UTF-8");
        return viewResolver;
    }

    @Bean(name = "freemarkerConfig")
    public FreeMarkerConfigurer getFreemarkerConfig() {
        FreeMarkerConfigurer config = new FreeMarkerConfigurer();
        config.setTemplateLoaderPath("classpath:/templates");
        return config;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.ak.project"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title(applicationName)
                .description(swaggerApplicationDescription)
                .version(buildProperties != null ? buildProperties.getVersion() : "1.0.0")
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .displayRequestDuration(true)
                .validatorUrl("")
                .deepLinking(true)
                .displayOperationId(true)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .operationsSorter(OperationsSorter.METHOD)
                .defaultModelRendering(ModelRendering.MODEL)
                .build();
    }


    @Bean
    public Docket api(ServletContext servletContext) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("org.ak.project"))
                .paths(PathSelectors.any()).build()
                .apiInfo(this.metaData())
                .securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private SecurityContext securityContext() {

        List<Predicate<String>> pathPattern = new ArrayList<>();

        List<Predicate<String>> exclPathPattern = new ArrayList<>();

        securedPaths.getSecuredResources()
                .forEach(sp -> Arrays.asList(sp.getPaths().split(",\\s*"))
                        .forEach(path->{
                            pathPattern.add(ant(path));


                        }));

        Arrays.asList(ignoredPaths.split(",\\s*"))
                .forEach(path-> exclPathPattern.add(Predicates.not(ant(path))));


        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(Predicates.and(Predicates.or(pathPattern),Predicates.and(exclPathPattern)))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }
}
