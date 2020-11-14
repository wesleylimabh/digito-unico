package br.com.inter.desafio.domain.service;

import br.com.inter.desafio.domain.model.SingleDigit;
import br.com.inter.desafio.domain.repository.SingleDigitRepository;
import br.com.inter.desafio.domain.service.interfaces.CacheServiceInterface;
import br.com.inter.desafio.domain.service.interfaces.SingleDigitServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SingleDigitServiceImpl implements SingleDigitServiceInterface {

    private SingleDigitRepository repository;
    private CacheServiceInterface cacheService;

    @Autowired
    public SingleDigitServiceImpl(SingleDigitRepository repository, CacheServiceInterface cacheService) {
        this.repository = repository;
        this.cacheService = cacheService;
    }

    @Override
    public SingleDigit save(SingleDigit singleDigit) {
        Integer result = cacheService.get(singleDigit.toDto());

        if (result == null) {
            result = digitoUnico(singleDigit.getNumber(), singleDigit.getMultiplier());
            singleDigit.setResult(result);

            cacheService.put(singleDigit.toDto(), result);
        }

        singleDigit.setResult(result);
        return repository.save(singleDigit);
    }

    private Integer digitoUnico(String n, Integer k) {

        if (n.length() == 1) {
            return Integer.valueOf(n);
        }

        if (k != null) {
            n = getConcatenation(n, k);
        }

        return calculateSingleDigit(n);
    }

    private String getConcatenation(String n, Integer k) {
        return n.repeat(k);
    }

    private Integer calculateSingleDigit(String input) {
        while (input.length() > 1) {
            input = sumDigits(input);
        }

        return Integer.valueOf(input);
    }

    private String sumDigits(String input) {
        Integer sum = 0;
        for (Character digit : input.toCharArray()) {
            sum += Integer.parseInt(digit.toString());
        }

        return sum.toString();
    }
}
