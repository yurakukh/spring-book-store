package mate.academy.springbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.springbookstore.dto.order.CreateOrderRequestDto;
import mate.academy.springbookstore.dto.order.OrderResponseDto;
import mate.academy.springbookstore.dto.order.UpdateOrderRequestDto;
import mate.academy.springbookstore.dto.orderitem.OrderItemResponseDto;
import mate.academy.springbookstore.model.User;
import mate.academy.springbookstore.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Place order", description = "Place order")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public OrderResponseDto placeOrder(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateOrderRequestDto requestDto
    ) {
        return orderService.placeOrder(user, requestDto);
    }

    @Operation(summary = "Get orders", description = "Retrieve order history")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<OrderResponseDto> getOrders(
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        return orderService.getUserOrders(user, pageable);
    }

    @Operation(summary = "Get order items", description = "Retrieve all order items for order")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getOrderItems(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId
    ) {
        return orderService.getOrderItems(user, orderId);
    }

    @Operation(
            summary = "Get order item",
            description = "Retrieve a specific order item within an order")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{id}")
    public OrderItemResponseDto getOrderItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @PathVariable Long id
    ) {
        return orderService.getOrderItem(user, orderId, id);
    }

    @Operation(summary = "Update status", description = "Update order status")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderRequestDto requestDto) {
        return orderService.updateStatus(id, requestDto.status());
    }
}
