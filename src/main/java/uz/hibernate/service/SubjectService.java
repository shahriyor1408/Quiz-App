package uz.hibernate.service;

import lombok.NonNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.subject.SubjectDAO;
import uz.hibernate.domains.Subject;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.AppErrorVO;
import uz.hibernate.vo.DataVO;
import uz.hibernate.vo.Session;
import uz.hibernate.vo.http.Response;
import uz.hibernate.vo.subject.SubjectCreateVO;
import uz.hibernate.vo.subject.SubjectUpdateVO;
import uz.hibernate.vo.subject.SubjectVO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SubjectService extends AbstractDAO<SubjectDAO> implements GenericCRUDService<
        SubjectVO,
        SubjectCreateVO,
        SubjectUpdateVO,
        Long
        > {

    private static SubjectService instance;
//    private final AuthUserValidator validator;

    private SubjectService() {
        super(
                ApplicationContextHolder.getBean(SubjectDAO.class),
                ApplicationContextHolder.getBean(BaseUtil.class)
        );
    }

    @Override
    public Response<DataVO<Long>> create(@NonNull SubjectCreateVO vo) {
        Optional<Subject> optionalSubject = dao.findByName(vo.getName());
        if (optionalSubject.isPresent() && !optionalSubject.get().isDeleted()) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("This subject is already added!")
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .build()), false);
        }
        Subject subject = Subject
                .childBuilder()
                .name(vo.getName())
                .createdBy(vo.getCreatedBy())
                .build();
        return new Response<>(new DataVO<>(dao.save(subject).getId()));
    }

    @Override
    public Response<DataVO<Void>> update(@NonNull SubjectUpdateVO vo) {
        Optional<Subject> optionalAuthUser = dao.findByName(vo.getCurrent_name());
        if (optionalAuthUser.isEmpty() || optionalAuthUser.get().isDeleted()) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Such subject not found")
                    .build()), false);
        }
        try {
            dao.update(vo.getCurrent_name(), vo.getNew_name(), Session.sessionUser.getUserId());
            return new Response<>(new DataVO<>(null, true));
        } catch (Exception e) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage(e.getMessage())
                    .developerMessage(e.getCause().getLocalizedMessage())
                    .build()), false);
        }
    }

    @Override
    public Response<DataVO<Void>> delete(@NonNull Long aLong) {
        Subject subject = dao.findById(aLong);
        if (Objects.isNull(subject)) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Subject not found!")
                    .build()), false);
        }

        SubjectDAO dao1 = SubjectDAO.getInstance();
        try {
            dao1.delete(subject.getName(), Session.sessionUser.getId());
            return new Response<>(new DataVO<>(null, true));
        } catch (Exception e) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage(e.getMessage())
                    .developerMessage(e.getCause().getLocalizedMessage())
                    .build()), false);
        }
    }

    @Override
    public Response<DataVO<SubjectVO>> get(@NonNull Long aLong) {
        Optional<Subject> optionalSubject = dao.findSubjectById(aLong);
        if (optionalSubject.isEmpty()) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("This subject is not found")
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .build()), false);
        }
        Subject subject = optionalSubject.get();
        return new Response<>(new DataVO<>(SubjectVO.childBuilder()
                .name(subject.getName())
                .id(subject.getId())
                .build()), true);
    }

    @Override
    public Response<DataVO<List<SubjectVO>>> getAll() {
        return null;
    }

    public static SubjectService getInstance() {
        if (instance == null) {
            instance = new SubjectService();
        }
        return instance;
    }

    public Optional<List<Subject>> subjectShowList() {
        return dao.subjectShowList();
    }

    public Optional<Subject> getByUserId(Long userId) {
        return dao.findByUserId(userId);
    }

    public Optional<Subject> findByName(String subjectName) {
        return dao.findByName(subjectName);
    }
}
