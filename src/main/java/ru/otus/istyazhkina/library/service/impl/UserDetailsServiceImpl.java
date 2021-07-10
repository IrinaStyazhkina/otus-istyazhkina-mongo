package ru.otus.istyazhkina.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.istyazhkina.library.domain.entity.User;
import ru.otus.istyazhkina.library.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(s).orElseThrow(() -> new UsernameNotFoundException("No user found by this username"));
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), user.getAuthorities());
    }
}
