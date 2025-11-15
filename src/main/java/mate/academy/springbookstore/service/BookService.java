package mate.academy.springbookstore.service;

import java.util.List;
import mate.academy.springbookstore.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
