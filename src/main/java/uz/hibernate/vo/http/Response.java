package uz.hibernate.vo.http;

import lombok.*;
import uz.hibernate.vo.AppErrorVO;
import uz.hibernate.vo.DataVO;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Response<T> {
    private T body;
    private Integer total;
    private boolean ok = true;

    public Response(T body) {
        this.body = body;
    }

    public Response(T body, Integer total) {
        this.body = body;
        this.total = total;
    }

    public Response(T data, Boolean ok) {
        this.body = data;
        this.ok = ok;
    }
}
