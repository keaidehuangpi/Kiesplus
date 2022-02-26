/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package cn.KiesPro.module.modules.player;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.management.FriendManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class MCF
extends Module {
    private boolean down;

    public MCF() {
        super("MCF", new String[]{"middleclickfriends", "middleclick"}, ModuleType.Player);
        this.setColor(new Color(241, 175, 67).getRGB());
    }

    @EventTarget
    private void onClick(EventPreUpdate e) {
        if (Mouse.isButtonDown((int)2) && !this.down) {
            if (this.mc.objectMouseOver.entityHit != null) {
                EntityPlayer player = (EntityPlayer)this.mc.objectMouseOver.entityHit;
                String playername = player.getName();
                if (!FriendManager.isFriend(playername)) {
                    this.mc.thePlayer.sendChatMessage(".f add " + playername);
                } else {
                    this.mc.thePlayer.sendChatMessage(".f del " + playername);
                }
            }
            this.down = true;
        }
        if (!Mouse.isButtonDown((int)2)) {
            this.down = false;
        }
    }
}

