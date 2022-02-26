/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.movement;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.misc.EventCollideWithBlock;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.events.world.MotionUpdateEvent;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.PlayerUtil;
import cn.KiesPro.utils.Stopwatch;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Jesus
extends Module {
    int stage;
    int water;
    private Stopwatch timer;
    private boolean wasWater;
    private int ticks;
    public Mode mode = new Mode("Mode", "mode", (Enum[])JesusMode.values(), (Enum)JesusMode.Solid);
    public Jesus() {
        super("Jesus", new String[]{"waterwalk", "float"}, ModuleType.Movement);
        this.setColor(new Color(188, 233, 248).getRGB());
        this.addValues(mode);
    }

    @Override
    public void onEnable() {
        this.wasWater = false;
    }
    
    private boolean canJeboos() {
        return mc.thePlayer.fallDistance < 3.0f && !mc.gameSettings.keyBindJump.isPressed() && !PlayerUtil.isInLiquid() && !mc.thePlayer.isSneaking();
    }
    
    boolean shouldJesus() {
        final double x = mc.thePlayer.posX;
        final double y = mc.thePlayer.posY;
        final double z = mc.thePlayer.posZ;
        final ArrayList<BlockPos> pos = new ArrayList<BlockPos>(Arrays.asList(new BlockPos(x + 0.3, y, z + 0.3), new BlockPos(x - 0.3, y, z + 0.3), new BlockPos(x + 0.3, y, z - 0.3), new BlockPos(x - 0.3, y, z - 0.3)));
        for (final BlockPos po : pos) {
            if (!(mc.theWorld.getBlockState(po).getBlock() instanceof BlockLiquid)) {
                continue;
            }
            if (mc.theWorld.getBlockState(po).getProperties().get((Object)BlockLiquid.LEVEL) instanceof Integer && (int)mc.theWorld.getBlockState(po).getProperties().get((Object)BlockLiquid.LEVEL) <= 4) {
                return true;
            }
        }
        return false;
    }
    
    @EventTarget
    public void onPacket(EventPacketSend e) {
    	if(mode.getValue() == JesusMode.Beta) {
	        if (e.getPacket() instanceof C03PacketPlayer && this.canJeboos() && BlockHelper.isOnLiquid()) {
	            C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
	            packet.y = mc.thePlayer.ticksExisted % 2 == 0 ? packet.y + 0.01 : packet.y - 0.01;
	        }
    	}
    }
    
    @EventTarget
    public void onBB(EventCollideWithBlock e) {
    	if(mode.getValue() == JesusMode.Beta) {
	        if (e.getBlock() instanceof BlockLiquid && this.canJeboos()) {
	            e.setBoundingBox(new AxisAlignedBB((double)e.getPos().getX(), (double)e.getPos().getY(), (double)e.getPos().getZ(), (double)e.getPos().getX() + 1.0, (double)e.getPos().getY() + 1.0, (double)e.getPos().getZ() + 1.0));
	        }
    	}
    }
    
    @EventTarget
    public final void onUpdate(final MotionUpdateEvent event) {
    	this.setSuffix(mode.getValue());
    	if(mode.getValue() == JesusMode.Solid) {
	        if (mc.thePlayer.isInWater() && !mc.thePlayer.isSneaking() && this.shouldJesus()) {
	            mc.thePlayer.motionY = 0.09;
	        }
	        if (!event.isPre()) {
	            return;
	        }
	        if (mc.thePlayer.onGround || mc.thePlayer.isOnLadder()) {
	            this.wasWater = false;
	        }
	        if (mc.thePlayer.motionY > 0.0 && this.wasWater) {
	            if (mc.thePlayer.motionY <= 0.11) {
	                final EntityPlayerSP thePlayer;
	                final EntityPlayerSP player = thePlayer = mc.thePlayer;
	                thePlayer.motionY *= 1.2671;
	            }
	            final EntityPlayerSP thePlayer2;
	            final EntityPlayerSP player2 = thePlayer2 = mc.thePlayer;
	            thePlayer2.motionY += 0.05172;
	        }
	        if (this.isInLiquid() && !mc.thePlayer.isSneaking()) {
	            if (this.ticks < 3) {
	                mc.thePlayer.motionY = 0.13;
	                ++this.ticks;
	                this.wasWater = false;
	            }
	            else {
	                mc.thePlayer.motionY = 0.5;
	                this.ticks = 0;
	                this.wasWater = true;
	            }
	        }
    	}else {
            if (BlockHelper.isInLiquid() && !mc.thePlayer.isSneaking() && !mc.gameSettings.keyBindJump.isPressed()) {
                mc.thePlayer.motionY = 0.05;
                mc.thePlayer.onGround = false;
            }
    	}
    }
    
    private boolean isInLiquid() {
        if (mc.thePlayer == null) {
            return false;
        }
        for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                final BlockPos pos = new BlockPos(x, (int)mc.thePlayer.boundingBox.minY, z);
                final Block block = mc.theWorld.getBlockState(pos).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    return block instanceof BlockLiquid;
                }
            }
        }
        return false;
    }
    
    public double getMotionY(double stage) {
        --stage;
        final double[] motion = { 0.5, 0.484, 0.468, 0.436, 0.404, 0.372, 0.34, 0.308, 0.276, 0.244, 0.212, 0.18, 0.166, 0.166, 0.156, 0.123, 0.135, 0.111, 0.086, 0.098, 0.073, 0.048, 0.06, 0.036, 0.0106, 0.015, 0.004, 0.004, 0.004, 0.004, -0.013, -0.045, -0.077, -0.109 };
        if (stage < motion.length && stage >= 0.0) {
            return motion[(int)stage];
        }
        return -999.0;
    }
    
    public boolean isOnGround(final double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    public void setMotion(final double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    enum JesusMode {
    	Solid,
    	Beta;
    }
}

