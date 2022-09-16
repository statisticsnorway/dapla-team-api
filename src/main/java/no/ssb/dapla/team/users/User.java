package no.ssb.dapla.team.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "AdUser")
public class User {
    @Id
    @NonNull
    @Column(unique = true)
    private String emailShort;
    private String name;
    private String email;

}
