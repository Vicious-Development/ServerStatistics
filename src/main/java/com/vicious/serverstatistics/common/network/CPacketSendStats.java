package com.vicious.serverstatistics.common.network;

import com.vicious.serverstatistics.common.storage.SyncableStatData;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.StatsCounter;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketSendStats extends SSPacket {
    //Clone of the vanilla stats packet but remade for sending the server stats.
    private final Object2IntMap<Stat<?>> stats;

    public CPacketSendStats(SyncableStatData stats) {
        StatsCounter counter = stats.counter.getValue();
        this.stats = counter.stats;
    }

    public CPacketSendStats(FriendlyByteBuf buf) {
        this.stats = buf.readMap(Object2IntOpenHashMap::new, (buf2) -> {
            int i = buf2.readVarInt();
            int j = buf2.readVarInt();
            return readStatCap(Registry.STAT_TYPE.byId(i), j);
        }, FriendlyByteBuf::readVarInt);
    }

    @Override
    public void toBytes(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeMap(this.stats, this::writeStatCap, FriendlyByteBuf::writeVarInt);
    }

    @Override
    public boolean handleOnClient() {
        return true;
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        Minecraft.getInstance().execute(()->{
            StatsCounter counter = new StatsCounter();
            stats.forEach((s,i)->{
                counter.setValue(null,s,i);
            });
            Minecraft.getInstance().setScreen(new StatsScreen(Minecraft.getInstance().screen,counter));
        });
    }

    private static <T> Stat<T> readStatCap(StatType<T> p_178596_, int p_178597_) {
        return p_178596_.get(p_178596_.getRegistry().byId(p_178597_));
    }

    private <T> int getStatIdCap(Stat<T> p_178594_) {
        return p_178594_.getType().getRegistry().getId(p_178594_.getValue());
    }
    private <T> void writeStatCap(FriendlyByteBuf p_237570_, Stat<T> p_237571_) {
        p_237570_.writeVarInt(Registry.STAT_TYPE.getId(p_237571_.getType()));
        p_237570_.writeVarInt(this.getStatIdCap(p_237571_));
    }
}
