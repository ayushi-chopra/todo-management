package com.todo_management.security;

import com.todo_management.entity.User;
import com.todo_management.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
                () -> new UsernameNotFoundException("User does not exits with username or email"));
        Set<GrantedAuthority> authorities=user.getRoles().stream().
                map(role->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
//        System.out.println(">>> loadUserByUsername: principal=" + user.getUsername()
//                + " usernameOrEmailParam=" + usernameOrEmail
//                + " password=[PROTECTED] roles=" + user.getRoles()
//                + " authorities=" + authorities);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(),authorities
        );
    }
}
