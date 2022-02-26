/*
 * Decompiled with CFR 0_132.
 * 
 * SRC By Autumn, Base > ETB ×ª»»
 */
package cn.KiesPro.module.modules.player;

import com.google.common.collect.Multimap;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.events.world.EventTick;
import cn.KiesPro.api.events.world.MotionUpdateEvent;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.combat.Killaura;
import cn.KiesPro.utils.Stopwatch;
import cn.KiesPro.utils.TimerUtil;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InvCleaner
extends Module {
	Stopwatch timer = new Stopwatch();
	ArrayList<Integer> whitelistedItems = new ArrayList();
	private Numbers<Double> maxblocks = new Numbers<Double>("Maxblocks", "Maxblocks", 128.0, 0.0, 256.0, 8.0);
	private Numbers<Double> delay = new Numbers<Double>("Delay", "delay", 200.0, 0.0, 500.0, 10.0);
	private Option<Boolean> openinv = new Option<Boolean>("OpenInv", "openinv", false);
	public Stopwatch droptimer = new Stopwatch();
	public static boolean complete;
	private int[] itemHelmet = new int[] { 298, 302, 306, 310, 314 };
	private int[] itemChestplate = new int[] { 299, 303, 307, 311, 315 };
	private int[] itemLeggings = new int[] { 300, 304, 308, 312, 316 };
	private int[] itemBoots = new int[] { 301, 305, 309, 313, 317 };
	public static int weaponSlot = 36;
	public static int pickaxeSlot = 37;
	public static int axeSlot = 38;
	public static int shovelSlot = 39;
	private static List<Block> blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water,
			Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane,
			Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice,
			Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch,
			Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore,
			Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore,
			Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate,
			Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever,
			Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower,
			Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower,
			Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock,
			Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall,
			Blocks.oak_fence);

    public InvCleaner() {
        super("InvManager", new String[]{"InvManager", "InvCleaner"}, ModuleType.World);
		this.addValues(this.maxblocks, delay, openinv);
    }
    
    @EventTarget
	public void onPre(MotionUpdateEvent event) {
		if (this.openinv.getValue().booleanValue() && !(mc.currentScreen instanceof GuiInventory)) {
			return;
		}
        if (mc.thePlayer.openContainer instanceof ContainerChest && mc.currentScreen instanceof GuiContainer) {
            return;
        }
		if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory
				|| mc.currentScreen instanceof GuiChat) {
			if (this.timer.elapsed(this.delay.getValue().intValue()) && weaponSlot >= 36) {
				if (!mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
					this.getBestWeapon(weaponSlot);
				} else if (!this.isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) {
					this.getBestWeapon(weaponSlot);
				}
			}
			if (this.timer.elapsed(this.delay.getValue().intValue()) && pickaxeSlot >= 36) {
				this.getBestPickaxe(pickaxeSlot);
			}
			if (this.timer.elapsed(this.delay.getValue().intValue()) && shovelSlot >= 36) {
				this.getBestShovel(shovelSlot);
			}
			if (this.timer.elapsed(this.delay.getValue().intValue()) && axeSlot >= 36) {
				this.getBestAxe(axeSlot);
			}
			if (this.timer.elapsed(this.delay.getValue().intValue())) {
				for (int i = 9; i < 45; ++i) {
					if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
							|| !this.shouldDrop(mc.thePlayer.inventoryContainer.getSlot(i).getStack(), i))
						continue;
					this.drop(i);
					this.timer.reset();
					if (this.delay.getValue() > 0.0)
						break;
				}
			}
			if (this.timer.elapsed(this.delay.getValue().intValue()) && Killaura.curTarget == null) {
				changeArmor();
			}
		}
	}

	private void changeArmor() {
		String[] armorType = new String[] { "boots", "leggings", "chestplate", "helmet" };
		for (int i = 0; i < 4; ++i) {
			int bestArmor = this.getBestArmor(armorType[i]);
			if (bestArmor != -1) {
				putOnItem(8 - i, bestArmor);
				timer.reset();
			}
		}
	}

	private void putOnItem(int armorSlot, int slot) {
		if (armorSlot != -1 && mc.thePlayer.inventoryContainer.getSlot(armorSlot).getStack() != null) {
			this.dropOldArmor(armorSlot);
		}
		this.inventoryAction(slot);
	}

	private int getBestArmor(String armorType) {
		return this.getBestInInventory(armorType);
	}

	private void dropOldArmor(int slot) {
		mc.thePlayer.inventoryContainer.slotClick(slot, 0, 4, mc.thePlayer);
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
	}

	private void inventoryAction(int click) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, click, 1, 1, mc.thePlayer);
	}

	private int[] getIdsByName(String armorName) {
		switch (armorName.hashCode()) {
		case -1220934547: {
			if (!armorName.equals("helmet"))
				break;
			return this.itemHelmet;
		}
		case 93922241: {
			if (!armorName.equals("boots"))
				break;
			return this.itemBoots;
		}
		case 1069952181: {
			if (!armorName.equals("chestplate"))
				break;
			return this.itemChestplate;
		}
		case 1735676010: {
			if (!armorName.equals("leggings"))
				break;
			return this.itemLeggings;
		}
		}
		return new int[0];
	}

	private List findArmor(String armorName) {
		int[] itemIds = this.getIdsByName(armorName);
		ArrayList<Integer> availableSlots = new ArrayList<Integer>();
		for (int slots = 9; slots < mc.thePlayer.inventoryContainer.getInventory().size(); ++slots) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(slots).getStack();
			if (itemStack == null)
				continue;
			int itemId = Item.getIdFromItem(itemStack.getItem());
			int[] array = itemIds;
			int length = itemIds.length;
			for (int i = 0; i < length; ++i) {
				int ids = array[i];
				if (itemId != ids)
					continue;
				availableSlots.add(slots);
			}
		}
		return availableSlots;
	}

	private int getBestInInventory(String armorName) {
		int slot = -1;
		Iterator var4 = this.findArmor(armorName).iterator();
		while (var4.hasNext()) {
			int slots = (Integer) var4.next();
			if (slot == -1) {
				slot = slots;
			}
			if (mc.thePlayer.inventoryContainer.getSlot(slots) == null
					|| !(mc.thePlayer.inventoryContainer.getSlot(slots).getStack().getItem() instanceof ItemArmor)
					|| this.getValence(mc.thePlayer.inventoryContainer.getSlot(slots).getStack()) <= this
							.getValence(mc.thePlayer.inventoryContainer.getSlot(slot).getStack()))
				continue;
			slot = slots;
		}
		return slot;
	}

	private double getProtectionValue(ItemStack stack) {
		return !(stack.getItem() instanceof ItemArmor) ? 0.0
				: (double) ((ItemArmor) stack.getItem()).damageReduceAmount
						+ (double) ((100 - ((ItemArmor) stack.getItem()).damageReduceAmount * 4)
								* EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4)
								* 0.0075
						+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)
						+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack)
						+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)
						+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId,
								stack)
						+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.respiration.effectId, stack)
						+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId,
								stack);
	}

	private int getValence(ItemStack itemStack) {
		int valence = 0;
		if (itemStack == null) {
			return 0;
		}
		if (itemStack.getItem() instanceof ItemArmor) {
			valence += ((ItemArmor) itemStack.getItem()).damageReduceAmount;
		}
		if (itemStack != null && itemStack.hasTagCompound()) {
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(0).getDouble("lvl");
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(1).getDouble("lvl");
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(2).getDouble("lvl");
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(3).getDouble("lvl");
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(4).getDouble("lvl");
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(5).getDouble("lvl");
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(6).getDouble("lvl");
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(7).getDouble("lvl");
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(8).getDouble("lvl");
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(9).getDouble("lvl");
			valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(34).getDouble("lvl");
		}
		valence += (int) this.getProtectionValue(itemStack);
		return valence += itemStack.getMaxDamage() - itemStack.getItemDamage();
	}

	private void autosword() {
		int best = -1;
		float swordDamage = 0.0f;
		for (int i = 9; i < 45; ++i) {
			ItemStack is;
			float swordD;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| !((is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemSword)
					|| (swordD = this.getSharpnessLevel(is)) <= swordDamage)
				continue;
			swordDamage = swordD;
			best = i;
		}
		ItemStack current = mc.thePlayer.inventoryContainer.getSlot(36).getStack();
		if (!(best == -1 || current != null && current.getItem() instanceof ItemSword
				&& swordDamage <= this.getSharpnessLevel(current))) {
			mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, best, 0, 2, mc.thePlayer);
			timer.reset();
		}
	}

	private float getSharpnessLevel(ItemStack stack) {
		float damage = ((ItemSword) stack.getItem()).getDamageVsEntity();
		damage += (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
		return damage += (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
	}

	public void shiftClick(int slot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
	}

	public void swap(int slot1, int hotbarSlot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
	}

	public void drop(int slot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
	}

	public boolean isBestWeapon(ItemStack stack) {
		float damage = this.getDamage(stack);
		for (int i = 9; i < 45; ++i) {
			ItemStack is;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| this.getDamage(is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()) <= damage
					|| !(is.getItem() instanceof ItemSword))
				continue;
			return false;
		}
		if (stack.getItem() instanceof ItemSword) {
			return true;
		}
		return false;
	}

	public void getBestWeapon(int slot) {
		for (int i = 9; i < 45; ++i) {
			ItemStack is;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| !this.isBestWeapon(is = mc.thePlayer.inventoryContainer.getSlot(i).getStack())
					|| this.getDamage(is) <= 0.0f || !(is.getItem() instanceof ItemSword))
				continue;
			this.swap(i, slot - 36);
			this.timer.reset();
			break;
		}
	}

	private float getDamage(ItemStack stack) {
		float damage = 0;
		Item item = stack.getItem();
		if (item instanceof ItemTool) {
			damage += getSpeed(stack);
		}
		if (item instanceof ItemSword) {
			damage += getAttackDamage(stack);
		} else {
			damage += 1;
		}
		return damage;
	}

	private float getAttackDamage(final ItemStack itemStack) {
		float damage = ((ItemSword) itemStack.getItem()).getDamageVsEntity();
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
		return damage;
	}

	private float getSpeed(ItemStack stack) {
		return ((ItemTool) stack.getItem()).getToolMaterial().getEfficiencyOnProperMaterial();
	}

	public boolean shouldDrop(ItemStack stack, int slot) {
		if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
			return false;
		}
		if (stack.getDisplayName().toLowerCase().contains("k||")) {
			return false;
		}
		if (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemAppleGold) {
			return false;
		}
		if (stack.getItem() instanceof ItemBlock && (double) this.getBlockCount() > this.maxblocks.getValue()
				&& !(stack.getItem() instanceof ItemSkull)) {
			return true;
		}
		if (stack.getItem() instanceof ItemBlock && (double) this.getBlockCount() <= this.maxblocks.getValue()
				|| stack.getItem() instanceof ItemSkull) {
			return false;
		}
		if (slot == weaponSlot && this.isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())
				|| slot == pickaxeSlot
						&& this.isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())
						&& pickaxeSlot >= 0
				|| slot == axeSlot && this.isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())
						&& axeSlot >= 0
				|| slot == shovelSlot
						&& this.isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())
						&& shovelSlot >= 0) {
			return false;
		}
		if (stack.getItem() instanceof ItemArmor) {
			for (int type = 1; type < 5; ++type) {
				if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack() && isBestArmor(mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack(), type)
						|| !isBestArmor(stack, type))
					continue;
				return false;
			}
		}
		if (stack.getItem() instanceof ItemPotion && this.isBadPotion(stack)) {
			return true;
		}
		if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool
				|| stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor) {
			return true;
		}
		if (stack.getItem().getUnlocalizedName().contains("tnt")
				|| stack.getItem().getUnlocalizedName().contains("stick")
				|| stack.getItem().getUnlocalizedName().contains("egg")
				|| stack.getItem().getUnlocalizedName().contains("string")
				|| stack.getItem().getUnlocalizedName().contains("cake")
				|| stack.getItem().getUnlocalizedName().contains("mushroom")
				|| stack.getItem().getUnlocalizedName().contains("flint")
				|| stack.getItem().getUnlocalizedName().contains("compass")
				|| stack.getItem().getUnlocalizedName().contains("dyePowder")
				|| stack.getItem().getUnlocalizedName().contains("feather")
				|| stack.getItem().getUnlocalizedName().contains("bucket")
				|| stack.getItem().getUnlocalizedName().contains("chest")
						&& !stack.getDisplayName().toLowerCase().contains("collect")
				|| stack.getItem().getUnlocalizedName().contains("snow")
				|| stack.getItem().getUnlocalizedName().contains("fish")
				|| stack.getItem().getUnlocalizedName().contains("enchant")
				|| stack.getItem().getUnlocalizedName().contains("exp")
				|| stack.getItem().getUnlocalizedName().contains("shears")
				|| stack.getItem().getUnlocalizedName().contains("anvil")
				|| stack.getItem().getUnlocalizedName().contains("torch")
				|| stack.getItem().getUnlocalizedName().contains("seeds")
				|| stack.getItem().getUnlocalizedName().contains("leather")
				|| stack.getItem().getUnlocalizedName().contains("reeds")
				|| stack.getItem().getUnlocalizedName().contains("skull")
				|| stack.getItem().getUnlocalizedName().contains("record")
				|| stack.getItem().getUnlocalizedName().contains("snowball")
				|| stack.getItem() instanceof ItemGlassBottle
				|| stack.getItem().getUnlocalizedName().contains("piston")) {
			return true;
		}
		return false;
	}

	public boolean isBestArmor(ItemStack stack, int type) {
		float prot = getProtection(stack);
		String strType = "";
		if (type == 1) {
			strType = "helmet";
		} else if (type == 2) {
			strType = "chestplate";
		} else if (type == 3) {
			strType = "leggings";
		} else if (type == 4) {
			strType = "boots";
		}
		if (!stack.getUnlocalizedName().contains(strType)) {
			return false;
		}
		for (int i = 5; i < 45; ++i) {
			ItemStack is;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| getProtection(is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()) <= prot
					|| !is.getUnlocalizedName().contains(strType))
				continue;
			return false;
		}
		return true;
	}

	public static float getProtection(ItemStack stack) {
		float prot = 0.0f;
		if (stack.getItem() instanceof ItemArmor) {
			ItemArmor armor = (ItemArmor) stack.getItem();
			prot = (float) ((double) prot
					+ ((double) armor.damageReduceAmount + (double) ((100 - armor.damageReduceAmount)
							* EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075));
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)
							/ 100.0);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)
							/ 100.0);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack)
							/ 100.0);
		}
		return prot;
	}

	public ArrayList<Integer> getWhitelistedItem() {
		return this.whitelistedItems;
	}

	private int getBlockCount() {
		int blockCount = 0;
		for (int i = 0; i < 45; ++i) {
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
				continue;
			ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			Item item = is.getItem();
			if (!(is.getItem() instanceof ItemBlock) || blacklistedBlocks.contains(((ItemBlock) item).getBlock()))
				continue;
			blockCount += is.stackSize;
		}
		return blockCount;
	}

	private void getBestPickaxe(int slot) {
		for (int i = 9; i < 45; ++i) {
			ItemStack is;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| !this.isBestPickaxe(is = mc.thePlayer.inventoryContainer.getSlot(i).getStack())
					|| pickaxeSlot == i || this.isBestWeapon(is))
				continue;
			if (!mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
				this.swap(i, pickaxeSlot - 36);
				this.timer.reset();
				if (this.delay.getValue().intValue() <= 0)
					continue;
				return;
			}
			if (this.isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack()))
				continue;
			this.swap(i, pickaxeSlot - 36);
			this.timer.reset();
			if (this.delay.getValue().intValue() <= 0)
				continue;
			return;
		}
	}

	private void getBestShovel(int slot) {
		for (int i = 9; i < 45; ++i) {
			ItemStack is;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| !this.isBestShovel(is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || shovelSlot == i
					|| this.isBestWeapon(is))
				continue;
			if (!mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
				this.swap(i, shovelSlot - 36);
				this.timer.reset();
				if (this.delay.getValue().intValue() <= 0)
					continue;
				return;
			}
			if (this.isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack()))
				continue;
			this.swap(i, shovelSlot - 36);
			this.timer.reset();
			if (this.delay.getValue().intValue() <= 0)
				continue;
			return;
		}
	}

	private void getBestAxe(int slot) {
		for (int i = 9; i < 45; ++i) {
			ItemStack is;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| !this.isBestAxe(is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || axeSlot == i
					|| this.isBestWeapon(is))
				continue;
			if (!mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
				this.swap(i, axeSlot - 36);
				this.timer.reset();
				if (this.delay.getValue().intValue() <= 0)
					continue;
				return;
			}
			if (this.isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack()))
				continue;
			this.swap(i, axeSlot - 36);
			this.timer.reset();
			if (this.delay.getValue().intValue() <= 0)
				continue;
			return;
		}
	}

	private boolean isBestPickaxe(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemPickaxe)) {
			return false;
		}
		float value = this.getToolEffect(stack);
		for (int i = 9; i < 45; ++i) {
			ItemStack is;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| this.getToolEffect(is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()) <= value
					|| !(is.getItem() instanceof ItemPickaxe))
				continue;
			return false;
		}
		return true;
	}

	private boolean isBestShovel(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemSpade)) {
			return false;
		}
		float value = this.getToolEffect(stack);
		for (int i = 9; i < 45; ++i) {
			ItemStack is;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| this.getToolEffect(is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()) <= value
					|| !(is.getItem() instanceof ItemSpade))
				continue;
			return false;
		}
		return true;
	}

	private boolean isBestAxe(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemAxe)) {
			return false;
		}
		float value = this.getToolEffect(stack);
		for (int i = 9; i < 45; ++i) {
			ItemStack is;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| this.getToolEffect(is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()) <= value
					|| !(is.getItem() instanceof ItemAxe) || this.isBestWeapon(stack))
				continue;
			return false;
		}
		return true;
	}

	private float getToolEffect(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemTool)) {
			return 0.0f;
		}
		String name = item.getUnlocalizedName();
		ItemTool tool = (ItemTool) item;
		float value = 1.0f;
		if (item instanceof ItemPickaxe) {
			value = tool.getStrVsBlock(stack, Blocks.stone);
			if (name.toLowerCase().contains("gold")) {
				value -= 5.0f;
			}
		} else if (item instanceof ItemSpade) {
			value = tool.getStrVsBlock(stack, Blocks.dirt);
			if (name.toLowerCase().contains("gold")) {
				value -= 5.0f;
			}
		} else if (item instanceof ItemAxe) {
			value = tool.getStrVsBlock(stack, Blocks.log);
			if (name.toLowerCase().contains("gold")) {
				value -= 5.0f;
			}
		} else {
			return 1.0f;
		}
		value = (float) ((double) value
				+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
		value = (float) ((double) value
				+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
		return value;
	}

	private boolean isBadPotion(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ItemPotion) {
			ItemPotion potion = (ItemPotion) stack.getItem();
			if (potion.getEffects(stack) == null) {
				return true;
			}
			for (PotionEffect o : potion.getEffects(stack)) {
				PotionEffect effect = o;
				if (effect.getPotionID() != Potion.poison.getId() && effect.getPotionID() != Potion.harm.getId()
						&& effect.getPotionID() != Potion.moveSlowdown.getId()
						&& effect.getPotionID() != Potion.weakness.getId())
					continue;
				return true;
			}
		}
		return false;
	}

	boolean invContainsType(int type) {
		for (int i = 9; i < 45; ++i) {
			Item item;
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
					|| !((item = (mc.thePlayer.inventoryContainer.getSlot(i).getStack())
							.getItem()) instanceof ItemArmor))
				continue;
			ItemArmor armor = (ItemArmor) item;
			if (type != armor.armorType)
				continue;
			return true;
		}
		return false;
	}
}