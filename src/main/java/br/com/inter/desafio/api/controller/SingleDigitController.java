package br.com.inter.desafio.api.controller;

import br.com.inter.desafio.api.dto.SingleDigitDTO;
import br.com.inter.desafio.domain.model.SingleDigit;
import br.com.inter.desafio.domain.model.User;
import br.com.inter.desafio.domain.service.interfaces.SingleDigitServiceInterface;
import br.com.inter.desafio.domain.service.interfaces.UserServiceInterface;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Api(tags = "SingleDigit")
@RequestMapping(value = "/single-digits", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class SingleDigitController {

    private final SingleDigitServiceInterface singleDigitService;
    private final ModelMapper modelMapper;
    private final UserServiceInterface userService;

    @Autowired
    public SingleDigitController(SingleDigitServiceInterface singleDigitService,
                                 ModelMapper mapper,
                                 UserServiceInterface userService) {
        this.singleDigitService = singleDigitService;
        this.modelMapper = mapper;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleDigitDTO createSingleDigit(@Valid @RequestBody SingleDigitDTO singleDigitDTO,
                                 @RequestParam(name = "user_id", required = false) Long userId) {
        User user = null;

        if (userId != null) {
            user = userService.findById(userId);
        }

        SingleDigit singleDigit = toEntity(singleDigitDTO);
        singleDigit = singleDigitService.save(singleDigit);

        if (user != null) {
            List<SingleDigit> singleDigitList = user.getSingleDigits();
            singleDigitList.add(singleDigit);
            userService.save(user);
        }

        return toDTO(singleDigit);
    }

    @GetMapping("/user/{userId}")
    public List<SingleDigitDTO> findSingleDigitsFromUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return toCollectionModel(user.getSingleDigits());
    }


    private SingleDigitDTO toDTO(SingleDigit singleDigit) {
        return modelMapper.map(singleDigit, SingleDigitDTO.class);
    }

    private List<SingleDigitDTO> toCollectionModel(List<SingleDigit> singleDigitList) {
        return singleDigitList.stream()
                .map(singleDigit -> toDTO(singleDigit))
                .collect(Collectors.toList());
    }

    private SingleDigit toEntity(SingleDigitDTO singleDigitDTO) {
        return modelMapper.map(singleDigitDTO, SingleDigit.class);
    }
}
