package com.velaris.core.repository.view;

import com.velaris.core.entity.view.StatsCategoryTrendView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatsCategoryTrendViewRepository extends JpaRepository<StatsCategoryTrendView, Long> {
    List<StatsCategoryTrendView> findByOwnerIdAndCategoryOrderByCreatedDateAsc(Long ownerId, String category);
}
