package com.example.geolocationconverter.repository;

import com.example.geolocationconverter.model.dto.AddressDto;
import com.example.geolocationconverter.model.dto.CoordinateDto;
import com.example.geolocationconverter.model.entity.GeocodingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeocodingResultRepository extends JpaRepository<GeocodingResult, Long> {

    @Query(value = "select gr from GeocodingResult gr " +
            "where gr.city = :city and gr.street = :street and gr.house = :house")
    Optional<CoordinateDto> findByAddress(String city, String street, String house);

    @Query(value = "select gr from GeocodingResult gr " +
            "where gr.latitude = :latitude and gr.longitude = :longitude")
    Optional<AddressDto> findByCoordinate(Double latitude, Double longitude);

}
