package usj.genielogiciel.investingapp.service;

import org.springframework.stereotype.Service;
import usj.genielogiciel.investingapp.model.AppUser;
import usj.genielogiciel.investingapp.model.Role;

import java.util.List;


public interface UserService
{
    AppUser saveUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser getUser(String username);
    List<AppUser> getUsers();
}
