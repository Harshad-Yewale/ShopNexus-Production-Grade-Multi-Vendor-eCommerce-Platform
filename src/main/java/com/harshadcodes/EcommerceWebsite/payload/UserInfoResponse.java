package com.harshadcodes.EcommerceWebsite.payload;

import java.util.List;

public record UserInfoResponse(
        Long id,
        String username,
        List<String> roles,
        String token
) {
}
