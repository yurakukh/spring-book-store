package mate.academy.springbookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import mate.academy.springbookstore.model.OrderStatus;

public record UpdateOrderRequestDto(
        @NotNull(message = "{order.request.status.notnull}")
        OrderStatus status
) {
}
