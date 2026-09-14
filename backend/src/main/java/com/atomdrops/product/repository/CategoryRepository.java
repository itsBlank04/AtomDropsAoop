package com.atomdrops.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.atomdrops.product.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderBySortOrderAscNameAsc();
}
