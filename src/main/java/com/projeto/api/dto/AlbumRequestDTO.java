package com.projeto.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record AlbumRequestDTO(
    @NotBlank String title,
    @NotEmpty List<Long> artistIds
) {}