package uz.hibernate.domains;

import jakarta.persistence.*;
import lombok.*;
import uz.hibernate.domains.auth.AuthUser;
import uz.hibernate.enums.AuthRole;
import uz.hibernate.enums.Status;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private AuthRole role;
    private Status status;
    private Timestamp firstLoggedIn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_user_id")
    private AuthUser authUser;
}
