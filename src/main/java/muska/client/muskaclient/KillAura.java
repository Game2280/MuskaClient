package muska.client.muskaclient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class KillAura {
    private boolean killAuraEnabled = false;
    private static final double RANGE = 4.0; // Радиус действия KillAura в блоках
    private long lastAttackTime = 0; // Время последней атаки в тиках
    private static final int ATTACK_COOLDOWN = 12; // Кулдаун в тиках (0.6 секунды при 20 тиках/сек)

    public KillAura() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isKillAuraEnabled() {
        return killAuraEnabled;
    }

    public void setKillAuraEnabled(boolean enabled) {
        this.killAuraEnabled = enabled;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !killAuraEnabled) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }

        long currentTime = mc.level.getGameTime(); // Текущее время в тиках
        if (currentTime - lastAttackTime < ATTACK_COOLDOWN) {
            return; // Не атакуем, если кулдаун ещё не прошёл
        }

        // Ищем ближайшую живую сущность в радиусе
        for (Entity entity : mc.level.getEntities(mc.player, mc.player.getBoundingBox().inflate(RANGE))) {
            if (entity instanceof LivingEntity && entity != mc.player && entity.isAlive()) {
                double distance = mc.player.distanceTo(entity);
                if (distance <= RANGE) {
                    // Поворачиваем игрока к цели
                    lookAtEntity(mc.player, entity);

                    // Атакуем
                    mc.player.swing(Hand.MAIN_HAND);
                    mc.gameMode.attack(mc.player, entity);
                    lastAttackTime = currentTime; // Обновляем время последней атаки
                    break; // Атакуем только одну цель за тик
                }
            }
        }
    }

    private void lookAtEntity(Entity player, Entity target) {
        Vector3d playerPos = player.position().add(0, player.getEyeHeight(), 0); // Позиция глаз игрока
        Vector3d targetPos = target.position().add(0, target.getEyeHeight() / 2, 0); // Центр цели
        Vector3d diff = targetPos.subtract(playerPos);

        // Вычисляем углы поворота
        double yaw = Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90.0;
        double pitch = Math.toDegrees(-Math.atan2(diff.y, Math.sqrt(diff.x * diff.x + diff.z * diff.z)));

        // Применяем углы
        player.yRot = (float) yaw;
        player.xRot = (float) pitch;
    }
}