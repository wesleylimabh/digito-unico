package br.com.inter.desafio.domain.service;

import br.com.inter.desafio.api.dto.SingleDigitDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheServiceImplTest {

    @Test
    void shouldCacheResult_WhenRegisteringSingleDigit() {
        // Arrange
        CacheServiceImpl cacheService = new CacheServiceImpl();
        SingleDigitDTO dto = makeSingleDigitDTO(1);

        // Act
        cacheService.put(dto, 1);
        Integer result = cacheService.get(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result);
    }


    @Test
    void shouldStoreOnlyLastTenResults() {
        // Arrange
        CacheServiceImpl cacheService = new CacheServiceImpl();
        for (int i = 1; i <= 11 ; i++) {
            SingleDigitDTO dto = makeSingleDigitDTO(i);
            cacheService.put(dto, dto.getResult());
        }

        // Act
        Integer resutFirstEntry = cacheService.get(makeSingleDigitDTO(1));
        Integer resutSecondEntry = cacheService.get(makeSingleDigitDTO(2));
        Integer resutLastEntry = cacheService.get(makeSingleDigitDTO(11));

        // Assert
        assertNull(resutFirstEntry);

        assertNotNull(resutLastEntry);
        assertEquals(11, resutLastEntry);

        assertNotNull(resutSecondEntry);
        assertEquals(2, resutSecondEntry);

    }

    private SingleDigitDTO makeSingleDigitDTO(int i) {
        SingleDigitDTO dto = new SingleDigitDTO();
        dto.setNumber(String.valueOf(i));
        dto.setMultiplier(i);
        dto.setResult(i);
        return dto;
    }
}