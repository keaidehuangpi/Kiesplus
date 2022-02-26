/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.awt.Color;
import java.util.Random;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class AntiObbyTrap
        extends Module {
    public AntiObbyTrap() {
        super("AntiObbyTrap", new String[]{"AntiObbyTrap"}, ModuleType.Player);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }

    @EventTarget
    public void onUpdate(EventPreUpdate e) {
        if (this.mc.theWorld.getBlockState(new BlockPos(new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0, this.mc.thePlayer.posZ))).getBlock() == Block.getBlockById(49)) {
            this.mc.playerController.onPlayerDestroyBlock(new BlockPos(new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ)), EnumFacing.UP);
        }
    }
}

