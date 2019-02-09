package com.yangxiaochen.demo.spring.resetdocs.controller;

import com.yangxiaochen.demo.spring.resetdocs.controller.vo.CreateOrderParam;
import com.yangxiaochen.demo.spring.resetdocs.controller.vo.OrderInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Profile({"default", "local", "test", "prod"})
public class IndexControllerImpl implements IndexController {



    @Override
    public String simple() {
        throw new UnsupportedOperationException("Implement is not available");
    }

    @Override
    public Result<OrderInfo> orderInfo(@PathVariable("orderId") String orderId) {
        throw new UnsupportedOperationException("Implement is not available");
    }

    @Override
    public Result<OrderInfo> orderInfoById(@RequestParam(name = "id") String orderId) {
        throw new UnsupportedOperationException("Implement is not available");
    }

    @Override
    public Result<OrderInfo> orderInfoWithHeader(@RequestHeader(name = "id") String orderId) {
        throw new UnsupportedOperationException("Implement is not available");
    }

    @Override
    public Result<CreateOrderParam> createOrder(@RequestBody CreateOrderParam createOrderParam, @RequestHeader String appId) {
        throw new UnsupportedOperationException("Implement is not available");
    }
}
