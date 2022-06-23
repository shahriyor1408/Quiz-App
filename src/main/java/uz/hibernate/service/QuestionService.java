package uz.hibernate.service;

import lombok.NonNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.quiz.QuestionDAO;
import uz.hibernate.domains.Question;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.http.Response;
import uz.hibernate.vo.quiz.QuestionCreateVO;
import uz.hibernate.vo.quiz.QuestionUpdateVO;
import uz.hibernate.vo.quiz.QuestionVO;

import java.util.List;
import java.util.Optional;

public class QuestionService extends AbstractDAO<QuestionDAO> implements GenericCRUDService<
        QuestionVO,
        QuestionCreateVO,
        QuestionUpdateVO,
        Long> {


    private static QuestionService instance;

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
    public Response<Long> create(@NonNull QuestionCreateVO vo) {
        Optional<Question> questionOptional = dao.findByText(vo.getText());
        if (questionOptional.isPresent()) {
            throw new RuntimeException("Question already exist!");
        }

        Question question = Question.childBuilder()
                .text(vo.getText())
                .type(vo.getType())
                .build();
        return new Response<>(dao.save(question).getId());
    }

    @Override
    public Response<Void> update(@NonNull QuestionUpdateVO vo) {
        return null;
    }

    @Override
    public Response<Void> delete(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<QuestionVO> get(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<List<QuestionVO>> getAll() {
        return null;
    }
}
