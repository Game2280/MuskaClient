package muska.client.muskaclient;

import java.util.ArrayList;
import java.util.List;

public class Help {
    private static final List<String> helpMessages = new ArrayList<>();
    private static final int LINES_PER_PAGE = 9;

    static {
        helpMessages.add("Всевозможные команды:");
        helpMessages.add(".bind [название_чита] [кнопка] - Привязать чит к клавише");
        helpMessages.add(".bind clear [название_чита] - Удалить привязку чита");
        helpMessages.add(".bind clear all - Удалить все привязки");
        helpMessages.add(".msg disable - Отключить уведомления GUI");
        helpMessages.add(".msg disable all - Отключить все уведомления");
        helpMessages.add(".msg enable - Включить все уведомления");
        helpMessages.add(".ver - Показать версию клиента");
        helpMessages.add(".help [page] - Показать этот список команд (страница)");
        helpMessages.add(".tp [x] [y] [z] - Телепортироваться на указанные координаты");
        helpMessages.add(".clear - Очистить чат от сообщений клиента");
        helpMessages.add("Доступные читы: gui, waterwalk, fly, killaura, teleport, fastbreak, nofall, god, speed");
    }

    public static void showHelp(int page) {
        int totalPages = (int) Math.ceil((double) helpMessages.size() / LINES_PER_PAGE);

        if (page < 1 || page > totalPages) {
            Message.sendForced("Страница " + page + " не существует. Доступные страницы: 1-" + totalPages);
            return;
        }

        int startIndex = (page - 1) * LINES_PER_PAGE;
        int endIndex = Math.min(startIndex + LINES_PER_PAGE, helpMessages.size());

        for (int i = startIndex; i < endIndex; i++) {
            Message.sendForced(helpMessages.get(i));
        }

        Message.sendForced("Страница " + page + " из " + totalPages + ". Используйте .help [номер_страницы]");
    }

    public static void showHelp() {
        showHelp(1);
    }
}