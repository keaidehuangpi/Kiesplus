/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.world;

import java.awt.Color;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.misc.EventCollideWithBlock;
import cn.KiesPro.api.events.world.EventMove;
import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.api.events.world.EventPostUpdate;
import cn.KiesPro.api.events.world.EventTick;
import cn.KiesPro.api.events.world.PushEvent;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.PlayerUtil;
import cn.KiesPro.utils.math.RotationUtil;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class Phase
extends Module {
    private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])PhaseMode.values(), (Enum)PhaseMode.NewNCP);
    private boolean mineplexClip;
    private TickTimer mineplexTickTimer = new TickTimer();
    public Phase() {
        super("Phase", new String[]{"noclip"}, ModuleType.World);
        this.setColor(new Color(255, 166, 25).getRGB());
        this.addValues(this.mode);
    }
    
    @EventTarget
    private void onBlockCollision(EventCollideWithBlock e) {
        if (e.getBoundingBox() != null && e.getBoundingBox().maxY > this.mc.thePlayer.boundingBox.minY && this.mc.thePlayer.isSneaking() && this.mode.getValue() != PhaseMode.OldNCP) {
            e.setBoundingBox(null);
        }
        if (e.getBoundingBox() != null && e.getBoundingBox().maxY > this.mc.thePlayer.boundingBox.minY && this.mode.getValue() == PhaseMode.OldNCP) {
            e.setBoundingBox(null);
        }
    }

    @EventTarget
    public void onPush(PushEvent event) {
    	event.setCancelled(true);
    }
    
    @EventTarget
    private void onMove(EventMove e) {
        if (BlockHelper.insideBlock() && this.mc.thePlayer.isSneaking() && this.mode.getValue() == PhaseMode.SkipClip) {
            this.mc.thePlayer.boundingBox.offsetAndUpdate((double)this.mc.thePlayer.movementInput.moveForward * 3.6 * Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)) + (double)this.mc.thePlayer.movementInput.moveStrafe * 3.6 * Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)), 0.0, (double)this.mc.thePlayer.movementInput.moveForward * 3.6 * Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)) - (double)this.mc.thePlayer.movementInput.moveStrafe * 3.6 * Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)));
        }
        if(mode.getValue() == PhaseMode.Mineplex) {
            if (mc.thePlayer.isCollidedHorizontally)
                mineplexClip = true;
            if (!mineplexClip)
                return;

            mineplexTickTimer.update();

            e.setX(0);
            e.setZ(0);

            if (mineplexTickTimer.hasTimePassed(3)) {
                mineplexTickTimer.reset();
                mineplexClip = false;
            } else if (mineplexTickTimer.hasTimePassed(1)) {
                final double offset = mineplexTickTimer.hasTimePassed(2) ? 1.6D : 0.06D;
                final double direction = PlayerUtil.getDirection();

                mc.thePlayer.setPosition(mc.thePlayer.posX + (-Math.sin(direction) * offset), mc.thePlayer.posY, mc.thePlayer.posZ + (Math.cos(direction) * offset));
            }
        }
    }

    @EventTarget
    private void onUpdate(EventPostUpdate e) {
        if (BlockHelper.insideBlock()) {
            if (this.mode.getValue() == PhaseMode.NewNCP && this.mc.thePlayer.isSneaking()) {
                this.mc.thePlayer.boundingBox.offsetAndUpdate(0.0524 * Math.cos(Math.toRadians(RotationUtil.yaw() + 90.0f)), 0.0, 0.0524 * Math.sin(Math.toRadians(RotationUtil.yaw() + 90.0f)));
            }
            if (this.mode.getValue() == PhaseMode.OldNCP && this.mc.thePlayer.isCollidedVertically) {
                double x = (double)(- MathHelper.sin(this.mc.thePlayer.getDirection())) * 0.2;
                double z = (double)MathHelper.cos(this.mc.thePlayer.getDirection()) * 0.2;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z, false));
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, Double.MIN_VALUE, this.mc.thePlayer.posZ + z, true));
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z);
            }
            if (this.mc.thePlayer.onGround && this.mode.getValue() == PhaseMode.NewNCP) {
                this.mc.thePlayer.jump();
            }
        }
    }

    static enum PhaseMode {
        NewNCP,
        OldNCP,
        SkipClip,
        Mineplex;
    }
    
    class TickTimer {

        private int tick;

        public void update() {
            tick++;
        }

        public void reset() {
            tick = 0;
        }

        public boolean hasTimePassed(final int ticks) {
            return tick >= ticks;
        }
    }

}

