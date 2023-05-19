package com.vicious.serverstatistics.common;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;

import java.util.Map;

public interface IAdvancements {
    public Map<Advancement, AdvancementProgress> getProgress();
}
