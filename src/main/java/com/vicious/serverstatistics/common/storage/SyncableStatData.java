package com.vicious.serverstatistics.common.storage;

import com.vicious.serverstatistics.common.event.ServerStatsResetEvent;
import com.vicious.serverstatistics.common.event.StatChangedEvent;
import com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation.Obscured;
import com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation.ReadOnly;
import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.implementations.SyncableArrayHashSet;
import com.vicious.viciouscore.common.data.implementations.SyncableDataTable;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AdvancementEvent;

import java.util.Set;
import java.util.UUID;

public class SyncableStatData extends SyncableCompound {
    @Obscured
    public SyncableDataTable<AdvancementData> advancers = new SyncableDataTable<>("achievers", AdvancementData::new);
    @Obscured
    public SyncableArrayHashSet<Participant> participants = new SyncableArrayHashSet<>("participants",Participant::new);
    @ReadOnly
    public SyncableStatsCounter counter = new SyncableStatsCounter("stats");

    public SyncableStatData(String key) {
        super(key);
        advancers.supports(AdvancementData::getKey,ResourceLocation.class);
    }

    @Override
    public void deserializeNBT(CompoundTag tag, DataAccessor sender) {
        super.deserializeNBT(tag, sender);
        if(participants.value.isEmpty()){
            advancers = new SyncableDataTable<>("achievers", AdvancementData::new);
            counter = new SyncableStatsCounter("stats");
            MinecraftForge.EVENT_BUS.post(new ServerStatsResetEvent());
        }
    }

    private AdvancementData ensureExists(ResourceLocation rl){
        AdvancementData achievements = advancers.get(rl);
        if(achievements == null){
            achievements = new AdvancementData(rl);
        }
        advancers.add(achievements);
        return achievements;
    }
    public void advance(AdvancementEvent.AdvancementProgressEvent event){
        if(event.getEntity() instanceof ServerPlayer sp) {
            if (event.getProgressType() == AdvancementEvent.AdvancementProgressEvent.ProgressType.REVOKE) {
                ensureExists(event.getAdvancement().getId()).revoke(sp, event.getAdvancement());
            }
            if (event.getAdvancementProgress().isDone()) {
                ensureExists(event.getAdvancement().getId()).achieve(sp,event.getAdvancement());
            }
        }
    }
    public Set<UUID> getAchievers(ResourceLocation rl){
        return ensureExists(rl).getAchievers();
    }

    public void awardStat(Stat<?> stat, int value, ServerPlayer player) {
        //It might be possible for time related statistics to exceed the maximum integer limit. For this reason I find it important to put a safe guard in.
        //This would really only occur of many players were constantly online, adding their maximum playtime together and producing enormous numbers.
        int statVal = counter.getValue().getValue(stat);
        if(statVal < Integer.MAX_VALUE-1) {
            StatChangedEvent sae = new StatChangedEvent(value,stat,player);
            MinecraftForge.EVENT_BUS.post(sae);
            if(!sae.isCanceled()) {
                counter.getValue().increment(null, stat, value);
            }
        }
    }

    public void resetStat(Stat<?> stat, ServerPlayer player) {
        awardStat(stat, -counter.getValue().getValue(stat),player);
    }
}
