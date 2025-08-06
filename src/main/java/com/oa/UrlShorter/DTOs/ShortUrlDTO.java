package com.oa.UrlShorter.DTOs;

import com.oa.UrlShorter.models.UserDTO;

import java.io.Serializable;
import java.time.Instant;

public record ShortUrlDTO(
        Long id,
        String shortKey,
        String originalUrl,
        Boolean isPrivate,
        Instant expiresAt,
        UserDTO createdBy,
        Long clickCount,
        Instant createdAt
) implements Serializable {}
