package com.oa.UrlShorter.DTOs;

import com.oa.UrlShorter.models.Role;

public record CreateUserCmd(
        String email,
        String password,
        String name,
        Role role
) {
}
