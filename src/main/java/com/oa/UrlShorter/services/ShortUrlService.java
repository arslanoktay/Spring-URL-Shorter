package com.oa.UrlShorter.services;

import com.oa.UrlShorter.models.ShortUrl;
import com.oa.UrlShorter.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortUrlService {

    private final UrlRepository urlRepository;

    public ShortUrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public List<ShortUrl> findAllPublicShortUrls() {
        return urlRepository.findByPublicShortUrls();
    }
}
