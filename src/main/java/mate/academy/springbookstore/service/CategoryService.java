package mate.academy.springbookstore.service;

import mate.academy.springbookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.springbookstore.dto.category.CategoryDto;
import mate.academy.springbookstore.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CreateCategoryRequestDto requestDto);

    Page<CategoryDto> findAll(Pageable pageable);

    Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto update(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);
}
