package mate.academy.springbookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequestDto(
        @NotBlank(message = "{order.request.shippingaddress.notblank}")
        @Size(max = 255, message = "{order.request.shippingaddress.size}")
        String shippingAddress
) {
}
