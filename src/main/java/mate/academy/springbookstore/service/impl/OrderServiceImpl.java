package mate.academy.springbookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.springbookstore.dto.order.CreateOrderRequestDto;
import mate.academy.springbookstore.dto.order.OrderResponseDto;
import mate.academy.springbookstore.dto.orderitem.OrderItemResponseDto;
import mate.academy.springbookstore.exception.EntityNotFoundException;
import mate.academy.springbookstore.mapper.OrderItemMapper;
import mate.academy.springbookstore.mapper.OrderMapper;
import mate.academy.springbookstore.model.Order;
import mate.academy.springbookstore.model.OrderItem;
import mate.academy.springbookstore.model.OrderStatus;
import mate.academy.springbookstore.model.ShoppingCart;
import mate.academy.springbookstore.model.User;
import mate.academy.springbookstore.repository.OrderRepository;
import mate.academy.springbookstore.repository.ShoppingCartRepository;
import mate.academy.springbookstore.service.OrderService;
import mate.academy.springbookstore.service.ShoppingCartService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto placeOrder(User user, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart for user "
                                + user.getId())
        );

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new RuntimeException("Can't place order, cart is empty for user with id "
                    + user.getId());
        }

        Order order = createOrder(user, requestDto.shippingAddress());

        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    return orderItem;
                })
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        order.setTotal(calculateTotal(orderItems));

        orderRepository.save(order);
        shoppingCartService.clearShoppingCart(user);

        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderResponseDto> getUserOrders(User user, Pageable pageable) {
        return orderRepository.findByUserId(user.getId(), pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderResponseDto updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order with id "
                        + orderId));

        order.setStatus(status);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItems(User user, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find order with id " + orderId + " for user " + user.getId()
                        )
                );

        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItem(User user, Long orderId, Long itemId) {
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find order with id " + orderId + " for user " + user.getId()
                        )
                );

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find order item with id " + itemId + " in order " + orderId
                        )
                );

        return orderItemMapper.toDto(orderItem);
    }

    private Order createOrder(User user, String shippingAddress) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);
        return order;
    }

    private BigDecimal calculateTotal(Set<OrderItem> items) {
        return items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
