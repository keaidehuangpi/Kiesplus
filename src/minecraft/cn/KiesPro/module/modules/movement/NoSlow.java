/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.movement;

import java.awt.Color;

import cn.KiesPro.Client;
import cn.KiesPro.api.EventTarget;
import cn.KiesPro.utils.*;
import cn.KiesPro.api.events.world.EventPostUpdate;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.events.world.EventStep;
import cn.KiesPro.api.events.world.MotionUpdateEvent;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.combat.Killaura;
import cn.KiesPro.ui.notification.Notification;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.PlayerUtil;
import cn.KiesPro.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.network.*;
import cn.KiesPro.*;
import net.minecraft.*;

public class NoSlow extends Module {
	public TimerUtil timer = new TimerUtil();
    private MSTimer msTimer= new MSTimer();
	private Option<Boolean> booster = new Option<Boolean>("booster", "booster", false);
	private Mode<Enum> mode = new Mode("Mode", "Mode", (Enum[]) SlowMode.values(), (Enum) SlowMode.Hypixel);
    public NoSlow() {
        super("NoSlow", new String[]{"noslowdown"}, ModuleType.Movement);
        this.setColor(new Color(216, 253, 100).getRGB());
        this.addValues(mode);
		this.addValues(booster);
		}

	@EventTarget
	public void onUpdate(MotionUpdateEvent e) {
		this.setSuffix(mode.getValue());
    if (mc.thePlayer.isBlocking() || (Killaura.blocking.getValue().booleanValue()&&Killaura.curTarget!=null&&isHoldingSword())) {

      if (mode.getValue() == SlowMode.liquidbounce) {
        sendPacket(e, true, true, false, 0, false, false);
      } else if (mode.getValue() == SlowMode.fdpaac) {
        if (mc.thePlayer.ticksExisted % 3 == 0) {
          sendPacket(e, true, false, false, 0, false, false);
        } else if (mc.thePlayer.ticksExisted % 3 == 1) {
          sendPacket(e, false, true, false, 0, false, false);
        } else if (mode.getValue() == SlowMode.fdpncp) {
          sendPacket(e, true, true, false, 0, false, false);
        } else if (mode.getValue() == SlowMode.fdpwatchdog2) {
          if (!e.isCancelled() == e.isPre()) {
            mc.getNetHandler()
                .addToSendQueue(
                    new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        BlockPos.ORIGIN,
                        EnumFacing.DOWN));
          } else {
            mc.getNetHandler()
                .addToSendQueue(
                    new C08PacketPlayerBlockPlacement(
                        new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
          }
        } else if (mode.getValue() == SlowMode.fdpwatchdog) {
          if (mc.thePlayer.ticksExisted % 2 == 0) {
            sendPacket(e, true, false, true, 50, true, false);
          } else {
            sendPacket(e, false, true, false, 0, true, true);
          }
        }
      }
		}


		if(mode.getValue() == SlowMode.Hypixel) {
			if(e.isPre()) {
				if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving() && isOnGround(0.42)){			
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				}
			}else {
				if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving() && isOnGround(0.42) && Killaura.target != null){
					mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(PlayerUtil.getHypixelBlockpos(mc.getSession().getUsername()), 255, mc.thePlayer.inventory.getCurrentItem(), 0,0,0));
				}
			}
		}else if(mode.getValue() == SlowMode.Hypixel2) {
			if(mc.thePlayer.isUsingItem()||mc.thePlayer.isBlocking()||Killaura.blocking.getValue().booleanValue()) {
	            if (e.isPre()) {
	                mc.playerController.syncCurrentPlayItem();
	                mc.getNetHandler().addToSendQueueSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
	            } else {
	                mc.playerController.syncCurrentPlayItem();
	                mc.getNetHandler().addToSendQueueSilent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
	            }
			}
    } else if (mode.getValue() == SlowMode.AAC) {

			if (mc.thePlayer.onGround || isOnGround(0.5)) {
        if (mc.thePlayer.isUsingItem()||mc.thePlayer.isBlocking()||Killaura.blocking.getValue().booleanValue()) {

            mc.thePlayer.sendQueue.addToSendQueue(
                new C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    BlockPos.ORIGIN,
                    EnumFacing.DOWN));
			//if (this.booster.getValue().booleanValue()){Helper.sendMessage("hi");}

			  }
		  }
		}else if (mode.getValue() == SlowMode.AAC5){
			if (mc.thePlayer.isUsingItem()||mc.thePlayer.isBlocking()||(Killaura.blocking.getValue().booleanValue()&&Killaura.curTarget!=null)){
				mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1,-1,-1),255, mc.thePlayer.inventory.getCurrentItem(),0f,0f,0f));
			}
			return;
		}
	}

    private boolean isBlocking() {
        return isHoldingSword() && mc.thePlayer.isBlocking();
    }
	
    public static boolean isHoldingSword() {
        return Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

	public void sendPacket(MotionUpdateEvent event,boolean sendC07,boolean sendC08,boolean delay,long delayValue,boolean onGround,boolean watchdog){
		C07PacketPlayerDigging digging= new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1,-1,-1), EnumFacing.DOWN);
		C08PacketPlayerBlockPlacement blockPlace= new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem());
		C08PacketPlayerBlockPlacement blockment= new C08PacketPlayerBlockPlacement(new BlockPos(-1,-1,-1),255, mc.thePlayer.inventory.getCurrentItem(),0f,0f,0f);
		if (onGround&&!mc.thePlayer.onGround){
			return;
		}
		if (sendC07&&(!event.isCancelled()== event.isPre())){
			//Helper.sendMessage("sendc07withpre");
			if (delay&&msTimer.hasTimePassed(delayValue)){
				mc.getNetHandler().addToSendQueue(digging);
			}else if (!delay){
				mc.getNetHandler().addToSendQueue(digging);
			}
		}
		if (sendC08&&!event.isCancelled()==!event.isPre()){
			if (delay&&msTimer.hasTimePassed(delayValue)&&watchdog){
				mc.getNetHandler().addToSendQueue(blockPlace);
				msTimer.reset();
			}else if (!delay&&!watchdog){
				mc.getNetHandler().addToSendQueue(blockPlace);
			}else if (watchdog){
				mc.getNetHandler().addToSendQueueSilent(blockment);
			}
		}
	}

    public static boolean isOnGround(double height) {
    	Minecraft.getMinecraft().getMinecraft().getMinecraft();
    	if (!Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
    		return true;
    	}
    	return false;
    }
    
    enum SlowMode {
    	Hypixel,
    	Hypixel2,
    	AAC,
		AAC5,
        liquidbounce,
        fdpaac,
        fdpncp,
        fdpwatchdog2,
		vanilla,
        fdpwatchdog;
    }
}

