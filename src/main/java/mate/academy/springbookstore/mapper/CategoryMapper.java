package mate.academy.springbookstore.mapper;

import mate.academy.springbookstore.config.MapperConfig;
import mate.academy.springbookstore.dto.category.CategoryDto;
import mate.academy.springbookstore.dto.category.CreateCategoryRequestDto;
import mate.academy.springbookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);

    void updateModel(@MappingTarget Category category, CreateCategoryRequestDto requestDto);
}
