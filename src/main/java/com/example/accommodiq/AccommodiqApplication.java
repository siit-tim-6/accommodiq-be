package com.example.accommodiq;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccommodiqApplication {
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure().buildValidatorFactory();
        return validatorFactory.getValidator();
    }

    public static void main(String[] args) {
        SpringApplication.run(AccommodiqApplication.class, args);
    }

}
