package com.velaris.core.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

//    @Mock
//    private StatsRepository statsRepository;
//
//    @Mock
//    private StatsTrendDiffViewRepository diffViewRepository;
//
//    @Mock
//    private StatsMapper mapper;
//
//    @InjectMocks
//    private StatsService statsService;
//
//    private UUID ownerId;
//
//    @BeforeEach
//    void setUp() {
//        ownerId = UUID.randomUUID();
//    }
//
//    @Test
//    void getCategoryStats_ShouldReturnMappedDtos() {
//        var entity1 = new StatsCategoryViewEntity(); // zależnie od implementacji repo
//        var entity2 = new StatsCategoryViewEntity();
//        var dto1 = new CategoryItem().category("Cards");
//        var dto2 = new CategoryItem().category("Lego");
//
//        when(statsRepository.getCategoryStats(ownerId)).thenReturn(List.of(entity1, entity2));
//        when(mapper.toDto(entity1)).thenReturn(dto1);
//        when(mapper.toDto(entity2)).thenReturn(dto2);
//
//        var result = statsService.getCategoryStats(ownerId);
//
//        assertThat(result).containsExactly(dto1, dto2);
//        verify(statsRepository).getCategoryStats(ownerId);
//        verify(mapper, times(2)).toDto(any());
//    }
//
//    @Test
//    void getTrendStats_ShouldReturnMappedDtos() {
//        var entity1 = new StatsTrendViewEntity();
//        var entity2 = new StatsTrendViewEntity();
//        var dto1 = new TrendItem().date(LocalDate.now()).totalValue(BigDecimal.valueOf(100));
//        var dto2 = new TrendItem().date(LocalDate.now()).totalValue(BigDecimal.valueOf(200));
//
//        when(statsRepository.getTrendStats(eq(ownerId), any(), any())).thenReturn(List.of(entity1, entity2));
//        when(mapper.toDto(entity1)).thenReturn(dto1);
//        when(mapper.toDto(entity2)).thenReturn(dto2);
//
//        var request = new TrendRequest().period(Period.WEEK);
//        var result = statsService.getTrendStats(ownerId, request);
//
//        assertThat(result).containsExactly(dto1, dto2);
//        verify(statsRepository).getTrendStats(eq(ownerId), any(), any());
//        verify(mapper, times(2)).toDto(any());
//    }
//
//    @Test
//    void getOverview_ShouldReturnMappedDto() {
//        var entity = new StatsOverviewViewEntity();
//        var dto = new OverviewItem().totalValue(BigDecimal.valueOf(1000));
//
//        when(statsRepository.getOverview(ownerId)).thenReturn(entity);
//        when(mapper.toDto(entity)).thenReturn(dto);
//
//        var result = statsService.getOverview(ownerId);
//
//        assertThat(result).isEqualTo(dto);
//        verify(statsRepository).getOverview(ownerId);
//        verify(mapper).toDto(entity);
//    }
//
//    @Test
//    void getTrendDiffStats_ShouldReturnMappedDtos() {
//        var entity1 = new StatsTrendDiffViewEntity();
//        var entity2 = new StatsTrendDiffViewEntity();
//        var dto1 = new TrendDiffItem().date(LocalDate.now()).totalValue(BigDecimal.valueOf(100));
//        var dto2 = new TrendDiffItem().date(LocalDate.now()).totalValue(BigDecimal.valueOf(200));
//
//        when(diffViewRepository.findByOwnerIdOrderByDateAsc(ownerId)).thenReturn(List.of(entity1, entity2));
//        when(mapper.toDto(entity1)).thenReturn(dto1);
//        when(mapper.toDto(entity2)).thenReturn(dto2);
//
//        var result = statsService.getTrendDiffStats(ownerId);
//
//        assertThat(result).containsExactly(dto1, dto2);
//        verify(diffViewRepository).findByOwnerIdOrderByDateAsc(ownerId);
//        verify(mapper, times(2)).toDto(any());
//    }
}