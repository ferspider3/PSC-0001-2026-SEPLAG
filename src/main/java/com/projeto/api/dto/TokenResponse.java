package com.projeto.api.dto;

public record TokenResponse(
    String accessToken, 
    String refreshToken
) {}