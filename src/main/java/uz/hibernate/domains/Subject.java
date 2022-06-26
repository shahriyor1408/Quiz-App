package uz.hibernate.domains;

import jakarta.persistence.*;
import lombok.*;
import uz.hibernate.domains.auth.AuthUser;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject extends Auditable {
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER)
    @Column(nullable = false)
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<TestHistory> testHistoryList = new ArrayList<>();
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private AuthUser authUser;

    @Builder(builderMethodName = "childBuilder")
    public Subject(Long id, Timestamp createdAt, Long createdBy, Timestamp updatedAt, Long updatedBy, boolean deleted, String name) {
        super(id, createdAt, createdBy, updatedAt, updatedBy, deleted);
        this.name = name;
    }

    @Override
    public String toString() {
        String username = null;
        int size = 0;
        if (!Objects.isNull(authUser)) {
            username = authUser.getUsername();
        }

        if (!Objects.isNull(questionList)) {
            size = questionList.size();
        }
        Long id = this.getId();

        return "Subject{" + "id:" + id +
                ", name='" + name + '\'' +
                ", number questions=" + size +
                ", teacher=" + username +
                '}';
    }
}
