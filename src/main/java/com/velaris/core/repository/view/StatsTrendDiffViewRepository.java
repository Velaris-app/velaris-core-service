package com.velaris.core.repository.view;

import com.velaris.core.entity.view.StatsTrendDiffView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatsTrendDiffViewRepository extends JpaRepository<StatsTrendDiffView, Long> {
    List<StatsTrendDiffView> findByOwnerIdOrderByDateAsc(Long ownerId);
}
