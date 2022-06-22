package uz.hibernate.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uz.hibernate.utils.BaseUtil;

public class AbstractDAO<D extends BaseDAO> {
    protected final D dao;
    protected final Gson gson;
    protected final BaseUtil utils;

    public AbstractDAO(D dao, BaseUtil utils) {
        this.dao = dao;
        this.gson = new GsonBuilder().create();
        this.utils = utils;
    }
}
