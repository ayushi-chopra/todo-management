package com.todo_management.service;

import com.todo_management.dto.LoginDto;
import com.todo_management.dto.RegisterDto;

public interface AuthService {
    void register(RegisterDto registerDto);
    String login(LoginDto loginDto);
}
