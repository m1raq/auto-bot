package telegramBot.bot;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import telegramBot.database.dto.HelloDto;
import telegramBot.database.dto.UserDto;
import telegramBot.database.entity.UserEntity;
import telegramBot.database.entity.UtmEntity;
import telegramBot.database.fetchers.HelloQueries;
import telegramBot.database.fetchers.UserQueries;
import telegramBot.database.fetchers.UtmQueries;


import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@AllArgsConstructor
public class BotMethods {

    private Update update;

    public static final Set<String> OWNER_USERNAMES = Set.of(
            "cfogoogle"  // Daniil - разработчик
    );

    protected SendMessage authUser(String username, Message message) {
        Long chatId = message.getChatId();

        if(message.getText().contains("utm_source")){
            String utm = message.getText().substring("/start utm_source-".length());
            UtmEntity utmEntity = new UtmEntity();
            utmEntity.setUtmValue(utm);
            utmEntity.setClientUsername(message.getChat().getUserName());
            UtmQueries.save(utmEntity);
        }

        try {
            UserDto user = UserQueries.getByUsername(username);

            if (OWNER_USERNAMES.contains(user.getUsername())) {
                return sendMessage(chatId, DefaultMessages.WELCOME_ADMIN);
            } else if (user.getId() > 0 && user.getIsManager()) {
                return sendMessage(chatId, DefaultMessages.WELCOME_MANAGER_MESSAGE);
            } else {
                return sendMessage(chatId, DefaultMessages.RETURN_MESSAGE);
            }
        } catch (Exception exception) {
            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setChatId(message.getChatId());
            user.setIsManager(false);
            user.setHasWaitingRequest(false);
            user.setContactingWithId(-1L);
            UserQueries.save(user);
            return sendMessage(chatId, HelloQueries.getHello().getValue());
        }
    }

    protected SendMessage addManager(Long chatId, String username, String managerUsername) {
        if (isAdmin(username)) {
            try {
                UserDto user = UserQueries.getByUsername(managerUsername);
                if (!user.getIsManager()) {
                    user.setIsManager(true);
                    UserQueries.update(user);
                    return sendMessage(chatId, DefaultMessages.USER_NOW_MANAGER_MESSAGE);
                } else {
                    return sendMessage(chatId, DefaultMessages.USER_ALREADY_IS_MANAGER);
                }
            } catch (NullPointerException ex) {
                return sendMessage(chatId, DefaultMessages.USER_NOT_FOUND_MESSAGE);
            }
        } else {
            return sendMessage(chatId, DefaultMessages.COMMAND_NOT_FOUND);
        }
    }

    protected SendMessage deleteManager(Long chatId, String username, String managerUsername) {
        if (isAdmin(username)) {
            try {
                UserDto user = UserQueries.getByUsername(managerUsername);
                if (user.getIsManager()) {
                    user.setIsManager(false);
                    UserQueries.update(user);
                    return sendMessage(chatId, DefaultMessages.USER_NOW_NOT_MANAGER_MESSAGE);
                } else {
                    return sendMessage(chatId, DefaultMessages.USER_ALREADY_IS_NOT_MANAGER);
                }
            } catch (NullPointerException ex) {
                return sendMessage(chatId, DefaultMessages.USER_NOT_FOUND_MESSAGE);
            }
        } else {
            return sendMessage(chatId, DefaultMessages.COMMAND_NOT_FOUND);
        }
    }

    protected SendMessage getManagers(String username, Long chatId){
        if(OWNER_USERNAMES.contains(username)){
            StringBuilder stringBuilder = new StringBuilder();
            UserQueries
                    .getAllManager()
                    .forEach(userDto -> stringBuilder
                            .append("●  ")
                            .append(userDto.getUsername())
                            .append("\n"));
            if(stringBuilder.isEmpty()){
                return sendMessage(chatId, DefaultMessages.MANAGER_LIST_IS_EMPTY);
            } else {
                return sendMessage(chatId, stringBuilder.toString());
            }
        } else {
            return sendMessage(chatId, DefaultMessages.COMMAND_NOT_FOUND);
        }
    }

