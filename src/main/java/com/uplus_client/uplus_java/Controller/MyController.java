package com.uplus_client.uplus_java.Controller;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uplus_client.uplus_java.Service.MyService;

@CrossOrigin
@RestController
@RequestMapping("uplus_example/client")
public class MyController {

    @Autowired
    MyService myService;

    public MyController(){

    }
    @PostMapping("/admit")
    @ResponseBody
    public ResponseEntity<LinkedHashMap<String, String>> onAdmitController(){
        return myService.onAdmitService();
    }
    @PostMapping("/discharge")
    @ResponseBody
    public ResponseEntity<LinkedHashMap<String, String>> onDischargeController(){
        return myService.onDischargeService();
    }
    @PostMapping("/monitorInterface")
    @ResponseBody
    public ResponseEntity<LinkedHashMap<String, String>> onMonitorInterfaceController(){
        return myService.OnMonitorInterfaceService();
    }
    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<LinkedHashMap<String, String>> onOrderController(){
        return myService.OnOrderService();
    }
    @GetMapping("/summary")
    @ResponseBody
    public ResponseEntity<LinkedHashMap<String, String>> onSummaryOrderController(){
        return myService.OnSummaryOrderService();
    }
}
