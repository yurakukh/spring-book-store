package mate.academy.springbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.springbookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.springbookstore.dto.user.UserResponseDto;
import mate.academy.springbookstore.exception.RegistrationException;
import mate.academy.springbookstore.mapper.UserMapper;
import mate.academy.springbookstore.model.User;
import mate.academy.springbookstore.repository.UserRepository;
import mate.academy.springbookstore.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail((requestDto.getEmail())).isPresent()) {
            throw new RegistrationException("Can't register user: " + requestDto.getEmail()
                    + " email already exists");
        }
        User user = userMapper.toModel(requestDto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
