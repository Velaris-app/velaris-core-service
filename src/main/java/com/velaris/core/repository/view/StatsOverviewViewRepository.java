package com.velaris.core.repository.view;

import com.velaris.core.entity.view.StatsOverviewView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsOverviewViewRepository extends JpaRepository<StatsOverviewView, Long> {
    StatsOverviewView findByOwnerId(Long ownerId);
}
