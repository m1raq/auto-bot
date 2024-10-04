package telegramBot.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "user_data")
@Entity
public class UserEntity {


    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "has_waiting_request")
    private Boolean hasWaitingRequest;

    @Column(name = "is_manager")
    private Boolean isManager;

    @Column(name = "contacting_with_id")
    private Long contactingWithId = (long) -1;



}
