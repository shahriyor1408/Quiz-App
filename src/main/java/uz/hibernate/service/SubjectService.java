package uz.hibernate.service;

import lombok.NonNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.subject.SubjectDAO;
import uz.hibernate.domains.Subject;
import uz.hibernate.exceptions.CustomSQLException;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.Session;
import uz.hibernate.vo.http.Response;
import uz.hibernate.vo.subject.SubjectCreateVO;
import uz.hibernate.vo.subject.SubjectUpdateVO;
import uz.hibernate.vo.subject.SubjectVO;

import java.util.List;
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
    public Response<Long> create(@NonNull SubjectCreateVO vo) {
        Optional<Subject> optionalSubject = dao.findByName(vo.getName());
        if (optionalSubject.isPresent()) {
            throw new RuntimeException("subject already exist!");
        }
        Subject subject = Subject
                .childBuilder()
                .name(vo.getName())
                .createdBy(vo.getCreatedBy())
                .build();
        return new Response<>(dao.save(subject).getId());
    }

    @Override
    public Response<Void> update(@NonNull SubjectUpdateVO vo) {
        Optional<Subject> optionalAuthUser = dao.findByName(vo.getCurrent_name());
        if (optionalAuthUser.isPresent()) {
            throw new RuntimeException("subject already exist!");
        }

        SubjectDAO dao1 = new SubjectDAO();
        try {
            dao1.update(vo.getCurrent_name(), vo.getNew_name(), Session.sessionUser.getId());
        } catch (CustomSQLException e) {
            throw new RuntimeException(e.getCause().getLocalizedMessage());
        }


        return null;
    }

    @Override
    public Response<Void> delete(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<SubjectVO> get(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<List<SubjectVO>> getAll() {
        return null;
    }

    public static SubjectService getInstance() {
        if (instance == null) {
            instance = new SubjectService();
        }
        return instance;
    }
}
