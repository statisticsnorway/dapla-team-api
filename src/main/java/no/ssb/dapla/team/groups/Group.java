package no.ssb.dapla.team.groups;

import jakarta.persistence.*;
import lombok.*;
import no.ssb.dapla.team.users.User;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Set;

@Data
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
    private Set<User> users;


}
