package com.vicious.serverstatistics.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class StatAwardedEvent extends Event {
    private int award;
    private Stat<?> stat;
    private ServerPlayer player;

    public StatAwardedEvent(int award, Stat<?> stat, ServerPlayer player){
        this.award=award;
        this.stat=stat;
        this.player=player;
    }

    public int getAward(){
        return award;
    }
    public Stat<?> getStat(){
        return stat;
    }
    public ServerPlayer getPlayer(){
        return player;
    }
}
