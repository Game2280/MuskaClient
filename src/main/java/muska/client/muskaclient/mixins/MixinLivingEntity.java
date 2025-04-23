package muska.client.muskaclient.mixins;

import muska.client.muskaclient.God;
import muska.client.muskaclient.NoFall;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void onCauseFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Boolean> cir) {
        // Проверяем, что сущность — это игрок
        if (!(((Object) this) instanceof PlayerEntity)) {
            return; // Если это не игрок, ничего не делаем
        }

        NoFall noFall = NoFall.getInstance();
        if (noFall != null && noFall.isNoFallEnabled()) {
            cir.setReturnValue(false); // Отменяем урон от падения для игрока
            cir.cancel();
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // Проверяем, что сущность — это игрок
        if (!(((Object) this) instanceof PlayerEntity)) {
            return; // Если это не игрок, ничего не делаем
        }

        God god = God.getInstance();
        if (god != null && god.isGodEnabled()) {
            cir.setReturnValue(false); // Отменяем нанесение урона для игрока
            cir.cancel();
        }
    }
}