package com.example.geolocationconverter.controller;

import com.example.geolocationconverter.model.dto.AddressDto;
import com.example.geolocationconverter.model.dto.CoordinateDtoImpl;
import com.example.geolocationconverter.service.ReverseGeocoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReverseGeocoderController {

    private final ReverseGeocoderService reverseGeocoderService;


    @PostMapping("/reverse-geocoder")
    public AddressDto getAddressByCoordinates(CoordinateDtoImpl coordinates) {
        return reverseGeocoderService.getAddressByCoordinates(coordinates);
    }

}
