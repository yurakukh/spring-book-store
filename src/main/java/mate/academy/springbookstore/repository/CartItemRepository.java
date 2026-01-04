package mate.academy.springbookstore.repository;

import java.util.Optional;
import mate.academy.springbookstore.model.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @EntityGraph(attributePaths = {"book"})
    Page<CartItem> findByShoppingCartUserId(Long userId, Pageable pageable);

    Optional<CartItem> findByShoppingCartIdAndBookId(
            Long shoppingCartId,
            Long bookId
    );

    Optional<CartItem> findByIdAndShoppingCartUserId(
            Long cartItemId,
            Long userId
    );
}
