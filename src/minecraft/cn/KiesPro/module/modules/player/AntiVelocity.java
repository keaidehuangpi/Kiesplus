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
    private Numbers<Double> percentage = new Numbers<Double>("Percentage", "percentage", 0.0, 0.0, 100.0, 5.0);

    public AntiVelocity() {
        super("Velocity", new String[]{"antivelocity", "antiknockback", "antikb"}, ModuleType.Player);
        this.addValues(this.percentage);
        this.setColor(new Color(191, 191, 191).getRGB());
    }

    @EventTarget
    private void onUpdate(EventPreUpdate e) {
        this.setSuffix(percentage.getValue() + "%");
    }

    @EventTarget
    private void onPacket(EventPacketRecieve e) {
        if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
            if (this.percentage.getValue().equals(0.0)) {
                e.setCancelled(true);
            } else {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();
                packet.motionX = (int) (this.percentage.getValue() / 100.0);
                packet.motionY = (int) (this.percentage.getValue() / 100.0);
                packet.motionZ = (int) (this.percentage.getValue() / 100.0);
            }
        }
    }
}

