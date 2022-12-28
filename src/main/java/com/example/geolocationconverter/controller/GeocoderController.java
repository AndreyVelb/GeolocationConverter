package com.example.geolocationconverter.controller;

import com.example.geolocationconverter.model.dto.AddressDtoImpl;
import com.example.geolocationconverter.model.dto.CoordinateDto;
import com.example.geolocationconverter.service.GeocoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GeocoderController {

    private final GeocoderService geocoderService;


    @PostMapping("/geocoder")
    public CoordinateDto getCoordinatesByAddress(AddressDtoImpl address) {
        return geocoderService.getCoordinatesByAddress(address);
    }

}
