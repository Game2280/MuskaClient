package muska.client.muskaclient;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class MuskaGui extends Screen {
    private final WaterWalk waterWalk;
    private final Fly fly;
    private final KillAura killAura;
    private final Hud hud;
    private final ChestStealer chestStealer;
    private final Teleport teleport;
    private final FastBreak fastBreak;
    private final NoFall noFall;
    private final God god;
    private final Speed speed;

    private Button waterWalkButton;
    private Button flyButton;
    private Button killAuraButton;
    private Button hudButton;
    private Button showVersionButton; // Новая кнопка для Show version
    private Button chestStealerButton;
    private Button teleportButton;
    private Button fastBreakButton;
    private Button noFallButton;
    private Button godButton;
    private Button speedButton;
    private Button speedIncreaseButton;
    private Button speedDecreaseButton;
    private Button speedValueButton; // Кнопка для отображения текущего значения множителя скорости

    public MuskaGui(WaterWalk waterWalk, Fly fly, KillAura killAura, Hud hud, ChestStealer chestStealer, Teleport teleport, FastBreak fastBreak, NoFall noFall, God god, Speed speed) {
        super(new StringTextComponent("Muska Client GUI"));
        this.waterWalk = waterWalk;
        this.fly = fly;
        this.killAura = killAura;
        this.hud = hud;
        this.chestStealer = chestStealer;
        this.teleport = teleport;
        this.fastBreak = fastBreak;
        this.noFall = noFall;
        this.god = god;
        this.speed = speed;
    }

    @Override
    protected void init() {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int buttonSpacing = 5;
        int startY = this.height / 2 - 100;

        // HUD (размер и положение из декомпилированного кода: x=5, y=5, ширина=60, высота=20)
        this.hudButton = new Button(
                5, 5, 60, 20,
                new StringTextComponent("Hud: " + (hud.isHudEnabled() ? "ON" : "OFF")),
                button -> {
                    hud.setHudEnabled(!hud.isHudEnabled());
                    button.setMessage(new StringTextComponent("Hud: " + (hud.isHudEnabled() ? "ON" : "OFF")));
                }
        );
        this.addButton(hudButton);

        // Show version (размер и положение из декомпилированного кода: x=5, y=30, ширина=120, высота=20)
        this.showVersionButton = new Button(
                5, 30, 120, 20,
                new StringTextComponent("Show version: " + (hud.isVersionShown() ? "ON" : "OFF")),
                button -> {
                    hud.setVersionShown(!hud.isVersionShown());
                    button.setMessage(new StringTextComponent("Show version: " + (hud.isVersionShown() ? "ON" : "OFF")));
                }
        );
        this.addButton(showVersionButton);

        // WaterWalk
        this.waterWalkButton = new Button(
                this.width / 2 - buttonWidth / 2, startY, buttonWidth, buttonHeight,
                new StringTextComponent("WaterWalk: " + (waterWalk.isWaterWalkEnabled() ? "ON" : "OFF")),
                button -> {
                    waterWalk.setWaterWalkEnabled(!waterWalk.isWaterWalkEnabled());
                    button.setMessage(new StringTextComponent("WaterWalk: " + (waterWalk.isWaterWalkEnabled() ? "ON" : "OFF")));
                }
        );
        this.addButton(waterWalkButton);

        // Fly
        this.flyButton = new Button(
                this.width / 2 - buttonWidth / 2, startY + (buttonHeight + buttonSpacing), buttonWidth, buttonHeight,
                new StringTextComponent("Fly: " + (fly.isFlyEnabled() ? "ON" : "OFF")),
                button -> {
                    fly.setFlyEnabled(!fly.isFlyEnabled());
                    button.setMessage(new StringTextComponent("Fly: " + (fly.isFlyEnabled() ? "ON" : "OFF")));
                }
        );
        this.addButton(flyButton);

        // KillAura
        this.killAuraButton = new Button(
                this.width / 2 - buttonWidth / 2, startY + 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight,
                new StringTextComponent("KillAura: " + (killAura.isKillAuraEnabled() ? "ON" : "OFF")),
                button -> {
                    killAura.setKillAuraEnabled(!killAura.isKillAuraEnabled());
                    button.setMessage(new StringTextComponent("KillAura: " + (killAura.isKillAuraEnabled() ? "ON" : "OFF")));
                }
        );
        this.addButton(killAuraButton);

        // ChestStealer
        this.chestStealerButton = new Button(
                this.width / 2 - buttonWidth / 2, startY + 3 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight,
                new StringTextComponent("ChestStealer Button: " + (chestStealer.isChestStealerEnabled() ? "ON" : "OFF")),
                button -> {
                    chestStealer.setChestStealerEnabled(!chestStealer.isChestStealerEnabled());
                    button.setMessage(new StringTextComponent("ChestStealer Button: " + (chestStealer.isChestStealerEnabled() ? "ON" : "OFF")));
                }
        );
        this.addButton(chestStealerButton);

        // Teleport
        this.teleportButton = new Button(
                this.width / 2 - buttonWidth / 2, startY + 4 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight,
                new StringTextComponent("Teleport: " + (teleport.isTeleportEnabled() ? "ON" : "OFF")),
                button -> {
                    teleport.setTeleportEnabled(!teleport.isTeleportEnabled());
                    button.setMessage(new StringTextComponent("Teleport: " + (teleport.isTeleportEnabled() ? "ON" : "OFF")));
                }
        );
        this.addButton(teleportButton);

        // FastBreak
        this.fastBreakButton = new Button(
                this.width / 2 - buttonWidth / 2, startY + 5 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight,
                new StringTextComponent("FastBreak: " + (fastBreak.isFastBreakEnabled() ? "ON" : "OFF")),
                button -> {
                    fastBreak.setFastBreakEnabled(!fastBreak.isFastBreakEnabled());
                    button.setMessage(new StringTextComponent("FastBreak: " + (fastBreak.isFastBreakEnabled() ? "ON" : "OFF")));
                }
        );
        this.addButton(fastBreakButton);

        // NoFall
        this.noFallButton = new Button(
                this.width / 2 - buttonWidth / 2, startY + 6 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight,
                new StringTextComponent("NoFall: " + (noFall.isNoFallEnabled() ? "ON" : "OFF")),
                button -> {
                    noFall.setNoFallEnabled(!noFall.isNoFallEnabled());
                    button.setMessage(new StringTextComponent("NoFall: " + (noFall.isNoFallEnabled() ? "ON" : "OFF")));
                }
        );
        this.addButton(noFallButton);

        // God
        this.godButton = new Button(
                this.width / 2 - buttonWidth / 2, startY + 7 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight,
                new StringTextComponent("God: " + (god.isGodEnabled() ? "ON" : "OFF")),
                button -> {
                    god.setGodEnabled(!god.isGodEnabled());
                    button.setMessage(new StringTextComponent("God: " + (god.isGodEnabled() ? "ON" : "OFF")));
                }
        );
        this.addButton(godButton);

        // Speed
        this.speedButton = new Button(
                this.width / 2 - buttonWidth / 2, startY + 8 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight,
                new StringTextComponent("Speed: " + (speed.isSpeedEnabled() ? "ON" : "OFF")),
                button -> {
                    speed.setSpeedEnabled(!speed.isSpeedEnabled());
                    button.setMessage(new StringTextComponent("Speed: " + (speed.isSpeedEnabled() ? "ON" : "OFF")));
                }
        );
        this.addButton(speedButton);

        // Speed Value Display
        this.speedValueButton = new Button(
                this.width / 2 - buttonWidth / 2, startY + 9 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight,
                new StringTextComponent("Speed Multiplier: " + speed.getSpeedMultiplier()),
                button -> {} // Кнопка только для отображения, действия не требуется
        );
        this.addButton(speedValueButton);

        // Speed Increase (+)
        this.speedIncreaseButton = new Button(
                this.width / 2 - buttonWidth / 2 + buttonWidth + buttonSpacing, startY + 9 * (buttonHeight + buttonSpacing), 20, buttonHeight,
                new StringTextComponent("+"),
                button -> {
                    speed.increaseSpeedMultiplier();
                    speedValueButton.setMessage(new StringTextComponent("Speed Multiplier: " + speed.getSpeedMultiplier()));
                }
        );
        this.addButton(speedIncreaseButton);

        // Speed Decrease (-)
        this.speedDecreaseButton = new Button(
                this.width / 2 - buttonWidth / 2 - 20 - buttonSpacing, startY + 9 * (buttonHeight + buttonSpacing), 20, buttonHeight,
                new StringTextComponent("-"),
                button -> {
                    speed.decreaseSpeedMultiplier();
                    speedValueButton.setMessage(new StringTextComponent("Speed Multiplier: " + speed.getSpeedMultiplier()));
                }
        );
        this.addButton(speedDecreaseButton);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, "Muska Client GUI", this.width / 2, 20, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}