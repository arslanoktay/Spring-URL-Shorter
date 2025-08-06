package com.oa.UrlShorter.DTOs;

import java.io.Serializable;

public record UserDTO(
        Long id,
        String name
) implements Serializable {}
