package com.oa.UrlShorter.controllers;

import com.oa.UrlShorter.DTOs.ShortUrlDTO;
import com.oa.UrlShorter.services.ShortUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ShortUrlService shortUrlService;

    public HomeController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<ShortUrlDTO> shortUrls = shortUrlService.findAllPublicShortUrls();

        model.addAttribute("shortUrls", shortUrls);
        model.addAttribute("baseUrl", "http://localhost:8080");
        return "index";
    }
}

// thymleaf ile artık index.html değil html desen olur.
// @ResponseBody -> direk bir mesaj dönmeyi sağlar(String json vb.). RestController oto dahildir. Controller ise view arar.
