package uz.hibernate.vo;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class GenericVO implements BaseVO {
    protected long id;
}