    protected SendMessage sendNotificationToManager(String username, Long chatId) {
        AtomicReference<SendMessage> sendMessage = new AtomicReference<>(new SendMessage());

        try {

            if(UserQueries.getByUsername(username).getIsManager()){
                return sendMessage(chatId, DefaultMessages.ERROR_ASK_FOR_MANAGER);
            }

            if (UserQueries.getByUsername(username).getContactingWithId() == -1
                    && !UserQueries.getByUsername(username).getHasWaitingRequest()) {
                List<UserDto> userList = UserQueries.getAll();
                UserDto client = UserQueries.getByUsername(username);
                client.setHasWaitingRequest(true);
                UserQueries.update(client);

                userList.forEach(userDto -> {
                    if (userDto.getIsManager() && userDto.getContactingWithId() == -1) {
                        List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();
                        List<InlineKeyboardButton> row = new ArrayList<>();

                        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

                        InlineKeyboardButton accept = new InlineKeyboardButton();
                        accept.setText("//Принять//");
                        accept.setCallbackData("/acceptrequest_" + username);


                        row.add(accept);
                        buttonList.add(row);
                        keyboardMarkup.setKeyboard(buttonList);

                        SendMessage objectSM = new SendMessage(String.valueOf(userDto.getChatId())
                                , DefaultMessages.NEW_REQUEST_MESSAGE);

                        objectSM.setReplyMarkup(keyboardMarkup);

                        sendMessage.set(objectSM);
                    }
                });

                return sendMessage.get();
            } else {
                return new SendMessage(String.valueOf(chatId), DefaultMessages.REQUEST_ALREADY_WAS_SENT);
            }
        } catch (NullPointerException ex){
            return new SendMessage(String.valueOf(chatId), DefaultMessages.EXPECT_REGISTRATION);
        }

    }



    protected SendMessage acceptRequest(String clientUsername, String managerUsername
            , Long clientChatId, Long managerChatId){

        if(UserQueries.getByUsername(managerUsername).getContactingWithId() != -1){
            return sendMessage(managerChatId, DefaultMessages.YOU_ALREADY_HAVE_DIALOG_MESSAGE);
        }

        if(UserQueries.getByUsername(clientUsername).getHasWaitingRequest()){
            if(UserQueries.getByUsername(managerUsername).getContactingWithId() == -1){
                UserDto manager = UserQueries.getByUsername(managerUsername);
                UserDto client = UserQueries.getByUsername(clientUsername);

                client.setHasWaitingRequest(false);
                manager.setHasWaitingRequest(false);
                manager.setContactingWithId(clientChatId);
                client.setContactingWithId(managerChatId);

                UserQueries.update(manager);
                UserQueries.update(client);

                return sendMessage(clientChatId, DefaultMessages.REQUEST_TAKEN_MANAGER_MESSAGE);
            } else {
                return sendMessage(clientChatId, DefaultMessages.REQUEST_ALREADY_WAS_SENT);
            }
        } else {
            return sendMessage(managerChatId, DefaultMessages.USER_NOT_WAITING_MESSAGE);
        }


    }

    protected SendMessage stopDialog(Long chatId, String username){
        try {
            UserDto user = UserQueries.getByUsername(username);
            if(user.getContactingWithId() != -1 && !user.getIsManager()){
                UserDto user2 = UserQueries
                        .getAll()
                        .stream()
                        .filter(userDto -> Objects.equals(userDto.getChatId(), user.getContactingWithId()))
                        .findFirst()
                        .get();

                user.setContactingWithId(-1L);
                user2.setContactingWithId(-1L);
                UserQueries.update(user);
                UserQueries.update(user2);
                return sendMessage(chatId, DefaultMessages.DIALOG_WAS_STOPPED_FOR_CLIENT);
            } else if(user.getContactingWithId() != -1 && user.getIsManager()) {
                UserDto user2 = UserQueries
                        .getAll()
                        .stream()
                        .filter(userDto -> Objects.equals(userDto.getChatId(), user.getContactingWithId()))
                        .findFirst()
                        .get();

                user.setContactingWithId(-1L);
                user2.setContactingWithId(-1L);
                UserQueries.update(user);
                UserQueries.update(user2);
                return sendMessage(chatId, DefaultMessages.DIALOG_WAS_STOPPED_FOR_MANAGER);
            }
        } catch (NullPointerException ex){
            return sendMessage(chatId, DefaultMessages.EXPECT_REGISTRATION);
        }
        return sendMessage(chatId, DefaultMessages.COMMAND_NOT_FOUND);
    }

    protected SendMessage stopSearch(Long chatId, String username){
        try {
            UserDto userDto = UserQueries.getByUsername(username);
            userDto.setHasWaitingRequest(false);
            UserQueries.update(userDto);
            return sendMessage(chatId, DefaultMessages.SEARCH_WAS_STOPPED);
        } catch (Exception ex){
            return sendMessage(chatId, DefaultMessages.EXPECT_REGISTRATION);
        }
    }

