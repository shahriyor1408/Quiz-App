package uz.hibernate.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import uz.hibernate.enums.AuthRole;

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

}
