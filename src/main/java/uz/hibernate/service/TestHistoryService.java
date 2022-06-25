package uz.hibernate.service;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import uz.hibernate.config.ApplicationContextHolder;
import uz.hibernate.dao.AbstractDAO;
import uz.hibernate.dao.TestHistoryDAO;
import uz.hibernate.utils.BaseUtil;
import uz.hibernate.vo.DataVO;
import uz.hibernate.vo.http.Response;
import uz.hibernate.vo.testHistory.TestHistoryCreateVO;
import uz.hibernate.vo.testHistory.TestHistoryUpdateVO;
import uz.hibernate.vo.testHistory.TestHistoryVO;

import java.util.List;

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

    @Override
    public Response<DataVO<Long>> create(@NonNull TestHistoryCreateVO vo) {
        return null;
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
        return null;
    }
}
