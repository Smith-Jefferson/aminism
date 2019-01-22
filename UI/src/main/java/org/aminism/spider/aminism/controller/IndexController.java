package org.aminism.spider.aminism.controller;

import org.aminism.spider.log.CLogManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ygxie on 2017/10/26.
 */
@Controller
public class IndexController {

    @RequestMapping(value="/",method = RequestMethod.GET)
    public String index(){
        CLogManager.info("index","test");
        return "/index/index";
    }

}
