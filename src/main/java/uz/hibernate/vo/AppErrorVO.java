package uz.hibernate.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AppErrorVO {
    private Timestamp timestamp;
    private String friendlyMessage;
    private String developerMessage;

    public AppErrorVO(String friendlyMessage, String developerMessage) {
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
        this.friendlyMessage = friendlyMessage;
        this.developerMessage = developerMessage;
    }

    public AppErrorVO(String friendlyMessage) {
        this(friendlyMessage, friendlyMessage);
    }
}
