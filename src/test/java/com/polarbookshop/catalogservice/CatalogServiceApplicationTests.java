package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenPostRequestThenBookCreated() {
        Book expectedBook = Book.builder()
                .isbn("1231231231")
                .title("Title")
                .author("Author")
                .price(9.90)
                .build();

        webTestClient.post()
                .uri("/books")
                .bodyValue(expectedBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
                });

    }

    @Test
    public void whenGetRequestWithIdThenBookReturned() {
        String bookIsbn = "1231231230";
        Book bookToCreate = Book.builder()
                .isbn(bookIsbn)
                .title("Title")
                .author("Author")
                .price(9.90)
                .build();

        Book expectedBook = webTestClient
                .post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .value(book -> assertThat(book).isNotNull())
                .returnResult()
                .getResponseBody();

        assertThat(expectedBook).isNotNull();

        webTestClient.get()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Book.class)
                .value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
                });

    }

    @Test
    public void whenPutRequestThenBookUpdated() {
        String bookIsbn = "1231231232";
        Book bookToCreate = Book.builder()
                .isbn(bookIsbn)
                .title("Title")
                .author("Author")
                .price(9.90)
                .build();

        Book createdBook = webTestClient
                .post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .value(book -> assertThat(book).isNotNull())
                .returnResult()
                .getResponseBody();

        assertThat(createdBook).isNotNull();

        Book bookToUpdate = Book.builder()
                .isbn(createdBook.getIsbn())
                .title(createdBook.getTitle())
                .author(createdBook.getAuthor())
                .price(7.95)
                .version(createdBook.getVersion())
                .build();

        webTestClient
                .put()
                .uri("/books/" + bookIsbn)
                .bodyValue(bookToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .value(actualBook -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.getPrice()).isEqualTo(bookToUpdate.getPrice());
                });
    }

    @Test
    public void whenDeleteRequestThenBookDeleted() {
        String bookIsbn = "1231231233";
        Book bookToCreate = Book.builder()
                .isbn(bookIsbn)
                .title("Title")
                .author("Author")
                .price(9.90)
                .build();

        webTestClient
                .post()
                .uri("/books")
                .bodyValue(bookToCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .delete()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/books/" + bookIsbn)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(errorMessage ->
                    assertThat(errorMessage).isEqualTo("The book with ISBN " + bookIsbn + " was not found.")
                );
    }

}
