package com.todo_management.service.impl;

import com.todo_management.dto.LoginDto;
import com.todo_management.dto.RegisterDto;
import com.todo_management.entity.Role;
import com.todo_management.entity.User;
import com.todo_management.exception.TodoApiException;
import com.todo_management.repository.RoleRepository;
import com.todo_management.repository.UserRepository;
import com.todo_management.security.JwtTokenProvider;
import com.todo_management.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;

    @Override
    public void register(RegisterDto registerDto) {
       if(userRepository.existsByEmail(registerDto.getEmail())){
           throw new TodoApiException(HttpStatus.BAD_REQUEST,"Email already exists");
       }
       if(userRepository.existsByUsername(registerDto.getUsername())){
           throw new TodoApiException(HttpStatus.BAD_REQUEST,"Username already exists");
       }

       User user=new User();
       user.setName(registerDto.getName());
       user.setEmail(registerDto.getEmail());
       user.setUsername(registerDto.getUsername());
       user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
       Set<Role> roles= new HashSet<>();
        Role roleUser = roleRepository.findByName("ROLE_USER");
        roles.add(roleUser);
        user.setRoles(roles);
        userRepository.save(user);


    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token= tokenProvider.generateToken(authenticate);
        return token;
    }
}
