package mate.academy.springbookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mate.academy.springbookstore.validation.annotation.FieldMatch;

@Setter
@Getter
@RequiredArgsConstructor
@FieldMatch(firstFieldName = "password",
        secondFieldName = "repeatPassword",
        message = "{user.request.password.mismatch}")
public class UserRegistrationRequestDto {
    @NotBlank(message = "{user.request.firstname.notblank}")
    @Size(max = 255, message = "{user.request.firstname.size}")
    private String firstName;

    @NotBlank(message = "{user.request.lastname.notblank}")
    @Size(max = 255, message = "{user.request.lastname.size}")
    private String lastName;

    @NotBlank(message = "{user.request.email.notblank}")
    @Email(message = "{user.request.email.format}")
    private String email;

    @NotBlank(message = "{user.request.password.notblank}")
    @Size(min = 8, max = 100, message = "{user.request.password.size}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "{user.request.password.format}"
    )
    private String password;

    @NotBlank(message = "{user.request.repeatpassword.notblank}")
    @Size(min = 8, max = 100, message = "{user.request.repeatpassword.size}")
    private String repeatPassword;

    @Size(max = 255, message = "{user.request.shippingaddress.size}")
    private String shippingAddress;
}
