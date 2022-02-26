package cn.KiesPro.module.modules.player;

import java.awt.Color;

import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity
extends Module {
    private double motionX;
    private double motionZ;
    private Numbers<Double> percentage = new Numbers<Double>("Percentage", "percentage", 0.0, 0.0, 100.0, 5.0);
    public static Mode mode = new Mode("Mode", "mode", (Enum[])AntiVelocityMode.values(), (Enum)AntiVelocityMode.Normal);
    public Velocity() {
    	
        super("Velocity", new String[]{"antivelocity", "antiknockback", "antikb"}, ModuleType.Player);
        this.addValues(this.percentage,mode);
        this.setColor(new Color(191, 191, 191).getRGB());
        
    }


    @EventHandler
    private void onPacket(EventPacketRecieve e) {
		super.setSuffix("Simple");
    	 if (this.mode.getValue() == AntiVelocityMode.Normal) {
        if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
            if (this.percentage.getValue().equals(0.0)) {
                e.setCancelled(true);
            } else {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                    packet.motionX = (int)(this.percentage.getValue() / 100.0);
                    packet.motionY = (int)(this.percentage.getValue() / 100.0);
                    packet.motionZ = (int)(this.percentage.getValue() / 100.0);
                }
            }
        }

    


	 if (this.mode.getValue() == AntiVelocityMode.AAC) {
		 
		 if (Minecraft.thePlayer.hurtTime == 9) {
             this.motionX = Minecraft.thePlayer.motionX;
             this.motionZ = Minecraft.thePlayer.motionZ;
         } else if (Minecraft.thePlayer.hurtTime == 4) {
             Minecraft.thePlayer.motionX = (- this.motionX) * 0.6;
             Minecraft.thePlayer.motionZ = (- this.motionZ) * 0.6;

         }
	 }
}
	 
	 enum AntiVelocityMode {
		 
	    	Normal,
	    	AAC,
	    	
	    	
	    }
	 }
    



