package com.vadovates.app.recipevault.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() { return userRepository.findAll(); }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> search (String q) {
        return userRepository.findByDisplayNameContainingIgnoreCase(q);
    }

    @Transactional
    public User create (CreateUserRequest userRequest) {
        userRepository.findByEmail(userRequest.email()).ifPresent(existing -> {
            throw new UserAlreadyExistsException(userRequest.email());
        });

        User user = new User (
                userRequest.email(),
                hashPassword(userRequest.password()),
                userRequest.displayName()
        );

        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, CreateUserRequest userRequest) {
        User user = findById(id);
        user.setPasswordHash(hashPassword(userRequest.password()));
        user.setDisplayName(userRequest.displayName());
        user.setUpdatedAt();
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
    }

    private String hashPassword(String password) {
        // TODO: zamienić na bcrypt przy implementacji Spring Security
        return password;
    }
}
