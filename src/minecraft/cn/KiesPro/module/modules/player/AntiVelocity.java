/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.player;

import java.awt.Color;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class AntiVelocity
        extends Module {
    private Numbers<Double> horizontal = new Numbers<Double>("Horizontal", "Horizontal", 0.0, 0.0, 100.0, 1.0);
    private Numbers<Double> vertical = new Numbers<Double>("Vertical", "Vertical", 0.0, 0.0, 100.0, 1.0);

    public AntiVelocity() {
        super("Velocity", new String[]{"antivelocity", "antiknockback", "antikb"}, ModuleType.Player);
        this.addValues(horizontal,vertical);
        this.setColor(new Color(191, 191, 191).getRGB());
    }
    @EventTarget
    public void onPacketReceive(EventPacketRecieve event) {
       // if (mode.getValue().equals("Packet")) {
            if (event.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();

                if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                    if (horizontal.getValue() == 0f && vertical.getValue() == 0f) {
                        event.setCancelled(true);
                    } else {
                        packet.motionX=(int) (packet.getMotionX() * horizontal.getValue() / 100.0);
                        packet.motionY=(int) (packet.getMotionY() * vertical.getValue() / 100.0);
                        packet.motionZ=(int) (packet.getMotionZ() * horizontal.getValue() / 100.0);
                    }
                }
            }

            //hypixel fucker
            if (event.getPacket() instanceof S27PacketExplosion) {
                handle(event);
            }
       // }
    }


    public void handle(EventPacketRecieve event) {
        S27PacketExplosion packet = (S27PacketExplosion) event.getPacket();

        if (horizontal.getValue() == 0f && vertical.getValue() == 0f) {
            event.setCancelled(true);
        } else {
            packet.posX=(packet.getX() * horizontal.getValue() / 100.0f);
            packet.posY=(packet.getY() * vertical.getValue() / 100.0f);
            packet.posZ=(packet.getZ() * horizontal.getValue() / 100.0f);
        }
    }

}

