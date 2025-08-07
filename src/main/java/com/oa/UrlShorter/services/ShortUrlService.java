package com.oa.UrlShorter.services;

import com.oa.UrlShorter.ApplicationProperties;
import com.oa.UrlShorter.DTOs.CreateShortUrlCmd;
import com.oa.UrlShorter.DTOs.ShortUrlDTO;
import com.oa.UrlShorter.models.ShortUrl;
import com.oa.UrlShorter.repository.UrlRepository;
import com.oa.UrlShorter.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional(readOnly = true)
public class ShortUrlService {

    private final UrlRepository urlRepository;
    private final EntityMapper entityMapper;
    private final ApplicationProperties properties;
    private final UserRepository userRepository;

    public ShortUrlService(UrlRepository urlRepository, EntityMapper entityMapper, ApplicationProperties properties, UserRepository userRepository) {
        this.urlRepository = urlRepository;
        this.entityMapper = entityMapper;
        this.properties = properties;
        this.userRepository = userRepository;
    }

    public List<ShortUrlDTO> findAllPublicShortUrls() {
        return urlRepository.findByPublicShortUrls()
                .stream().map(entityMapper::toShortUrlDTO).toList();
    }

    @Transactional
    public ShortUrlDTO createShortUrl(CreateShortUrlCmd createShortUrlCmd) {
        if (properties.validateOriginalUrl()) {
            boolean urlExists = UrlExistenceValidator.isUrlExists(createShortUrlCmd.originalUrl());
            if (!urlExists) {
                throw new RuntimeException("Invalid URL: " + createShortUrlCmd.originalUrl());
            }
        }
        var shortKey = generateUniqueShortKey();
        var shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(createShortUrlCmd.originalUrl());
        shortUrl.setShortKey(shortKey);
        if (createShortUrlCmd.userId() == null) {
            shortUrl.setCreatedAt(null);
            shortUrl.setIsPrivate(false);
            shortUrl.setExpiresAt(Instant.now().plus(properties.defaultExpiryInDays(), DAYS));
        } else {
            shortUrl.setCreatedBy(userRepository.findById(createShortUrlCmd.userId()).orElseThrow());
            shortUrl.setIsPrivate(createShortUrlCmd.isPrivate() != null && createShortUrlCmd.isPrivate());
            shortUrl.setExpiresAt(createShortUrlCmd.expirationInDays() != null ? Instant.now().plus(createShortUrlCmd.expirationInDays(), DAYS) : null);
        }
        shortUrl.setClickCount(0L);
        shortUrl.setCreatedAt(Instant.now());
        urlRepository.save(shortUrl);

        return entityMapper.toShortUrlDTO(shortUrl);
    }

    // Generate a Short Key, Unique search
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

    // Generate a Short Key
    private static String generateRandomShortKey() {
        StringBuilder sb = new StringBuilder(SHORT_KEY_LENGTH);
        for (int i = 0; i < SHORT_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    @Transactional
    public Optional<ShortUrlDTO> accessShortUrl(String shortKey, Long userId) {
        Optional<ShortUrl> shortUrlOptional = urlRepository.findByShortKey(shortKey);
        if (shortUrlOptional.isEmpty()) {
            return Optional.empty();
        }
        ShortUrl shortUrl = shortUrlOptional.get();

        if (shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(Instant.now())) {
            return Optional.empty();
        }
        if (shortUrl.getIsPrivate() != null && shortUrl.getCreatedBy().getId() != null && !Objects.equals(shortUrl.getCreatedBy().getId(), userId)) {
            return Optional.empty();
        }
        shortUrl.setClickCount(shortUrl.getClickCount()+1);
        urlRepository.save(shortUrl);
        return shortUrlOptional.map(entityMapper::toShortUrlDTO);
    }
}

// createShortUrl metodu hem write hem read transactional diğerleri read sadece.Override ettik
