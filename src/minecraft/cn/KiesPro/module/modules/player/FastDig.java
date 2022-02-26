package cn.KiesPro.module.modules.player;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.events.world.EventTick;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastDig
extends Module {
	boolean bzs = false;
    double bzx = 0.0;
    BlockPos pos;
    EnumFacing face;
	private static Numbers<Double> aps = new Numbers<Double>("APS", "APS", 0.7, 0.0, 1.0, 0.1);
	public Mode<Enum> mode = new Mode("Mode","Mode",(Enum[])DigMode.values(),(Enum)DigMode.Sigma);
    public FastDig() {
        super("SpeedMine", new String[]{"speedmine"}, ModuleType.Player);
        this.setColor(new Color(223, 233, 233).getRGB());
        this.addValues(mode,aps);
    }
    
    @EventTarget
    private void onUpdate(EventPreUpdate e) {
    	if(this.mode.getValue() == DigMode.Sigma) {
	        this.mc.playerController.blockHitDelay = 0;
	        if (this.mc.playerController.curBlockDamageMP < 0.7f) return;
	        this.mc.playerController.curBlockDamageMP = 1.0f;
    	}else if(this.mode.getValue() == DigMode.Remix) {
    		mc.playerController.blockHitDelay = 0;
    		if (mc.playerController.curBlockDamageMP >= ((Double)aps.getValue()).doubleValue()) {
    			mc.playerController.curBlockDamageMP = 1.0F;
    		}   if (mc.playerController.curBlockDamageMP > 0.001D) {
    			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		  mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		 mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		 mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		 mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		 mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.DOWN));
    		}
    	}else if(mode.getValue() == DigMode.Remix2) {
			if(mc.playerController.extendedReach()) {
				mc.playerController.blockHitDelay = 0;
			} 
			else if(bzs) {
				Block block = mc.theWorld.getBlockState(pos).getBlock();
				bzx += (block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, pos) * 1.4);
				if (bzx >= 1.0) {
					mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState(), 11);
					mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, face));
					bzx = 0.0;
					bzs = false;
				}
			}
    	}
    }
    
    @EventTarget
    public void onPacket(EventPacketSend e) {
        if(e.getPacket() instanceof C07PacketPlayerDigging && mc.playerController != null) {
        	C07PacketPlayerDigging ex = ((C07PacketPlayerDigging)e.getPacket());
            if(ex.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                bzs = true;
                pos = ex.getPosition();
                face = ex.getFacing();
                bzx = 0.0;
            }
            else if(ex.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || ex.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                bzs = false;
                pos = null;
                face = null;
            }
        }
    }
    
    @Override
	public void onDisable() {
    	mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
	}
    
    enum DigMode {
    	Sigma,
    	Remix,
    	Remix2;
    }
}
