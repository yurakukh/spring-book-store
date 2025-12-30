package mate.academy.springbookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateCartItemRequestDto(
        @NotNull(message = "{cartitem.request.bookid.notnull}")
        Long bookId,
        @Min(value = 1, message = "{cartitem.request.quantity.min}")
        int quantity
) {
}
