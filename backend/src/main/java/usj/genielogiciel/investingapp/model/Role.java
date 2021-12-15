package usj.genielogiciel.investingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
// @Table
@Data                   // getters and setters
@NoArgsConstructor      // self-explanatory
@AllArgsConstructor     // self-explanatory
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotEmpty(message = "Role name cannot be empty")
    private String name;
}
