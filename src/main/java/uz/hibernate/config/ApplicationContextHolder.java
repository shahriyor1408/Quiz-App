package uz.hibernate.config;

import uz.hibernate.dao.auth.AuthUserDAO;
import uz.hibernate.service.AuthUserService;
import uz.hibernate.utils.BaseUtil;

public class ApplicationContextHolder {

    @SuppressWarnings("unchecked")
    public static  <T> T getBean(String beanName) {
        return switch (beanName) {
            case "AuthUserDAO" -> (T) AuthUserDAO.getInstance();
            case "AuthUserService" -> (T) AuthUserService.getInstance();
            case "BaseUtil" -> (T) BaseUtil.getInstance();
            default -> throw new RuntimeException("Bean Not Found");
        };
    }

    public static  <T> T getBean(Class<T> clazz) {
        String beanName = clazz.getSimpleName();
        return getBean(beanName);
    }

}
