package models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(

        @Schema(description = "Requester ID", example = "68be45a7bdaf18149fd4d94b")
        @Size(min = 24, max = 36, message = "requesterId must be between 24 and 36 characters")
        @NotBlank(message = "requesterId cannot be blank")
        String requesterId,

        @Schema(description = "Customer ID", example = "68be45a7bdaf18149fd4d94b")
        @Size(min = 24, max = 36, message = "customerId must be between 24 and 36 characters")
        @NotBlank(message = "customerId cannot be blank")
        String customerId,

        @Schema(description = "Title of order", example = "Fix my computer")
        @Size(min = 3, max = 50, message = "title must be between 3 and 50 characters")
        @NotBlank(message = "title cannot be blank")
        String title,

        @Schema(description = "Description of order", example = "My computer is not turning on")
        @Size(min = 10, max = 3000, message = "description must be between 10 and 3000 characters")
        @NotBlank(message = "description cannot be blank")
        String description,

        @Schema(description = "Status of order", allowableValues = {"Open", "In progress", "Closed", "Canceled"})
        @Size(min = 4, max = 11, message = "status must be between 4 and 11 characters")
        @NotBlank(message = "status cannot be blank")
        String status

) {}
