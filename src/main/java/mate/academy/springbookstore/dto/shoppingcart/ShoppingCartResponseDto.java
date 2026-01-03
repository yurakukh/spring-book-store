package mate.academy.springbookstore.dto.shoppingcart;

import java.util.Set;
import mate.academy.springbookstore.dto.cartitem.CartItemResponseDto;

public record ShoppingCartResponseDto(
        Long id,
        Long userId,
        Set<CartItemResponseDto> cartItems
) {
}
