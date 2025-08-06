package com.oa.UrlShorter.controllers;

import com.oa.UrlShorter.DTOs.CreateShortUrlCmd;
import com.oa.UrlShorter.DTOs.CreateShortUrlForm;
import com.oa.UrlShorter.DTOs.ShortUrlDTO;
import com.oa.UrlShorter.services.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("createShortUrlForm", new CreateShortUrlForm(""));
        return "index";
    }

    @PostMapping("/shorts-urls")
    String createShortUrl(@ModelAttribute("createShortUrlForm") @Valid CreateShortUrlForm createShortUrlForm,
                          BindingResult bindingResult, // atribute dan önce gelmeli ?
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if(bindingResult.hasErrors()) {
            List<ShortUrlDTO> shortUrls = shortUrlService.findAllPublicShortUrls();
            model.addAttribute("shortUrls", shortUrls);
            model.addAttribute("baseUrl", "http://localhost:8080");
            return "index";
        }

        try {
            CreateShortUrlCmd cmd = new CreateShortUrlCmd(createShortUrlForm.originalUrl());
            var shortUrlDto = shortUrlService.createShortUrl(cmd);

            redirectAttributes.addFlashAttribute("successMessage","Short URL created successfully "
                        + "http://localhost:8080/s/" + shortUrlDto.shortKey());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create Short Url");
        }

        return "redirect:/";
    }

}

// thymleaf ile artık index.html değil html desen olur.
// @ResponseBody -> direk bir mesaj dönmeyi sağlar(String json vb.). RestController oto dahildir. Controller ise view arar.
