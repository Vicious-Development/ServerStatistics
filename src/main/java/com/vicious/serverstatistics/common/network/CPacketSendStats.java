package com.vicious.serverstatistics.common.network;

import com.vicious.serverstatistics.common.storage.SyncableStatData;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.StatsCounter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
        this.stats = buf.readMap(Object2IntOpenHashMap::new, (b) -> {
            StatType<?> stattype = b.readById(BuiltInRegistries.STAT_TYPE);
            return readStatCap(b, stattype);
        }, FriendlyByteBuf::readVarInt);
    }

    @Override
    public void toBytes(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeMap(this.stats, CPacketSendStats::writeStatCap, FriendlyByteBuf::writeVarInt);
    }

    @Override
    public boolean handleOnClient() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        Minecraft.getInstance().execute(()->{
            StatsCounter counter = new StatsCounter();
            stats.forEach((s,i)->{
                counter.setValue(null,s,i);
            });
            Minecraft.getInstance().setScreen(new StatsScreen(Minecraft.getInstance().screen,counter));
        });
    }

    private static <T> Stat<T> readStatCap(FriendlyByteBuf p_237573_, StatType<T> p_237574_) {
        return p_237574_.get(p_237573_.readById(p_237574_.getRegistry()));
    }

    private static <T> void writeStatCap(FriendlyByteBuf p_237570_, Stat<T> p_237571_) {
        p_237570_.writeId(BuiltInRegistries.STAT_TYPE, p_237571_.getType());
        p_237570_.writeId(p_237571_.getType().getRegistry(), p_237571_.getValue());
    }
}
