package muska.client.muskaclient;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FastBreak {
    private boolean isFastBreakEnabled = false;
    private static final float BASE_SPEED_MULTIPLIER = 6.0F; // Ускорение для большинства блоков
    private static final float OBSIDIAN_SPEED_MULTIPLIER = 25.0F; // Ускорение для обсидиана

    public FastBreak() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isFastBreakEnabled() {
        return isFastBreakEnabled;
    }

    public void setFastBreakEnabled(boolean enabled) {
        this.isFastBreakEnabled = enabled;
        Message.sendKeyMessage("FastBreak " + (isFastBreakEnabled ? "Enabled" : "Disabled"));
    }

    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!isFastBreakEnabled) return;

        PlayerEntity player = event.getPlayer();
        if (player == null) return;

        float multiplier = BASE_SPEED_MULTIPLIER;
        if (event.getState().getBlock() == Blocks.OBSIDIAN) {
            multiplier = OBSIDIAN_SPEED_MULTIPLIER; // Ускорение для обсидиана
        }

        event.setNewSpeed(event.getNewSpeed() * multiplier);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!isFastBreakEnabled) return;

        PlayerEntity player = event.getPlayer();
        World world = (World) event.getWorld();
        Block block = event.getState().getBlock();

        if (block == Blocks.OBSIDIAN) {
            // Принудительно дропаем обсидиан
            Block.dropResources(event.getState(), world, event.getPos(), null, player, player.getMainHandItem());
        }
    }
}