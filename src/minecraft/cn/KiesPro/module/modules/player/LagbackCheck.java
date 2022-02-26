/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.player;

import java.awt.Color;

import cn.KiesPro.Client;
import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.movement.Flight;
import cn.KiesPro.module.modules.movement.Longjump;
import cn.KiesPro.module.modules.movement.Speed;
import cn.KiesPro.ui.notification.Notification;
import cn.KiesPro.utils.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class LagbackCheck
extends Module {
    public LagbackCheck() {
        super("LagbackCheck", new String[]{"lagback"}, ModuleType.Player);
        this.setColor(new Color(242, 137, 73).getRGB());
    }
    
	@EventTarget
	public void onPacket(EventPacketRecieve ep) {
		//this.setSuffix("Hypixel");
        if(ModuleManager.getModuleByName("Scaffold").isEnabled() &&
        		ModuleManager.getModuleByName("Speed").isEnabled()) {
       	 ModuleManager.getModuleByName("Speed").setEnabled(false);
        }
        /*if(!(ep.getPacket() instanceof S08PacketPlayerPosLook)) {
            return;
         };
         if(ModuleManager.getModuleByName("Speed").isEnabled()) {
        	 ModuleManager.getModuleByName("Speed").setEnabled(false);
         }
         if(ModuleManager.getModuleByName("Flight").isEnabled()) {
        	 ModuleManager.getModuleByName("Flight").setEnabled(false);
         }
         if(ModuleManager.getModuleByName("LongFly").isEnabled()) {
        	 ModuleManager.getModuleByName("LongFly").setEnabled(false);
         }
         if(ModuleManager.getModuleByName("LongJump").isEnabled()) {
        	 ModuleManager.getModuleByName("LongJump").setEnabled(false);
         }*/
        if(ep.getPacket() instanceof S08PacketPlayerPosLook) {
        	if(Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
        		//Helper.sendMessage("Lagback [Speed]!");
        		Client.instance.getNotificationManager().sendClientMessage("FlagDelector in Speed.", Notification.Type.WARNING);
        		Client.instance.getModuleManager().getModuleByClass(Speed.class).setEnabled(false);
        	}
        	
        	if(Client.instance.getModuleManager().getModuleByClass(Flight.class).isEnabled()) {
        		//Helper.sendMessage("Lagback [Flight]!");
        		Client.instance.getNotificationManager().sendClientMessage("FlagDelector in Fly.", Notification.Type.WARNING);
        		Client.instance.getModuleManager().getModuleByClass(Flight.class).setEnabled(false);
        	}
        	
        	if(Client.instance.getModuleManager().getModuleByClass(Longjump.class).isEnabled()) {
        		//Helper.sendMessage("Lagback [LongJump]!");
        		Client.instance.getNotificationManager().sendClientMessage("FlagDelector in LongJump.", Notification.Type.WARNING);
        		Client.instance.getModuleManager().getModuleByClass(Longjump.class).setEnabled(false);
        	}
	    }
	}
}

