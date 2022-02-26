/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.player;

import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.misc.EventChat;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.Helper;

public class AutoPlay
        extends Module {
    public AutoPlay() {
        super("AutoPlay", new String[]{"fastplay"}, ModuleType.Player);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }

    @EventTarget
    private void onChat(EventChat e) {
    	/*if(AutoGG.fake == true) {
    		AutoGG.fake = false;*/
        if (e.getMessage().contains("��ʤ����") || e.getMessage().contains("You won") || e.getMessage().contains("������") || e.getMessage().contains("You died")) {
            Helper.sendMessage("Join new game. [ Solo Hypixel ]");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mc.thePlayer.sendChatMessage("/play solo_normal");
                    this.cancel();
                }
            }, 700L);
        }
    }
}

