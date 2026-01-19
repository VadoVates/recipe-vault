package com.vadovates.app.recipevault.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO: Na czas testów robię otwarty dostęp przez API, potem będzie poprawiane.

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.findAll()
            .stream()
            .map(UserDto::from)
            .toList();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return UserDto.from(userService.findById(id));
    }

    @GetMapping("/search")
    public List<UserDto> search(@RequestParam String q) {
        return userService.search(q)
                .stream()
                .map(UserDto::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody CreateUserRequest userRequest) {
        return UserDto.from(userService.create(userRequest));
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @Valid @RequestBody CreateUserRequest userRequest) {
        return UserDto.from(userService.update(id, userRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
