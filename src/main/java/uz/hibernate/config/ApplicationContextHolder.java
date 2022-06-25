package uz.hibernate.config;

import uz.hibernate.dao.SolveTestDAO;
import uz.hibernate.dao.TestHistoryDAO;
import uz.hibernate.dao.auth.AuthUserDAO;
import uz.hibernate.dao.quiz.AnswerDAO;
import uz.hibernate.dao.quiz.QuestionDAO;
import uz.hibernate.dao.subject.SubjectDAO;
import uz.hibernate.service.*;
import uz.hibernate.utils.BaseUtil;

public class ApplicationContextHolder {

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return switch (beanName) {
            case "AuthUserDAO" -> (T) AuthUserDAO.getInstance();
            case "AuthUserService" -> (T) AuthUserService.getInstance();
            case "BaseUtil" -> (T) BaseUtil.getInstance();
            case "QuestionDAO" -> (T) QuestionDAO.getInstance();
            case "QuestionService" -> (T) QuestionService.getInstance();
            case "SubjectDAO" -> (T) SubjectDAO.getInstance();
            case "SubjectService" -> (T) SubjectService.getInstance();
            case "AnswerDAO" -> (T) AnswerDAO.getInstance();
            case "AnswerService" -> (T) AnswerService.getInstance();
            case "SolveTestService" -> (T) SolveTestService.getInstance();
            case "SolveTestDAO" -> (T) SolveTestDAO.getInstance();
            case "TestHistoryDAO" -> (T) TestHistoryDAO.getInstance();
            case "TestHistoryService" -> (T) TestHistoryService.getInstance();
            default -> throw new RuntimeException("Bean Not Found");
        };
    }

    public static <T> T getBean(Class<T> clazz) {
        String beanName = clazz.getSimpleName();
        return getBean(beanName);
    }
}
