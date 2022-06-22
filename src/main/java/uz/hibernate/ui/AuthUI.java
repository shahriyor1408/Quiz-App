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
            BaseUtils.println("Login -> 1");
            BaseUtils.println("Register -> 2");
        } else {
            if (Session.sessionUser.getRole().equals(AuthRole.USER)) {
                BaseUtils.println("Show subjects -> 3");
                BaseUtils.println("Solve test    -> 4");
                BaseUtils.println("Show history  -> 5");
            } else if (Session.sessionUser.getRole().equals(AuthRole.TEACHER)) {
                BaseUtils.println("Create quiz    -> 6");
                BaseUtils.println("Show quiz list -> 7");
                BaseUtils.println("Update quiz    -> 8");
                BaseUtils.println("Delete quiz    -> 9");
            } else if (Session.sessionUser.getRole().equals(AuthRole.ADMIN)) {
                BaseUtils.println("Show subjects -> 3");
                BaseUtils.println("Solve test    -> 4");
                BaseUtils.println("Show history  -> 5");
                BaseUtils.println("Create quiz    -> 6");
                BaseUtils.println("Show quiz list -> 7");
                BaseUtils.println("Update quiz    -> 8");
                BaseUtils.println("Delete quiz    -> 9");
                BaseUtils.println("Subject CRUD   -> 10");
                BaseUtils.println("Block user     -> 11");
            }
            BaseUtils.println("Logout    -> 9");
        }

        BaseUtils.println("Quit -> q");
        String choice = BaseUtils.readText("?:");
        switch (choice) {
            case "1" -> authUI.login();
            case "2" -> authUI.register();
            case "3" -> authUI.subjectShowList();
            case "4" -> authUI.solveTest();
            case "5" -> authUI.showHistory();
            case "6" -> authUI.quizCreate();
            case "7" -> authUI.quizListShow();
            case "8" -> authUI.updateQuiz();
            case "9" -> authUI.deleteQuiz();
            case "10" -> authUI.subjectCRUD();
            case "11" -> authUI.blockUser();
            case "q" -> {
                BaseUtils.println("Bye", Colors.CYAN);
                System.exit(0);
            }
            default -> BaseUtils.println("Wrong Choice", Colors.RED);
        }
        main(args);
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
