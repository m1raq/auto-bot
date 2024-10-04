package telegramBot.database.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UtmDto {

    private Long id;
    private String utmValue;
    private String clientUsername;

}
