package uz.hibernate.ui;

import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.auth.AuthUserDAO;
import uz.hibernate.domains.Subject;
import uz.hibernate.enums.AuthRole;
import uz.hibernate.enums.QuestionType;
import uz.hibernate.exceptions.CustomSQLException;
import uz.hibernate.service.AnswerService;
import uz.hibernate.service.AuthUserService;
import uz.hibernate.service.QuestionService;
import uz.hibernate.service.SubjectService;
import uz.hibernate.vo.Session;
import uz.hibernate.vo.auth.AuthUserCreateVO;
import uz.hibernate.vo.auth.ResetPasswordVO;
import uz.hibernate.vo.http.Response;
import uz.hibernate.vo.quiz.AnswerCreateVO;
import uz.hibernate.vo.quiz.QuestionCreateVO;
import uz.hibernate.vo.subject.SubjectCreateVO;
import uz.hibernate.vo.subject.SubjectUpdateVO;
import uz.jl.BaseUtils;
import uz.jl.Colors;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AuthUI {
    static AuthUserService serviceUser = ApplicationContextHolder.getBean(AuthUserService.class);
    static QuestionService questionService = ApplicationContextHolder.getBean(QuestionService.class);
    static AnswerService answerService = ApplicationContextHolder.getBean(AnswerService.class);
    static AuthUserDAO authUserDAO = ApplicationContextHolder.getBean(AuthUserDAO.class);
    static SubjectService serviceSubject = ApplicationContextHolder.getBean(SubjectService.class);
    static AuthUI authUI = new AuthUI();

    public static void main(String[] args) {
        if (Objects.isNull(Session.sessionUser)) {
            BaseUtils.println("Login    -> 1");
            BaseUtils.println("Register -> 2");
        } else {
            if (Session.sessionUser.getRole().equals(AuthRole.USER)) {
                BaseUtils.println("Show subjects -> 3");
                BaseUtils.println("Solve test    -> 4");
                BaseUtils.println("Show history  -> 5");
                BaseUtils.println("Settings      -> 6");
            } else if (Session.sessionUser.getRole().equals(AuthRole.TEACHER)) {
                BaseUtils.println("Create quiz    -> 7");
                BaseUtils.println("Show quiz list -> 8");
                BaseUtils.println("Update quiz    -> 9");
                BaseUtils.println("Delete quiz    -> 10");
                BaseUtils.println("Answer CRUD    -> 11");
            } else if (Session.sessionUser.getRole().equals(AuthRole.ADMIN)) {
                BaseUtils.println("Show subjects     -> 3");
                BaseUtils.println("Solve test        -> 4");
                BaseUtils.println("Show history      -> 5");
                BaseUtils.println("Block user        -> 12");
                BaseUtils.println("Subject CRUD      -> 13");
                BaseUtils.println("Show all users    -> 14");
                BaseUtils.println("Give teacher role -> 15");
            }
            BaseUtils.println("Logout    -> 0");
        }

        BaseUtils.println("Quit -> q");
        String choice = BaseUtils.readText("?:");
        switch (choice) {
            case "1" -> authUI.login();
            case "2" -> authUI.register();
            case "3" -> authUI.subjectShowList();
            case "4" -> authUI.solveTest();
            case "5" -> authUI.showHistory();
            case "6" -> authUI.settings();
            case "7" -> authUI.quizCreate();
            case "8" -> authUI.quizListShow();
            case "9" -> authUI.updateQuiz();
            case "10" -> authUI.deleteQuiz();
            case "11" -> authUI.answerCRUD();
            case "12" -> authUI.blockUser();
            case "13" -> authUI.subjectCRUD();
            case "14" -> authUI.showAllUsers();
            case "15" -> authUI.giveTeacherRole();
            case "0" -> authUI.logout();
            case "q" -> {
                BaseUtils.println("Bye", Colors.CYAN);
                System.exit(0);
            }
            default -> BaseUtils.println("Wrong Choice", Colors.RED);
        }
        main(args);
    }

    private void logout() {
        serviceUser.deleteSession(Session.sessionUser.getId());
        Session.sessionUser = null;
    }

    private void giveTeacherRole() {
        /***
         * ME
         */
        String id = BaseUtils.readText("Enter userId : ");
        String s_id = BaseUtils.readText("Enter subjectId : ");
        serviceUser.giveTeacherPermission(id, s_id);
    }

    private void answerCRUD() {
        BaseUtils.println("Create answer    -> 1");
        BaseUtils.println("Show answer list -> 2");
        BaseUtils.println("Update answer    -> 3");
        BaseUtils.println("Delete answer    -> 4");
        BaseUtils.println("Back             -> 0");
        BaseUtils.println("Quit             -> q");

        String choice = BaseUtils.readText("?:");
        switch (choice) {
            case "1" -> authUI.answerShowList();
            case "2" -> authUI.answerUpdate();
            case "3" -> authUI.answerDelete();
            case "4" -> {
                return;
            }
            case "q" -> {
                BaseUtils.println("Bye", Colors.CYAN);
                System.exit(0);
            }
            default -> BaseUtils.println("Wrong Choice", Colors.RED);
        }
        answerCRUD();
    }

    private void answerDelete() {

    }

    private void answerUpdate() {

    }

    private void answerShowList() {

    }

    private void showAllUsers() {
        /***
         * ME
         */
    }

    private void settings() {
        BaseUtils.println("Update user    -> 1");
        BaseUtils.println("Reset password -> 2");
        BaseUtils.println("Back           -> 0");
        BaseUtils.println("Quit           -> q");
        String choice = BaseUtils.readText("?:");
        switch (choice) {
            case "1" -> authUI.updateUser();
            case "2" -> authUI.resetPassword();
            case "0" -> {
                return;
            }
            case "q" -> {
                BaseUtils.println("Bye", Colors.CYAN);
                System.exit(0);
            }
            default -> BaseUtils.println("Wrong Choice", Colors.RED);
        }
        settings();
    }

    private void resetPassword() {
        ResetPasswordVO resetPasswordVO = ResetPasswordVO.builder()
                .newPassword(BaseUtils.readText("Old password: "))
                .newPassword(BaseUtils.readText("New password: "))
                .confirmPassword(BaseUtils.readText("New password again: ")).build();
        serviceUser.resetPassword(resetPasswordVO);
    }

    private void updateUser() {
        /***
         * ME
         */
    }

    private void blockUser() {
        /***
         * ME
         */

    }

    private void subjectCRUD() {
        BaseUtils.println("Create subject    -> 1");
        BaseUtils.println("Show subject list -> 2");
        BaseUtils.println("Update subject    -> 3");
        BaseUtils.println("Delete subject    -> 4");
        BaseUtils.println("Back              -> 0");
        BaseUtils.println("Quit -> q");

        String choice = BaseUtils.readText("?:");
        switch (choice) {
            case "1" -> authUI.subjectCreate();
            case "2" -> authUI.subjectShowList();
            case "3" -> authUI.subjectUpdate();
            case "4" -> authUI.subjectDelete();
            case "0" -> {
                return;
            }
            case "q" -> {
                BaseUtils.println("Bye", Colors.CYAN);
                System.exit(0);
            }
            default -> BaseUtils.println("Wrong Choice", Colors.RED);
        }
        subjectCRUD();
    }

    private void subjectDelete() {

    }

    private void subjectUpdate() {
        SubjectUpdateVO vo = SubjectUpdateVO.childBuilder()
                .current_name(BaseUtils.readText("enter subject name to update: "))
                .new_name(BaseUtils.readText("enter new name"))
                .build();
        print_response(serviceSubject.update(vo));
    }

    private void subjectCreate() {
        SubjectCreateVO vo = SubjectCreateVO.builder()
                .name(BaseUtils.readText("Create name: "))
                .createdBy(Session.sessionUser.getId())
                .build();
        print_response(serviceSubject.create(vo));
    }

    private void deleteQuiz() {

    }

    private void updateQuiz() {

    }

    private void quizListShow() {

    }

    private void quizCreate() {
        String testDescription = BaseUtils.readText("Enter description : ");
        BaseUtils.println("EASY  -> 1");
        BaseUtils.println("MEDIUM -> 2");
        BaseUtils.println("HARD   -> 3");

        String type = BaseUtils.readText("?:");

        switch (type) {
            case "1" -> type = "EASY";
            case "2" -> type = "MEDIUM";
            case "3" -> type = "HARD";
        }

        String variantA = BaseUtils.readText("Enter variant A : ");
        String variantB = BaseUtils.readText("Enter variant B : ");
        String variantC = BaseUtils.readText("Enter variant C : ");

        String correctAnswer = BaseUtils.readText("Enter correct answer for example \"A\" : ");

        switch (correctAnswer) {

            case "A" -> correctAnswer = variantA;
            case "B" -> correctAnswer = variantB;
            case "C" -> correctAnswer = variantC;
            default -> {
                correctAnswer = variantA;
            }

        }
        QuestionCreateVO vo = QuestionCreateVO.builder()
                .text(testDescription)
                .type(QuestionType.valueOf(type))
                .build();

        AnswerCreateVO vo1 = AnswerCreateVO.builder()
                .variantA(variantA)
                .variantB(variantB)
                .variantC(variantC)
                .correctAnswer(correctAnswer)
                .build();
        print_response(questionService.create(vo));
        print_response(answerService.create(vo1));
    }

    private void showHistory() {

    }

    private void solveTest() {

    }

    private void subjectShowList() {
        Optional subjects = authUserDAO.subjectShowList();
        List<Subject> subjectList = (List<Subject>) subjects.get();
        if (subjectList.isEmpty()) {
            BaseUtils.println("Subject not found", Colors.GREEN);
        } else {
            subjectList.forEach(subject -> BaseUtils.println(subject,Colors.GREEN));
        }
    }

    private void register() {
        AuthUserCreateVO vo = AuthUserCreateVO.builder()
                .username(BaseUtils.readText("Create username: "))
                .email(BaseUtils.readText("Enter email: "))
                .password(BaseUtils.readText("Create password: "))
                .build();
        print_response(serviceUser.create(vo));
    }

    private void login() {
        String username = BaseUtils.readText("Enter username: ");
        String password = BaseUtils.readText("Enter password: ");
        print_response(serviceUser.login(username, password));
    }

    public void print_response(Response response) {
        String color = !response.isOk() ? Colors.RED : Colors.GREEN;
        BaseUtils.println(BaseUtils.gson.toJson(response.getBody()), color);
    }
}
