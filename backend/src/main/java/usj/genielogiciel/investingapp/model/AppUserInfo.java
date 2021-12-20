package usj.genielogiciel.investingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data                   // getters and setters
@NoArgsConstructor
@AllArgsConstructor
public class AppUserInfo
{
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Name cannot be empty")
    private String name;
}
