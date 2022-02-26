/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.player;

import net.minecraft.network.play.server.S45PacketTitle;

import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.misc.EventChat;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class AutoGG
        extends Module {
    public AutoGG() {
        super("AutoGG", new String[]{"autogg"}, ModuleType.Player);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }

    @EventTarget
    private void onPacket(EventPacketSend e) {
        if (e.getPacket() instanceof S45PacketTitle) {
            S45PacketTitle packet = (S45PacketTitle) e.getPacket();
            if (packet.getType().equals(S45PacketTitle.Type.TITLE)) {
                String text = packet.getMessage().getUnformattedText();
                if (text.equals("ʤ��") || text.equals("Victory")) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mc.thePlayer.sendChatMessage("gg");
                            this.cancel();
                        }
                    }, 10L);
                }
            }
        }
    }
}

