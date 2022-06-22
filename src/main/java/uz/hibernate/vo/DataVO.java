package uz.hibernate.vo;

import lombok.Getter;

@Getter
public class DataVO<T> {
    private T body;
    private boolean success;
    private Long total;
    private AppErrorVO errorDTO;

    public DataVO(T body, Long total) {
        this(body);
        this.total = total;
    }

    public DataVO(AppErrorVO errorDTO) {
        this.errorDTO = errorDTO;
        this.success = false;
    }
    public DataVO(T body) {
        this.body = body;
        this.success = true;
    }


}
