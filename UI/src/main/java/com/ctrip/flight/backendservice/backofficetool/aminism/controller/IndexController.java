package com.ctrip.flight.backendservice.backofficetool.aminism.controller;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.App;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool.CLogManager;

/**
 * Created by ygxie on 2017/10/26.
 */
@Controller
public class IndexController {
    @Autowired
    App app;

    @RequestMapping(value="/",method = RequestMethod.GET)
    public String index(){
        CLogManager.info("index","test");
        return "/index/index";
    }

    @RequestMapping(value="/index/startSpider",method = RequestMethod.GET)
    @ResponseBody
    public String startSpider(){
        try {
           app.run();
        } catch (Exception e) {
            return JSONArray.toJSON(e).toString();
        }
        return "FINISH";
    }
}
