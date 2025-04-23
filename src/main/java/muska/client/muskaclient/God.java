package muska.client.muskaclient;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class God {
    private static God instance; // Синглтон
    private boolean isGodEnabled = false;

    public God() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static God getInstance() {
        return instance;
    }

    public boolean isGodEnabled() {
        return isGodEnabled;
    }

    public void setGodEnabled(boolean enabled) {
        this.isGodEnabled = enabled;
        Message.sendKeyMessage("God " + (isGodEnabled ? "Enabled" : "Disabled"));
    }
}