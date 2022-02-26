/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.player;

import java.awt.Color;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.misc.EventChat;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.MoveUtil;
import cn.KiesPro.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall
        extends Module {
    double fall;
    private Mode mode = new Mode("Mode", "mode", (Enum[]) NoFallMode.values(), (Enum) NoFallMode.Hypixel);
    //public Numbers<Double> falldistance = new Numbers<Double>("FallDistance", "FallDistance", 2.5D, 1.0D, 4.0D, 0.5D);
    private float lastFall;

    public NoFall() {
        super("NoFall", new String[]{"Nofalldamage"}, ModuleType.Player);
        this.setColor(new Color(242, 137, 73).getRGB());
        this.addValues(mode);
    }

    @EventTarget
    public void onPacket(EventPacketSend e) {
        if (mode.getValue() == NoFallMode.Hypixel && e.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();

            if (!mc.thePlayer.capabilities.isFlying && !mc.thePlayer.capabilities.disableDamage && mc.thePlayer.motionY < 0.0d && packet.isMoving() && mc.thePlayer.fallDistance > 2.0f) {
                e.setCancelled(true);
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(packet.x, packet.y, packet.z, packet.onGround));
            }
        }
    }

    //private DelayTimer timer = new DelayTimer();


    @EventTarget
    public void onMove(EventPreUpdate event) {
        if (mode.getValue() == NoFallMode.Normal) {
            if (mc.thePlayer.fallDistance > 2 || PlayerUtils.getDistanceToFall() > 2) {
                event.ground = true;
            }
        } else if (mode.getValue() == NoFallMode.Setback) {
            if (mc.thePlayer.fallDistance > 5) {
                event.y += 0.2;
            }
        }
        if (mode.getValue() == NoFallMode.Hypixel) {
            if (!mc.thePlayer.capabilities.isFlying && !mc.thePlayer.capabilities.disableDamage && mc.thePlayer.motionY < 0.0d && mc.thePlayer.fallDistance > 3.0f) {
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
            }
        }


    }

    enum NoFallMode {
        Normal,
        Setback,
        Hypixel;

    }

}

