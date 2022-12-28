package com.example.geolocationconverter.service;

import com.example.geolocationconverter.exception.IncompleteAddressException;
import com.example.geolocationconverter.exception.InvalidRequestStatusException;
import com.example.geolocationconverter.exception.NullResponseFromGoogleApiException;
import com.example.geolocationconverter.exception.OverDailyLimitStatusException;
import com.example.geolocationconverter.exception.OverQueryLimitStatusException;
import com.example.geolocationconverter.exception.RequestDeniedStatusException;
import com.example.geolocationconverter.exception.UnknownErrorStatusException;
import com.example.geolocationconverter.exception.UnknownStatusException;
import com.example.geolocationconverter.exception.ZeroResultStatusException;
import com.example.geolocationconverter.model.entity.GeocodingResult;
import com.example.geolocationconverter.model.dto.AddressDtoImpl;
import com.example.geolocationconverter.model.response.AddressComponent;
import com.example.geolocationconverter.model.response.GoogleResponse;
import com.example.geolocationconverter.model.dto.CoordinateDtoImpl;
import com.example.geolocationconverter.model.response.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class RequestService {

    @Value("${request.key}")
    private String googleKey;
    @Value("${request.url}")
    private String url;


    public GeocodingResult getCoordinatesFromGeocodingApi(AddressDtoImpl address) {
        GoogleResponse googleResponse = sendGeocodingRequest(address).share().block();

        if (googleResponse != null) {
            switch (googleResponse.getStatus()) {
                case OK -> {
                    Location location = googleResponse.getResults().get(0).getGeometry().getLocation();
                    return createGeocodingResult(address, location);
                }
                case ZERO_RESULTS -> throw new ZeroResultStatusException(
                        "Geocode was successful but returned no results. " +
                                "This may occur if the geocoder was passed a non-existent address");
                case OVER_DAILY_LIMIT -> throw new OverDailyLimitStatusException(
                        "Internal problems on the server. We apologise");
                case OVER_QUERY_LIMIT -> throw new OverQueryLimitStatusException(
                        "Internal problems on the server. We apologise");
                case REQUEST_DENIED -> throw new RequestDeniedStatusException(
                        "Internal problems on the server. We apologise");
                case INVALID_REQUEST -> throw new InvalidRequestStatusException(
                        "You entered the wrong address");
                case UNKNOWN_ERROR -> throw new UnknownErrorStatusException(
                        "Internal problems on the server. We apologise");
                default -> throw new UnknownStatusException(
                        "Internal problems on the server. We apologise");
            }
        } else throw new NullResponseFromGoogleApiException("Internal problems on the server. We apologise");
    }


    public GeocodingResult getAddressFromGeocodingApi(CoordinateDtoImpl coordinate) {
        GoogleResponse googleResponse = sendReverseGeocodingRequest(coordinate).share().block();

        if (googleResponse != null) {
            switch (googleResponse.getStatus()) {
                case OK -> {
                    AddressDtoImpl address = extractAddressFromGoogleResponse(googleResponse);
                    return createGeocodingResult(address, coordinate);
                }
                case ZERO_RESULTS -> throw new ZeroResultStatusException(
                        "Geocode was successful but returned no results. " +
                                "This may occur if the geocoder was passed a non-existent address");
                case OVER_QUERY_LIMIT -> throw new OverQueryLimitStatusException(
                        "Internal problems on the server. We apologise");
                case REQUEST_DENIED -> throw new RequestDeniedStatusException(
                        "Internal problems on the server. We apologise");
                case INVALID_REQUEST -> throw new InvalidRequestStatusException(
                        "You entered the wrong address");
                case UNKNOWN_ERROR -> throw new UnknownErrorStatusException(
                        "Internal problems on the server. We apologise");
                default -> throw new UnknownStatusException(
                        "Internal problems on the server. We apologise");
            }
        } else throw new NullResponseFromGoogleApiException(
                "Internal problems on the server. We apologise");
    }


    private Mono<GoogleResponse> sendGeocodingRequest(AddressDtoImpl geocodingRequest) {
        String requestUrl = createGeocodingUrl(geocodingRequest);

        WebClient webClient = WebClient.create(requestUrl);

        return webClient
                .get()
                .uri(requestUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GoogleResponse.class);
    }


    private Mono<GoogleResponse> sendReverseGeocodingRequest(CoordinateDtoImpl coordinate) {
        String requestUrl = createReverseGeocodingUrl(coordinate);

        WebClient webClient = WebClient.create(requestUrl);

        return webClient
                .get()
                .uri(requestUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GoogleResponse.class);
    }


    private AddressDtoImpl extractAddressFromGoogleResponse(GoogleResponse googleResponse) {
        String city = "";
        String street = "";
        String house = "";
        for (AddressComponent addressComponent : googleResponse.getResults().get(0).getAddressComponents()) {
            for (String type : addressComponent.getTypes()) {
                if (type.equals("locality")) {
                   city = addressComponent.getLongName();
                }

                if (type.equals("street_address") || type.equals("route")) {
                    street = addressComponent.getLongName();
                }

                if (type.equals("street_number")) {
                    house = addressComponent.getLongName();
                }
            }
        }

        if (!Objects.equals(city, "") && !Objects.equals(street, "") && !Objects.equals(house, "")) {
            return AddressDtoImpl.builder()
                    .city(city)
                    .street(street)
                    .house(house)
                    .build();
        } else
            throw new IncompleteAddressException("You have specified coordinates for which there is no full address");
    }


    private String createGeocodingUrl(AddressDtoImpl geocodingRequest) {
        return url
                + "?address="
                + geocodingRequest.getCity() + " "
                + geocodingRequest.getStreet() + " "
                + geocodingRequest.getHouse()
                + "&language=en"
                + "&key=" + googleKey;
    }


    private String createReverseGeocodingUrl(CoordinateDtoImpl reverseGeocodingRequest) {
        return url
                + "?latlng="
                + reverseGeocodingRequest.getLatitude() + ","
                + reverseGeocodingRequest.getLongitude()
                + "&language=en"
                + "&location_type=ROOFTOP&result_type=street_address"
                + "&key=" + googleKey;
    }


    private GeocodingResult createGeocodingResult(AddressDtoImpl address, CoordinateDtoImpl coordinate) {
        return GeocodingResult.builder()
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .latitude(coordinate.getLatitude())
                .longitude(coordinate.getLongitude())
                .build();
    }


    private GeocodingResult createGeocodingResult(AddressDtoImpl address, Location location) {
        return GeocodingResult.builder()
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .latitude(Double.valueOf(location.getLatitude()))
                .longitude(Double.valueOf(location.getLongitude()))
                .build();
    }


}
