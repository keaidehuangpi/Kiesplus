/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.combat;

import java.awt.Color;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;

public class FastBow
extends Module {
    private TimerUtil timer = new TimerUtil();
    private Option<Boolean> faithful = new Option<Boolean>("Faithful", "faithful", true);
    int counter;

    public FastBow() {
        super("FastBow", new String[]{"zoombow", "quickbow"}, ModuleType.Combat);
        this.setColor(new Color(255, 99, 99).getRGB());
        this.addValues(this.faithful);
        this.counter = 0;
    }

    private boolean canConsume() {
        if (this.mc.thePlayer.inventory.getCurrentItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
            return true;
        }
        return false;
    }

    private void killGuardian() {
        if (this.timer.hasReached(1000.0)) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - Double.POSITIVE_INFINITY, this.mc.thePlayer.posZ, false));
            this.timer.reset();
        }
    }

    @EventTarget
    private void onUpdate(EventPreUpdate e) {
        if (this.faithful.getValue().booleanValue()) {
            if (this.mc.thePlayer.onGround && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && this.mc.gameSettings.keyBindUseItem.pressed) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
                int index = 0;
                while (index < 16) {
                    if (!this.mc.thePlayer.isDead) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.09, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, true));
                    }
                    ++index;
                }
                this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            if (this.mc.thePlayer.onGround && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && this.mc.gameSettings.keyBindUseItem.pressed) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
                if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                    this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getCurrentEquippedItem(), 12);
                    ++this.counter;
                    if (this.counter > 0) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.0, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                        this.counter = 0;
                    }
                    int dist = 20;
                    int index = 0;
                    while (index < dist) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                        ++index;
                    }
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    this.mc.playerController.onStoppedUsingItem(this.mc.thePlayer);
                }
            }
        } else if (this.mc.thePlayer.onGround && this.canConsume() && this.mc.gameSettings.keyBindUseItem.pressed) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
            int i = 0;
            while (i < 20) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-9, this.mc.thePlayer.posZ, true));
                ++i;
            }
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        } else {
            this.mc.timer.timerSpeed = 1.0f;
        }
    }

    @EventTarget
    public void onRecieve(EventPacketRecieve event) {
        if (event.getPacket() instanceof S18PacketEntityTeleport) {
            S18PacketEntityTeleport packet = (S18PacketEntityTeleport)event.getPacket();
            if (this.mc.thePlayer != null) {
                packet.yaw = (byte) mc.thePlayer.rotationYaw;
            }
            packet.pitch = (byte) mc.thePlayer.rotationPitch;
        }
    }
}

