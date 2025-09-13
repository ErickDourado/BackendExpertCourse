package models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequest(

        @Schema(description = "Refresh token", example = "157528da-f100-47d5-96e9-5ed48bd7d98b")
        @NotBlank(message = "Refresh token cannot be empty")
        @Size(message = "Refresh token must contains between 16 and 50 characters", min = 16, max = 50)
        String refreshToken

) {}
