package com.velaris.core.repository.view;

import com.velaris.core.entity.view.StatsTrendView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatsTrendViewRepository extends JpaRepository<StatsTrendView, Long> {
    List<StatsTrendView> findAllByOwnerId(Long ownerId);
}
