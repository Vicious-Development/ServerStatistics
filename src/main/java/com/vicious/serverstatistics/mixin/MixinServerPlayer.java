package com.vicious.serverstatistics.mixin;

import com.vicious.serverstatistics.ServerStatistics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {
    @Inject(method = "awardStat",at = @At("HEAD"))
    public void awardGlobal(Stat<?> stat, int value, CallbackInfo ci){
        ServerStatistics.getData().awardStat(stat,value,asSP());
    }

    private ServerPlayer asSP(){
        return ServerPlayer.class.cast(this);
    }
}
