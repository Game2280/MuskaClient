package muska.client.muskaclient.mixins;

import muska.client.muskaclient.Speed;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
    @Inject(method = "travel", at = @At("HEAD"))
    private void onTravel(Vector3d movementInput, CallbackInfo ci) {
        Speed speed = Speed.getInstance();
        if (speed != null && speed.isSpeedEnabled()) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            float multiplier = speed.getSpeedMultiplier();
            Vector3d currentVelocity = player.getDeltaMovement();
            Vector3d newVelocity = currentVelocity.scale(multiplier);
            player.setDeltaMovement(newVelocity);
        }
    }
}