package mate.academy.springbookstore.service;

import java.util.List;
import mate.academy.springbookstore.dto.BookDto;
import mate.academy.springbookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);
}
