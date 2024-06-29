package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookJsonTests {

    @Autowired
    private JacksonTester<Book> json;

    @Test
    void testSerialize() throws IOException {
        var now = Instant.now();
        var book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .author("Author")
                .price(9.90)
                .createdDate(now)
                .lastModifiedDate(now)
                .version(21)
                .build();
        var jsonContent = json.write(book);

        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn").isEqualTo(book.getIsbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title").isEqualTo(book.getTitle());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author").isEqualTo(book.getAuthor());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price").isEqualTo(book.getPrice());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate").isEqualTo(book.getCreatedDate().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate").isEqualTo(book.getLastModifiedDate().toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version").isEqualTo(book.getVersion());
    }

    @Test
    void testDeserialize() throws IOException {
        var instant = Instant.parse("2021-09-07T22:50:37.135029Z");
        var content = """
                {
                    "isbn": "1234567890",
                    "title": "Title",
                    "author": "Author",
                    "price": 9.90,
                    "createdDate": "2021-09-07T22:50:37.135029Z",
                    "lastModifiedDate": "2021-09-07T22:50:37.135029Z",
                    "version": 21
                }
                """;

        var book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .author("Author")
                .price(9.90)
                .createdDate(instant)
                .lastModifiedDate(instant)
                .version(21)
                .build();

        assertThat(json.parse(content).getObject())
                .usingRecursiveComparison()
                .isEqualTo(book);
    }

}
