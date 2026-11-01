package com.zjgsu.lby.workorder.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zjgsu.lby.workorder.exception.DependencyUnavailableException;
import com.zjgsu.lby.workorder.model.WorkOrder;
import com.zjgsu.lby.workorder.service.WorkOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WorkOrderController.class)
class WorkOrderControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean WorkOrderService workOrders;

    private static final String BODY = """
        {"vehicleId":"v1","partId":"p1","partQuantity":2,
         "serviceAdvisorName":"Advisor","technicianName":"Technician","faultDescription":"Engine noise"}
        """;

    @Test
    void createsWorkOrder() throws Exception {
        WorkOrder order = new WorkOrder("w1", "v1", "p1", 2, "Advisor", "Technician", "Engine noise");
        given(workOrders.create(any())).willReturn(order);
        mvc.perform(post("/api/work-orders").contentType(MediaType.APPLICATION_JSON).content(BODY))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.vehicleId").value("v1"))
            .andExpect(jsonPath("$.partId").value("p1"))
            .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void rejectsInvalidRequest() throws Exception {
        mvc.perform(post("/api/work-orders").contentType(MediaType.APPLICATION_JSON)
                .content("{\"vehicleId\":\"\",\"partId\":\"\",\"partQuantity\":0}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void returns503WhenDependencyUnavailable() throws Exception {
        given(workOrders.create(any())).willThrow(new DependencyUnavailableException("车辆服务不可用"));
        mvc.perform(post("/api/work-orders").contentType(MediaType.APPLICATION_JSON).content(BODY))
            .andExpect(status().isServiceUnavailable())
            .andExpect(jsonPath("$.title").value("Dependency unavailable"));
    }
}
