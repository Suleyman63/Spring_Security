package com.example.demo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min=6, max=40)
    private String password;
    private String email;
    private Set<String> role;

}