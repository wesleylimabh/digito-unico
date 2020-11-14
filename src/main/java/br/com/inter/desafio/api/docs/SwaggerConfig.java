package br.com.inter.desafio.api.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiDoc() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(getApiInfo())
                .tags(new Tag("User", "Endpoints para operações CRUD de Usuarios"))
                .tags(new Tag("SingleDigit", "Endpoints para operações de cálculo do dígito único"))
                .select().apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build();
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Desafio API")
                .description("API de cálculo de digito único proposto no desafio do Banco Inter")
                .version("0.0.1")
                .license("MIT")
                .licenseUrl("https://opensource.org/licenses/MIT")
                .contact(new Contact("Wesley Lima", "https://www.linkedin.com/in/wesleylima15", "wesleylima@gmx.com"))
                .build();
    }



}
