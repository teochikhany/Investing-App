package usj.genielogiciel.investingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usj.genielogiciel.investingapp.model.AppUser;
import usj.genielogiciel.investingapp.model.Role;
import usj.genielogiciel.investingapp.repository.RoleRepository;
import usj.genielogiciel.investingapp.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor    // constructor with Dependency Injection for repositories
@Transactional              // to save everything made ?? maybe
@Slf4j                      // for logging
public class UserServiceImpl implements UserService, UserDetailsService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser saveUser(AppUser user)
    {
        log.info("Saving new user {} to database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role)
    {
        log.info("Saving new role {} to database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName)
    {
        log.info("Adding role {} to user {}", roleName, username);
        var user = userRepository.findByUsername(username);
        var role = roleRepository.findByName(roleName);
        user.getRoles().add(role);          // this will save to the database because of @Transactional
    }

    @Override
    public AppUser getUser(String username)
    {
        log.info("Fetching user {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> getUsers()
    {
        log.info("Fetching all user");
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AppUser user = userRepository.findByUsername(username);

        if (user == null)
        {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }

        log.info("user found in database: {}", username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())) );

        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
