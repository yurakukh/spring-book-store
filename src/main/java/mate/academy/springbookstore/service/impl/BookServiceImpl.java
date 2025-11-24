package mate.academy.springbookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.springbookstore.dto.BookDto;
import mate.academy.springbookstore.dto.CreateBookRequestDto;
import mate.academy.springbookstore.exception.EntityNotFoundException;
import mate.academy.springbookstore.mapper.BookMapper;
import mate.academy.springbookstore.model.Book;
import mate.academy.springbookstore.repository.BookRepository;
import mate.academy.springbookstore.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id " + id));
        bookMapper.updateModel(book, requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
