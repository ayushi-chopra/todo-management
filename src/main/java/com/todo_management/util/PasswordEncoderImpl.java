package com.todo_management.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderImpl {
    public static void main(String[] args) {

        PasswordEncoder encoder=new BCryptPasswordEncoder();
        System.out.println(encoder.encode("ayushi"));
        System.out.println((encoder.encode("admin")));
    }
}
