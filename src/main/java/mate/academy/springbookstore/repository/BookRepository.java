package mate.academy.springbookstore.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.springbookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findById(Long id);

    List<Book> findAll();
}
