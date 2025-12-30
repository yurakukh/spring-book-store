package mate.academy.springbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.springbookstore.dto.cartitem.CartItemResponseDto;
import mate.academy.springbookstore.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.springbookstore.exception.EntityNotFoundException;
import mate.academy.springbookstore.mapper.CartItemMapper;
import mate.academy.springbookstore.mapper.ShoppingCartMapper;
import mate.academy.springbookstore.model.Book;
import mate.academy.springbookstore.model.CartItem;
import mate.academy.springbookstore.model.ShoppingCart;
import mate.academy.springbookstore.model.User;
import mate.academy.springbookstore.repository.BookRepository;
import mate.academy.springbookstore.repository.CartItemRepository;
import mate.academy.springbookstore.repository.ShoppingCartRepository;
import mate.academy.springbookstore.service.ShoppingCartService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public Page<CartItemResponseDto> getCartItemsByUser(User user, Pageable pageable) {
        return cartItemRepository.findByShoppingCartUserId(user.getId(), pageable)
                .map(cartItemMapper::toDto);
    }

    @Override
    public ShoppingCartResponseDto addBookToShoppingCart(
            User user,
            CreateCartItemRequestDto requestDto
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart by user " + user.getId())
        );

        Book book = bookRepository.findById(requestDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id " + requestDto.bookId())
        );

        CartItem existingCartItem = cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCart.getId(), book.getId())
                .orElseGet(() -> {
                    CartItem cartItem = new CartItem();
                    cartItem.setShoppingCart(shoppingCart);
                    cartItem.setBook(book);
                    cartItem.setQuantity(0);
                    return cartItem;
                });
        existingCartItem.setQuantity(existingCartItem.getQuantity() + requestDto.quantity());
        cartItemRepository.save(existingCartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public CartItemResponseDto updateCartItem(
            Long cartItemId,
            UpdateCartItemRequestDto requestDto,
            User user
    ) {
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartUserId(cartItemId, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find cart item " + cartItemId + " for user " + user.getId()
                        )
                );
        cartItem.setQuantity(requestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void removeCartItem(Long cartItemId, User user) {
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartUserId(cartItemId, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find cart item " + cartItemId + " for user " + user.getId()
                        )
                );
        cartItemRepository.delete(cartItem);
    }
}
