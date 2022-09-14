package no.ssb.dapla.team.users;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "SSBUser")
public class User {
    @Id
    @NonNull
    private String emailShort;
    private String name;
    private String email;

}
