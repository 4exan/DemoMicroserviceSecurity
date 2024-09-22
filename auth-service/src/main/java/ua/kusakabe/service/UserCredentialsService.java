package ua.kusakabe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.kusakabe.repository.UserCredentialRepository;

@Service
public class UserCredentialsService implements UserDetailsService {

    private final UserCredentialRepository repo;

    @Autowired
    public UserCredentialsService(UserCredentialRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
