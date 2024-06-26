package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
class BookRepositoryJpaTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findBookByIsbnWhenExisting() {
        var bookIsbn = "1234561237";
        var book = Book.builder()
                .isbn(bookIsbn)
                .title("Title")
                .author("Author")
                .price(12.90)
                .publisher("Polarsophia")
                .build();

        entityManager.persist(book);

        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void findAllBooks() {
        var book1 = Book.builder()
                .isbn("1234561235")
                .title("Title")
                .author("Author")
                .price(12.90)
                .publisher("Polarsophia")
                .build();
        var book2 = Book.builder()
                .isbn("1234561236")
                .title("Another Title")
                .author("Author")
                .price(12.90)
                .publisher("Polarsophia")
                .build();

        entityManager.persist(book1);
        entityManager.persist(book2);

        Iterable<Book> actualBooks = bookRepository.findAll();

        assertThat(StreamSupport.stream(actualBooks.spliterator(), true)
                .filter(book -> book.getIsbn().equals(book1.getIsbn()) || book.getIsbn().equals(book2.getIsbn()))
                .toList()).hasSize(2);
    }

    @Test
    void findBookByIsbnWhenNotExisting() {
        Optional<Book> actualBook = bookRepository.findByIsbn("1234561238");
        assertThat(actualBook).isEmpty();
    }

    @Test
    void existsByIsbnWhenExisting() {
        var bookIsbn = "1234561239";
        var bookToCrate = Book.builder()
                .isbn(bookIsbn)
                .title("Title")
                .author("Author")
                .price(12.90)
                .publisher("Polarsophia")
                .build();
        entityManager.persist(bookToCrate);

        boolean existing = bookRepository.existsByIsbn(bookIsbn);

        assertThat(existing).isTrue();
    }

    @Test
    void existsByIsbnWhenNotExisting() {
        boolean existing = bookRepository.existsByIsbn("1234561240");
        assertThat(existing).isFalse();
    }

    @Test
    void deleteByIsbn() {
        var bookIsbn = "1234561241";
        var bookToCreate = Book.builder()
                .isbn(bookIsbn)
                .title("Title")
                .author("Author")
                .price(12.90)
                .publisher("Polarsophia")
                .build();
        entityManager.persist(bookToCreate);

        bookRepository.deleteByIsbn(bookIsbn);

        assertThat(entityManager.find(Book.class, bookIsbn)).isNull();
    }

}
