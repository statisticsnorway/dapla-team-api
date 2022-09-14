package no.ssb.dapla.team.groups;

import jakarta.persistence.*;
import lombok.*;
import no.ssb.dapla.team.users.User;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AuthGroup")
public class Group {
    @Id
    @NonNull
    private String id;

    @OneToMany
    @Cascade(CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<User> users;


}
