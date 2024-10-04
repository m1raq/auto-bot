package telegramBot.database.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;

    private String username;

    private Long chatId;

    private Boolean hasWaitingRequest;

    private Boolean isManager;

    private Long contactingWithId = (long) -1;


}
