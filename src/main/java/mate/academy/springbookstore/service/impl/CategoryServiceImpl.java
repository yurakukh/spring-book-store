package mate.academy.springbookstore.service.impl;

import lombok.RequiredArgsConstructor;
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
import mate.academy.springbookstore.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found by id " + id);
        }
        Page<Book> books = bookRepository.findAllByCategoriesId(id, pageable);
        return books.map(bookMapper::toDtoWithoutCategories);

    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category with id " + id)
        );
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category with id " + id)
        );
        categoryMapper.updateModel(category, requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
