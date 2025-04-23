package muska.client.muskaclient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class KeybindManager {
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

    private final Map<String, Integer> keybinds = new HashMap<>();
    private final Map<Integer, Boolean> keyStates = new HashMap<>();
    private final Map<String, Integer> keyNameToCode = new HashMap<>();

    public KeybindManager(WaterWalk waterWalk, Fly fly, KillAura killAura, Hud hud, ChestStealer chestStealer, Teleport teleport, FastBreak fastBreak, NoFall noFall, God god, Speed speed) {
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
        initializeKeyMap();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatEvent event) {
        String message = event.getMessage().trim();
        if (message.equalsIgnoreCase(".ver")) {
            event.setCanceled(true);
            Message.sendForced("Muska Client v" + Version.getVersion());
            return;
        }
        if (message.toLowerCase().startsWith(".help")) {
            event.setCanceled(true);
            String[] parts = message.split(" ");
            if (parts.length == 1) {
                Help.showHelp();
            } else if (parts.length == 2) {
                try {
                    int page = Integer.parseInt(parts[1]);
                    Help.showHelp(page);
                } catch (NumberFormatException e) {
                    Message.sendForced("Ошибка: Укажите номер страницы числом. Пример: .help 2");
                }
            } else {
                Message.sendForced("Использование: .help [номер_страницы]");
            }
            return;
        }
        if (message.equalsIgnoreCase(".clear")) {
            event.setCanceled(true);
            ClientMessagesClear.clearChat();
            return;
        }
        if (message.equalsIgnoreCase(".msg enable")) {
            event.setCanceled(true);
            Message.setMessagesEnabled(true, true);
            return;
        }
        if (message.equalsIgnoreCase(".msg disable")) {
            event.setCanceled(true);
            Message.setMessagesEnabled(false, false);
            return;
        }
        if (message.equalsIgnoreCase(".msg disable all")) {
            event.setCanceled(true);
            Message.setMessagesEnabled(false, true);
            return;
        }
        if (message.toLowerCase().startsWith(".bind")) {
            event.setCanceled(true);
            String[] parts = message.split(" ");
            if (parts.length < 2) {
                Message.sendForced("Использование: .bind [название_чита] [кнопка], .bind clear [название_чита] или .bind clear all");
                return;
            }
            String subCommand = parts[1].toLowerCase();
            if (subCommand.equals("clear")) {
                if (parts.length < 3) {
                    Message.sendForced("Использование: .bind clear [название_чита] или .bind clear all");
                    return;
                }
                String target = parts[2].toLowerCase();
                if (target.equals("all")) {
                    keybinds.clear();
                    keyStates.clear();
                    Message.sendForced("Все привязки клавиш удалены.");
                    return;
                }
                String cheatName = target;
                if (!keybinds.containsKey(cheatName)) {
                    Message.sendForced("Привязка для " + cheatName + " не найдена.");
                    return;
                }
                keybinds.remove(cheatName);
                Message.sendForced("Привязка для " + cheatName + " удалена.");
                return;
            }
            if (parts.length != 3) {
                Message.sendForced("Использование: .bind [название_чита] [кнопка]");
                return;
            }
            String cheatName = parts[1].toLowerCase();
            String keyName = parts[2].toLowerCase();
            if (!cheatName.equals("gui") && !cheatName.equals("waterwalk") && !cheatName.equals("fly") && !cheatName.equals("killaura") && !cheatName.equals("teleport") && !cheatName.equals("fastbreak") && !cheatName.equals("nofall") && !cheatName.equals("god") && !cheatName.equals("speed")) {
                Message.sendForced("Неизвестный чит: " + cheatName + ". Доступные читы: gui, waterwalk, fly, killaura, teleport, fastbreak, nofall, god, speed");
                return;
            }
            Integer keyCode = keyNameToCode.get(keyName);
            if (keyCode == null) {
                Message.sendForced("Неизвестная клавиша: " + keyName + ". Примеры: lshift, j, space, a, 1");
                return;
            }
            keybinds.put(cheatName, keyCode);
            Message.sendForced("Привязка установлена: " + cheatName + " -> " + keyName);
            return;
        }

        if (message.startsWith(".tp")) {
            event.setCanceled(true);
            String[] parts = message.split(" ");
            if (parts.length != 4) {
                Message.sendForced("Использование: .tp [x] [y] [z]");
                return;
            }

            double x, y, z;
            try {
                x = Double.parseDouble(parts[1]);
                y = Double.parseDouble(parts[2]);
                z = Double.parseDouble(parts[3]);
            } catch (NumberFormatException e) {
                Message.sendForced("Координаты должны быть числами! Пример: .tp 100 64 200");
                return;
            }

            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            if (player == null || mc.level == null) {
                Message.sendForced("Не удалось выполнить телепортацию: игрок или мир не доступны.");
                return;
            }

            BlockPos targetPos = new BlockPos(x, y, z);
            BlockPos safePos = findSafePosition(mc, targetPos);
            if (safePos == null) {
                Message.sendForced("Не удалось найти безопасную позицию для телепортации.");
                return;
            }

            player.setPos(safePos.getX() + 0.5, safePos.getY(), safePos.getZ() + 0.5);
            Message.sendForced("Телепортирован на " + safePos.getX() + ", " + safePos.getY() + ", " + safePos.getZ());
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        for (Map.Entry<String, Integer> entry : keybinds.entrySet()) {
            String cheatName = entry.getKey();
            int keyCode = entry.getValue();
            boolean isKeyDown = event.getKey() == keyCode && event.getAction() == GLFW.GLFW_PRESS;
            boolean wasKeyDown = keyStates.getOrDefault(keyCode, false);
            if (isKeyDown && !wasKeyDown) {
                toggleCheat(cheatName);
            }
            keyStates.put(keyCode, isKeyDown);
        }
    }

    private void toggleCheat(String cheatName) {
        switch (cheatName) {
            case "gui":
                Minecraft.getInstance().setScreen(new MuskaGui(waterWalk, fly, killAura, hud, chestStealer, teleport, fastBreak, noFall, god, speed));
                break;
            case "waterwalk":
                waterWalk.setWaterWalkEnabled(!waterWalk.isWaterWalkEnabled());
                break;
            case "fly":
                fly.setFlyEnabled(!fly.isFlyEnabled());
                break;
            case "killaura":
                killAura.setKillAuraEnabled(!killAura.isKillAuraEnabled());
                break;
            case "teleport":
                teleport.setTeleportEnabled(!teleport.isTeleportEnabled());
                break;
            case "fastbreak":
                fastBreak.setFastBreakEnabled(!fastBreak.isFastBreakEnabled());
                break;
            case "nofall":
                noFall.setNoFallEnabled(!noFall.isNoFallEnabled());
                break;
            case "god":
                god.setGodEnabled(!god.isGodEnabled());
                break;
            case "speed":
                speed.setSpeedEnabled(!speed.isSpeedEnabled());
                break;
        }
    }

    private void initializeKeyMap() {
        for (Field field : GLFW.class.getDeclaredFields()) {
            if (field.getName().startsWith("GLFW_KEY_")) {
                try {
                    int keyCode = field.getInt(null);
                    String keyName = field.getName().replace("GLFW_KEY_", "").toLowerCase();
                    keyNameToCode.put(keyName, keyCode);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        keyNameToCode.put("lshift", GLFW.GLFW_KEY_LEFT_SHIFT);
        keyNameToCode.put("rshift", GLFW.GLFW_KEY_RIGHT_SHIFT);
        keyNameToCode.put("lctrl", GLFW.GLFW_KEY_LEFT_CONTROL);
        keyNameToCode.put("rctrl", GLFW.GLFW_KEY_RIGHT_CONTROL);
        keyNameToCode.put("lalt", GLFW.GLFW_KEY_LEFT_ALT);
        keyNameToCode.put("ralt", GLFW.GLFW_KEY_RIGHT_ALT);
    }

    private BlockPos findSafePosition(Minecraft mc, BlockPos targetPos) {
        BlockPos pos = targetPos;
        while (!mc.level.isEmptyBlock(pos) && pos.getY() < 256) {
            pos = pos.above();
        }
        if (pos.getY() >= 256) {
            return null;
        }
        BlockPos below = pos.below();
        if (mc.level.isEmptyBlock(below)) {
            while (mc.level.isEmptyBlock(below) && below.getY() > 0) {
                below = below.below();
                pos = pos.below();
            }
            if (below.getY() <= 0) {
                return null;
            }
        }
        return pos;
    }
}