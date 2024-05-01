package com.ohgiraffers.springjpa.order.service;

import com.ohgiraffers.springjpa.order.entity.Payments;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentsService {

    public Payments orderPayments(int value){

        Payments payments = new Payments();
        payments.setPaymentsDate(new Date());
        
        // 외부 결제 api 반환정보를 담아서 반환해줌
        return payments;
    }

}
