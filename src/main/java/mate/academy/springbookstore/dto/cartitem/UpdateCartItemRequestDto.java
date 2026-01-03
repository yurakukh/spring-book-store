package mate.academy.springbookstore.dto.cartitem;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequestDto(
        @Positive(message = "{cartitem.request.quantity.min}")
        int quantity
) {
}
