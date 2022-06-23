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

    @Column(nullable = false,unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private AuthRole role;

    @Convert(converter = Status.StatusConvertor.class)
    private Status status;

    private Timestamp firstLoggedIn;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "auth_user_id")
    private AuthUser authUser;
}
