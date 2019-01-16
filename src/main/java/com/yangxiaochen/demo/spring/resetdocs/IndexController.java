package com.yangxiaochen.demo.spring.resetdocs;

import com.yangxiaochen.demo.spring.resetdocs.vo.CreateOrderParam;
import org.springframework.web.bind.annotation.*;

/**
 * @author yangxiaochen
 */
@RestController
public class IndexController {

    @RequestMapping("/a")
    public String a() {
        return "a";
    }


    @RequestMapping(value = "/order/create", method = RequestMethod.POST, consumes = "application/json")
    public Result<CreateOrderParam> createOrder(@RequestBody CreateOrderParam createOrderParam, @RequestHeader String appId) {
        return Result.success().withData(createOrderParam);
    }
}
