package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookJsonTests {

    @Autowired
    private JacksonTester<Book> json;

    @Test
    public void testSerialize() throws IOException {
        Book book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .author("Author")
                .price(9.90)
                .version(1)
                .build();

        JsonContent<Book> jsonContent = json.write(book);

        assertThat(jsonContent).extractingJsonPathValue("@.isbn").isEqualTo(book.getIsbn());
        assertThat(jsonContent).extractingJsonPathValue("@.title").isEqualTo(book.getTitle());
        assertThat(jsonContent).extractingJsonPathValue("@.author").isEqualTo(book.getAuthor());
        assertThat(jsonContent).extractingJsonPathValue("@.price").isEqualTo(book.getPrice());
        assertThat(jsonContent).extractingJsonPathValue("@.version").isEqualTo(book.getVersion());
    }

    @Test
    public void deserialize() throws IOException {
        String content = "{\n" +
                "  \"isbn\": \"1234567890\",\n" +
                "  \"title\": \"Title\",\n" +
                "  \"author\": \"Author\",\n" +
                "  \"price\": 9.90,\n" +
                "  \"version\": 2\n" +
                "}";

        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(Book.builder()
                        .isbn("1234567890")
                        .title("Title")
                        .author("Author")
                        .price(9.90)
                        .version(2)
                        .build());
    }

}
