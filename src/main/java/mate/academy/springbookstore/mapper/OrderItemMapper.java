package mate.academy.springbookstore.mapper;

import mate.academy.springbookstore.config.MapperConfig;
import mate.academy.springbookstore.dto.orderitem.OrderItemResponseDto;
import mate.academy.springbookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
