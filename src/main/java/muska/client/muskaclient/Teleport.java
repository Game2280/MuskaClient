package muska.client.muskaclient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Teleport {
    private boolean isTeleportEnabled = false;
    private static final double MAX_TELEPORT_DISTANCE = 50.0; // Максимальная дистанция телепортации

    public Teleport() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isTeleportEnabled() {
        return isTeleportEnabled;
    }

    public void setTeleportEnabled(boolean enabled) {
        this.isTeleportEnabled = enabled;
        Message.sendKeyMessage("Teleport " + (isTeleportEnabled ? "Enabled" : "Disabled"));
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (!isTeleportEnabled) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null || mc.level == null) {
            return;
        }

        // Проверяем, нажата ли правая кнопка мыши (код 1 для правой кнопки в GLFW)
        if (event.getButton() == 1 && event.getAction() == 1) { // 1 = нажатие
            // Получаем результат трассировки луча (на что смотрит игрок)
            RayTraceResult rayTraceResult = mc.hitResult;
            if (rayTraceResult == null || rayTraceResult.getType() != RayTraceResult.Type.BLOCK) {
                Message.send("No block targeted for teleportation.");
                return;
            }

            BlockRayTraceResult blockRayTrace = (BlockRayTraceResult) rayTraceResult;
            BlockPos targetPos = blockRayTrace.getBlockPos();

            // Проверяем дистанцию до цели
            double distance = player.distanceToSqr(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
            if (distance > MAX_TELEPORT_DISTANCE * MAX_TELEPORT_DISTANCE) {
                Message.send("Target is too far! Max distance: " + MAX_TELEPORT_DISTANCE + " blocks.");
                return;
            }

            // Находим безопасную позицию для телепортации (на один блок выше)
            BlockPos teleportPos = targetPos.above();
            while (!mc.level.isEmptyBlock(teleportPos) && teleportPos.getY() < 256) {
                teleportPos = teleportPos.above();
            }
            if (teleportPos.getY() >= 256) {
                Message.send("No safe position to teleport to.");
                return;
            }

            // Телепортируем игрока
            player.setPos(teleportPos.getX() + 0.5, teleportPos.getY(), teleportPos.getZ() + 0.5);
            Message.send("Teleported to " + teleportPos.getX() + ", " + teleportPos.getY() + ", " + teleportPos.getZ());
        }
    }
}