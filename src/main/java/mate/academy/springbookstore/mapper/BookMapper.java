package mate.academy.springbookstore.mapper;

import mate.academy.springbookstore.config.MapperConfig;
import mate.academy.springbookstore.dto.book.BookDto;
import mate.academy.springbookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.springbookstore.dto.book.CreateBookRequestDto;
import mate.academy.springbookstore.model.Book;
import mate.academy.springbookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateModel(@MappingTarget Book book, CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() == null) {
            return;
        }
        bookDto.setCategoryIds(
                book.getCategories()
                        .stream()
                        .map(Category::getId)
                        .toList()
        );
    }
}
