package usj.genielogiciel.investingapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usj.genielogiciel.investingapp.model.AppUser;
import usj.genielogiciel.investingapp.model.Role;
import usj.genielogiciel.investingapp.model.RoleToUserForm;
import usj.genielogiciel.investingapp.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController
{
    private final UserService userService;

    @GetMapping("/users")
    private ResponseEntity<List<AppUser>> getUsers()
    {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/user/save")
    private ResponseEntity<AppUser> addUser(@RequestBody AppUser user)
    {
        return new ResponseEntity<AppUser>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/role/save")
    private ResponseEntity<Role> addRole(@RequestBody Role role)
    {
        return new ResponseEntity<Role>(userService.saveRole(role), HttpStatus.CREATED);
    }

    @PostMapping("/role/saveToUser")
    private ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form)
    {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
}
