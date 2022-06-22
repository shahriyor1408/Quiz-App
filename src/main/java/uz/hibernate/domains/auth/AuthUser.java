package uz.hibernate.domains.auth;

import jakarta.persistence.*;
import lombok.*;
import uz.hibernate.domains.Auditable;
import uz.hibernate.domains.SessionEntity;
import uz.hibernate.domains.TestHistory;
import uz.hibernate.enums.AuthRole;
import uz.hibernate.enums.Status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser extends Auditable {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private AuthRole role;

    @Convert(converter = Status.StatusConvertor.class)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @OrderColumn(name = "type")
    private List<TestHistory> testHistoryList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_user_id")
    private SessionEntity session;

    @Builder(builderMethodName = "childBuilder")
    public AuthUser(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, String username, String password, String email, AuthRole role, Status status) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
    }
}
