/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.render;

import java.awt.Color;

import cn.KiesPro.Client;
import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.rendering.EventRenderCape;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.management.FriendManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.ui.ClickGui.CSGOClickUI;
import cn.KiesPro.ui.ClickGui.YolaMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ClickGui
extends Module {
	public Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])gui.values(), (Enum)gui.Old);
    public ClickGui() {
        super("ClickGui", new String[]{"Clickgui"}, ModuleType.Render);
        this.addValues(mode);
    }

	@Override
	public void onEnable() {
		switch (mode.getValue().toString()) {
			case "Old":
			this.mc.displayGuiScreen(new CSGOClickUI());
			break;
			case "New":
			this.mc.displayGuiScreen(new YolaMenu());
			break;
		}
		this.setEnabled(false);
	}
	
	enum gui {
		Old,
		New;
	}
}

