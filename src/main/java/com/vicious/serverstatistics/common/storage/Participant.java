package com.vicious.serverstatistics.common.storage;

import com.vicious.viciouscore.common.data.DataAccessor;
import com.vicious.viciouscore.common.data.IVCNBTSerializable;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;
import java.util.UUID;

public class Participant implements IVCNBTSerializable {
    private UUID uuid;
    public Participant(UUID uuid){
        this.uuid=uuid;
    }
    public UUID getUUID(){
        return uuid;
    }

    public Participant(){}


    @Override
    public void serializeNBT(CompoundTag compoundTag, DataAccessor dataAccessor) {
        compoundTag.putUUID("u",uuid);
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag, DataAccessor dataAccessor) {
        uuid = compoundTag.getUUID("u");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
