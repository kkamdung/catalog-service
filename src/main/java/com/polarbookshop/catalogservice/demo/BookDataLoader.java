package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Profile("testdata")
@Component
public class BookDataLoader {

    private final BookRepository bookRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        bookRepository.deleteAll();

        Book book1 = Book.builder()
                .isbn("1234567891")
                .title("Northern Lights")
                .author("Lyra Silverstar")
                .price(9.90)
                .build();
        Book book2 = Book.builder()
                .isbn("1234567892")
                .title("Polar Journey")
                .author("Iorek Polarson")
                .price(12.90)
                .build();

        bookRepository.saveAll(Arrays.asList(book1, book2));
    }

}
