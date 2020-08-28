package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.repository.DevUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevUserService implements UserDetailsService {

    private final DevUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return Optional.of(repository.findByUserName(userName))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
