package com.example.demo.controller;


import com.example.demo.model.ERoller;
import com.example.demo.model.Kisi;
import com.example.demo.model.KisiRole;
import com.example.demo.repository.KisiRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.request.JwtResponse;
import com.example.demo.request.LoginRequest;
import com.example.demo.request.MesajResponse;
import com.example.demo.request.RegisterRequest;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.servie.KisiServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    KisiRepository kisiRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> girisYap(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.
                authenticate( new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.JwtOlustur(authentication);

        KisiServiceImp loginKisi = (KisiServiceImp) authentication.getPrincipal();

        List<String> roller = loginKisi.getAuthorities().stream().
                map(item -> item.getAuthority()).
                collect(Collectors.toList());

        return ResponseEntity.ok( new JwtResponse(jwt,
                loginKisi.getId(),
                loginKisi.getUsername(),
                loginKisi.getEmail(),
                roller
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> kayitOl(@RequestBody RegisterRequest registerRequest) {
        if (kisiRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest().
                    body(new MesajResponse("Hata: username kullaniliyor"));
        }
        if (kisiRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest().
                    body(new MesajResponse("Hata: email kullaniliyor"));
        }


        Kisi kisi = new Kisi(registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getEmail());

        Set<String> stringRoller = registerRequest.getRole();
        Set<KisiRole> roller = new HashSet<>();

        if (stringRoller == null) {
            KisiRole userRole = roleRepository.findByName(ERoller.ROLE_USER).
                    orElseThrow(() -> new RuntimeException("hata: Veritabaninda Role kayitli değil"));
            roller.add(userRole);
        } else {
            stringRoller.forEach(role -> {
                switch (role) {
                    case "admin":
                        KisiRole adminRole = roleRepository.findByName(ERoller.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Hata: Role mevcut değil."));
                        roller.add(adminRole);
                        break;
                    case "mod":
                        KisiRole modRole = roleRepository.findByName(ERoller.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("EHata: Role mevcut değil."));
                        roller.add(modRole);
                        break;
                    default:
                        KisiRole userRole = roleRepository.findByName(ERoller.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Hata: Role mevcut değil."));
                        roller.add(userRole);
                }
            });

            kisi.setRoller(roller);
            kisiRepository.save(kisi);

        }
        return ResponseEntity.ok(new MesajResponse("Kullanıcı başarıyla kaydedildi."));
    }
}