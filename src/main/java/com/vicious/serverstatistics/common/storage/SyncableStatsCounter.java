package com.vicious.serverstatistics.common.storage;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.structures.SyncableValue;
import com.vicious.viciouscore.common.util.server.ServerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.util.datafix.DataFixers;

public class SyncableStatsCounter extends SyncableValue<ServerStatsCounter> {
    public SyncableStatsCounter(String key) {
        super(key, new ServerStatsCounter(ServerHelper.server,new FakeFile("")));
    }

    @Override
    public void serializeNBT(CompoundTag compoundTag, DataAccessor dataAccessor) {
        compoundTag.putString(KEY,value.toJson());
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag, DataAccessor dataAccessor) {
        value.parseLocal(DataFixers.getDataFixer(),compoundTag.getString(KEY));
    }
}
