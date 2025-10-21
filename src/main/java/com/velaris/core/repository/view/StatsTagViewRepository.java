package com.velaris.core.repository.view;

import com.velaris.core.entity.view.StatsTagView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatsTagViewRepository extends JpaRepository<StatsTagView, Long> {
    List<StatsTagView> findByOwnerId(Long ownerId);
}
