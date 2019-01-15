package com.yangxiaochen.demo.spring.resetdocs;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangxiaochen
 */
@RestController
public class IndexController {

    @RequestMapping("/a")
    public String a() {
        return "a";
    }
}
