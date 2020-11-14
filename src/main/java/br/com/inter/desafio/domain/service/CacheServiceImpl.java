package br.com.inter.desafio.domain.service;

import br.com.inter.desafio.api.dto.SingleDigitDTO;
import br.com.inter.desafio.domain.service.interfaces.CacheServiceInterface;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CacheServiceImpl implements CacheServiceInterface {

    private static final Map<SingleDigitDTO, Integer> CACHE = new LinkedHashMap<>();
    public static final int MAXIMUM_CACHE_SIZE = 10;

    @Override
    public void put(SingleDigitDTO singleDigitDTO, Integer result) {
        CACHE.putIfAbsent(singleDigitDTO, result);
        if (CACHE.size() > MAXIMUM_CACHE_SIZE) {
            SingleDigitDTO key = CACHE.entrySet().iterator().next().getKey();
            CACHE.remove(key);
        }
    }

    @Override
    public Integer get(SingleDigitDTO dto) {
        return CACHE.get(dto);
    }
}
