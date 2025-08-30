package com.erick.authserviceapi.service;

import com.erick.authserviceapi.model.User;
import com.erick.authserviceapi.repository.UserRepository;
import com.erick.authserviceapi.security.dtos.UserDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return UserDetailsDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getProfiles().stream()
                        .map(profile -> new SimpleGrantedAuthority(profile.getDescription()))
                        .collect(Collectors.toSet()))
                .build();
    }

}
