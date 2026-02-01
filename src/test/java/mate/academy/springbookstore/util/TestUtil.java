package mate.academy.springbookstore.util;

import java.math.BigDecimal;
import java.util.List;
import mate.academy.springbookstore.dto.book.BookDto;
import mate.academy.springbookstore.dto.book.CreateBookRequestDto;
import mate.academy.springbookstore.dto.category.CategoryDto;
import mate.academy.springbookstore.dto.category.CreateCategoryRequestDto;
import mate.academy.springbookstore.model.Book;
import mate.academy.springbookstore.model.Category;

public class TestUtil {
    private TestUtil() {
    }

    public static CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto("Novel", "Classic novels");
    }

    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle("Process");
        dto.setAuthor("F. Kafka");
        dto.setIsbn("978-0743273565");
        dto.setPrice(BigDecimal.valueOf(66.66));
        dto.setCategoryIds(List.of(1L));
        return dto;
    }

    public static Category createCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Novel");
        return category;
    }

    public static Book createBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Process");
        book.setAuthor("F. Kafka");
        book.setIsbn("978-0743273565");
        book.setPrice(BigDecimal.valueOf(66.66));
        return book;
    }

    public static BookDto createBookDto() {
        BookDto dto = new BookDto();
        dto.setId(1L);
        dto.setTitle("Process");
        dto.setAuthor("F. Kafka");
        return dto;
    }

    public static CategoryDto createCategoryDto(Long id) {
        return new CategoryDto(id, "Novel", "Classic novels");
    }
}
