package com.zjgsu.lby.workorder.client;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.zjgsu.lby.workorder.exception.DependencyUnavailableException;
import com.zjgsu.lby.workorder.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

class RemoteServiceClientTest {
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final RemoteServiceClient client = new RemoteServiceClient(
        restTemplate, "http://vehicle-service:8081", "http://part-service:8083");

    @Test
    void mapsMissingVehicleToResourceNotFound() {
        given(restTemplate.getForObject("http://vehicle-service:8081/api/vehicles/{id}",
            VehicleSummary.class, "v1"))
            .willThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> client.validateVehicle("v1"));
    }

    @Test
    void mapsTimeoutToDependencyUnavailable() {
        given(restTemplate.getForObject("http://vehicle-service:8081/api/vehicles/{id}",
            VehicleSummary.class, "v1"))
            .willThrow(new ResourceAccessException("timeout"));

        assertThrows(DependencyUnavailableException.class, () -> client.validateVehicle("v1"));
    }
}
