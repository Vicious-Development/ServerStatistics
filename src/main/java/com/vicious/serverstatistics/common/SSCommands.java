package com.vicious.serverstatistics.common;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.vicious.serverstatistics.ServerStatistics;
import com.vicious.serverstatistics.common.network.CPacketSendStats;
import com.vicious.serverstatistics.common.network.SSNetwork;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SSCommands {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> cmd = Commands.literal("serverstats")
                .executes((ctx)->sendStatsPacket(ctx.getSource()));
        event.getDispatcher().register(cmd);
    }
    public static int sendStatsPacket(CommandSourceStack src){
        try {
            if (src.getEntity() instanceof ServerPlayer sp) {
                SSNetwork.getInstance().sendToPlayer(sp, new CPacketSendStats(ServerStatistics.getData()));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
