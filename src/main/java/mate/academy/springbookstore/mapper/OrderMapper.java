package mate.academy.springbookstore.mapper;

import mate.academy.springbookstore.config.MapperConfig;
import mate.academy.springbookstore.dto.order.OrderResponseDto;
import mate.academy.springbookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "orderItems")
    OrderResponseDto toDto(Order order);
}
