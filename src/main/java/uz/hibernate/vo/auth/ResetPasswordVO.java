package uz.hibernate.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordVO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
