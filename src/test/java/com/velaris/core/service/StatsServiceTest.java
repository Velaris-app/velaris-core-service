package com.velaris.core.service;

import com.velaris.api.model.StatsCategoryItem;
import com.velaris.api.model.StatsOverview;
import com.velaris.api.model.StatsTrendItem;
import com.velaris.core.entity.view.StatsCategoryView;
import com.velaris.core.entity.view.StatsOverviewView;
import com.velaris.core.entity.view.StatsTrendView;
import com.velaris.core.mapper.StatsMapper;
import com.velaris.core.repository.view.StatsCategoryViewRepository;
import com.velaris.core.repository.view.StatsOverviewViewRepository;
import com.velaris.core.repository.view.StatsTrendViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private StatsCategoryViewRepository categoryRepo;

    @Mock
    private StatsTrendViewRepository trendRepo;

    @Mock
    private StatsOverviewViewRepository overviewRepo;

    @Mock
    private StatsMapper mapper;

    @InjectMocks
    private StatsService statsService;

    private final Long ownerId = 123L;

    @BeforeEach
    void setUp() {
        reset(categoryRepo, trendRepo, overviewRepo, mapper);
    }

    @Test
    void getCategoryStats_ShouldReturnMappedDtos() {
        var entity1 = new StatsCategoryView();
        var entity2 = new StatsCategoryView();
        var dto1 = new StatsCategoryItem().category("Cards");
        var dto2 = new StatsCategoryItem().category("Lego");

        when(categoryRepo.findAllByOwnerId(ownerId)).thenReturn(List.of(entity1, entity2));
        when(mapper.toDto(entity1)).thenReturn(dto1);
        when(mapper.toDto(entity2)).thenReturn(dto2);

        var result = statsService.getCategoryStats(ownerId);

        assertThat(result).containsExactly(dto1, dto2);
        verify(categoryRepo).findAllByOwnerId(ownerId);
        verify(mapper, times(2)).toDto(any(StatsCategoryView.class));
    }

    @Test
    void getTrendStats_ShouldReturnMappedDtos() {
        var entity1 = new StatsTrendView();
        var entity2 = new StatsTrendView();
        var dto1 = new StatsTrendItem().date(OffsetDateTime.now()).totalValue(BigDecimal.valueOf(100));
        var dto2 = new StatsTrendItem().date(OffsetDateTime.now()).totalValue(BigDecimal.valueOf(200));

        when(trendRepo.findAllByOwnerId(ownerId)).thenReturn(List.of(entity1, entity2));
        when(mapper.toDto(entity1)).thenReturn(dto1);
        when(mapper.toDto(entity2)).thenReturn(dto2);

        var result = statsService.getTrendStats(ownerId);

        assertThat(result).containsExactly(dto1, dto2);
        verify(trendRepo).findAllByOwnerId(ownerId);
        verify(mapper, times(2)).toDto(any(StatsTrendView.class));
    }

    @Test
    void getOverview_ShouldReturnMappedDto() {
        var entity = new StatsOverviewView();
        var dto = new StatsOverview().totalValue(BigDecimal.valueOf(1000.0));

        when(overviewRepo.findByOwnerId(ownerId)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        var result = statsService.getOverview(ownerId);

        assertThat(result).isEqualTo(dto);
        verify(overviewRepo).findByOwnerId(ownerId);
        verify(mapper).toDto(entity);
    }

    @Test
    void getOverview_ShouldHandleNullEntity() {
        when(overviewRepo.findByOwnerId(ownerId)).thenReturn(null);
        when(mapper.toDto(isNull(StatsOverviewView.class))).thenReturn(null);

        var result = statsService.getOverview(ownerId);

        assertThat(result).isNull();
        verify(overviewRepo).findByOwnerId(ownerId);
        verify(mapper).toDto(isNull(StatsOverviewView.class));
    }
}