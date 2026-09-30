package com.example.demo.annotation;

import com.example.demo.validator.BookIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookIdValidator.class)
public @interface ExistingBookId {

    String message() default "Sách không tồn tại trong hệ thống";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}