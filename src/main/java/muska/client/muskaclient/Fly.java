package muska.client.muskaclient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Fly {
    private boolean flyEnabled = false;

    public Fly() {
        // Регистрируем этот класс как обработчик событий
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isFlyEnabled() {
        return flyEnabled;
    }

    public void setFlyEnabled(boolean enabled) {
        this.flyEnabled = enabled;
        // Обновляем состояние полёта у игрока
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            player.abilities.mayfly = enabled;
            if (!enabled) {
                // Отключаем полёт, если функция выключена
                player.abilities.flying = false;
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null || !flyEnabled) {
            return;
        }

        // Проверяем, нажата ли клавиша прыжка (по умолчанию пробел)
        if (Minecraft.getInstance().options.keyJump.isDown()) {
            player.abilities.flying = true;
        }
    }
}