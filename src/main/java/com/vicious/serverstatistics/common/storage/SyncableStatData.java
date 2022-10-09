package com.vicious.serverstatistics.common.storage;

import com.vicious.serverstatistics.common.event.StatChangedEvent;
import com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation.Obscured;
import com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation.ReadOnly;
import com.vicious.viciouscore.common.data.implementations.SyncableDataTable;
import com.vicious.viciouscore.common.data.structures.SyncableCompound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Set;
import java.util.UUID;

public class SyncableStatData extends SyncableCompound {
    @Obscured
    public SyncableDataTable<AchievementData> achievers = new SyncableDataTable<>("achievers",AchievementData::new);
    @ReadOnly
    public SyncableStatsCounter counter = new SyncableStatsCounter("stats");

    public SyncableStatData(String key) {
        super(key);
        achievers.supports(AchievementData::getKey,ResourceLocation.class);
    }

    private AchievementData ensureExists(ResourceLocation rl){
        AchievementData achievements = achievers.get(rl);
        if(achievements == null){
            achievements = new AchievementData(rl);
        }
        achievers.add(achievements);
        return achievements;
    }

    @SubscribeEvent
    public void achieve(AdvancementEvent.AdvancementEarnEvent event){
        if(event.getEntity() instanceof ServerPlayer sp){
            ensureExists(event.getAdvancement().getId()).achieve(sp);
        }
    }
    @SubscribeEvent
    public void revoke(AdvancementEvent.AdvancementProgressEvent event){
        if(event.getProgressType() == AdvancementEvent.AdvancementProgressEvent.ProgressType.REVOKE) {
            if (event.getEntity() instanceof ServerPlayer sp) {
                ensureExists(event.getAdvancement().getId()).revoke(sp);
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
