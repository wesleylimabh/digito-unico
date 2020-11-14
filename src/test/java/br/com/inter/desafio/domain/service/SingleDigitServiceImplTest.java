package br.com.inter.desafio.domain.service;

import br.com.inter.desafio.domain.model.SingleDigit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SingleDigitServiceImplTest {

    @Autowired
    private SingleDigitServiceImpl service;

    @Test
    void shouldSaveSingleDigit_WhenValid() {
        // Arrange
        SingleDigit singleDigit = new SingleDigit();
        singleDigit.setNumber("1");
        singleDigit.setMultiplier(1);

        // Act
        SingleDigit saved = service.save(singleDigit);

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void shouldCalculateSingleDigit_WhenValidStringNumber() {
        // Arrange
        Integer expect = 2;
        String numberToCalculate = "9875";
        String methodName = "calculateSingleDigit";

        // Act
        // Assert
        assertTrue(ReflectionTestUtils.invokeMethod(service, methodName, numberToCalculate ).equals(expect));

    }

    @Test
    void shouldRepeatValueOfStringFourTimes() {
        // Arrange
        String expect = "9875987598759875";
        String numberToCalculate = "9875";
        Integer multiplier = 4;
        String methodName = "getConcatenation";

        // Act
        // Assert
        assertTrue(ReflectionTestUtils.invokeMethod(service, methodName, numberToCalculate, multiplier ).equals(expect));

    }

    @Test
    void shouldReturnSameNumber_WhenItIsSingleDigit() {
        // Arrange
        Integer expect = 1;
        String numberToCalculate = "1";
        String methodName = "digitoUnico";

        // Act
        // Assert
        assertTrue(ReflectionTestUtils.invokeMethod(service, methodName, numberToCalculate, null ).equals(expect));

    }

    @Test
    void shouldCalculateSingleDigit_WhenMultiplierIsInformed() {
        // Arrange
        Integer expect = 8;
        String numberToCalculate = "9875";
        Integer multiplier = 4;
        String methodName = "digitoUnico";

        // Act
        // Assert
        assertTrue(ReflectionTestUtils.invokeMethod(service, methodName, numberToCalculate, multiplier ).equals(expect));

    }

    @Test
    void shouldThrowException_WhenAssignedInvalidValue() {
        // Arrange
        SingleDigit singleDigit = new SingleDigit();
        singleDigit.setNumber("1");
        singleDigit.setMultiplier(0);

        // Assert
        assertThrows(ConstraintViolationException.class, () -> {
            SingleDigit saved = service.save(singleDigit);

        });

    }
}