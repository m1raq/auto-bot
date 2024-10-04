package telegramBot.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "utm_mark")
@Entity
public class UtmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "utm_value")
    private String utmValue;

    @Column(name = "client_username")
    private String clientUsername;

}
