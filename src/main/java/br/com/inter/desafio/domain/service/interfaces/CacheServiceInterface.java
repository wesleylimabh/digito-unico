package br.com.inter.desafio.domain.service.interfaces;

import br.com.inter.desafio.api.dto.SingleDigitDTO;

public interface CacheServiceInterface {
    void put(SingleDigitDTO dto, Integer result);

    Integer get(SingleDigitDTO dto);
}
