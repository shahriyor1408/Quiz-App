package uz.hibernate.service;

import lombok.NonNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.quiz.AnswerDAO;
import uz.hibernate.dao.quiz.QuestionDAO;
import uz.hibernate.domains.Answer;
import uz.hibernate.domains.Question;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.AppErrorVO;
import uz.hibernate.vo.DataVO;
import uz.hibernate.vo.http.Response;
import uz.hibernate.vo.quiz.AnswerCreateVO;
import uz.hibernate.vo.quiz.AnswerUpdateVO;
import uz.hibernate.vo.quiz.AnswerVO;

import java.util.List;
import java.util.Objects;

public class AnswerService extends AbstractDAO<AnswerDAO> implements GenericCRUDService<
        AnswerVO,
        AnswerCreateVO,
        AnswerUpdateVO,
        Long> {


    private static AnswerService instance;

    private AnswerService() {
        super(
                ApplicationContextHolder.getBean(AnswerDAO.class),
                ApplicationContextHolder.getBean(BaseUtil.class)
        );
    }

    public static AnswerService getInstance() {
        if (instance == null) {
            instance = new AnswerService();
        }
        return instance;
    }

    @Override
    public Response<DataVO<Long>> create(@NonNull AnswerCreateVO vo) {
        Question question = QuestionDAO.getInstance().findById(vo.getQuestionId());
        if (Objects.isNull(question)) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Question not found!")
                    .build()), false);
        }
        Answer answer = Answer.childBuilder()
                .variantA(vo.getVariantA())
                .variantB(vo.getVariantB())
                .variantC(vo.getVariantC())
                .correctAnswer(vo.getCorrectAnswer())
                .question(question)
                .build();

        return new Response<>(new DataVO<>(dao.saveAnswer(answer).getId()));
    }

    @Override
    public Response<DataVO<Void>> update(@NonNull AnswerUpdateVO vo) {
        return null;
    }

    @Override
    public Response<DataVO<Void>> delete(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<DataVO<AnswerVO>> get(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<DataVO<List<AnswerVO>>> getAll() {
        return null;
    }
}