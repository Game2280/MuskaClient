package muska.client.muskaclient;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WaterWalk {
    private boolean waterWalkEnabled = false;

    public WaterWalk() {
        // Регистрируем этот класс как обработчик событий
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isWaterWalkEnabled() {
        return waterWalkEnabled;
    }

    public void setWaterWalkEnabled(boolean enabled) {
        this.waterWalkEnabled = enabled;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        // Проверяем, что событие относится к игроку
        if (!(event.getEntity() instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) event.getEntity();
        World world = player.level;

        // Проверяем, включён ли WaterWalk
        if (!waterWalkEnabled) {
            return;
        }

        // Проверяем, находится ли игрок над водой
        BlockPos posBelow = player.blockPosition().below();
        BlockState blockBelow = world.getBlockState(posBelow);

        // Если блок под игроком — вода (или текущая вода) и игрок не в воде
        if ((blockBelow.getBlock() == Blocks.WATER || blockBelow.getBlock() == Blocks.BUBBLE_COLUMN) && !player.isInWater()) {
            // Проверяем, что игрок не сидит (не использует Shift)
            if (!player.isCrouching()) {
                // Получаем высоту поверхности воды
                double waterHeight = posBelow.getY() + blockBelow.getFluidState().getHeight(world, posBelow) + 1.0;

                // Если игрок ниже уровня воды, поднимаем его
                if (player.getY() < waterHeight) {
                    player.setPos(player.getX(), waterHeight, player.getZ());
                    player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z); // Убираем вертикальное падение
                    player.fallDistance = 0; // Сбрасываем дистанцию падения, чтобы не было урона
                    player.setOnGround(true); // Устанавливаем, что игрок "на земле", чтобы он мог прыгать
                }
            }
        }
    }
}