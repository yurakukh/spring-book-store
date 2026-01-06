package mate.academy.springbookstore.service;

import java.util.List;
import mate.academy.springbookstore.dto.order.CreateOrderRequestDto;
import mate.academy.springbookstore.dto.order.OrderResponseDto;
import mate.academy.springbookstore.dto.orderitem.OrderItemResponseDto;
import mate.academy.springbookstore.model.OrderStatus;
import mate.academy.springbookstore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto placeOrder(User user, CreateOrderRequestDto requestDto);

    Page<OrderResponseDto> getUserOrders(User user, Pageable pageable);

    OrderResponseDto updateStatus(Long orderId, OrderStatus status);

    List<OrderItemResponseDto> getOrderItems(User user, Long orderId);

    OrderItemResponseDto getOrderItem(User user, Long orderId, Long itemId);
}
