package com.vicious.serverstatistics.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class StatChangedEvent extends Event {
    private int change;
    private Stat<?> stat;
    private ServerPlayer player;

    public StatChangedEvent(int change, Stat<?> stat, ServerPlayer player){
        this.change=change;
        this.stat=stat;
        this.player=player;
    }

    public int getChange(){
        return change;
    }
    public Stat<?> getStat(){
        return stat;
    }
    public ServerPlayer getPlayer(){
        return player;
    }
}
