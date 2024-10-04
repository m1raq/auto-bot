package telegramBot.database.mapper;

import telegramBot.database.dto.UserDto;
import telegramBot.database.entity.UserEntity;


public class UserMapper {

    public static UserDto mapToDto(UserEntity userEntity){
        UserDto userDto = new UserDto();

        userDto.setId(userEntity.getId());
        userDto.setUsername(userEntity.getUsername());
        userDto.setChatId(userEntity.getChatId());
        userDto.setIsManager(userEntity.getIsManager());
        userDto.setHasWaitingRequest(userEntity.getHasWaitingRequest());
        userDto.setContactingWithId(userEntity.getContactingWithId());

        return userDto;
    }

    public static UserEntity mapToEntity(UserDto userDto){
        UserEntity userEntity = new UserEntity();

        userEntity.setId(userDto.getId());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setChatId(userDto.getChatId());
        userEntity.setIsManager(userDto.getIsManager());
        userEntity.setHasWaitingRequest(userDto.getHasWaitingRequest());
        userEntity.setContactingWithId(userDto.getContactingWithId());

        return userEntity;
    }

}
