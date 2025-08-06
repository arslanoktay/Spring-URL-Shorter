package com.oa.UrlShorter.services;

import com.oa.UrlShorter.DTOs.CreateShortUrlCmd;
import com.oa.UrlShorter.DTOs.ShortUrlDTO;
import com.oa.UrlShorter.models.ShortUrl;
import com.oa.UrlShorter.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortUrlService {

    private final UrlRepository urlRepository;
    private final EntityMapper entityMapper;

    public ShortUrlService(UrlRepository urlRepository, EntityMapper entityMapper) {
        this.urlRepository = urlRepository;
        this.entityMapper = entityMapper;
    }

    public List<ShortUrlDTO> findAllPublicShortUrls() {
        return urlRepository.findByPublicShortUrls()
                .stream().map(entityMapper::toShortUrlDTO).toList();
    }

    public ShortUrlDTO createShortUrl(CreateShortUrlCmd createShortUrlCmd) {
        var shortUrl = new ShortUrl();

        return null;
    }
}
