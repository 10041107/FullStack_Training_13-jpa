package com.ohgiraffers.springjpa.order.controller;

import com.ohgiraffers.springjpa.order.dto.OrderAndPaymentsVO;
import com.ohgiraffers.springjpa.order.entity.MenuOrder;
import com.ohgiraffers.springjpa.order.service.OrderService;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;



    @PostMapping
    public ResponseEntity order(@RequestBody HashMap<String, Integer> menuCode){

        MenuOrder result = orderService.order(menuCode.get("menuCode"));
        
        if(Objects.isNull(result)){
            return ResponseEntity.status(400).body("주문 실패");
        }

        OrderAndPaymentsVO orderAndPaymentsVO = new OrderAndPaymentsVO();
        orderAndPaymentsVO.setPayments(result.getPayments());
        orderAndPaymentsVO.setMenuCode(result.getMenu());
        orderAndPaymentsVO.setOrderDate(result.getOrderDate());


        return ResponseEntity.ok(orderAndPaymentsVO);
    }


}
