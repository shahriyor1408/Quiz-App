package uz.hibernate.service;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.TestHistoryDAO;
import uz.hibernate.dao.subject.SubjectDAO;
import uz.hibernate.domains.Subject;
import uz.hibernate.domains.TestHistory;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.AppErrorVO;
import uz.hibernate.vo.DataVO;
import uz.hibernate.vo.Session;
import uz.hibernate.vo.http.Response;
import uz.hibernate.vo.testHistory.TestHistoryCreateVO;
import uz.hibernate.vo.testHistory.TestHistoryUpdateVO;
import uz.hibernate.vo.testHistory.TestHistoryVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TestHistoryService extends AbstractDAO<TestHistoryDAO> implements GenericCRUDService<
        TestHistoryVO,
        TestHistoryCreateVO,
        TestHistoryUpdateVO,
        Long> {
    private static TestHistoryService instance;

    public TestHistoryService() {
        super(ApplicationContextHolder.getBean(TestHistoryDAO.class),
                ApplicationContextHolder.getBean(BaseUtil.class));
    }

    public static TestHistoryService getInstance() {
        if (instance == null) {
            instance = new TestHistoryService();
        }
        return instance;
    }

    @Override
    public Response<DataVO<Long>> create(@NonNull TestHistoryCreateVO vo) {
        TestHistory testHistory = TestHistory.childBuilder()
                .startedAt(vo.getStartedAt())
                .finishedAt(vo.getFinishedAt())
                .correct_quiz_count(vo.getCorrectAnswers())
                .quiz_count(vo.getQuizNumber())
                .build();
        return new Response<>(new DataVO<>(dao.save(testHistory).getId()));
    }

    @Override
    public Response<DataVO<Void>> update(@NotNull TestHistoryUpdateVO vo) {
        return null;
    }

    @Override
    public Response<DataVO<Void>> delete(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<DataVO<TestHistoryVO>> get(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<DataVO<List<TestHistoryVO>>> getAll() {
        Optional<Subject> optionalSubject = SubjectDAO.getInstance().findByUserId(Session.sessionUser.getUserId());
        if ((optionalSubject.isEmpty())) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Subject not found").build()), false);
        }
        Subject subject = optionalSubject.get();
        return getAll(subject.getName());
    }

    public Response<DataVO<List<TestHistoryVO>>> getAll(String subjectName) {
        List<TestHistory> testHistories = dao.findAll();
        List<TestHistoryVO> historyVOList = new ArrayList<>();

        for (TestHistory testHistory : testHistories) {
            if (testHistory.getSubject().getName().equals(subjectName)) {
                historyVOList.add(
                        TestHistoryVO.childBuilder()
                                .id(testHistory.getId())
                                .quizNumber(testHistory.getQuiz_count())
                                .startedAt(testHistory.getStartedAt())
                                .finishedAt(testHistory.getFinishedAt())
                                .correctAnswers(testHistory.getCorrect_quiz_count())
                                .subjectName(testHistory.getSubject().getName())
                                .build()
                );
            }
        }
        return new Response<>(new DataVO<>(historyVOList));
    }
}
