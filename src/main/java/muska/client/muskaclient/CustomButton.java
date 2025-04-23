package muska.client.muskaclient;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CustomButton extends Button {
    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    private final float textScale;
    private final Minecraft minecraft;

    public CustomButton(int x, int y, int width, int height, ITextComponent title, IPressable onPress, float textScale) {
        super(x, y, width, height, title, onPress);
        this.textScale = textScale;
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        minecraft.getTextureManager().bind(WIDGETS_LOCATION);
        int textureOffset = this.isHovered ? 2 : 1;

        blit(matrixStack, this.x, this.y, 0, 46 + textureOffset * 20, this.width / 2, this.height);
        blit(matrixStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + textureOffset * 20, this.width / 2, this.height);

        matrixStack.pushPose();
        matrixStack.translate(this.x + this.width / 2.0F, this.y + (this.height - 8 * textScale) / 2.0F, 0);
        matrixStack.scale(textScale * 0.9F, textScale * 0.9F, 1.0F);

        float textX = -minecraft.font.width(this.getMessage()) / 2.0F;
        minecraft.font.drawShadow(matrixStack, this.getMessage(), textX, 0, 0xFFFFFF);

        matrixStack.popPose();
    }
}
