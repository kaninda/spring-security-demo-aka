package com.aka.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping("/private")
    public String privatePage (){
        return "hello 🥳🤩 this is a private part";
    }

    @GetMapping("/")
    public String publicPage(){
        return "hello, this is only public side";
    }

}
