package com.vicious.serverstatistics.common.event;

import net.minecraft.advancements.Advancement;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.AdvancementEvent;

/**
 * This is a 1.18.2 feature only, in 1.19.2 forge ver 43.1.33, an advancementrevokation event is introduced.
 * This does not exist in 1.19.2 ServerStats and is for mixins only.
 */
public class ForgeARE extends AdvancementEvent {
    public ForgeARE(Player player, Advancement advancement) {
        super(player, advancement);
    }
}
