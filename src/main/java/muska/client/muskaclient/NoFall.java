package muska.client.muskaclient;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class NoFall {
    private static NoFall instance; // Синглтон
    private boolean isNoFallEnabled = false;

    public NoFall() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static NoFall getInstance() {
        return instance;
    }

    public boolean isNoFallEnabled() {
        return isNoFallEnabled;
    }

    public void setNoFallEnabled(boolean enabled) {
        this.isNoFallEnabled = enabled;
        Message.sendKeyMessage("NoFall " + (isNoFallEnabled ? "Enabled" : "Disabled"));
        // Обновляем состояние игрока, если нужно
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.fallDistance = 0.0F; // Сбрасываем дистанцию падения
        }
    }
}