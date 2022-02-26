package cn.KiesPro.api.events.world;

import cn.KiesPro.api.Event;
import net.minecraft.network.Packet;

public class EventPacketReceive extends Event {
    private static Packet packet;

    public EventPacketReceive(Packet packet) {
        this.packet = packet;
    }

    public static Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
