package uz.hibernate.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.hibernate.vo.BaseVO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserCreateVO implements BaseVO {
    private String username;
    private String password;
    private String email;
}
