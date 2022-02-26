/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.world;

import java.awt.Color;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate
extends Module {
    public NoRotate() {
        super("AntiDesync", new String[]{"norotate","rotate"}, ModuleType.World);
        this.setColor(new Color(17, 250, 154).getRGB());
    }

    @EventTarget
    private void onPacket(EventPacketSend e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook look = (S08PacketPlayerPosLook)e.getPacket();
            look.yaw = this.mc.thePlayer.rotationYaw;
            look.pitch = this.mc.thePlayer.rotationPitch;
        }
    }
}

