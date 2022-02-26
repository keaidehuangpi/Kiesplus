/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.movement;

import java.awt.Color;
import java.util.List;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventMove;
import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.events.world.MotionUpdateEvent;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.PlayerUtil;
import cn.KiesPro.utils.math.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Longjump
extends Module {
    private double lastDif;
    private double moveSpeed;
    private int stage;
    private int groundTicks;
    private Option<Boolean> Hypixel = new Option<Boolean>("Hypixel", "Hypixel", true);
    public Longjump() {
        super("LongJump", new String[]{"lj", "jumpman", "jump"}, ModuleType.Movement);
        this.setColor(new Color(76, 67, 216).getRGB());
        this.addValues(Hypixel);
    }
    
    @Override
    public void onEnable() {
		if (this.isStair(mc.theWorld
				.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)).getBlock())
				|| this.isStair2(mc.theWorld
						.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock()) || mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) {
			this.setEnabled(false);
			Helper.sendMessage("不要在台阶上开!");
		}
        this.lastDif = 0.0;
        this.moveSpeed = 0.0;
        this.stage = 0;
        this.groundTicks = 1;
    }
    
	private boolean isStair(Block blockIn) {
		return blockIn instanceof BlockSlab;
	}
	
	private boolean isStair2(Block blockIn) {
		return blockIn instanceof BlockStairs;
	}
    
    @EventTarget
    public void onUpdate(MotionUpdateEvent e) {
    	this.setSuffix("Hypixel");
        if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
        	e.setPosY(e.getPosY() + 7.435E-4);
        }
        double xDif = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDif = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        this.lastDif = Math.sqrt(xDif * xDif + zDif * zDif);
        if (mc.thePlayer.moving() && mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && this.stage > 2) {
            ++this.groundTicks;
        }
        if (this.groundTicks > 1) {
        	this.setEnabled(false);
        }
    }
    
    @EventTarget
    private void onMove(EventMove e) {
    	if(this.isEnabled()) {
    		boolean watchdog = this.Hypixel.getValue();
    		if(mc.thePlayer.isMoving()) {
	    		switch (this.stage) {
	        		case 0: 
			        case 1: {
			            this.moveSpeed = 0.0;
			            break;
			        }
			        case 2: {
			            if (!mc.thePlayer.onGround || !mc.thePlayer.isCollidedVertically) break;
			            e.y = mc.thePlayer.motionY = getJumpBoostModifier(0.3999999463558197);
			            this.moveSpeed = getBaseMoveSpeed() * 2.0;
			            break;
			        }
			        case 3: {
			            this.moveSpeed = getBaseMoveSpeed() * 2.149f;
			            break;
			        }
			        case 4: {
			        	if (!watchdog) break;
			            this.moveSpeed *= 1.6f;
			            break;
			        }
			        default: {
			            if (mc.thePlayer.motionY < 0.0) {
			            	mc.thePlayer.motionY *= 0.5;
			            }
			            this.moveSpeed = this.lastDif - this.lastDif / 159.0;
			        }
			    }
	    		this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
	    		++this.stage;
    		}
    		setSpeed(e, this.moveSpeed);
    	}
    }

	public static double getJumpBoostModifier(double baseJumpHeight) {
	    if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
	        int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
	        baseJumpHeight += (double)((float)(amplifier + 1) * 0.1f);
	    }
	    return baseJumpHeight;
	}
	
    public static void setSpeed(EventMove moveEvent, double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe, Minecraft.getMinecraft().thePlayer.movementInput.moveForward);
    }
	
    public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                yaw += (float)(forward > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }

    double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

}

