package com.salsel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PdfController {

    @GetMapping("/about")
    public String about(){
        System.out.println("Inside about handler...");
        return "about";
    }
}
