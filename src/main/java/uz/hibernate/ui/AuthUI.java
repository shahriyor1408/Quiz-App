package uz.hibernate.ui;

import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.auth.AuthUserDAO;
import uz.hibernate.dao.subject.SubjectDAO;
import uz.hibernate.domains.Subject;
import uz.hibernate.enums.AuthRole;
import uz.hibernate.enums.QuestionType;
import uz.hibernate.service.*;
import uz.hibernate.vo.DataVO;
import uz.hibernate.vo.Session;
import uz.hibernate.vo.auth.AuthUserCreateVO;
import uz.hibernate.vo.auth.AuthUserUpdateVO;
import uz.hibernate.vo.auth.ResetPasswordVO;
import uz.hibernate.vo.http.Response;
import uz.hibernate.vo.quiz.AnswerCreateVO;
import uz.hibernate.vo.quiz.AnswerUpdateVO;
import uz.hibernate.vo.quiz.QuestionCreateVO;
import uz.hibernate.vo.quiz.QuestionUpdateVO;
import uz.hibernate.vo.subject.SubjectCreateVO;
import uz.hibernate.vo.subject.SubjectUpdateVO;
import uz.hibernate.vo.subject.SubjectVO;
import uz.jl.BaseUtils;
import uz.jl.Colors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AuthUI {
    static AuthUserService serviceUser = ApplicationContextHolder.getBean(AuthUserService.class);
    static QuestionService questionService = ApplicationContextHolder.getBean(QuestionService.class);
    static AnswerService answerService = ApplicationContextHolder.getBean(AnswerService.class);
    static SubjectService serviceSubject = ApplicationContextHolder.getBean(SubjectService.class);
    static SolveTestService solveTestService = ApplicationContextHolder.getBean(SolveTestService.class);
    static TestHistoryService testHistoryService = ApplicationContextHolder.getBean(TestHistoryService.class);
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
                BaseUtils.println("Answer update  -> 11");
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
            case "11" -> authUI.answerUpdate();
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
        Response<DataVO<Void>> response = serviceUser.deleteSession(Session.sessionUser.getId());
        print_response(response);
        Session.sessionUser = null;
    }

    private void giveTeacherRole() {
        /***
         * ME
         */
        String username = BaseUtils.readText("Enter username : ");
        String subjectName = BaseUtils.readText("Enter subjectName : ");
        print_response(serviceUser.giveTeacherPermission(username, subjectName));
    }

    private void answerUpdate() {
        /***
         * Javohir
         */
        String answerId = BaseUtils.readText("Enter answer id to update answers : ");

        BaseUtils.println("Enter new variants one by one  :  ");
        String variantA = BaseUtils.readText("Enter variant A : ");
        String variantB = BaseUtils.readText("Enter variant B : ");
        String variantC = BaseUtils.readText("Enter variant C : ");

        String correctAnswer = BaseUtils.readText("Enter variant correct answer for example \"A\" : ");
        switch (correctAnswer) {
            case "A" -> correctAnswer = variantA;
            case "B" -> correctAnswer = variantB;
            case "C" -> correctAnswer = variantC;
            default -> {
                correctAnswer = variantA;
            }
        }

        BaseUtils.println("******* Updating process ******", Colors.YELLOW);

        AnswerUpdateVO vo = AnswerUpdateVO.childBuilder()
                .id(Long.parseLong(answerId))
                .variantA(variantA)
                .variantB(variantB)
                .variantC(variantC)
                .correctAnswer(correctAnswer)
                .build();

        print_response(answerService.update(vo));
    }

    private void showAllUsers() {
        print_response(serviceUser.getAll());
    }

    private void settings() {
        BaseUtils.println("Update user    -> 1");
        BaseUtils.println("Reset password -> 2");
        BaseUtils.println("Back           -> 0");
        String choice = BaseUtils.readText("?:");
        switch (choice) {
            case "1" -> authUI.updateUser();
            case "2" -> authUI.resetPassword();
            case "0" -> {
                return;
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
        print_response(serviceUser.resetPassword(resetPasswordVO));
    }

    private void updateUser() {
        /***
         * ME
         */
        BaseUtils.println("***** Choose option you want to update *****", Colors.YELLOW);
        BaseUtils.println("Username     -> 1");
        BaseUtils.println("Email        -> 2");
        String choice = BaseUtils.readText("?: ");

        String username = null;
        String email = null;

        switch (choice) {
            case "1" -> username = BaseUtils.readText("Enter new username: ");
            case "2" -> email = BaseUtils.readText("Enter new email: ");
            default -> {
                BaseUtils.println("Wrong choice", Colors.RED);
                return;
            }
        }
        AuthUserUpdateVO authUserUpdateVO = AuthUserUpdateVO.childBuilder()
                .id(Session.sessionUser.getUserId())
                .username(username)
                .email(email)
                .build();
        print_response(serviceUser.update(authUserUpdateVO));
    }

    private void blockUser() {
        /***
         * ME
         */
        String id = BaseUtils.readText("Enter id: ");
        print_response(serviceUser.delete(Long.valueOf(id)));
    }

    private void subjectCRUD() {
        BaseUtils.println("Create subject    -> 1");
        BaseUtils.println("Show subject list -> 2");
        BaseUtils.println("Update subject    -> 3");
        BaseUtils.println("Delete subject    -> 4");
        BaseUtils.println("Back              -> 0");

        String choice = BaseUtils.readText("?:");
        switch (choice) {
            case "1" -> authUI.subjectCreate();
            case "2" -> authUI.subjectShowList();
            case "3" -> authUI.subjectUpdate();
            case "4" -> authUI.subjectDelete();
            case "0" -> {
                return;
            }
            default -> BaseUtils.println("Wrong Choice", Colors.RED);
        }
        subjectCRUD();
    }

    private void subjectDelete() {
        /***
         * Shohruh aka
         */
        SubjectVO vo = SubjectVO.childBuilder()
                .name(BaseUtils.readText("enter subject name to delete: "))
                .build();

        SubjectDAO dao = SubjectDAO.getInstance();
        Optional<Subject> subject = dao.findByName(vo.getName());
        if (subject.isEmpty()) {
            throw new RuntimeException("subject does not exist!");
        }
        print_response(serviceSubject.delete(subject.get().getId()));
    }

    private void subjectUpdate() {
        SubjectUpdateVO vo = SubjectUpdateVO.childBuilder()
                .current_name(BaseUtils.readText("enter subject name to update: "))
                .new_name(BaseUtils.readText("enter new name : "))
                .build();
        print_response(serviceSubject.update(vo));
    }

    private void subjectCreate() {
        SubjectCreateVO vo = SubjectCreateVO.builder()
                .name(BaseUtils.readText("Create name: "))
                .createdBy(Session.sessionUser.getUserId())
                .build();
        print_response(serviceSubject.create(vo));
    }

    private void deleteQuiz() {
        /***
         * Javohir
         */
        String questionId = BaseUtils.readText("Enter current question id : ");
        BaseUtils.println("***************** Deleting in process *************** ");
        print_response(questionService.delete(Long.valueOf(questionId)));
    }

    private void updateQuiz() {
        /***
         * Javohir
         */
        String currentDescription = BaseUtils.readText("Enter current question description : ");
        String newDescription = BaseUtils.readText("Enter new question description : ");
        BaseUtils.println("Choose question difficulty : ");
        BaseUtils.println("1.EASY");
        BaseUtils.println("2.MEDIUM");
        BaseUtils.println("3.HARD");

        BaseUtils.println("********* Updating process ********** ");

        String type = BaseUtils.readText("?:");
        QuestionType questionType = null;

        switch (type) {
            case "1" -> questionType = QuestionType.EASY;
            case "2" -> questionType = QuestionType.MEDIUM;
            case "3" -> questionType = QuestionType.HARD;
        }

        QuestionUpdateVO vo = QuestionUpdateVO.childBuilder()
                .currentText(currentDescription)
                .text(newDescription)
                .type(questionType)
                .build();

        print_response(questionService.update(vo));
    }

    private void quizListShow() {
        /***
         * Jahongir aka
         */
        BaseUtils.println("********** Question list  *************", Colors.YELLOW);
        print_response(questionService.getAll());
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
        Response<DataVO<Long>> response = questionService.createQuestion(vo, vo1);
        print_response(response);
    }

    private void showHistory() {
        if (Session.sessionUser.getRole().equals(AuthRole.ADMIN)) {
            BaseUtils.readText(" ********* Subject list **********");
            subjectShowList();
            String subjectName = BaseUtils.readText("Enter subject name : ");

            print_response(testHistoryService.getAll(subjectName));
        } else {
            print_response(testHistoryService.getAll());
        }
    }

    private void solveTest() {
        /***
         * Team work (developing some ideas by Shohruh aka)
         */
        BaseUtils.println("\n************************************************************************", Colors.YELLOW);
        subjectShowList();
        String subjectId = BaseUtils.readText("Enter subject id: ");


        if (SubjectService.getInstance().get(Long.valueOf(subjectId)).isOk()) {
            BaseUtils.println("Subject has successfully found!", Colors.PURPLE);
            BaseUtils.println("Choose difficulty level: ");
        } else {
            print_response(SubjectService.getInstance().get(Long.valueOf(subjectId)));
            return;
        }


        BaseUtils.println(QuestionType.EASY.name() + " -> 1");
        BaseUtils.println(QuestionType.MEDIUM.name() + " -> 2");
        BaseUtils.println(QuestionType.HARD.name() + " -> 3");
        String quizType = BaseUtils.readText("Choose one option: ");
        BaseUtils.println("How many quiz do you want to solve ?");
        String quizNumber = BaseUtils.readText("Enter amount: ");
        QuestionType questionType = null;
        switch (quizType) {

            case "1" -> questionType = QuestionType.EASY;
            case "2" -> questionType = QuestionType.MEDIUM;
            case "3" -> questionType = QuestionType.HARD;
            default -> questionType = QuestionType.EASY;
        }
        print_response(SolveTestService.solveTest(subjectId, questionType, quizNumber));
    }

    private void subjectShowList() {
        Optional<List<Subject>> subjects = serviceSubject.subjectShowList();
        if (subjects.isEmpty() || subjects.get().isEmpty()) {
            BaseUtils.println("Subject not found", Colors.GREEN);
        } else {
            List<Subject> subjectList = subjects.get();
            subjectList.forEach(subject -> BaseUtils.println(subject, Colors.GREEN));
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
        BaseUtils.println(BaseUtils.gson.toJson(response), color);
    }
}
