package uz.hibernate.service;

import lombok.NonNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.quiz.AnswerDAO;
import uz.hibernate.domains.Answer;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.http.Response;
import uz.hibernate.vo.quiz.AnswerCreateVO;
import uz.hibernate.vo.quiz.AnswerUpdateVO;
import uz.hibernate.vo.quiz.AnswerVO;

import java.util.List;

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
    public Response<Long> create(@NonNull AnswerCreateVO vo) {

        Answer answer = Answer.childBuilder()
                .variantA(vo.getVariantA())
                .variantB(vo.getVariantB())
                .variantC(vo.getVariantC())
                .correctAnswer(vo.getCorrectAnswer())
                .build();

        return new Response<>(dao.saveAnswer(answer).getId());
    }

    @Override
    public Response<Void> update(@NonNull AnswerUpdateVO vo) {
        return null;
    }

    @Override
    public Response<Void> delete(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<AnswerVO> get(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<List<AnswerVO>> getAll() {
        return null;
    }
}