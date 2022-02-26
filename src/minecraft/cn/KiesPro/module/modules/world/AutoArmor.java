/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.Stopwatch;
import cn.KiesPro.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.DamageSource;

public class AutoArmor
        extends Module {
    private Numbers<Double> delay = new Numbers<Double>("Delay", "delay", 50.0, 0.0, 1000.0, 10.0);
    private List<Integer>[] allArmors = new List[4];
    private boolean equipping;
    public static final Stopwatch INV_STOPWATCH = new Stopwatch();
    private int[] bestArmorSlot;

    public AutoArmor() {
        super("AutoArmor", new String[]{"armorswap", "autoarmour"}, ModuleType.World);
        this.addValues(this.delay);
        this.setColor(new Color(27, 104, 204).getRGB());
    }

    @EventTarget
    private void onPre(EventPreUpdate e) {
        if ((mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
            this.collectBestArmor();
            EntityPlayerSP player = mc.thePlayer;
            int inventoryId = player.inventoryContainer.windowId;
            for (int i = 0; i < 4; ++i) {
                int slot;
                if (this.bestArmorSlot[i] == -1) continue;
                if (!this.equipping) {
                    this.equipping = true;
                    player.sendQueue.addToSendQueueSilent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                }
                int bestSlot = this.bestArmorSlot[i];
                ItemStack oldArmor = mc.thePlayer.inventory.armorItemInSlot(i);
                if (this.checkDelay()) {
                    return;
                }
                if (oldArmor != null) {
                    mc.playerController.windowClick(inventoryId, 8 - i, 0, 4, player);
                    INV_STOPWATCH.reset();
                }
                int n = slot = bestSlot < 9 ? bestSlot + 36 : bestSlot;
                if (this.checkDelay()) {
                    return;
                }
                mc.playerController.windowClick(inventoryId, slot, 0, 1, player);
                INV_STOPWATCH.reset();
                if (!this.equipping) continue;
                player.sendQueue.addToSendQueueSilent(new C0DPacketCloseWindow(inventoryId));
                this.equipping = false;
            }
        }
    }

    private boolean checkDelay() {
        return !INV_STOPWATCH.elapsed(((Double) this.delay.getValue()).longValue());
    }

    private void collectBestArmor() {
        ItemArmor armor;
        ItemStack itemStack;
        int i;
        int[] bestArmorDamageReduction = new int[4];
        this.bestArmorSlot = new int[4];
        Arrays.fill(bestArmorDamageReduction, -1);
        Arrays.fill(this.bestArmorSlot, -1);
        for (i = 0; i < this.bestArmorSlot.length; ++i) {
            int currentProtectionLevel;
            itemStack = mc.thePlayer.inventory.armorItemInSlot(i);
            this.allArmors[i] = new ArrayList<Integer>();
            if (itemStack == null || itemStack.getItem() == null) continue;
            armor = (ItemArmor) itemStack.getItem();
            bestArmorDamageReduction[i] = currentProtectionLevel = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
        }
        for (i = 0; i < 36; ++i) {
            itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null || !(itemStack.getItem() instanceof ItemArmor))
                continue;
            armor = (ItemArmor) itemStack.getItem();
            int armorType = 3 - armor.armorType;
            this.allArmors[armorType].add(i);
            int slotProtectionLevel = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
            if (bestArmorDamageReduction[armorType] >= slotProtectionLevel) continue;
            bestArmorDamageReduction[armorType] = slotProtectionLevel;
            this.bestArmorSlot[armorType] = i;
        }
    }
}