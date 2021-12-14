package usj.genielogiciel.investingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import usj.genielogiciel.investingapp.model.AppUser;
import usj.genielogiciel.investingapp.model.Role;
import usj.genielogiciel.investingapp.repository.RoleRepository;
import usj.genielogiciel.investingapp.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor    // constructor with Dependency Injection for repositories
@Transactional              // to save everything made ?? maybe
@Slf4j                      // for logging
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public AppUser saveUser(AppUser user)
    {
        log.info("Saving new user {} to database", user.getName());
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
}
