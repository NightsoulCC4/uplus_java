package com.uplus_client.uplus_java.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uplus_client.uplus_java.Service.AdmitService;
import com.uplus_client.uplus_java.Service.DischargeService;
import com.uplus_client.uplus_java.Service.OrderService;

@CrossOrigin
@RestController
@RequestMapping("uplus_example/client")
public class MyController {

    @Autowired
    AdmitService admitService;

    @Autowired
    DischargeService dischargeService;

    @Autowired
    OrderService orderService;

    public MyController(){

    }

    @PostMapping("/admit")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> onAdmitController(){
        return admitService.onAdmitService();
    }

    @PostMapping("/discharge")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> onDischargeController(){
        return dischargeService.onDischargeService();
    }

    /* @PostMapping("/monitorInterface")
    @ResponseBody
    public ResponseEntity<LinkedHashMap<String, String>> onMonitorInterfaceController(){
        return admitService.OnMonitorInterfaceService();
    } */

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> onOrderController(){
        return orderService.OnOrderService();
    }

    /* @GetMapping("/summary")
    @ResponseBody
    public ResponseEntity<LinkedHashMap<String, String>> onSummaryOrderController(){
        return admitService.OnSummaryOrderService();
    } */
    
}
