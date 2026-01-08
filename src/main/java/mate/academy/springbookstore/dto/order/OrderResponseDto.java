package mate.academy.springbookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import mate.academy.springbookstore.dto.orderitem.OrderItemResponseDto;
import mate.academy.springbookstore.model.OrderStatus;

public record OrderResponseDto(
        Long id,
        Long userId,
        Set<OrderItemResponseDto> orderItems,
        LocalDateTime orderDate,
        BigDecimal total,
        OrderStatus status
) {
}
