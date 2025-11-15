package mate.academy.springbookstore.service;

import mate.academy.springbookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
