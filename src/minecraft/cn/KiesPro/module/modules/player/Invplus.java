/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package cn.KiesPro.module.modules.player;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.lwjgl.input.Keyboard;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.misc.EventInventory;
import cn.KiesPro.api.events.rendering.EventRender2D;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.math.RotationUtil;

public class Invplus
extends Module {
    public Option<Boolean> sw = new Option<Boolean>("ScreenWalk", "screenwalk", true);
    private Option<Boolean> xc = new Option<Boolean>("MoreInventory", "MoreInventory", false);

    public Invplus() {
        super("Inventory+", new String[]{"inventorywalk", "invwalk", "inventorymove", "inv+"}, ModuleType.Player);
        this.setColor(new Color(174, 174, 227).getRGB());
        this.addValues(this.sw, this.xc);
    }

    @EventTarget
    public void onEvent(EventPacketSend event) {
        if (event.getPacket() instanceof C0DPacketCloseWindow && this.xc.getValue().booleanValue()) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onInv(EventInventory event) {
        if (this.xc.getValue().booleanValue()) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    private void onRender(EventRender2D e) {
        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat) && this.sw.getValue().booleanValue()) {
            if (Keyboard.isKeyDown((int)200)) {
                RotationUtil.pitch(RotationUtil.pitch() - 2.0f);
            }
            if (Keyboard.isKeyDown((int)208)) {
                RotationUtil.pitch(RotationUtil.pitch() + 2.0f);
            }
            if (Keyboard.isKeyDown((int)203)) {
                RotationUtil.yaw(RotationUtil.yaw() - 2.0f);
            }
            if (Keyboard.isKeyDown((int)205)) {
                RotationUtil.yaw(RotationUtil.yaw() + 2.0f);
            }
        }
    }
}

