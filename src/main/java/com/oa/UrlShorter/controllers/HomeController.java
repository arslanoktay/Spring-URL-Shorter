package com.oa.UrlShorter.controllers;

import com.oa.UrlShorter.ApplicationProperties;
import com.oa.UrlShorter.DTOs.CreateShortUrlCmd;
import com.oa.UrlShorter.DTOs.CreateShortUrlForm;
import com.oa.UrlShorter.DTOs.ShortUrlDTO;
import com.oa.UrlShorter.exceptions.ShortUrlNotFoundException;
import com.oa.UrlShorter.models.PagedResult;
import com.oa.UrlShorter.models.User;
import com.oa.UrlShorter.services.SecurityUtils;
import com.oa.UrlShorter.services.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final ShortUrlService shortUrlService;
    private final ApplicationProperties properties;
    private final SecurityUtils securityUtils;

    public HomeController(ShortUrlService shortUrlService, ApplicationProperties properties, SecurityUtils securityUtils) {
        this.shortUrlService = shortUrlService;
        this.properties = properties;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/")
    public String home(
            //Pageable pageable,
            Model model,
            @RequestParam(defaultValue = "1") Integer page
    ) {
        User currentUser = securityUtils.getCurrentUser();

        this.addShortUrlsDataToModel(model,page);
        model.addAttribute("paginationUrl","/");
        model.addAttribute("createShortUrlForm", new CreateShortUrlForm("", false, null));
        return "index";
    }

    @PostMapping("/shorts-urls")
    String createShortUrl(@ModelAttribute("createShortUrlForm") @Valid CreateShortUrlForm createShortUrlForm,
                          BindingResult bindingResult, // atribute dan önce gelmeli ?
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if(bindingResult.hasErrors()) {
            this.addShortUrlsDataToModel(model,1);
            return "index";
        }

        try {
            Long userId = securityUtils.getCurrentUserId();
            CreateShortUrlCmd cmd = new CreateShortUrlCmd(
                    createShortUrlForm.originalUrl(),
                    createShortUrlForm.isPrivate(),
                    createShortUrlForm.expirationInDays(),
                    userId
            );
            var shortUrlDto = shortUrlService.createShortUrl(cmd);

            redirectAttributes.addFlashAttribute("successMessage","Short URL created successfully "
                        + properties.baseUrl() + "/s/" + shortUrlDto.shortKey());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create Short Url");
        }

        return "redirect:/";
    }

    @GetMapping("/s/{shortKey}")
    String redirectToOriginalUrl(@PathVariable String shortKey) {
        Long userId = securityUtils.getCurrentUserId();
        Optional<ShortUrlDTO> shortUrlDTOOptional =shortUrlService.accessShortUrl(shortKey, userId);
        if (shortUrlDTOOptional.isEmpty()) {
            throw new ShortUrlNotFoundException("Invalid short key: " + shortKey);
        }
        ShortUrlDTO shortUrlDTO = shortUrlDTOOptional.get();
        return "redirect:" + shortUrlDTO.originalUrl();

    }

    @GetMapping("/login")
    String loginForm() {
        return "login";
    }

    @GetMapping("/my-urls")
    public String showUserUrls(
        @RequestParam(defaultValue = "1") int page,
        Model model)
    {
        var currentUserId = securityUtils.getCurrentUserId();
        PagedResult<ShortUrlDTO> myUrls = shortUrlService.getUserShortUrls(currentUserId, page, properties.pageSize());
        model.addAttribute("shortUrls", myUrls);
        model.addAttribute("baseUrl", properties.baseUrl());
        model.addAttribute("paginationUrl","/my-urls");
        return "my-urls";
    }

    @PostMapping("/delete-urls")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR')") //@PreAuthorize("isAuthenticated()")
    public String deleteUrls(
            @RequestParam(value = "ids", required = false) List<Long> ids,
            RedirectAttributes redirectAttributes)
    {
        if (ids == null || ids.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "No URLs selected for delete"
            );
            return "redirect:/my-urls";
        }
        try {
            var currentUserId = securityUtils.getCurrentUserId();
            shortUrlService.deleteUserShortUrls(ids,currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Selected URLs have been deleted successfully");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting URLs: " + ex.getMessage());
        }
        return "redirect:/my-urls";
    }

    private void addShortUrlsDataToModel(Model model, int pageNo) {
        PagedResult<ShortUrlDTO> shortUrls = shortUrlService.findAllPublicShortUrls(pageNo, properties.pageSize());
        model.addAttribute("shortUrls", shortUrls);
        model.addAttribute("baseUrl", properties.baseUrl());
    }

}

// thymleaf ile artık index.html değil html desen olur.
// @ResponseBody -> direk bir mesaj dönmeyi sağlar(String json vb.). RestController oto dahildir. Controller ise view arar.
// Form başarılı ise her zaman redirect atmamız gerekmekte