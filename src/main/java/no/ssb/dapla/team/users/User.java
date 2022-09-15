package no.ssb.dapla.team.users;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "SSB_USER")
public class User {
    @Id
    @NonNull
    @Column(unique = true)
    private String emailShort;
    private String name;
    private String email;

}
