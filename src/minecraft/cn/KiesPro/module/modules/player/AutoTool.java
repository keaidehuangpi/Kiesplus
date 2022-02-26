/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

import java.awt.Color;
import java.util.Random;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.combat.Killaura;

public class AutoTool
        extends Module {
    public static Option<Boolean> swords = new Option("Sword", "Sword", true);

    public AutoTool() {
        super("AutoTool", new String[]{"tools", "tool"}, ModuleType.Player);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.addValues(swords);
    }

    public static float getDamageLevel(ItemStack stack) {
        if (stack.getItem() instanceof ItemSword) {
            ItemSword sword = (ItemSword) stack.getItem();
            float sharpness = (float) EnchantmentHelper.getEnchantmentLevel((int) Enchantment.sharpness.effectId, (ItemStack) stack) * 1.25f;
            float fireAspect = (float) EnchantmentHelper.getEnchantmentLevel((int) Enchantment.fireAspect.effectId, (ItemStack) stack) * 1.5f;
            return sword.getDamageVsEntity() + sharpness + fireAspect;
        }
        return 0.0f;
    }

    @EventTarget
    public void onUpdate(EventPreUpdate event) {
        if (ModuleManager.getModuleByName("Aura").isEnabled() && Killaura.curTarget != null && swords.getValue().booleanValue()) {
            ItemStack itemStack;
            float damage = 1.0f;
            int bestSwordSlot = -1;
            for (int i = 0; i < 9; ++i) {
                float damageLevel;
                itemStack = mc.thePlayer.inventory.getStackInSlot(i);
                if (itemStack == null || !(itemStack.getItem() instanceof ItemSword) || !((damageLevel = getDamageLevel((ItemStack) itemStack)) > damage))
                    continue;
                damage = damageLevel;
                bestSwordSlot = i;
            }
            if (bestSwordSlot != -1) {
                mc.thePlayer.inventory.currentItem = bestSwordSlot;
            }
        }
    }

    @EventTarget
    public void handle(EventPacketSend event) {
        if (!(event.getPacket() instanceof C07PacketPlayerDigging) || event.getType() != 0) {
            return;
        }
        C07PacketPlayerDigging packet = (C07PacketPlayerDigging) event.getPacket();
        if (packet.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            AutoTool.autotool(packet.getPosition());
        }
    }

    private static void autotool(BlockPos position) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(position).getBlock();
        int itemIndex = AutoTool.getStrongestItem(block);
        if (itemIndex < 0) {
            return;
        }
        float itemStrength = AutoTool.getStrengthAgainstBlock(block, Minecraft.getMinecraft().thePlayer.inventory.mainInventory[itemIndex]);
        if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null && AutoTool.getStrengthAgainstBlock(block, Minecraft.getMinecraft().thePlayer.getHeldItem()) >= itemStrength) {
            return;
        }
        Minecraft.getMinecraft().thePlayer.inventory.currentItem = itemIndex;
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(itemIndex));
    }

    private static int getStrongestItem(Block block) {
        float strength = Float.NEGATIVE_INFINITY;
        int strongest = -1;
        int i = 0;
        while (i < 9) {
            float itemStrength;
            ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() != null && (itemStrength = AutoTool.getStrengthAgainstBlock(block, itemStack)) > strength && itemStrength != 1.0f) {
                strongest = i;
                strength = itemStrength;
            }
            ++i;
        }
        return strongest;
    }

    public static float getStrengthAgainstBlock(Block block, ItemStack item) {
        float strength = item.getStrVsBlock(block);
        if (!EnchantmentHelper.getEnchantments(item).containsKey(Enchantment.efficiency.effectId) || strength == 1.0f) {
            return strength;
        }
        int enchantLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, item);
        return strength + (float) (enchantLevel * enchantLevel + 1);
    }
}

