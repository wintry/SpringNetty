package com.xuyh.SpringNetty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class controller {


    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String index() {
        return  "hello";
    }
}
