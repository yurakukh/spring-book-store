package mate.academy.springbookstore.dto.orderitem;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}
