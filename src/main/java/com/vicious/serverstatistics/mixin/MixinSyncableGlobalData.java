package com.vicious.serverstatistics.mixin;

import com.vicious.serverstatistics.common.storage.IStatData;
import com.vicious.serverstatistics.common.storage.SyncableStatData;
import com.vicious.viciouscore.aunotamation.isyncablecompoundholder.annotation.Obscured;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncableGlobalData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SyncableGlobalData.class)
public class MixinSyncableGlobalData implements IStatData {
    @Obscured
    public SyncableStatData serverstatistics = new SyncableStatData("serverstatistics");

    @Override
    public SyncableStatData getStatData() {
        return serverstatistics;
    }
}

