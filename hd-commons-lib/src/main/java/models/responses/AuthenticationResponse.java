package models.responses;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record AuthenticationResponse(
        String token,
        String type
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
