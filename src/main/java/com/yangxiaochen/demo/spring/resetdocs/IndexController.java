package com.yangxiaochen.demo.spring.resetdocs;

import com.yangxiaochen.demo.spring.resetdocs.vo.CreateOrderParam;
import com.yangxiaochen.demo.spring.resetdocs.vo.OrderInfo;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

/**
 * @author yangxiaochen
 */
@RestController
public class IndexController {

    @RequestMapping("/simple")
    public String simple() {
        return "OK";
    }

    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
    public Result<OrderInfo> orderInfo(@PathVariable("orderId") String orderId) {
        OrderInfo.SkuInfo skuInfo = new OrderInfo.SkuInfo();
        skuInfo.setId("sku-001");
        skuInfo.setName("规格1");
        skuInfo.setCount(1);
        skuInfo.setPrice(200L);
        OrderInfo.SkuInfo skuInfo2 = new OrderInfo.SkuInfo();
        skuInfo2.setId("sku-001");
        skuInfo2.setName("规格1");
        skuInfo2.setCount(2);
        skuInfo2.setPrice(300L);

        OrderInfo.Goods goods = new OrderInfo.Goods();
        goods.getSkuInfos().add(skuInfo);
        goods.getSkuInfos().add(skuInfo2);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGoods(goods);
        orderInfo.setUserId(10000000001L);
        orderInfo.setPayAmount(800L);

        orderInfo.setOrderId(orderId);
        orderInfo.setCreateTime(Date.from(Instant.now()));
        orderInfo.setPayTime(Date.from(Instant.now()));

        return Result.success().withData(orderInfo);
    }


    @RequestMapping(value = "/orderById", method = RequestMethod.GET)
    public Result<OrderInfo> orderInfoById(@RequestParam(name = "id") String orderId) {
        return orderInfo(orderId);
    }


    @RequestMapping(value = "/orderWithHeader", method = RequestMethod.GET)
    public Result<OrderInfo> orderInfoWithHeader(@RequestHeader(name = "id") String orderId) {
        return orderInfo(orderId);
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST, consumes = "application/json")
    public Result<CreateOrderParam> createOrder(@RequestBody CreateOrderParam createOrderParam, @RequestHeader String appId) {
        return Result.success().withData(createOrderParam);
    }
}
