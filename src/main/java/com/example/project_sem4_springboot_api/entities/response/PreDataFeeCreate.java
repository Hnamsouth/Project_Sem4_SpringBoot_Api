package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.PaymentTime;
import com.example.project_sem4_springboot_api.entities.Scope;
import com.example.project_sem4_springboot_api.entities.Unit;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PreDataFeeCreate {
    private List<PaymentTime> paymentTimeList;
    private List<Scope> scopeList;
    private List<Unit> unitList;
}
