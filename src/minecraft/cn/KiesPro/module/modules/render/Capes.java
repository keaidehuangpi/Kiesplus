package cn.KiesPro.module.modules.render;

import java.awt.Color;

import cn.KiesPro.Client;
import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.rendering.EventRenderCape;
import cn.KiesPro.management.FriendManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class Capes
extends Module {
    public Capes() {
        super("Capes", new String[]{"kape"}, ModuleType.Render);
        this.setColor(new Color(159, 190, 192).getRGB());
        this.setEnabled(true);
        this.setRemoved(true);
    }

    @EventHandler
    public void onRender(EventRenderCape event) {
        if (this.mc.theWorld != null && FriendManager.isFriend(event.getPlayer().getName())) {
            event.setLocation(Client.CLIENT_CAPE);
            event.setCancelled(true);
        }
    }
}

