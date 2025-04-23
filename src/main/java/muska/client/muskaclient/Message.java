package muska.client.muskaclient;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

public class Message {
    private static boolean messagesEnabled = true; // Уведомления от GUI
    private static boolean keyMessagesEnabled = true; // Уведомления от клавиш, .ver, .bind

    // Отправка обычных сообщений (зависит от messagesEnabled)
    public static void send(String message) {
        if (messagesEnabled) {
            sendForced(message);
        }
    }

    // Отправка сообщений от клавиш (зависит от keyMessagesEnabled)
    public static void sendKeyMessage(String message) {
        if (keyMessagesEnabled) {
            sendForced(message);
        }
    }

    // Принудительная отправка сообщений (зависит от keyMessagesEnabled)
    public static void sendForced(String message) {
        if (keyMessagesEnabled) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.sendMessage(new StringTextComponent(message), mc.player.getUUID());
            }
        }
    }

    // Отправка системных сообщений (не зависит от флагов)
    private static void sendSystem(String message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.sendMessage(new StringTextComponent(message), mc.player.getUUID());
        }
    }

    // Установка состояния уведомлений
    public static void setMessagesEnabled(boolean enabled, boolean all) {
        if (enabled != messagesEnabled || (all && enabled != keyMessagesEnabled)) {
            if (all) {
                sendSystem(enabled ? "Все оповещения включены." : "Все оповещения выключены.");
                messagesEnabled = enabled;
                keyMessagesEnabled = enabled;
            } else if (!enabled) { // Только отключение без all
                sendSystem("Оповещения выключены.");
                messagesEnabled = enabled;
            }
        }
    }

    public static boolean isMessagesEnabled() {
        return messagesEnabled;
    }

    public static boolean isKeyMessagesEnabled() {
        return keyMessagesEnabled;
    }
}