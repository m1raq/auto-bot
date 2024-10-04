package telegramBot.bot;



public class DefaultMessages {

    protected static final String WELCOME_ADMIN = """
            Это приветственное сообщение для админов бота...
            Введите /adminhelp для получения списка команд.
            """;

    protected static final String ADMIN_HELP = """
            ●   /addmanager [username] - добавить менеджера
            ●   /deletemanager [username] - удалить менеджера
            ●   /createutm [ссылка с utm меткой выделенной в <utm-mark></utm-mark>] - добавить метку
            ●   /deleteutm [utm value] - удалить метку
            ●   /allutm - все метки
            ●   /edithello [текст] - отредактировать приветственное сообщение
            ●   /getmanagers - получить список менеджеров
            """;

    protected static final String UTM_WAS_ADDED = """
            Utm-метка была добавлена.
            """;

    protected static final String MANAGER_LIST_IS_EMPTY = """
            Список менеджеров пуст
            """;

    protected static final String ERROR = """
            Что-то пошло не так..
            """;

    protected static final String ERROR_ASK_FOR_MANAGER = """
            Вы не можете отправлять заявку, т.к вы являетесь менеджером
            """;

    protected static final String DELETE_UTM = """
            Utm-метка была удалена
            """;

    protected static final String EXPECT_REGISTRATION = """
            Перед использованием бота введите /start для регистрации
            """;

    protected static final String RETURN_MESSAGE = """
            С возвращением!
            """;

    protected static final String WELCOME_MANAGER_MESSAGE = """
            Это приветственное сообщение для менеджеров. Если ты его видишь... Ну, ты, собственно, менеджер. Надеюсь.
            """;

    protected static final String NEW_REQUEST_MESSAGE = """
            Получена новая заявка!
            """;

    protected static final String REQUEST_TAKEN_MANAGER_MESSAGE = """
            Менеджер принял вашу заявку!
            ⚠️ Все сообщения из этого чата будут пересланы этому пользователю! ⚠️

            Чтобы прекратить общение, напишите 
            /stop
            """;

    protected static final String USER_ALREADY_IS_MANAGER = """
            Пользователь уже является менеджером
            """;

    protected static final String REQUEST_ALREADY_WAS_SENT = """
            Вы уже отправили заявку, дождитесь, пожалуйста, ответ менеджера.
            Чтобы отменить заявку введите 
            /stopsearch
            """;

    protected static final String USER_ALREADY_IS_NOT_MANAGER = """
            Пользователь не был менеджером
            """;

    protected static final String COMMAND_NOT_FOUND = """
            Некорректная команда
            """;


    protected static final String YOU_ALREADY_HAVE_DIALOG_MESSAGE = """
            У вас уже есть диалог с одним из пользователей! Используйте /stop, чтобы прекратить его.
            """;

    protected static final String USER_NOT_FOUND_MESSAGE = """
            ⚠️ Пользователь с таким username не найден!
            """;

    protected static final String USER_NOW_MANAGER_MESSAGE = """
            Данный пользователь теперь является менеджером!
            """;

    protected static final String HELLO_WAS_EDITED= """
            Приветственное сообщение было отредактировано.
            """;

    protected static final String USER_NOW_NOT_MANAGER_MESSAGE = """
            Данный пользователь больше не является менеджером!
            """;

    protected static final String REQUEST_WAS_SENT = """
            Мы приняли вашу заявку. Менеджер свяжется с вами в ближайшее время
            Чтобы отменить заявку введите /stopsearch
            """;


    protected static final String USER_NOT_WAITING_MESSAGE = """
            Этот пользователь больше не ждет ответа ни на какую заявку!
            """;

    protected static final String DIALOG_WAS_STOPPED_FOR_CLIENT = """
            Благодарим вас за обращение! Диалог был прекращен.
            """;

    protected static final String SEARCH_WAS_STOPPED = """
            Вы отменили заявку.
            """;

    protected static final String DIALOG_WAS_STOPPED_FOR_MANAGER = """
            Диалог с клиентом был приостановлен.
            """;

    protected static final String DIALOG_WAS_STARTED_FOR_MANAGER = """
            Вы начали диалог с клиентом. Для остановки напишите /stop
            """;

}
