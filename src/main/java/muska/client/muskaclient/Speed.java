package muska.client.muskaclient;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Speed {
    private static Speed instance;
    private boolean isSpeedEnabled = false;
    private float speedMultiplier = 50.0f; // Начальное значение 50

    public Speed() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static Speed getInstance() {
        return instance;
    }

    public boolean isSpeedEnabled() {
        return isSpeedEnabled;
    }

    public void setSpeedEnabled(boolean enabled) {
        this.isSpeedEnabled = enabled;
        Message.sendKeyMessage("Speed " + (isSpeedEnabled ? "Enabled" : "Disabled"));
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float multiplier) {
        this.speedMultiplier = multiplier;
    }

    // Метод для увеличения множителя скорости
    public void increaseSpeedMultiplier() {
        this.speedMultiplier += 1.0f;
        Message.sendKeyMessage("Speed multiplier increased to " + this.speedMultiplier);
    }

    // Метод для уменьшения множителя скорости (с проверкой на минимальное значение)
    public void decreaseSpeedMultiplier() {
        if (this.speedMultiplier > 1.0f) {
            this.speedMultiplier -= 1.0f;
            Message.sendKeyMessage("Speed multiplier decreased to " + this.speedMultiplier);
        } else {
            Message.sendKeyMessage("Speed multiplier cannot be less than 1.0");
        }
    }
}