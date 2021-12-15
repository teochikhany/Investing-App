package usj.genielogiciel.investingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;

// @Table this if for defining a different name to the table
@Entity
@Data                   // getters and setters
@NoArgsConstructor
@AllArgsConstructor
public class AppUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
}
