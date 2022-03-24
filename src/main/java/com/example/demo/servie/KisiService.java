package com.example.demo.servie;

import com.example.demo.model.Kisi;
import com.example.demo.repository.KisiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KisiService implements UserDetailsService {

    @Autowired
    KisiRepository kisiRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Kisi kisi = kisiRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("Kullanici bulunamadi" + username));

        return KisiServiceImp.kisiOlustur(kisi);
    }
}
