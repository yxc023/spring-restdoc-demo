package com.yangxiaochen.demo.spring.resetdocs.controller;

import com.yangxiaochen.demo.spring.resetdocs.controller.vo.CreateOrderParam;
import com.yangxiaochen.demo.spring.resetdocs.controller.vo.OrderInfo;
import org.springframework.web.bind.annotation.*;

public interface IndexController {

    @RequestMapping("/simple")
    String simple();

    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
    Result<OrderInfo> orderInfo(@PathVariable("orderId") String orderId);


    @RequestMapping(value = "/orderById", method = RequestMethod.GET)
    Result<OrderInfo> orderInfoById(@RequestParam(name = "id") String orderId);


    @RequestMapping(value = "/orderWithHeader", method = RequestMethod.GET)
    Result<OrderInfo> orderInfoWithHeader(@RequestHeader(name = "id") String orderId);

    @RequestMapping(value = "/order/create", method = RequestMethod.POST, consumes = "application/json")
    Result<CreateOrderParam> createOrder(@RequestBody CreateOrderParam createOrderParam, @RequestHeader String appId);
}
