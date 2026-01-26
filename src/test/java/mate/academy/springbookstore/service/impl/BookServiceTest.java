package mate.academy.springbookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @DisplayName("Verify save() method works correctly")
    void save_ValidRequestDto_ReturnsBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setCategoryIds(List.of(1L));

        Book book = new Book();
        Category category = new Category();
        category.setId(1L);

        BookDto expectedDto = new BookDto();
        expectedDto.setTitle("Test Book");

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(categoryRepository.findAllById(requestDto.getCategoryIds())).thenReturn(List.of(category));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actualDto = bookService.save(requestDto);

        assertEquals(expectedDto, actualDto);
        verify(bookRepository, times(1)).save(book);
        verify(categoryRepository, times(1)).findAllById(any());
    }

    @Test
    @DisplayName("Verify findById() returns BookDto when book exists")
    void findById_ValidId_ReturnsBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        BookDto expectedDto = new BookDto();
        expectedDto.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actualDto = bookService.findById(bookId);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Verify findById() throws exception when book doesn't exist")
    void findById_InvalidId_ThrowsException() {
        Long bookId = 100L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.findById(bookId);
        });

        String expectedMessage = "Can't find book with id " + bookId;
        assertEquals(expectedMessage, exception.getMessage());
    }
}
