package com.design.map.config;

import com.design.map.dto.GeometryModel;
import org.geojson.GeoJsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ServletContextAware;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.Date;

/**
 * @auther Meron Abraha 7/16/18
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig implements ServletContextAware {

    @Autowired
    private Environment environment;

    private ServletContext servletContext;

    @Bean
    public Docket api() {
        return new Docket( DocumentationType.SWAGGER_2 )
                .select()
                .apis( RequestHandlerSelectors.any() )
                .paths( PathSelectors.regex( "/api/.*" ) )
                .build()
                .apiInfo( apiInfo() )
                .directModelSubstitute( Date.class, String.class ) // Substitute String for Java Date in API model in Swagger-UI and api-docs JSON
                .directModelSubstitute( GeoJsonObject.class, GeometryModel.class )
                .pathProvider( new RelativePathProvider( servletContext ) )
                .useDefaultResponseMessages( false );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title( environment.getRequiredProperty( "swagger.api.info.title" ) )
                .description( environment.getRequiredProperty( "swagger.api.info.description" ) )
                .contact( new Contact(
                        environment.getRequiredProperty( "swagger.api.info.contact-name" ),
                        environment.getRequiredProperty( "swagger.api.info.contact-url" ),
                        environment.getRequiredProperty( "swagger.api.info.contact-email" ) ) )
                .version( environment.getRequiredProperty( "swagger.api.info.version" ) )
                .termsOfServiceUrl( environment.getRequiredProperty( "swagger.api.info.terms-of-service-url" ) )
                .license( environment.getRequiredProperty( "swagger.api.info.license" ) )
                .licenseUrl( environment.getRequiredProperty( "swagger.api.info.license-url" ) )
                .build();
    }

    @Override
    public void setServletContext( ServletContext servletContext ) {
        this.servletContext = servletContext;
    }
}