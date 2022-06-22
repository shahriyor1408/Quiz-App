package uz.hibernate.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.hibernate.config.PasswordConfigurer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseUtil {
    private static BaseUtil instance;

    public String encode(String rawPassword) {
        return PasswordConfigurer.encode(rawPassword);
    }

    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return PasswordConfigurer.matchPassword(rawPassword, encodedPassword);
    }

    public static BaseUtil getInstance() {
        if (instance == null) {
            instance = new BaseUtil();
        }
        return instance;
    }
}
