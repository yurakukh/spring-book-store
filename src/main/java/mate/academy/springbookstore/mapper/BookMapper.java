package mate.academy.springbookstore.mapper;

import mate.academy.springbookstore.config.MapperConfig;
import mate.academy.springbookstore.dto.BookDto;
import mate.academy.springbookstore.dto.CreateBookRequestDto;
import mate.academy.springbookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateModel(@MappingTarget Book book, CreateBookRequestDto requestDto);
}
