package com.velaris.core.repository.view;

import com.velaris.core.entity.view.StatsCategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatsCategoryViewRepository extends JpaRepository<StatsCategoryView, Long> {
    List<StatsCategoryView> findAllByOwnerId(Long ownerId);
}
