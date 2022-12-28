package com.example.geolocationconverter.service;

import com.example.geolocationconverter.model.dto.AddressDto;
import com.example.geolocationconverter.model.dto.AddressDtoImpl;
import com.example.geolocationconverter.model.dto.CoordinateDtoImpl;
import com.example.geolocationconverter.model.entity.GeocodingResult;
import com.example.geolocationconverter.repository.GeocodingResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReverseGeocoderService {

    private final RequestService requestService;
    private final GeocodingResultRepository geocodingResultRepository;


    @Transactional
    public AddressDto getAddressByCoordinates(CoordinateDtoImpl coordinates) {
        Optional<AddressDto> resultFromDb = geocodingResultRepository.findByCoordinate(
                coordinates.getLatitude(),
                coordinates.getLongitude());

        if (resultFromDb.isPresent()) {
            return resultFromDb.get();
        } else {
            GeocodingResult resultFromGeocodingReverseApi = requestService.getAddressFromGeocodingApi(coordinates);
            geocodingResultRepository.save(resultFromGeocodingReverseApi);
            return AddressDtoImpl.builder()
                    .city(resultFromGeocodingReverseApi.getCity())
                    .street(resultFromGeocodingReverseApi.getStreet())
                    .house(resultFromGeocodingReverseApi.getHouse())
                    .build();
        }
    }
}
