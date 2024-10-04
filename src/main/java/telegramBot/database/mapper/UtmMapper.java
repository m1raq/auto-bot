package telegramBot.database.mapper;

import telegramBot.database.dto.UtmDto;
import telegramBot.database.entity.UtmEntity;

public class UtmMapper {

    public static UtmDto mapToDto(UtmEntity utmEntity){
        UtmDto utmDto = new UtmDto();
        utmDto.setId(utmEntity.getId());
        utmDto.setUtmValue(utmEntity.getUtmValue());
        utmDto.setClientUsername(utmEntity.getClientUsername());

        return utmDto;
    }


}
