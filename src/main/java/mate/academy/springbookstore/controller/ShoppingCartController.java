package mate.academy.springbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.springbookstore.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbookstore.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.springbookstore.model.User;
import mate.academy.springbookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get shopping cart", description = "Get user's shopping cart")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ShoppingCartResponseDto getShoppingCart(
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartService.getShoppingCart(user);
    }

    @Operation(summary = "Add book to shopping cart", description = "Add book to user's cart")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ShoppingCartResponseDto addBookToShoppingCart(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateCartItemRequestDto requestDto
    ) {
        return shoppingCartService.addBookToShoppingCart(user, requestDto);
    }

    @Operation(summary = "Update shopping cart", description = "Update books quantity in the cart")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartResponseDto updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartService.updateCartItem(cartItemId, requestDto, user);
    }

    @Operation(summary = "Delete shopping cart", description = "Delete user's shopping cart")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCartItem(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal User user
    ) {
        shoppingCartService.removeCartItem(cartItemId, user);
    }
}
