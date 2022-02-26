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
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall
extends Module {
	double fall;
	private Mode mode = new Mode("Mode", "mode", (Enum[])NoFallMode.values(), (Enum)NoFallMode.Hypixel);
	public Numbers<Double> falldistance = new Numbers<Double>("FallDistance", "FallDistance", 2.5D, 1.0D, 4.0D, 0.5D);
	private float lastFall;
	public NoFall() {
        super("NoFall", new String[]{"Nofalldamage"}, ModuleType.Player);
        this.setColor(new Color(242, 137, 73).getRGB());
        this.addValues(mode, falldistance);
    }
	
    @EventTarget
    private void onUpdate(EventPreUpdate e) {
    	this.setSuffix(mode.getValue());
    	if(mode.getValue() == NoFallMode.Hypixel) {
            NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
            float falldis = 2.4f + (float)MoveUtil.getJumpEffect();
	        if (mc.thePlayer.fallDistance - this.lastFall >= falldis && mc.thePlayer.capabilities.isCreativeMode == false) {
	            this.lastFall = mc.thePlayer.fallDistance;
	            var1.sendPacketNoEvent(new C03PacketPlayer(true));
	        } else if (mc.thePlayer.isCollidedVertically) {
	            this.lastFall = 0.0f;
	        }
        }else if(mode.getValue() == NoFallMode.Hypixel2) {
            NetworkManager var1 = mc.thePlayer.sendQueue.getNetworkManager();
            float falldis = falldistance.getValue().floatValue() + (float)MoveUtil.getJumpEffect();
	        if (mc.thePlayer.fallDistance - this.lastFall >= falldis && mc.thePlayer.capabilities.isCreativeMode == false) {
	            this.lastFall = mc.thePlayer.fallDistance;
	            var1.sendPacketNoEvent(new C03PacketPlayer(true));
	        } else if (mc.thePlayer.isCollidedVertically) {
	            this.lastFall = 0.0f;
	        }
        }else if(mode.getValue() == NoFallMode.Packet) {
            if (mc.thePlayer.fallDistance > 2F)
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
        }
    }
    
    @EventTarget
    private void onPacket(EventPacketSend e) {
    	if(mode.getValue() == NoFallMode.Edit) {
			if(e.getPacket() instanceof C03PacketPlayer) {
	    		if(!MoveUtil.isOnGround(0.001)){
	    			if(mc.thePlayer.motionY < -0.08)
	    				fall -= mc.thePlayer.motionY;
	    			if(fall > 2){
	    				fall = 0;
	    			
	    				mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
	        		}
	    		}else
	    			fall = 0;
				}
    	}
    }
    
    enum NoFallMode {
    	Hypixel,
    	Hypixel2,
    	Packet,
    	Edit;
    }
}

