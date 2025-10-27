package com.velaris.core.service;

import com.velaris.api.model.*;
import com.velaris.core.mapper.StatsMapper;
import com.velaris.core.repository.StatsRepository;
import com.velaris.core.repository.view.StatsTrendDiffViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final StatsTrendDiffViewRepository diffViewRepository;
    private final StatsMapper mapper;

    public List<CategoryItem> getCategoryStats(UUID ownerId) {
        return mapList(statsRepository.getCategoryStats(ownerId), mapper::toDto);
    }

    public List<TrendItem> getTrendStats(UUID ownerId, TrendRequest request) {
        OffsetDateTime[] range = resolveDateRange(request.getStartDate(), request.getEndDate(), request.getPeriod());
        return mapList(statsRepository.getTrendStats(ownerId, range[0], range[1]), mapper::toDto);
    }

    public OverviewItem getOverview(UUID ownerId) {
        return mapper.toDto(statsRepository.getOverview(ownerId));
    }

    public List<TagItem> getTagsStats(UUID ownerId) {
        return mapList(statsRepository.getTagsStats(ownerId), mapper::toDto);
    }

    public List<TrendDiffItem> getTrendDiffStats(UUID ownerId) {
        return mapList(diffViewRepository.findByOwnerIdOrderByDateAsc(ownerId), mapper::toDto);
    }

    public List<TopHoldingItem> getTopHoldings(UUID ownerId, SearchFilter searchFilter) {
        Pageable pageable = buildPageable(searchFilter.getPaginationRequest(), searchFilter.getSortRequest());
        return mapList(statsRepository.getTopHoldings(ownerId, pageable), mapper::toDto);
    }

    public List<CategoryTrendItem> getCategoryTrend(UUID ownerId, CategoryTrendRequest request) {
        OffsetDateTime[] range = resolveDateRange(request.getStartDate(), request.getEndDate(), request.getPeriod());
        return mapList(statsRepository.getCategoryTrend(ownerId, request.getCategory(), range[0], range[1]), mapper::toDto);
    }

    private OffsetDateTime[] resolveDateRange(OffsetDateTime start, OffsetDateTime end, Period period) {
        OffsetDateTime resolvedEnd = end != null ? end : OffsetDateTime.now();
        OffsetDateTime resolvedStart = start != null ? start : switch (period) {
            case MONTH -> resolvedEnd.minusMonths(1);
            case YEAR -> resolvedEnd.minusYears(1);
            default -> resolvedEnd.minusWeeks(1);
        };
        if (resolvedStart.isAfter(resolvedEnd)) {
            throw new IllegalArgumentException("startDate cannot be after endDate");
        }
        return new OffsetDateTime[]{resolvedStart, resolvedEnd};
    }

    private Pageable buildPageable(PaginationRequest paginationRequest, SortRequest sortRequest) {
        int resolvedPage = paginationRequest.getPage() != null ? paginationRequest.getPage() : 0;
        int resolvedSize = paginationRequest.getSize() != null ? paginationRequest.getSize() : 10;
        String resolvedSortBy = sortRequest.getSortBy();
        Sort.Direction direction;
        try {
            direction = sortRequest.getSortDirection() != null ? Sort.Direction.valueOf(sortRequest.getSortDirection().getValue()) : Sort.Direction.DESC;
        } catch (IllegalArgumentException e) {
            direction = Sort.Direction.DESC;
        }
        return PageRequest.of(resolvedPage, resolvedSize, Sort.by(direction, resolvedSortBy));
    }

    private <T, R> List<R> mapList(List<T> list, Function<T, R> mapperFunc) {
        return list.stream().map(mapperFunc).toList();
    }
}