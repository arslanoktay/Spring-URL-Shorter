package com.oa.UrlShorter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "URL Shortener - Thymeleaf");
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}

// thymleaf ile artık index.html değil html desen olur.
// @ResponseBody -> direk bir mesaj dönmeyi sağlar(String json vb.). RestController oto dahildir. Controller ise view arar.
