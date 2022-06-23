package uz.hibernate.vo.subject;

import lombok.*;
import uz.hibernate.vo.BaseVO;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCreateVO implements BaseVO {
    private String name;
    private Long createdBy;
}
