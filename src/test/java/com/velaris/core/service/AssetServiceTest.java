package com.velaris.core.service;

import com.velaris.api.model.Asset;
import com.velaris.core.entity.AssetEntity;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.mapper.AssetMapper;
import com.velaris.core.repository.jpa.JpaAssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private JpaAssetRepository jpaAssetRepository;

    @Mock
    private AssetMapper assetMapper;

    @InjectMocks
    private AssetService assetService;

    private Asset assetDto;
    private AssetEntity assetEntity;

    @BeforeEach
    void setUp() {
        assetDto = new Asset();
        assetDto.setId(1L);
        assetDto.setName("Test Asset");
        assetDto.setPurchasePrice(BigDecimal.valueOf(100));
        assetDto.setQuantity(2);

        assetEntity = new AssetEntity();
        assetEntity.setId(1L);
        assetEntity.setName("Test Asset");
        assetEntity.setPurchasePrice(BigDecimal.valueOf(100));
        assetEntity.setQuantity(2);
        assetEntity.setOwner(UserEntity.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .build());
    }

    @Test
    void testCreateAsset() {
        when(assetMapper.toEntity(assetDto)).thenReturn(assetEntity);
        when(jpaAssetRepository.save(assetEntity)).thenReturn(assetEntity);
        when(assetMapper.toDto(assetEntity)).thenReturn(assetDto);

        Asset result = assetService.createAsset(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), assetDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Asset");
        verify(jpaAssetRepository).save(assetEntity);
    }

    @Test
    void testGetAssetById() {
        when(jpaAssetRepository.findById(1L)).thenReturn(Optional.of(assetEntity));
        when(assetMapper.toDto(assetEntity)).thenReturn(assetDto);

        Asset result = assetService.getAssetById("1");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Asset");
        verify(jpaAssetRepository).findById(1L);
    }

    @Test
    void testGetAllAssets() {
        when(jpaAssetRepository.findAll()).thenReturn(List.of(assetEntity));
        when(assetMapper.toDto(assetEntity)).thenReturn(assetDto);

        List<Asset> result = assetService.getAllAssets();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Test Asset");
        verify(jpaAssetRepository).findAll();
    }

    @Test
    void testUpdateAsset() {
        Asset updatedDto = new Asset();
        updatedDto.setName("Updated Asset");

        when(jpaAssetRepository.findById(1L)).thenReturn(Optional.of(assetEntity));
        doAnswer(invocation -> {
            Asset dto = invocation.getArgument(0);
            AssetEntity existing = invocation.getArgument(1);
            existing.setName(dto.getName());
            return null;
        }).when(assetMapper).updateEntity(eq(updatedDto), eq(assetEntity));
        when(assetMapper.toDto(assetEntity)).thenReturn(updatedDto);

        Asset result = assetService.updateAsset("1", updatedDto);

        assertThat(result.getName()).isEqualTo("Updated Asset");
        verify(jpaAssetRepository).save(assetEntity);
    }

    @Test
    void testDeleteAsset() {
        assetService.deleteAsset("1");

        verify(jpaAssetRepository).deleteById(1L);
    }
}