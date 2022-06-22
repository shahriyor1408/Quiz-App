package uz.hibernate.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.hibernate.enums.AuthRole;
import uz.hibernate.enums.Status;
import uz.hibernate.vo.GenericVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserUpdateVO extends GenericVO {
    private String username;
    private String email;
    private AuthRole role;
    private Status status;
}
