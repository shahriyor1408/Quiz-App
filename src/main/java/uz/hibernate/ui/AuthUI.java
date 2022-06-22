package uz.hibernate.ui;

import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.enums.AuthRole;
import uz.hibernate.service.AuthUserService;
import uz.hibernate.vo.Session;
import uz.hibernate.vo.auth.AuthUserCreateVO;
import uz.hibernate.vo.http.Response;
import uz.jl.BaseUtils;
import uz.jl.Colors;

import java.util.Objects;

public class AuthUI {
    static AuthUserService serviceUser = ApplicationContextHolder.getBean(AuthUserService.class);
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
                BaseUtils.println("Create quiz       -> 7");
                BaseUtils.println("Show quiz list    -> 8");
                BaseUtils.println("Update quiz       -> 9");
                BaseUtils.println("Delete quiz       -> 10");
                BaseUtils.println("Answer CRUD       -> 11");
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

    }

    private void giveTeacherRole() {

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
            case "1" -> authUI.answerCreate();
            case "2" -> authUI.answerShowList();
            case "3" -> authUI.answerUpdate();
            case "4" -> authUI.answerDelete();
            case "5" -> {
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

    private void answerCreate() {

    }

    private void showAllUsers() {

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

    }

    private void updateUser() {

    }

    private void blockUser() {

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
            case "5" -> {
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

    }

    private void subjectCreate() {

    }

    private void deleteQuiz() {

    }

    private void updateQuiz() {

    }

    private void quizListShow() {

    }

    private void quizCreate() {

    }

    private void showHistory() {

    }

    private void solveTest() {

    }

    private void subjectShowList() {

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
