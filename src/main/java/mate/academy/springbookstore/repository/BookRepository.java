package mate.academy.springbookstore.repository;

import java.util.List;
import mate.academy.springbookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
