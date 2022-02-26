package cn.KiesPro.module.modules.player;

import java.awt.Color;

import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.management.FriendManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class NoCommand
extends Module {
    private boolean down;

    public NoCommand() {
        super("NoCommand", new String[]{"nocommand", "chatbypass"}, ModuleType.Player);
        this.setColor(new Color(241, 175, 67).getRGB());
    }
}

