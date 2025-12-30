package mate.academy.springbookstore.mapper;

import mate.academy.springbookstore.config.MapperConfig;
import mate.academy.springbookstore.dto.cartitem.CartItemResponseDto;
import mate.academy.springbookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);
}
