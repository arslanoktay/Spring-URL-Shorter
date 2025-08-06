package com.oa.UrlShorter.services;

import com.oa.UrlShorter.DTOs.CreateShortUrlCmd;
import com.oa.UrlShorter.DTOs.ShortUrlDTO;
import com.oa.UrlShorter.models.ShortUrl;
import com.oa.UrlShorter.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
        var shortKey = generateUniqueShortKey();
        var shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(createShortUrlCmd.originalUrl());
        shortUrl.setShortKey(shortKey);
        shortUrl.setCreatedBy(null);
        shortUrl.setIsPrivate(false);
        shortUrl.setClickCount(0L);
        shortUrl.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
        shortUrl.setCreatedAt(Instant.now());
        urlRepository.save(shortUrl);

        return entityMapper.toShortUrlDTO(shortUrl);
    }

    private String generateUniqueShortKey() {
        String shortKey;
        do {
            shortKey = generateRandomShortKey();
        } while (urlRepository.existsByShortKey(shortKey));
        return shortKey;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnoprstuvwxyz0123456789";
    private static final int SHORT_KEY_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomShortKey() {
        StringBuilder sb = new StringBuilder(SHORT_KEY_LENGTH);
        for (int i = 0; i < SHORT_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();

    }

}
