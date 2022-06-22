package uz.hibernate.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import uz.hibernate.enums.AuthRole;
import uz.hibernate.enums.Status;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {
    @Id
    private Long id;
    private String username;
    private AuthRole role;
    private Status status;
}
