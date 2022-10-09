package com.vicious.serverstatistics.common.event;

import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

public class AdvancementCompletelyRevokedEvent extends Event {
    private ServerPlayer player;
    private Advancement advancementKey;
    public AdvancementCompletelyRevokedEvent(ServerPlayer sp, Advancement key) {
        this.player=sp;
        this.advancementKey=key;
    }
    public ServerPlayer getPlayer(){
        return player;
    }
    public Advancement getAdvancement(){
        return advancementKey;
    }
}
