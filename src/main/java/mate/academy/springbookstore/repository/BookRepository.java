package mate.academy.springbookstore.repository;

import java.util.Optional;
import mate.academy.springbookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(Long id);

    Page<Book> findAllByCategoriesId(Long categoryId, Pageable pageable);
}
