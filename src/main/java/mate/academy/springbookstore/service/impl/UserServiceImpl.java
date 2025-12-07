package mate.academy.springbookstore.service.impl;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.springbookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.springbookstore.dto.user.UserResponseDto;
import mate.academy.springbookstore.exception.EntityNotFoundException;
import mate.academy.springbookstore.exception.RegistrationException;
import mate.academy.springbookstore.mapper.UserMapper;
import mate.academy.springbookstore.model.Role;
import mate.academy.springbookstore.model.User;
import mate.academy.springbookstore.repository.RoleRepository;
import mate.academy.springbookstore.repository.UserRepository;
import mate.academy.springbookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail((requestDto.getEmail())).isPresent()) {
            throw new RegistrationException("Can't register user: " + requestDto.getEmail()
                    + " email already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role defaultRole = roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find "
                        + Role.RoleName.USER + " role"));
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
