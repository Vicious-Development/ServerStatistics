package com.vicious.serverstatistics.common.network;

import com.vicious.viciouscore.common.network.VCNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class SSNetwork extends VCNetwork{
    public static SSNetwork instance;
    public static SSNetwork getInstance(){
        if(instance == null){
            instance = new SSNetwork();
            SSPacket.registerSS(CPacketSendStats.class, CPacketSendStats::new);
        }
        return instance;
    }

    @Override
    public SimpleChannel getChannel() {
        if(channel == null) channel = NetworkRegistry.newSimpleChannel(new ResourceLocation("serverstatistics", "network"), VCNetwork::getProtocolVersion,getProtocolVersion()::equals,getProtocolVersion()::equals);
        return channel;

    }
}
