package telegramBot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegramBot.database.fetchers.UserQueries;
import java.util.Objects;


public class TelegramBot extends TelegramLongPollingBot {


    @Override
    public void onUpdateReceived(Update update) {
        BotMethods methods = new BotMethods(update);

        try {
            if(update.hasCallbackQuery()){
                CallbackQuery callbackQuery = update.getCallbackQuery();

                if(callbackQuery.getData().startsWith("/acceptrequest_")
                        && UserQueries.getByUsername(callbackQuery.getFrom().getUserName()).getContactingWithId() == -1){
                    Long clientChatId = UserQueries
                            .getAll()
                            .stream()
                            .filter(userDto -> callbackQuery
                                    .getData().substring("/acceptrequest_".length())
                                    .equals(userDto.getUsername()))
                            .findFirst()
                            .get()
                            .getChatId();

                    Long managerChatId = UserQueries
                            .getAll()
                            .stream()
                            .filter(userDto -> callbackQuery
                                    .getFrom().getUserName()
                                    .equals(userDto.getUsername()))
                            .findFirst()
                            .get()
                            .getChatId();

                    String managerUsername = callbackQuery.getFrom().getUserName();
                    String clientUsername = callbackQuery.getData().substring("/acceptrequest_".length());

                    if(UserQueries.getByUsername(clientUsername).getHasWaitingRequest()) {
                        execute(methods.sendStartMessageToAnotherSide(managerChatId, managerUsername));
                    }
                    execute(methods.acceptRequest(clientUsername, managerUsername, clientChatId, managerChatId));

                } else {
                    execute(new SendMessage(String.valueOf(UserQueries
                            .getAll()
                            .stream()
                            .filter(userDto -> callbackQuery
                                    .getFrom().getUserName()
                                    .equals(userDto.getUsername()))
                            .findFirst()
                            .get()
                            .getChatId()), DefaultMessages.YOU_ALREADY_HAVE_DIALOG_MESSAGE));
                }

            } else if (update.getMessage().hasText() && update.getMessage().getText().startsWith("/addmanager")) {
                Message message = update.getMessage();

                execute(methods.addManager(message.getChatId()
                        , message.getChat().getUserName()
                        , message.getText().substring("/addmanager".length()).trim()));

            } else if (update.getMessage().hasText()
                    && update.getMessage().getText().equals("/start")
                    || update.getMessage().getText().startsWith("/start")) {
                Message message = update.getMessage();
                execute(methods.authUser(message.getChat().getUserName(), message));

                Message message1 = update.getMessage();
                if(!UserQueries.getByUsername(message1.getChat().getUserName()).getHasWaitingRequest()
                        && !UserQueries.getByUsername(message1.getChat().getUserName()).getIsManager()) {
                    execute(methods.requestWasSent(message1.getChatId()));
                    execute(methods.sendNotificationToManager(message1.getChat().getUserName(),
                            message1.getChatId()));
                } else {
                    execute(methods.sendNotificationToManager(message1.getChat().getUserName(),
                            message1.getChatId()));
                }

            } else if (update.getMessage().hasText() && update.getMessage().getText().startsWith("/deletemanager")) {
                Message message = update.getMessage();
                execute(methods.deleteManager(message.getChatId()
                        , message.getChat().getUserName()
                        , message.getText().substring("/deletemanager".length()).trim()));

            } else if(update.hasMessage() && update.getMessage().hasText() && !update.getMessage().getText().startsWith("/")){
                Message message = update.getMessage();
                execute(methods.connectionSendText(message.getChat().getUserName()
                        ,update.getMessage().getChatId()
                        ,update.getMessage().getText()));

            } else if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/stop")) {
                Message message = update.getMessage();
                Long contactChatId = UserQueries
                        .getByUsername(message.getChat().getUserName()).getContactingWithId();
                String userContactName = UserQueries.getAll()
                        .stream()
                        .filter(userDto -> Objects.equals(userDto.getContactingWithId(), message.getChatId()))
                        .toList().stream().findFirst().get().getUsername();

                execute(methods.stopDialog(message.getChatId(), message.getChat().getUserName()));
                execute(methods.sendStopMessageToAnotherSide(contactChatId, userContactName));

            } else if(update.hasMessage() && update.getMessage().hasText() && update.getMessage()
                    .getText().startsWith("/createutm")){
                Message message = update.getMessage();
                execute(methods.createUtm(message.getChatId(), message.getText(), message.getChat().getUserName()));

            } else if (update.hasMessage() && update.getMessage().hasText() && update.getMessage()
                    .getText().startsWith("/deleteutm")){
                Message message = update.getMessage();
                execute(methods.deleteUtm(message.getChatId(),
                       message.getText().substring("/deleteutm".length()).trim(), message.getChat().getUserName()));

            } else if(update.hasMessage() && update.getMessage().hasText() && update.getMessage()
                    .getText().equals("/adminhelp")){
                Message message = update.getMessage();
                execute(methods.getAdminHelp(message.getChatId(), message.getChat().getUserName()));

            } else if(update.hasMessage() && update.getMessage().hasText() && update.getMessage()
                    .getText().equals("/allutm")){
                Message message = update.getMessage();
                execute(methods.getAllUtm(message.getChatId(), message.getChat().getUserName()));

            } else if(update.hasMessage() && update.getMessage().hasText() && update.getMessage()
                    .getText().equals("/stopsearch")){
                Message message = update.getMessage();
                execute(methods.stopSearch(message.getChatId(), message.getChat().getUserName()));

            } else if(update.hasMessage() && update.getMessage().hasText()
                    && update.getMessage().getText().startsWith("/edithello")){
                Message message = update.getMessage();
                execute(methods.editHello(message.getChatId(),
                        message.getChat().getUserName(),
                        message.getText().substring("/edithello".length()).trim()));

            } else if(update.hasMessage() && update.getMessage().hasText()
                    && update.getMessage().getText().startsWith("/getmanagers")){
                Message message = update.getMessage();
                execute(methods.getManagers(message.getChat().getUserName(), message.getChatId()));

            }

        } catch (NullPointerException | TelegramApiException ex){
            ex.getCause();
        }


    }

    @Override
    public String getBotUsername() {
        return "auto11111_bot";
    }


    @Override
    public String getBotToken() {
        return "7499802297:AAGwDzvxMe3Bo1llpGnT1PVSetlol-EJ3Ks";
    }
}
