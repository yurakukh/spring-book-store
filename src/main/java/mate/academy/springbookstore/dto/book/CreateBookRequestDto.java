package mate.academy.springbookstore.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class CreateBookRequestDto {
    @NotBlank(message = "{book.request.title.notblank}")
    @Size(max = 255, message = "{book.request.title.size}")
    private String title;

    @NotBlank(message = "{book.request.author.notblank}")
    @Size(max = 255, message = "{book.request.author.size}")
    private String author;

    @NotBlank(message = "{book.request.isbn.notblank}")
    @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
            message = "{book.request.isbn.format}")
    private String isbn;

    @NotNull(message = "{book.request.price.notnull}")
    @DecimalMin(value = "0.01", message = "{book.request.price.min}")
    private BigDecimal price;

    @Size(max = 666, message = "{book.request.description.max}")
    private String description;

    @Size(max = 255, message = "{book.request.coverImage.max}")
    private String coverImage;

    @NotEmpty(message = "{book.request.categoryids.notblank}")
    private List<Long> categoryIds;
}
