package com.polarbookshop.catalogservice.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BookValidationTests {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void whenAllFieldsCorrectThenValidationSucceeds() {
        Book book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .author("Author")
                .price(9.90)
                .build();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenIsbnDefinedButIncorrectThenValidationFails() {
        Book book = Book.builder()
                .isbn("a234567890")
                .title("Title")
                .author("Author")
                .price(9.90)
                .build();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must be valid.");
    }

    @Test
    public void whenIsbnNotDefinedThenValidationFails() {
        Book book = Book.builder()
                .title("Title")
                .author("Author")
                .price(9.90)
                .build();

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book ISBN must be defined.");
    }

    @Test
    public void whenTitleIsNotDefinedThenValidationFails() {
        Book book = Book.builder()
                .isbn("1234567890")
                .author("Author")
                .price(9.90)
                .build();

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book title must be defined.");
    }

    @Test
    public void whenAuthorIsNotDefinedThenValidationFails() {
        Book book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .price(9.90)
                .build();

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book author must be defined.");
    }

    @Test
    public void whenPriceIsNotDefinedThenValidationFails() {
        Book book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .author("Author")
                .build();

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be defined.");
    }

    @Test
    public void whenPriceDefinedButZeroThenValidationFails() {
        Book book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .author("Author")
                .price(0.0)
                .build();

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be greater than zero.");
    }

    @Test
    public void whenPriceDefinedButNegativeThenValidationFails() {
        Book book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .author("Author")
                .price(-9.90)
                .build();

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be greater than zero.");
    }

}
