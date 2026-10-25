package com.zjgsu.lby.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zjgsu.lby.exception.BusinessRuleException;
import com.zjgsu.lby.exception.ResourceNotFoundException;
import com.zjgsu.lby.model.Vehicle;
import com.zjgsu.lby.model.WorkOrder;
import com.zjgsu.lby.service.WorkOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WorkOrderController.class)
class WorkOrderControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    WorkOrderService workOrders;

    @Test
    void createsWorkOrderWithLocation() throws Exception {
        Vehicle vehicle = new Vehicle("vehicle-1", "陆兵尧", "浙A12345", "大众", "迈腾", 52000);
        WorkOrder workOrder = new WorkOrder("work-order-1", vehicle, "王顾问", "李技师", "发动机异响");
        given(workOrders.create(any())).willReturn(workOrder);

        mvc.perform(post("/api/work-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "vehicleId": "vehicle-1",
                      "serviceAdvisorName": "王顾问",
                      "technicianName": "李技师",
                      "faultDescription": "发动机异响"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.plateNumber").value("浙A12345"))
            .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void rejectsInvalidRequest() throws Exception {
        mvc.perform(post("/api/work-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "vehicleId": "",
                      "serviceAdvisorName": "",
                      "technicianName": "",
                      "faultDescription": ""
                    }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Invalid request"));
    }

    @Test
    void returnsConflictForActiveWorkOrder() throws Exception {
        given(workOrders.create(any())).willThrow(
            new BusinessRuleException("该车辆已有未完成工单，不能重复创建")
        );

        mvc.perform(post("/api/work-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "vehicleId": "vehicle-1",
                      "serviceAdvisorName": "王顾问",
                      "technicianName": "李技师",
                      "faultDescription": "发动机异响"
                    }
                    """))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.detail").value("该车辆已有未完成工单，不能重复创建"));
    }

    @Test
    void returnsNotFoundForMissingWorkOrder() throws Exception {
        given(workOrders.findById("missing"))
            .willThrow(new ResourceNotFoundException("维修工单不存在: missing"));

        mvc.perform(get("/api/work-orders/missing"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.detail").value("维修工单不存在: missing"));
    }
}
