package mate.academy.springbookstore.service;

import mate.academy.springbookstore.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.springbookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart(User user);

    ShoppingCartResponseDto addBookToShoppingCart(User user, CreateCartItemRequestDto requestDto);

    ShoppingCartResponseDto updateCartItem(
            Long cartItemId,
            UpdateCartItemRequestDto requestDto,
            User user
    );

    void removeCartItem(Long cartItemId, User user);

    void createShoppingCartForUser(User user);
}
