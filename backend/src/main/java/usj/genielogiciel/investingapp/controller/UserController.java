package usj.genielogiciel.investingapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import usj.genielogiciel.investingapp.model.*;
import usj.genielogiciel.investingapp.security.SecurityUtils;
import usj.genielogiciel.investingapp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController
{
    private final UserService userService;

    @GetMapping("/users")
    private ResponseEntity<List<AppUserInfo>> getUsers()
    {
        return ResponseEntity.ok(userService.getUsers()
                .stream()
                .map(user -> new AppUserInfo(user.getUsername(),user.getName()))
                .collect(Collectors.toList()));
    }

    @PostMapping("/user/save")
    private ResponseEntity<webResponce> addUser(@RequestBody @Valid AppUser user, Errors errors)
    {

        if (errors.hasErrors()) {
            log.error("Variable Validation error in addUser");

            val result = webResponce.builder()
                                                .timestamp(new Date())
                                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                                .errorMessage(getErrors(errors))
                                                .build();
            return new ResponseEntity<webResponce>(result, HttpStatus.BAD_REQUEST);
        }

        final AppUser appUser = userService.saveUser(user);
        final AppUserInfo appUserInfo = new AppUserInfo(appUser.getUsername(), appUser.getName());

        val result = webResponce.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.CREATED.value())
                .data(appUserInfo.toString())
                .build();

        return new ResponseEntity<webResponce>(result, HttpStatus.CREATED);
    }

//    @PostMapping("/role/save")
//    private ResponseEntity<Role> addRole(@RequestBody @Valid Role role, Errors errors)
//    {
//        if (errors.hasErrors()) {
//            throw new VariableValidation(errors);
//        }
//
//        return new ResponseEntity<Role>(userService.saveRole(role), HttpStatus.CREATED);
//    }

    @PostMapping("/role/saveToUser")
    private ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form)
    {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    // In case of spring security, you can get your current logged-in user by
    // Object user = Authentication authentication;
    @GetMapping("/user")
    private ResponseEntity<String> getUser(Authentication authentication)
    {
        return new ResponseEntity<String>(authentication.getName(), HttpStatus.OK);
    }


    @GetMapping("/user/refreshtoken")
    private void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            try{
                // Reading the token from the request
                String refresh_token = authorizationHeader.substring("Bearer ".length());

                String username = SecurityUtils.getSubject(refresh_token);
                AppUser user = userService.getUser(username);

                String access_token = SecurityUtils.getAccessToken(user, request.getRequestURI());

                // putting the tokens in the response
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }
            catch (Exception e)
            {
                log.error("Error refreshing the token in {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
    }

    private String getErrors(Errors errors)
    {
        String exceptionMessage = "";
        List<ConstraintViolation<?>> violationsList = new ArrayList<>();

        for (ObjectError e : errors.getAllErrors())
        {
            violationsList.add(e.unwrap(ConstraintViolation.class));
        }

        // Construct a helpful message for each input violation
        for (ConstraintViolation<?> violation : violationsList)
        {
            exceptionMessage += violation.getMessage() + "\n";
        }

        return exceptionMessage;
    }
}
