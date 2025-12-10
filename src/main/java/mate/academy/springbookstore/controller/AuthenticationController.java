package mate.academy.springbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.springbookstore.dto.user.UserLoginRequestDto;
import mate.academy.springbookstore.dto.user.UserLoginResponseDto;
import mate.academy.springbookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.springbookstore.dto.user.UserResponseDto;
import mate.academy.springbookstore.security.AuthenticationService;
import mate.academy.springbookstore.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users management", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = "Register a new user",
            description = "Provide necessary information and register a new user")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto) {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Sign in", description = "Provide credentials to sign in")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
