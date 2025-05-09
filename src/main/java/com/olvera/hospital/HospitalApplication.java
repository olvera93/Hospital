package com.olvera.hospital;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Hospital microservice REST API Documentation",
                description = "Hospital microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Gonzalo Olvera",
                        email = "example@hotmail.com",
                        url = "https://github.com/olvera93"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://github.com/olvera93"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Hospital microservice REST API Documentation",
                url = ""
        )
)
public class HospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }

}
