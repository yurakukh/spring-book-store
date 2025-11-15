package mate.academy.springbookstore.repository.impl;

import java.util.List;
import mate.academy.springbookstore.model.Book;
import mate.academy.springbookstore.repository.BookRepository;

public class BookRepositoryImpl implements BookRepository {
    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return List.of();
    }
}
