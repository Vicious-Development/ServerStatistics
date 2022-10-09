package com.vicious.serverstatistics.common.storage;

import com.vicious.serverstatistics.common.event.AdvancedFirstTimeEvent;
import com.vicious.serverstatistics.common.event.AdvancementCompletelyRevokedEvent;
import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import net.minecraft.advancements.Advancement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

public class AdvancementData implements IVCNBTSerializable {
    private final Set<UUID> uuids = new HashSet<>();
    private ResourceLocation key;
    public AdvancementData(){}
    public AdvancementData(ResourceLocation key){
        this.key=key;
    }
    @Override
    public void serializeNBT(CompoundTag compoundTag, DataAccessor dataAccessor) {
        CompoundTag main = new CompoundTag();
        CompoundTag inner = new CompoundTag();
        int i = 0;
        for (UUID uuid : uuids) {
            inner.putUUID("e" + i,uuid);
            i++;
        }
        main.put("uuids",inner);
        main.putString("key",key.toString());
        compoundTag.put("ad",main);
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag, DataAccessor dataAccessor) {
        compoundTag = compoundTag.getCompound("ad");
        key = new ResourceLocation(compoundTag.getString("key"));
        compoundTag = compoundTag.getCompound("inner");
        for (String key : compoundTag.getAllKeys()) {
            uuids.add(compoundTag.getUUID(key));
        }
    }

    public ResourceLocation getKey() {
        return key;
    }

    public void achieve(ServerPlayer sp, Advancement advancement) {
        uuids.add(sp.getUUID());
        if(uuids.size() == 1){
            MinecraftForge.EVENT_BUS.post(new AdvancedFirstTimeEvent(sp,advancement));
        }
    }

    public void revoke(ServerPlayer sp, Advancement advancement) {
        uuids.remove(sp.getUUID());
        if(uuids.size() <= 0){
            MinecraftForge.EVENT_BUS.post(new AdvancementCompletelyRevokedEvent(sp,advancement));
        }
    }

    public Set<UUID> getAchievers() {
        return uuids;
    }
}
