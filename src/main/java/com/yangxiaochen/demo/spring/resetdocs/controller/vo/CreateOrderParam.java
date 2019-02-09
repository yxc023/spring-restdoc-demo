package com.yangxiaochen.demo.spring.resetdocs.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateOrderParam {

    private Goods goods;
    private Long payAmount;
    private Long userId;

    @Data
    public static class Goods {
        private List<SkuInfo> skuInfos = new ArrayList<>();
    }

    @Data
    public static class SkuInfo {
        @NotNull
        private String id;
        private String name;
        private Integer count;
        private Long price;
    }
}
