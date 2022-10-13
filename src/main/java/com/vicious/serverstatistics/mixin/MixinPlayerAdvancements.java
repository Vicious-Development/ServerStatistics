package com.vicious.serverstatistics.mixin;

import com.vicious.serverstatistics.common.event.ForgeARE;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class MixinPlayerAdvancements {

    @Shadow private ServerPlayer player;

    /**
     * Exact position of this in 1.19.2
     */
    @Inject(method = "revoke",at = @At(value = "CONSTANT", args = {"intValue=1"},shift = At.Shift.AFTER))
    public void revokeGlobal(Advancement advancement, String str, CallbackInfoReturnable<Boolean> cir){
        MinecraftForge.EVENT_BUS.post(new ForgeARE(player,advancement));
    }
}
