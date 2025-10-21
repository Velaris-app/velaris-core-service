package com.velaris.core.repository.view;

import com.velaris.core.entity.view.RecentActivitiesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecentActivitiesViewRepository extends JpaRepository<RecentActivitiesView, Long> {
    List<RecentActivitiesView> findAllByOwnerId(Long ownerId);
}
