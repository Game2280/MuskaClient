package muska.client.muskaclient;

public class ClientMessagesClear {
    public static void clearChat() {
        // Отправляем 100 пустых строк, чтобы "очистить" видимую часть чата
        for (int i = 0; i < 100; i++) {
            Message.sendForced("");
        }
    }
}