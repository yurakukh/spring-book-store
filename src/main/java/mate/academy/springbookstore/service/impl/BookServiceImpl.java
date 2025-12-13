package mate.academy.springbookstore.service.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.springbookstore.dto.book.BookDto;
import mate.academy.springbookstore.dto.book.CreateBookRequestDto;
import mate.academy.springbookstore.exception.EntityNotFoundException;
import mate.academy.springbookstore.mapper.BookMapper;
import mate.academy.springbookstore.model.Book;
import mate.academy.springbookstore.model.Category;
import mate.academy.springbookstore.repository.BookRepository;
import mate.academy.springbookstore.repository.CategoryRepository;
import mate.academy.springbookstore.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        Set<Category> categories = new HashSet<>(
                categoryRepository.findAllById(requestDto.getCategoryIds())
        );
        book.setCategories(categories);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
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
