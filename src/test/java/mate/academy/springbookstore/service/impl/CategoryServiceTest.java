package mate.academy.springbookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import mate.academy.springbookstore.dto.category.CategoryDto;
import mate.academy.springbookstore.dto.category.CreateCategoryRequestDto;
import mate.academy.springbookstore.exception.EntityNotFoundException;
import mate.academy.springbookstore.mapper.CategoryMapper;
import mate.academy.springbookstore.model.Category;
import mate.academy.springbookstore.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify save() method works correctly")
    void save_ValidRequestDto_ReturnsCategoryDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Fantasy", "Magic world");
        Category category = new Category();
        category.setName(requestDto.name());
        category.setDescription(requestDto.description());

        CategoryDto expectedDto = new CategoryDto(1L, "Fantasy", "Magic world");

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        CategoryDto actualDto = categoryService.save(requestDto);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("Verify findById() returns CategoryDto when exists")
    void findById_ValidId_ReturnsCategoryDto() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantasy");

        CategoryDto expectedDto = new CategoryDto(categoryId, "Fantasy", "Description");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        CategoryDto actualDto = categoryService.findById(categoryId);

        assertEquals(expectedDto, actualDto);
        assertEquals(categoryId, actualDto.id());
    }

    @Test
    @DisplayName("Verify findById() throws EntityNotFoundException when not found")
    void findById_InvalidId_ThrowsException() {
        Long categoryId = 999L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.findById(categoryId)
        );
        assertEquals("Can't find category with id " + categoryId, exception.getMessage());
    }

    @Test
    @DisplayName("Verify update() method works correctly")
    void update_ValidIdAndDto_ReturnsUpdatedDto() {
        Long categoryId = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Updated Name", "Updated Desc");
        Category category = new Category();
        category.setId(categoryId);

        CategoryDto expectedDto = new CategoryDto(categoryId, "Updated Name", "Updated Desc");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        CategoryDto actualDto = categoryService.update(categoryId, requestDto);

        assertEquals(expectedDto, actualDto);
        verify(categoryMapper).updateModel(category, requestDto);
        verify(categoryRepository).save(category);
    }
}
