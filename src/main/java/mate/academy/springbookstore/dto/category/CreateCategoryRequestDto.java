package mate.academy.springbookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotBlank(message = "{category.request.name.notblank}")
        @Size(max = 255, message = "{category.request.name.size}")
        String name,

        @Size(max = 666, message = "{category.request.description.size}")
        String description
) {
}
