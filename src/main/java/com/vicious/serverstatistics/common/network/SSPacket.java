package com.vicious.serverstatistics.common.network;

import com.vicious.viciouscore.common.network.VCPacket;
import com.vicious.viciouscore.common.util.SidedExecutor;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public abstract class SSPacket extends VCPacket{
    private static int id = -1;
    public static int nextId() {
        ++id;
        return id;
    }

    public static <T extends SSPacket> void registerSS(Class<T> type, Function<FriendlyByteBuf, T> decoderConstructor) {
        SSNetwork.getInstance().getChannel().registerMessage(nextId(),type, (pk,buf)->{
            try{
                pk.toBytes(buf);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        },decoderConstructor,(pk,ctx)->{
            try{
                if(pk.handleOnClient()){
                    SidedExecutor.clientOnly(()->pk.handle(ctx));
                }
                else if(pk.handleOnServer()){
                    pk.handle(ctx);
                }
                ctx.get().setPacketHandled(true);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
