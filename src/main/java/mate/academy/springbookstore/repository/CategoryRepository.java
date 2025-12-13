package mate.academy.springbookstore.repository;

import mate.academy.springbookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
