package com.vicious.serverstatistics;

import com.mojang.logging.LogUtils;
import com.vicious.serverstatistics.common.SSCommands;
import com.vicious.serverstatistics.common.network.SSNetwork;
import com.vicious.serverstatistics.common.storage.IStatData;
import com.vicious.serverstatistics.common.storage.SyncableStatData;
import com.vicious.viciouscore.common.data.implementations.attachable.SyncableGlobalData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ServerStatistics.MODID)
public class ServerStatistics {
    public static final String MODID = "serverstatistics";
    public static final Logger LOGGER = LogUtils.getLogger();
    public ServerStatistics() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(SSCommands.class);
        SSNetwork.getInstance();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Running Server Statistics.");
        SyncableGlobalData.getInstance().executeAs(IStatData.class,(isd)->{
            MinecraftForge.EVENT_BUS.register(isd.getStatData());
        });
    }
    public static SyncableStatData getData(){
        IStatData dat = (IStatData) SyncableGlobalData.getInstance();
        return dat.getStatData();
    }
}
