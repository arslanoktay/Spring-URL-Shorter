package com.oa.UrlShorter.services;

import com.oa.UrlShorter.DTOs.ShortUrlDTO;
import com.oa.UrlShorter.models.ShortUrl;
import com.oa.UrlShorter.models.User;
import com.oa.UrlShorter.models.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public ShortUrlDTO toShortUrlDTO(ShortUrl shortUrl) {
        UserDTO userDTO = null;
        if(shortUrl.getCreatedBy() != null) {
            userDTO = toUserDTO(shortUrl.getCreatedBy());
        }

        return new ShortUrlDTO(
                shortUrl.getId(),
                shortUrl.getShortKey(),
                shortUrl.getOriginalUrl(),
                shortUrl.getIsPrivate(),
                shortUrl.getExpiresAt(),
                userDTO,
                shortUrl.getClickCount(),
                shortUrl.getCreatedAt()
        );
    }

    public UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName());
    }
}
