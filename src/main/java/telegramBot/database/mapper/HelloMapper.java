package telegramBot.database.mapper;

import telegramBot.database.dto.HelloDto;
import telegramBot.database.entity.HelloEntity;

public class HelloMapper {

    public static HelloDto mapToDto(HelloEntity helloEntity){
        HelloDto helloDto = new HelloDto();

        helloDto.setId(helloEntity.getId());
        helloDto.setValue(helloEntity.getValue());

        return helloDto;
    }

    public static HelloEntity mapToEntity(HelloDto helloDto) {
        HelloEntity helloEntity = new HelloEntity();

        helloEntity.setValue(helloDto.getValue());

        return helloEntity;
    }

}
