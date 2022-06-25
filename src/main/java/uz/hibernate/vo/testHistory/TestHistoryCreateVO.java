package uz.hibernate.vo.testHistory;

import lombok.*;
import uz.hibernate.vo.BaseVO;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestHistoryCreateVO implements BaseVO {
    private Integer quizNumber;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Integer correctAnswers;
    private String subjectName;
}