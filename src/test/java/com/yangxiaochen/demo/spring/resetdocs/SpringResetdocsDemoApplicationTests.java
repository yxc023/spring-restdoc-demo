package com.yangxiaochen.demo.spring.resetdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yangxiaochen.demo.spring.resetdocs.vo.CreateOrderParam;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringResetdocsDemoApplicationTests {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    public static FieldDescriptor[] RESULT = new FieldDescriptor[] {
            PayloadDocumentation.subsectionWithPath("code").description("请求状态. 0: 成功, -1: 失败"),
            PayloadDocumentation.subsectionWithPath("message").description("错误消息."),
            PayloadDocumentation.subsectionWithPath("data").description("返回的数据.")
    };


    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(
                        documentationConfiguration(this.restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(Preprocessors.prettyPrint(), Preprocessors.maskLinks())
                                .withResponseDefaults(Preprocessors.prettyPrint(), Preprocessors.maskLinks())
                                .and()
                                .snippets()
                                .withDefaults(HttpDocumentation.httpRequest(), HttpDocumentation.httpResponse())
                )

                .build();
    }


    @Test
    public void a() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/a"))
                .andExpect(status().isOk())
                .andDo(document("index"));
    }

    @Test
    public void orderCreate() throws Exception {


        CreateOrderParam.SkuInfo skuInfo = new CreateOrderParam.SkuInfo();
        skuInfo.setId("sku-001");
        skuInfo.setName("规格1");
        skuInfo.setCount(1);
        skuInfo.setPrice(200L);
        CreateOrderParam.SkuInfo skuInfo2 = new CreateOrderParam.SkuInfo();
        skuInfo2.setId("sku-001");
        skuInfo2.setName("规格1");
        skuInfo2.setCount(2);
        skuInfo2.setPrice(300L);

        CreateOrderParam.Goods goods = new CreateOrderParam.Goods();
        goods.getSkuInfos().add(skuInfo);
        goods.getSkuInfos().add(skuInfo2);

        CreateOrderParam createOrderParam = new CreateOrderParam();
        createOrderParam.setGoods(goods);
        createOrderParam.setUserId(10000000001L);
        createOrderParam.setPayAmount(800L);

        ObjectMapper objectMapper = new ObjectMapper();

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(CreateOrderParam.class);


        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderParam))
                        .header("appId", "app001")
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(Result.success().withData(createOrderParam))))
                .andDo(document("{class_name}/{method_name}",
                        PayloadDocumentation.relaxedResponseFields(RESULT),
                        PayloadDocumentation.relaxedResponseFields(
                                PayloadDocumentation.beneathPath("data").withSubsectionId("Data"),
                                PayloadDocumentation.subsectionWithPath("goods").description("购买的商品").attributes(Attributes.key("约束").value(userConstraints.descriptionsForProperty("skuInfos[].id")))
                        )
                        ));

    }

}

