package mate.academy.springbookstore.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import mate.academy.springbookstore.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find book by valid ID and fetch categories")
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ValidId_ReturnsBookWithCategories() {
        Optional<Book> bookOptional = bookRepository.findById(1L);
        assertTrue(bookOptional.isPresent());
        Book book = bookOptional.get();
        assertEquals("Process", book.getTitle());

        assertFalse(book.getCategories().isEmpty());
        assertEquals(1, book.getCategories().size());
        assertEquals("Novel", book.getCategories().iterator().next().getName());
    }

    @Test
    @DisplayName("Find all books by valid category ID")
    @Sql(scripts = "/database/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/remove-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoriesId_ValidId_ReturnsPage() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> actual = bookRepository.findAllByCategoriesId(categoryId, pageable);

        assertEquals(1, actual.getTotalElements());
        assertEquals("Process", actual.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("Return empty page for non-existing category ID")
    void findAllByCategoriesId_NonExistingId_ReturnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> actual = bookRepository.findAllByCategoriesId(999L, pageable);

        assertTrue(actual.isEmpty());
    }
}