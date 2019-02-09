package com.yangxiaochen.demo.spring.resetdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.yangxiaochen.demo.spring.resetdocs.controller.Result;
import com.yangxiaochen.demo.spring.resetdocs.controller.vo.CreateOrderParam;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=mock")
public class ApplicationTests {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;


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
    public void simple() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/simple"))
                .andExpect(status().isOk())
                .andDo(document("simple",
                        flatSnippetArray(
                                PayloadDocumentation.requestBody(),
                                PayloadDocumentation.responseBody()
                        )
                ));
    }


    @Test
    public void orderWithPathVariable() throws Exception {
        this.mockMvc
                .perform(RestDocumentationRequestBuilders.get("/order/{orderId}", "10001"))
                .andExpect(status().isOk())
                .andDo(document("orderWithPathVariable",
                        flatSnippetArray(
                                RequestDocumentation.relaxedPathParameters(
                                        flatList(
                                                RequestDocumentation.parameterWithName("orderId").description("订单 id")
                                        )
                                ),
                                PayloadDocumentation.relaxedResponseFields(
                                        PayloadDocumentation.beneathPath("data"),
                                        flatList(
                                                PayloadDocumentation.fieldWithPath("goods").description("购买的商品")
                                        )
                                )
                        )
                ));
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

        this.mockMvc
                .perform(
                        RestDocumentationRequestBuilders.post("/order/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrderParam))
                                .header("appId", "app001")
                )

                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(Result.success().withData(createOrderParam))))

                .andDo(document("order/create",
                        PayloadDocumentation.relaxedResponseFields(
                                PayloadDocumentation.beneathPath("data"),
                                attributes(key("title").value("返回值说明")),
                                flatList(
                                        PayloadDocumentation.fieldWithPath("goods").description("购买的商品")
                                )
                        )
                ));

    }


    public static <T> List<T> flatList(Object... itemOrItems) {
        List<T> list = Lists.newArrayList();
        for (Object element : itemOrItems) {
            if (element.getClass().isArray()) {
                int length = Array.getLength(element);
                for (int i = 0; i < length; i++) {
                    list.add((T) Array.get(element, i));
                }
            } else if (element instanceof Collection) {
                list.addAll((Collection<? extends T>) element);
            } else {
                list.add((T) element);
            }
        }
        return list;
    }

    public static <T> T[] flatArray(Class type, Object... itemOrItems) {
        List<T> list = Lists.newArrayList();
        for (Object element : itemOrItems) {
            if (element.getClass().isArray()) {
                int length = Array.getLength(element);
                for (int i = 0; i < length; i++) {
                    list.add((T) Array.get(element, i));
                }
            } else if (element instanceof Collection) {
                list.addAll((Collection<? extends T>) element);
            } else {
                list.add((T) element);
            }
        }
        return list.toArray((T[]) Array.newInstance(type, 0));
    }

    public static Snippet[] flatSnippetArray(Object... itemOrItems) {
        List<Snippet> list = Lists.newArrayList();
        for (Object element : itemOrItems) {
            if (element.getClass().isArray()) {
                int length = Array.getLength(element);
                for (int i = 0; i < length; i++) {
                    list.add((Snippet) Array.get(element, i));
                }
            } else if (element instanceof Collection) {
                list.addAll((Collection<? extends Snippet>) element);
            } else {
                list.add((Snippet) element);
            }
        }
        return list.toArray(new Snippet[0]);
    }
}

