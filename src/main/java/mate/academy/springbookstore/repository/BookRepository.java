package mate.academy.springbookstore.repository;

import mate.academy.springbookstore.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