    protected SendMessage sendStopMessageToAnotherSide(Long chatId, String username){
        return UserQueries.getByUsername(username).getIsManager()
                ? sendMessage(chatId, DefaultMessages.DIALOG_WAS_STOPPED_FOR_MANAGER)
                : sendMessage(chatId, DefaultMessages.DIALOG_WAS_STOPPED_FOR_CLIENT);
    }

    protected SendMessage sendStartMessageToAnotherSide(Long chatId, String username){
        return UserQueries.getByUsername(username).getIsManager()
                ? sendMessage(chatId, DefaultMessages.DIALOG_WAS_STARTED_FOR_MANAGER)
                : sendMessage(chatId, DefaultMessages.REQUEST_TAKEN_MANAGER_MESSAGE);
    }

    protected SendMessage connectionSendText(String username, Long chatId, String text){

        try {
            UserDto user1 = UserQueries.getByUsername(username);
            if(user1.getContactingWithId() != -1){
                return sendMessage(user1.getContactingWithId(), text);
            }

            return sendMessage(chatId, DefaultMessages.COMMAND_NOT_FOUND);
        } catch (NullPointerException ex){
            return sendMessage(chatId, DefaultMessages.EXPECT_REGISTRATION);
        }


    }

    protected SendMessage requestWasSent(Long chatId){
        return sendMessage(chatId, DefaultMessages.REQUEST_WAS_SENT);
    }

    protected SendMessage createUtm(Long chatId, String urlWithUtm, String username){
        if (OWNER_USERNAMES.contains(username)) {
            try {
                UtmEntity utmEntity = new UtmEntity();
                Pattern pattern = Pattern.compile("<utm-mark>(.+?)</utm-mark>");
                Matcher matcher = pattern.matcher(urlWithUtm);
                if (matcher.find()) {
                    String utm = matcher.group(1);

                    utmEntity.setUtmValue(utm);
                    UtmQueries.save(utmEntity);
                    return sendMessage(chatId, DefaultMessages.UTM_WAS_ADDED);
                } else {
                    return sendMessage(chatId, DefaultMessages.ERROR);
                }
            } catch (Exception ex) {
                return sendMessage(chatId, DefaultMessages.ERROR);
            }
        }  else {
            return sendMessage(chatId, DefaultMessages.COMMAND_NOT_FOUND);
        }

    }

    protected SendMessage getAdminHelp(Long chatId, String username){
        return OWNER_USERNAMES.contains(username) ? sendMessage(chatId, DefaultMessages.ADMIN_HELP)
                : sendMessage(chatId, DefaultMessages.COMMAND_NOT_FOUND);
    }

    protected SendMessage deleteUtm(Long chatId, String value, String username){
        if (OWNER_USERNAMES.contains(username)) {
            try {
                UtmQueries.deleteByValue(value);
                return sendMessage(chatId, DefaultMessages.DELETE_UTM);
            } catch (Exception ex){
                return sendMessage(chatId, DefaultMessages.ERROR);
            }
        } else {
            return sendMessage(chatId, DefaultMessages.COMMAND_NOT_FOUND);
        }
    }

    protected SendMessage getAllUtm(Long chatId, String username){
        if (OWNER_USERNAMES.contains(username)) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                UtmQueries.getAll()
                        .forEach(utmDto -> stringBuilder.append("•  ")
                                .append(utmDto.getUtmValue())
                                .append("   -   ")
                                .append(utmDto.getClientUsername())
                                .append("\n"));

                return sendMessage(chatId, stringBuilder.toString());
            } catch (Exception ex){
                return sendMessage(chatId, DefaultMessages.ERROR);
            }
        } else {
            return sendMessage(chatId, DefaultMessages.COMMAND_NOT_FOUND);
        }
    }

    protected SendMessage editHello(Long chatId, String username, String text){

        if(OWNER_USERNAMES.contains(username)) {
            try {
                HelloDto helloDto = HelloQueries.getHello();
                helloDto.setValue(text);
                HelloQueries.edit(helloDto);
            } catch (Exception exception) {
                HelloDto helloDto = new HelloDto();
                helloDto.setValue(text);
                HelloQueries.edit(helloDto);
            }
            return sendMessage(chatId, DefaultMessages.HELLO_WAS_EDITED);
        } else {
            return sendMessage(chatId, DefaultMessages.COMMAND_NOT_FOUND);
        }
    }

    private String getUtmMark(){
        return update.getMessage().getText().substring("/start".length()).trim();
    }

    private SendMessage sendMessage(Long chatId, String text){
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }



    private Boolean isAdmin(String username){
        return OWNER_USERNAMES.contains(username);
    }



}
