package com.example.logger.security;

import com.example.logger.model.Client;
import com.example.logger.model.enums.ClientRole;
import com.example.logger.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Client client = clientRepository.getClientLogin(account).orElseThrow(() -> new UsernameNotFoundException("Account name not found"));
        return new User(client.getUsername(),client.getPassword(), mapRolesToAuthorities(client.getRole()));
    }

    private List<GrantedAuthority> mapRolesToAuthorities(ClientRole role){
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
}
