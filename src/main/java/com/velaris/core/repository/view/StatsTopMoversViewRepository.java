package com.velaris.core.repository.view;

import com.velaris.core.entity.view.StatsTopMoversView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatsTopMoversViewRepository extends JpaRepository<StatsTopMoversView, Long> {
    List<StatsTopMoversView> findByOwnerIdOrderByDeltaValueDesc(Long ownerId);
}
