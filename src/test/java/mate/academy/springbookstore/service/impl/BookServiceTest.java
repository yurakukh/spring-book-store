package mate.academy.springbookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import mate.academy.springbookstore.dto.book.BookDto;
import mate.academy.springbookstore.dto.book.CreateBookRequestDto;
import mate.academy.springbookstore.exception.EntityNotFoundException;
import mate.academy.springbookstore.mapper.BookMapper;
import mate.academy.springbookstore.model.Book;
import mate.academy.springbookstore.model.Category;
import mate.academy.springbookstore.repository.BookRepository;
import mate.academy.springbookstore.repository.CategoryRepository;
import mate.academy.springbookstore.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save book - success")
    void save_ValidRequest_ReturnsBookDto() {
        CreateBookRequestDto requestDto = TestUtil.createBookRequestDto();
        Book book = TestUtil.createBook();
        Category category = TestUtil.createCategory();
        BookDto expectedDto = TestUtil.createBookDto();

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(categoryRepository.findAllById(requestDto.getCategoryIds()))
                .thenReturn(List.of(category));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actual = bookService.save(requestDto);

        assertEquals(expectedDto, actual);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("Find book by id - success")
    void findById_ValidId_ReturnsBookDto() {
        Long id = 1L;
        Book book = TestUtil.createBook();
        BookDto dto = TestUtil.createBookDto();

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(dto);

        BookDto result = bookService.findById(id);

        assertEquals(dto, result);
    }

    @Test
    @DisplayName("Find book by id - not found")
    void findById_InvalidId_ThrowsException() {
        Long id = 99L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(id)
        );

        assertEquals("Can't find book with id " + id, ex.getMessage());
    }

    @Test
    @DisplayName("Find all books")
    void findAll_ReturnsPageOfBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Book book = TestUtil.createBook();
        BookDto dto = TestUtil.createBookDto();

        when(bookRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDto(book)).thenReturn(dto);

        Page<BookDto> result = bookService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
    }

    @Test
    @DisplayName("Update book - success")
    void update_ValidId_ReturnsUpdatedBookDto() {
        Long id = 1L;
        CreateBookRequestDto requestDto = TestUtil.createBookRequestDto();
        Book book = TestUtil.createBook();
        BookDto dto = TestUtil.createBookDto();

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(dto);

        BookDto result = bookService.update(id, requestDto);

        assertEquals(dto, result);
        verify(bookMapper).updateModel(book, requestDto);
    }

    @Test
    @DisplayName("Update book - not found")
    void update_InvalidId_ThrowsException() {
        Long id = 404L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.update(id, TestUtil.createBookRequestDto())
        );

        assertEquals("Can't find book with id " + id, ex.getMessage());
    }

    @Test
    @DisplayName("Delete book by id")
    void deleteById_ValidId_CallsRepository() {
        Long id = 1L;
        bookService.deleteById(id);
        verify(bookRepository).deleteById(id);
    }
}
