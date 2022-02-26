/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.combat;

import com.google.common.collect.Multimap;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.InventoryUtils;
import cn.KiesPro.utils.TimeHelper;
import cn.KiesPro.utils.TimerUtil;

import java.awt.Color;
import java.util.Collection;

import net.minecraft.network.play.client.C09PacketHeldItemChange;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSword
        extends Module {
    private ItemStack bestSword;
    private ItemStack prevBestSword;
    private boolean shouldSwitch = false;
    public TimerUtil timer = new TimerUtil();
    TimeHelper time = new TimeHelper();
    public Mode<Enum> mode = new Mode("mode", "mode", (Enum[]) SwordMode.values(), (Enum) SwordMode.Basic);
    public static Option<Boolean> swap = new Option("swap", "swap", true);

    public AutoSword() {
        super("AutoWeapon", new String[]{"autosword", "autoweapon"}, ModuleType.Combat);
        this.setColor(new Color(208, 30, 142).getRGB());
        this.addValues(mode, swap);

    }

    static enum SwordMode {
        Basic,
        Debug;
    }

    @EventTarget
    public void onUpdate(EventPreUpdate event) {
        if (mode.getValue() == SwordMode.Basic) {
            block5:
            {
                block6:
                {
                    if (!(mc.currentScreen instanceof GuiInventory) && mc.currentScreen != null) break block5;
                    if (mc.thePlayer.getCurrentEquippedItem() == null || !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))
                        break block6;
                    if (!this.timer.delay(100.0f)) break block5;
                    int i2 = 0;
                    while (i2 < 45) {
                        Item item;
                        float itemDamage;
                        float currentDamage;
                        if (mc.thePlayer.inventoryContainer.getSlot(i2).getHasStack() && (item = mc.thePlayer.inventoryContainer.getSlot(i2).getStack().getItem()) instanceof ItemSword && (itemDamage = this.getAttackDamage(mc.thePlayer.inventoryContainer.getSlot(i2).getStack())) > (currentDamage = this.getAttackDamage(mc.thePlayer.getCurrentEquippedItem()))) {
                            this.swap(i2, mc.thePlayer.inventory.currentItem);
                            this.timer.reset();
                            break block5;
                        }
                        ++i2;
                    }
                    break block5;
                }
                if (swap.getValue().booleanValue()) {
                    int i3 = 36;
                    while (i3 < 45) {
                        Item item;
                        if (mc.thePlayer.inventoryContainer.getSlot(i3).getHasStack() && (item = mc.thePlayer.inventoryContainer.getSlot(i3).getStack().getItem()) instanceof ItemSword && Killaura.curTarget != null) {
                            mc.thePlayer.inventory.currentItem = i3 - 36;
                            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                            mc.playerController.updateController();
                            break;
                        }
                        ++i3;
                    }
                }
            }
        } else {
            if (!this.time.isDelayComplete(100L) || this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiInventory)) {
                return;
            }
            int best = -1;
            float swordDamage = 0.0f;
            for (int i = 9; i < 45; ++i) {
                ItemStack is;
                float swordD;
                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemSword) || (swordD = this.getSharpnessLevel(is)) <= swordDamage)
                    continue;
                swordDamage = swordD;
                best = i;
            }
            ItemStack current = mc.thePlayer.inventoryContainer.getSlot(36).getStack();
            if (!(best == -1 || current != null && current.getItem() instanceof ItemSword && swordDamage <= this.getSharpnessLevel(current))) {
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, best, 0, 2, mc.thePlayer);
                this.time.reset();
            }
        }
    }

    private float getSharpnessLevel(ItemStack stack) {
        float damage = ((ItemSword) stack.getItem()).getDamageVsEntity();
        damage += (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
        return damage += (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }

    protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    private float getAttackDamage(ItemStack stack) {
        return !(stack.getItem() instanceof ItemSword) ? 0.0f : (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + ((ItemSword) stack.getItem()).getDamageVsEntity() + (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }
}