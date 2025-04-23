package muska.client.muskaclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod("muskaclient")
public class Muskaclient {
    private static final KeyBinding openGuiKey = new KeyBinding(
            "key.muskaclient.open_gui",
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "key.categories.muskaclient"
    );

    private WaterWalk waterWalk;
    private Fly fly;
    private KillAura killAura;
    private KeybindManager keybindManager;
    private Hud hud;
    private ChestStealer chestStealer;
    private Teleport teleport;
    private FastBreak fastBreak;
    private NoFall noFall;
    private God god;
    private Speed speed;

    public Muskaclient() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        System.out.println("Muska Client initialized!");
        this.waterWalk = new WaterWalk();
        this.fly = new Fly();
        this.killAura = new KillAura();
        this.hud = new Hud();
        this.chestStealer = new ChestStealer();
        this.teleport = new Teleport();
        this.fastBreak = new FastBreak();
        this.noFall = new NoFall();
        this.god = new God();
        this.speed = new Speed();
        this.keybindManager = new KeybindManager(waterWalk, fly, killAura, hud, chestStealer, teleport, fastBreak, noFall, god, speed);
        ClientRegistry.registerKeyBinding(openGuiKey);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @net.minecraftforge.eventbus.api.SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (openGuiKey.isDown() && Minecraft.getInstance().player != null) {
            Minecraft.getInstance().setScreen(new MuskaGui(waterWalk, fly, killAura, hud, chestStealer, teleport, fastBreak, noFall, god, speed));
        }
    }
}