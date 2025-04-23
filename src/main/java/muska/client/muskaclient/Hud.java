package muska.client.muskaclient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Hud {
    private boolean isHudEnabled = true;
    private boolean isVersionShown = true;

    public Hud() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isHudEnabled() {
        return isHudEnabled;
    }

    public void setHudEnabled(boolean enabled) {
        this.isHudEnabled = enabled;
        Message.sendKeyMessage("HUD " + (isHudEnabled ? "Enabled" : "Disabled"));
    }

    public boolean isVersionShown() {
        return isVersionShown;
    }

    public void setVersionShown(boolean shown) {
        this.isVersionShown = shown;
        Message.sendKeyMessage("Client Version " + (isVersionShown ? "Shown" : "Hidden"));
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (!isHudEnabled) {
            return;
        }
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null || mc.font == null) {
            return;
        }
        if (isVersionShown) {
            String versionText = "Muska Client v." + Version.getVersion();
            mc.font.drawShadow(event.getMatrixStack(), versionText, 5, 5, 0x55FF55);
        }
        float yaw = MathHelper.wrapDegrees(player.yRot);
        String direction = getDirection(yaw);
        BlockPos pos = player.blockPosition();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Biome biome = player.level.getBiome(pos);
        String biomeName = biome.getRegistryName() != null ? biome.getRegistryName().getPath() : "unknown";
        int xPos = 5;
        int yPos = mc.getWindow().getGuiScaledHeight() - 40;
        mc.font.drawShadow(event.getMatrixStack(), "Direction: " + direction, xPos, yPos, 0xFF3333);
        mc.font.drawShadow(event.getMatrixStack(), "X, Y, Z: " + x + ", " + y + ", " + z, xPos, yPos + 10, 0xFFFF55);
        mc.font.drawShadow(event.getMatrixStack(), "Biome: " + biomeName, xPos, yPos + 20, 0x33CCFF);
    }

    private String getDirection(float yaw) {
        if (yaw >= -22.5 && yaw < 22.5) {
            return "South";
        } else if (yaw >= 22.5 && yaw < 67.5) {
            return "South-West";
        } else if (yaw >= 67.5 && yaw < 112.5) {
            return "West";
        } else if (yaw >= 112.5 && yaw < 157.5) {
            return "North-West";
        } else if (yaw >= 157.5 || yaw < -157.5) {
            return "North";
        } else if (yaw >= -157.5 && yaw < -112.5) {
            return "North-East";
        } else if (yaw >= -112.5 && yaw < -67.5) {
            return "East";
        } else {
            return "South-East";
        }
    }
}