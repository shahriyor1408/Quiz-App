package uz.hibernate.service;

import lombok.NonNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.quiz.QuestionDAO;
import uz.hibernate.domains.Question;
import uz.hibernate.domains.Subject;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.AppErrorVO;
import uz.hibernate.vo.DataVO;
import uz.hibernate.vo.Session;
import uz.hibernate.vo.http.Response;
import uz.hibernate.vo.quiz.QuestionCreateVO;
import uz.hibernate.vo.quiz.QuestionUpdateVO;
import uz.hibernate.vo.quiz.QuestionVO;
import uz.jl.BaseUtils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class QuestionService extends AbstractDAO<QuestionDAO> implements GenericCRUDService<
        QuestionVO,
        QuestionCreateVO,
        QuestionUpdateVO,
        Long> {
    private static QuestionService instance;
    static SubjectService subjectService = SubjectService.getInstance();

    private QuestionService() {
        super(
                ApplicationContextHolder.getBean(QuestionDAO.class),
                ApplicationContextHolder.getBean(BaseUtil.class)
        );
    }

    public static QuestionService getInstance() {
        if (instance == null) {
            instance = new QuestionService();
        }
        return instance;
    }

    @Override
    public Response<DataVO<Long>> create(@NonNull QuestionCreateVO vo) {
        Optional<Question> questionOptional = null;
        try {
            questionOptional = dao.findByText(vo.getText());
            System.out.println(Session.sessionUser.getUserId());
            Optional<Subject> byUserId = subjectService.getByUserId(Session.sessionUser.getUserId());
            if (byUserId.isEmpty()) {
                return new Response<>(new DataVO<>(AppErrorVO.builder()
                        .friendlyMessage("Subject not found!")
                        .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                        .build()), false);
            }
            if (questionOptional.isPresent()) {
                return new Response<>(new DataVO<>(AppErrorVO.builder()
                        .friendlyMessage("Question already exist!")
                        .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                        .build()), false);
            }
            Question question = Question.childBuilder()
                    .text(vo.getText())
                    .type(vo.getType())
                    .subject(byUserId.get().getAuthUser().getSubject())
                    .createdBy(Session.sessionUser.getUserId())
                    .build();
            return new Response<>(new DataVO<>(dao.save(question).getId()));
        } catch (Exception e) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage(e.getMessage())
                    .developerMessage(e.getCause().getLocalizedMessage())
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .build()), false);
        }
    }

    @Override
    public Response<DataVO<Void>> update(@NonNull QuestionUpdateVO vo) {
        String textForFind = vo.getCurrentText();
        try {
            Optional<Question> questionOptional = dao.findByText(textForFind);
            if (questionOptional.isEmpty()) {
                throw new RuntimeException("Question not found!");
            }
            Question question = Question.childBuilder()
                    .text(vo.getText())
                    .type(vo.getType())
                    .build();
            QuestionDAO dao1 = new QuestionDAO();
            dao1.update(textForFind, question.getText(), question.getType());
            return new Response<>(new DataVO<>(null, true));
        } catch (Exception e) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage(e.getMessage())
                    .build()), false);
        }
    }

    @Override
    public Response<DataVO<Void>> delete(@NonNull Long id) {
        Optional<Question> question = dao.findByQuestionId(id);
        if (Objects.isNull(question)) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Question not found!")
                    .build()), false);
        }
        QuestionDAO dao1 = new QuestionDAO();
        try {
            dao1.delete(id);
        } catch (Exception e) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage(e.getMessage())
                    .build()), false);
        }
        return new Response<>(new DataVO<>(null, true));
    }

    @Override
    public Response<DataVO<QuestionVO>> get(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<DataVO<List<QuestionVO>>> getAll() {
        List<Question> questions = dao.findAll();

        if(Objects.isNull(questions)){
            BaseUtils.println("Question list is empty");
        }
        List<QuestionVO> questionVOList = new ArrayList<>();


        for (Question question : questions) {
            questionVOList.add(QuestionVO.childBuilder()
                    .id(question.getId())
                    .text(question.getText())
                    .type(question.getType())
                    .createdAt(question.getCreatedAt())
                    .subjectId(question.getSubject().getId())
                    .build());
        }

        return new Response<>(new DataVO<>(questionVOList));
    }
}
