package muska.client.muskaclient;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

public class Version {
    private static final String MOD_ID = "muskaclient"; // ID мода, должен совпадать с mods.toml
    private static String version = null;

    public static String getVersion() {
        if (version == null) {
            // Получаем информацию о моде из ModList
            ModList modList = ModList.get();
            modList.getMods().stream()
                    .filter(modInfo -> modInfo.getModId().equals(MOD_ID))
                    .findFirst()
                    .ifPresent(modInfo -> version = modInfo.getVersion().toString());

            // Если версия не найдена, возвращаем значение по умолчанию
            if (version == null) {
                version = "unknown";
            }
        }
        return version;
    }
}