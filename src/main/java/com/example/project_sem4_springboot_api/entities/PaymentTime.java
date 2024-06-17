package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.EPaymentTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_time")
public class PaymentTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private int time;

    public PaymentTime(EPaymentTime ePaymentTime) {
        this.name = ePaymentTime.getPaymentTime();
        if(ePaymentTime!= EPaymentTime.HANG_THANG){
            this.time = Integer.parseInt(ePaymentTime.name().substring(6));
        }
    }
}
