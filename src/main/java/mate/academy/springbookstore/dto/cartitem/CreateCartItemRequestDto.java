package mate.academy.springbookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @NotNull(message = "{cartitem.request.bookid.notnull}")
        @Positive
        Long bookId,
        @Positive(message = "{cartitem.request.quantity.min}")
        int quantity
) {
}
