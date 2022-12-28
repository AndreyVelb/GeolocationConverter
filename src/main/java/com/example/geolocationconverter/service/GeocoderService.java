package com.example.geolocationconverter.service;

import com.example.geolocationconverter.model.dto.CoordinateDto;
import com.example.geolocationconverter.model.dto.CoordinateDtoImpl;
import com.example.geolocationconverter.model.entity.GeocodingResult;
import com.example.geolocationconverter.model.dto.AddressDtoImpl;
import com.example.geolocationconverter.repository.GeocodingResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeocoderService {

    private final RequestService requestService;
    private final GeocodingResultRepository geocodingResultRepository;


    @Transactional
    public CoordinateDto getCoordinatesByAddress(AddressDtoImpl address) {
        Optional<CoordinateDto> resultFromDb = geocodingResultRepository.findByAddress(
                address.getCity(),
                address.getStreet(),
                address.getHouse());

        if (resultFromDb.isPresent()) {
            return resultFromDb.get();
        } else {
            GeocodingResult resultFromGeocodingApi = requestService.getCoordinatesFromGeocodingApi(address);
            geocodingResultRepository.save(resultFromGeocodingApi);
            return CoordinateDtoImpl.builder()
                    .latitude(resultFromGeocodingApi.getLatitude())
                    .longitude(resultFromGeocodingApi.getLongitude())
                    .build();
        }
    }
}
