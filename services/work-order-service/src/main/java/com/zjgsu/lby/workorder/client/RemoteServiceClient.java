package com.zjgsu.lby.workorder.client;

import com.zjgsu.lby.workorder.dto.PartReservationRequest;
import com.zjgsu.lby.workorder.exception.BusinessRuleException;
import com.zjgsu.lby.workorder.exception.DependencyUnavailableException;
import com.zjgsu.lby.workorder.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RemoteServiceClient {
    private final RestTemplate restTemplate;
    private final String vehicleServiceUrl;
    private final String partServiceUrl;

    public RemoteServiceClient(RestTemplate restTemplate,
                               @Value("${services.vehicle-url}") String vehicleServiceUrl,
                               @Value("${services.part-url}") String partServiceUrl) {
        this.restTemplate = restTemplate;
        this.vehicleServiceUrl = vehicleServiceUrl;
        this.partServiceUrl = partServiceUrl;
    }

    public VehicleSummary validateVehicle(String vehicleId) {
        try {
            VehicleSummary vehicle = restTemplate.getForObject(
                vehicleServiceUrl + "/api/vehicles/{id}", VehicleSummary.class, vehicleId);
            if (vehicle == null) {
                throw new DependencyUnavailableException("车辆服务返回空响应");
            }
            return vehicle;
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException("车辆不存在: " + vehicleId);
            }
            throw new DependencyUnavailableException("车辆服务调用失败: " + exception.getStatusCode());
        } catch (ResourceAccessException exception) {
            throw new DependencyUnavailableException("车辆服务不可用或响应超时");
        } catch (RestClientException exception) {
            throw new DependencyUnavailableException("车辆服务调用失败");
        }
    }

    public void reservePart(String partId, int quantity) {
        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(
                partServiceUrl + "/api/parts/{id}/reservations",
                new PartReservationRequest(quantity), Void.class, partId);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new DependencyUnavailableException("配件服务预留库存失败");
            }
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException("配件不存在: " + partId);
            }
            if (exception.getStatusCode().value() == 409) {
                throw new BusinessRuleException("配件库存不足或无法预留");
            }
            throw new DependencyUnavailableException("配件服务调用失败: " + exception.getStatusCode());
        } catch (ResourceAccessException exception) {
            throw new DependencyUnavailableException("配件服务不可用或响应超时");
        } catch (RestClientException exception) {
            throw new DependencyUnavailableException("配件服务调用失败");
        }
    }
}
