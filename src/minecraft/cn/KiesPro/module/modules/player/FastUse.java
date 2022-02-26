/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.player;

import java.awt.Color;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastUse
extends Module {
	private boolean usedTimer = false;
    public FastUse() {
        super("FastUse", new String[]{"fasteat", "fuse"}, ModuleType.Player);
    }

    private boolean canUseItem(Item item) {
    	boolean result = !((item instanceof ItemSword) || (item instanceof ItemBow));
        return result;
    }
    
    @EventTarget
    private void onUpdate(EventPreUpdate e) {
        if (usedTimer) {
            mc.timer.timerSpeed = 1F;
            usedTimer = false;
        }
		if(mc.thePlayer.getItemInUseDuration() >= 12F && canUseItem(mc.thePlayer.getItemInUse().getItem())) {
		mc.timer.timerSpeed = 1.22F;
		usedTimer = true;
		}
    }
    
    @Override
    public void onDisable() {
        if (usedTimer) {
            mc.timer.timerSpeed = 1F;
            usedTimer = false;
        }
        mc.timer.timerSpeed = 1;
    }
}

