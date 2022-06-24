package uz.hibernate.vo.auth;

import lombok.*;
import uz.hibernate.enums.AuthRole;
import uz.hibernate.vo.GenericVO;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserVO extends GenericVO {
    private String username;
    private String email;
    private AuthRole role;
    private Timestamp createdAt;
    private Long userId;
    private Boolean is_deleted;

    @Builder(builderMethodName = "childBuilder")
    public AuthUserVO(long id, String username, String email, AuthRole role, Timestamp createdAt, Long userId, boolean is_deleted) {
        super(id);
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.userId = userId;
        this.is_deleted = is_deleted;
    }
}
