package mate.academy.springbookstore.service;

import mate.academy.springbookstore.dto.cartitem.CartItemResponseDto;
import mate.academy.springbookstore.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.springbookstore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    Page<CartItemResponseDto> getCartItemsByUser(User user, Pageable pageable);

    ShoppingCartResponseDto addBookToShoppingCart(User user, CreateCartItemRequestDto requestDto);

    CartItemResponseDto updateCartItem(
            Long cartItemId,
            UpdateCartItemRequestDto requestDto,
            User user
    );

    void removeCartItem(Long cartItemId, User user);
}
