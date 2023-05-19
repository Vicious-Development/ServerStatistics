package com.vicious.serverstatistics.mixin;

import com.vicious.serverstatistics.common.IAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(PlayerAdvancements.class)
public class MixinPlayerAdvancements implements IAdvancements {
    @Shadow @Final private Map<Advancement, AdvancementProgress> progress;

    @Override
    public Map<Advancement, AdvancementProgress> getProgress() {
        return progress;
    }
}
