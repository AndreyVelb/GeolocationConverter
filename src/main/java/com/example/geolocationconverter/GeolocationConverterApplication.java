package com.example.geolocationconverter;

import com.example.geolocationconverter.controller.GeocoderController;
import com.example.geolocationconverter.controller.ReverseGeocoderController;
import com.example.geolocationconverter.model.dto.CoordinateDtoImpl;
import com.example.geolocationconverter.service.GeocoderService;
import com.example.geolocationconverter.service.RequestService;
import com.example.geolocationconverter.service.ReverseGeocoderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GeolocationConverterApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(GeolocationConverterApplication.class, args);
        RequestService requestService = applicationContext.getBean(RequestService.class);
        GeocoderService geocoderService = applicationContext.getBean(GeocoderService.class);
        ReverseGeocoderService reverseGeocoderService = applicationContext.getBean(ReverseGeocoderService.class);
        GeocoderController geocoderController = applicationContext.getBean(GeocoderController.class);
        ReverseGeocoderController reverseGeocoderController = applicationContext.getBean(ReverseGeocoderController.class);

        CoordinateDtoImpl coordinate = CoordinateDtoImpl.builder()
                .latitude(-22.354620)
                .longitude(-60.032605)
                .build();

        System.out.println(reverseGeocoderController.getAddressByCoordinates(coordinate));

//        System.out.println(reverseGeocoderService.getAddressByCoordinates(coordinate));
//        Mono<GoogleResponse> stringMono = requestService.sendReverseGeocodingRequest(coordinate);
//
//        GoogleResponse addressDto = stringMono.share().block();
//
//        System.out.println(addressDto.getResults().get(0).getFormattedAddress());
//        System.out.println(addressDto.getResults().get(0).getAddressComponents()[0].getLongName());
//        System.out.println(addressDto.getResults().get(0).getAddressComponents()[1].getLongName());
//        System.out.println(addressDto.getResults().get(0).getAddressComponents()[3].getLongName());
//        System.out.println(addressDto.getStatus());
//
//        GeocodingRequest geocodingRequest = GeocodingRequest.builder()
//                .city("Minsk")
//                .street("Grushevskaya")
//                .house(83)
//                .build();

//
//        System.out.println(addressDto.getResults().get(0).getFormattedAddress());
//        System.out.println(addressDto.getStatus());

//        Mono<GoogleResponse> monoGoogleResponse = requestService.sendGeocodingRequest(geocodingRequest);
//
//        GoogleResponse locationDto = monoGoogleResponse.share().block();
//
//        System.out.println(locationDto.getStatus());
//        System.out.println(locationDto.getResults().get(0).getGeometry().getLocation().getLatitude());
//        System.out.println(locationDto.getResults().get(0).getGeometry().getLocation().getLongitude());
    }

}
