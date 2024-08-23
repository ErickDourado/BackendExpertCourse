package models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public record AuthenticateRequest(
        @Schema(description = "User email", example = "erick@mail.com")
        @NotBlank(message = "Email cannot be empty")
        @Size(message = "Email must contains between 6 and 50 characters", min = 6, max = 50)
        @Email(message = "Invalid email")
        String email,

        @Schema(description = "User password", example = "123456")
        @NotBlank(message = "Password cannot be empty")
        @Size(message = "Password must contains between 3 and 50 characters", min = 3, max = 50)
        String password
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
