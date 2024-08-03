package models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.With;
import models.enums.ProfileEnum;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@With
public record UpdateUserRequest(
        @Schema(description = "User name", example = "Erick Gabriel")
        @Size(message = "Name must contains between 3 and 50 characters", min = 3, max = 50)
        String name,

        @Schema(description = "User email", example = "erick@mail.com")
        @Size(message = "Email must contains between 6 and 50 characters", min = 6, max = 50)
        @Email(message = "Invalid email")
        String email,

        @Schema(description = "User password", example = "123456")
        @Size(message = "Password must contains between 3 and 50 characters", min = 3, max = 50)
        String password,

        @Schema(description = "User profiles", example = "[\"ROLE_ADMIN\", \"ROLE_CUSTOMER\"]")
        Set<ProfileEnum> profiles
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
