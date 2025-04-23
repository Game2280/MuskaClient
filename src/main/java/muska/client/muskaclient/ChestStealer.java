package muska.client.muskaclient;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChestStealer {
    private boolean isChestStealerEnabled = true; // По умолчанию чит включён
    private boolean isStealing = false; // Флаг, указывающий, идёт ли процесс кражи
    private int currentSlot = 0; // Текущий слот, из которого крадём
    private int stealDelay = 5; // Задержка между кражами (в тиках)
    private int tickCounter = 0; // Счётчик тиков для задержки

    public ChestStealer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isChestStealerEnabled() {
        return isChestStealerEnabled;
    }

    public void setChestStealerEnabled(boolean enabled) {
        this.isChestStealerEnabled = enabled;
        Message.sendKeyMessage("ChestStealer " + (isChestStealerEnabled ? "Enabled" : "Disabled"));
    }

    // Добавляем кнопку "Steal all resources" в интерфейс сундука
    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!isChestStealerEnabled) {
            return;
        }

        if (event.getGui() instanceof ChestScreen) {
            ChestScreen chestScreen = (ChestScreen) event.getGui();
            event.addWidget(new CustomButton(
                    chestScreen.getGuiLeft() + chestScreen.getXSize() - 85, // Скорректированное положение
                    chestScreen.getGuiTop() + 3, 85, 10,
                    new net.minecraft.util.text.StringTextComponent("Steal all resources"),
                    button -> startStealing(),
                    0.85F // Увеличиваем масштаб текста
            ));
        }
    }

    // Начинаем процесс кражи
    private void startStealing() {
        if (isStealing) {
            return; // Если уже крадём, не начинаем заново
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof ChestScreen) {
            isStealing = true;
            currentSlot = 0; // Начинаем с первого слота
            tickCounter = 0;
            Message.send("Started stealing resources...");
        }
    }

    // Обрабатываем кражу предметов поочерёдно
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !isStealing) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (!(mc.screen instanceof ChestScreen)) {
            stopStealing(); // Если сундук закрыт, останавливаем кражу
            return;
        }

        ChestScreen chestScreen = (ChestScreen) mc.screen;
        if (chestScreen == null || mc.player == null) {
            stopStealing();
            return;
        }

        // Проверяем задержку
        if (tickCounter < stealDelay) {
            tickCounter++;
            return;
        }
        tickCounter = 0; // Сбрасываем счётчик

        // Получаем слоты сундука (верхняя часть инвентаря — это сундук)
        int chestSlots = chestScreen.getMenu().slots.size() - 36; // 36 — это слоты инвентаря игрока
        if (currentSlot >= chestSlots) {
            stopStealing(); // Если все слоты обработаны, останавливаем
            return;
        }

        Slot slot = chestScreen.getMenu().slots.get(currentSlot);
        if (slot.hasItem()) {
            // Кликаем по слоту, чтобы переместить предмет в инвентарь игрока
            mc.gameMode.handleInventoryMouseClick(
                    chestScreen.getMenu().containerId,
                    currentSlot,
                    0, // Кнопка мыши (0 — левая)
                    ClickType.QUICK_MOVE,
                    mc.player
            );
        }

        currentSlot++; // Переходим к следующему слоту
    }

    // Останавливаем процесс кражи
    private void stopStealing() {
        isStealing = false;
        currentSlot = 0;
        Message.send("Finished stealing resources.");
    }
}