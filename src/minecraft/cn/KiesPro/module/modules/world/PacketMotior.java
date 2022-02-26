package cn.KiesPro.module.modules.world;


import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.movement.Flight;
import cn.KiesPro.module.modules.movement.Speed;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.TimerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketMotior
extends Module {
    private int packetcount;
    private TimerUtil time = new TimerUtil();

    public PacketMotior() {
        super("PacketMotior", new String[]{"PacketMotior"}, ModuleType.World);
    }

    @EventHandler
    public void OnUpdate(EventPreUpdate event) {
        if (this.time.delay(1000.0f)) {
            this.setSuffix((Object)("PPS:" + this.packetcount));
            if (this.packetcount > 22) {
                Helper.sendMessage((String)"Packet Warning!!");
            }
            this.packetcount = 0;
            this.time.reset();
        }
    }

    @EventHandler
    public void Packet(EventPacketSend event) {
        if (event.getPacket() instanceof C03PacketPlayer && !ModuleManager.getModuleByClass(Flight.class).isEnabled() && !ModuleManager.getModuleByClass(Speed.class).isEnabled()) {
            ++this.packetcount;
        }
    }
}

 