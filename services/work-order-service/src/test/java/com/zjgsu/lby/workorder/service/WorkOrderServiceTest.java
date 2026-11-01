package com.zjgsu.lby.workorder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import com.zjgsu.lby.workorder.client.RemoteServiceClient;
import com.zjgsu.lby.workorder.dto.WorkOrderCreateRequest;
import com.zjgsu.lby.workorder.exception.BusinessRuleException;
import com.zjgsu.lby.workorder.exception.DependencyUnavailableException;
import com.zjgsu.lby.workorder.exception.ResourceNotFoundException;
import com.zjgsu.lby.workorder.model.WorkOrder;
import com.zjgsu.lby.workorder.model.WorkOrderStatus;
import com.zjgsu.lby.workorder.repository.WorkOrderRepository;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class WorkOrderServiceTest {
    private final WorkOrderRepository repository = mock(WorkOrderRepository.class);
    private final RemoteServiceClient remote = mock(RemoteServiceClient.class);
    private final WorkOrderService service = new WorkOrderService(repository, remote);

    private final WorkOrderCreateRequest request = new WorkOrderCreateRequest(
        "v1", "p1", 2, "Advisor", "Technician", "Engine noise");

    @Test
    void createsOrderAfterVehicleAndPartChecks() {
        given(repository.save(any(WorkOrder.class))).willAnswer(call -> call.getArgument(0));

        WorkOrder order = service.create(request);

        assertEquals("v1", order.getVehicleId());
        assertEquals("p1", order.getPartId());
        assertEquals(WorkOrderStatus.CREATED, order.getStatus());
    }

    @Test
    void rejectsDuplicateActiveOrderBeforeRemoteCalls() {
        given(repository.existsByVehicleIdAndStatusIn("v1",
            Set.of(WorkOrderStatus.CREATED, WorkOrderStatus.IN_PROGRESS))).willReturn(true);

        assertThrows(BusinessRuleException.class, () -> service.create(request));
        verifyNoInteractions(remote);
    }

    @Test
    void propagatesMissingVehicle() {
        Mockito.doThrow(new ResourceNotFoundException("车辆不存在: v1"))
            .when(remote).validateVehicle("v1");

        assertThrows(ResourceNotFoundException.class, () -> service.create(request));
    }

    @Test
    void propagatesDependencyFailure() {
        Mockito.doThrow(new DependencyUnavailableException("车辆服务不可用"))
            .when(remote).validateVehicle("v1");

        assertThrows(DependencyUnavailableException.class, () -> service.create(request));
    }
}
