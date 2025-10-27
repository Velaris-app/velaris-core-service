package com.velaris.core.service;

import com.velaris.api.model.Asset;
import com.velaris.core.entity.AssetEntity;
import com.velaris.core.entity.UserEntity;
import com.velaris.core.mapper.AssetMapper;
import com.velaris.core.repository.AssetRepository;
import com.velaris.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecentActivityService recentActivityService;

    @InjectMocks
    private AssetService assetService;

    private Asset assetDto;
    private AssetEntity assetEntity;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .username("owner")
                .email("owner@example.com")
                .passwordHash("pass")
                .build();

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
        assetEntity.setOwner(user);
    }

    @Test
    void testCreateAsset() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(assetMapper.toEntity(assetDto)).thenReturn(assetEntity);
        when(assetRepository.save(assetEntity)).thenReturn(assetEntity);
        when(assetMapper.toDto(assetEntity)).thenReturn(assetDto);

        Asset result = assetService.createAsset(user.getId(), assetDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Asset");
        verify(assetRepository).save(assetEntity);
    }

    @Test
    void testUpdateAsset() {
        Asset updatedDto = new Asset();
        updatedDto.setName("Updated Asset");

        when(assetRepository.findById(1L)).thenReturn(Optional.of(assetEntity));
        doAnswer(invocation -> {
            Asset dto = invocation.getArgument(0);
            AssetEntity existing = invocation.getArgument(1);
            existing.setName(dto.getName());
            return null;
        }).when(assetMapper).updateEntity(any(Asset.class), any(AssetEntity.class));

        when(assetMapper.toDto(assetEntity)).thenReturn(updatedDto);

        Asset result = assetService.updateAsset("1", updatedDto);

        assertThat(result.getName()).isEqualTo("Updated Asset");
        verify(assetRepository).save(assetEntity);
        verify(recentActivityService).logActivity(any(), any(), any()); // <- upewnij się, że logActivity jest wywołane
    }

    @Test
    void testDeleteAsset() {
        when(assetRepository.findById(1L)).thenReturn(Optional.of(assetEntity));

        assetService.deleteAsset("1");

        verify(assetRepository).deleteById(1L);
    }
}