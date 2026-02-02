package mate.academy.springbookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.springbookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.springbookstore.dto.category.CategoryDto;
import mate.academy.springbookstore.dto.category.CreateCategoryRequestDto;
import mate.academy.springbookstore.exception.EntityNotFoundException;
import mate.academy.springbookstore.mapper.BookMapper;
import mate.academy.springbookstore.mapper.CategoryMapper;
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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save category - success")
    void save_ValidRequest_ReturnsCategoryDto() {
        CreateCategoryRequestDto requestDto = TestUtil.createCategoryRequestDto();
        Category category = TestUtil.createCategory();
        CategoryDto dto = TestUtil.createCategoryDto(1L);

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(dto);

        CategoryDto result = categoryService.save(requestDto);

        assertEquals(dto, result);
    }

    @Test
    @DisplayName("Find category by id - success")
    void findById_ValidId_ReturnsCategoryDto() {
        Long id = 1L;
        Category category = TestUtil.createCategory();
        CategoryDto dto = TestUtil.createCategoryDto(id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(dto);

        CategoryDto result = categoryService.findById(id);

        assertEquals(dto, result);
    }

    @Test
    @DisplayName("Find category by id - not found")
    void findById_InvalidId_ThrowsException() {
        Long id = 404L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.findById(id)
        );

        assertEquals("Can't find category with id " + id, ex.getMessage());
    }

    @Test
    @DisplayName("Find all categories")
    void findAll_ReturnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Category category = TestUtil.createCategory();
        CategoryDto dto = TestUtil.createCategoryDto(1L);

        when(categoryRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(category)));
        when(categoryMapper.toDto(category)).thenReturn(dto);

        Page<CategoryDto> result = categoryService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Get books by category - success")
    void getBooksByCategoryId_ValidId_ReturnsBooks() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 5);

        Book book = TestUtil.createBook();

        BookDtoWithoutCategoryIds expectedDto = new BookDtoWithoutCategoryIds(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getCoverImage()
        );

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(bookRepository.findAllByCategoriesId(categoryId, pageable))
                .thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(expectedDto);

        Page<BookDtoWithoutCategoryIds> result =
                categoryService.getBooksByCategoryId(categoryId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(expectedDto, result.getContent().get(0));
    }

    @Test
    @DisplayName("Get books by category - category not found")
    void getBooksByCategoryId_InvalidId_ThrowsException() {
        Long id = 99L;
        when(categoryRepository.existsById(id)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getBooksByCategoryId(id, PageRequest.of(0, 5))
        );

        assertEquals("Category not found by id " + id, ex.getMessage());
    }

    @Test
    @DisplayName("Update category - success")
    void update_ValidId_ReturnsUpdatedDto() {
        Long id = 1L;
        CreateCategoryRequestDto requestDto = TestUtil.createCategoryRequestDto();
        Category category = TestUtil.createCategory();
        CategoryDto dto = TestUtil.createCategoryDto(id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(dto);

        CategoryDto result = categoryService.update(id, requestDto);

        assertEquals(dto, result);
        verify(categoryMapper).updateModel(category, requestDto);
    }

    @Test
    @DisplayName("Update category - not found")
    void update_InvalidId_ThrowsException() {
        Long id = 404L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.update(id, TestUtil.createCategoryRequestDto())
        );

        assertEquals("Can't find category with id " + id, ex.getMessage());
    }

    @Test
    @DisplayName("Delete category by id")
    void deleteById_ValidId_CallsRepository() {
        Long id = 1L;
        categoryService.deleteById(id);
        verify(categoryRepository).deleteById(id);
    }
}
