package cn.KiesPro.module.modules.render;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;
import optifine.Config;
import org.lwjgl.opengl.GL11;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.rendering.EventRender3D;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.management.FriendManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.combat.AntiBot;
import cn.KiesPro.module.modules.player.Teams;
import cn.KiesPro.utils.render.RenderUtil;

public class Nametags
extends Module {
	private Option<Boolean> armor = new Option<Boolean>("Armor", "armor", true);
	private Option<Boolean> dura = new Option<Boolean>("Durability", "durability", true);
	private Option<Boolean> distance = new Option<Boolean>("Distance", "distance", false);
	private Numbers<Double> scale = new Numbers<Double>("Scale", "scale", 1.0, 1.0, 5.0, 0.1);
	public ArrayList<Entity> entities = new ArrayList();
	public Option<Boolean> invis = new Option<Boolean>("Invisibles", "invisibles", false);
	public int i = 0;

	public Nametags() {
		super("NameTags", new String[] { "tags" }, ModuleType.Render);
		setColor(new Color(29, 187, 102).getRGB());
		addValues(armor, dura, invis, scale,distance);
	}

	@EventTarget
	private void onRender(EventRender3D render) {
		double renderPosX = mc.getRenderManager().viewerPosX;
		double renderPosY = mc.getRenderManager().viewerPosY;
		double renderPosZ = mc.getRenderManager().viewerPosZ;
		for (Object o : mc.theWorld.playerEntities) {
			EntityPlayer p = (EntityPlayer) o;
			if (p != mc.thePlayer) {
				double pX = p.prevPosX + (p.posX - p.prevPosX) * render.getPartialTicks() - renderPosX;
				double pY = p.prevPosY + (p.posY - p.prevPosY) * render.getPartialTicks() - renderPosY;
				double pZ = p.prevPosZ + (p.posZ - p.prevPosZ) * render.getPartialTicks() - renderPosZ;
				renderNameTag(p, String.valueOf(p.getDisplayName()), pX, pY, pZ);
			}
		}

	}

	public void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
		FontRenderer fr = mc.fontRendererObj;
		float var10 = mc.thePlayer.getDistanceToEntity(entity) / 6.0F;
		if (var10 < 0.8F) {
			var10 = 0.8F;
		}
		pY += entity.isSneaking() ? 0.5D : 0.7D;
		float var11 = (float) (var10 * scale.getValue().doubleValue());
		var11 /= 100.0F;
		tag = entity.getName();
		String var12 = "";
		AntiBot ab = new AntiBot();
		if (ab.isEntityBot(entity)) {
			var12 = "\u00a79";
		} else {
			var12 = "";
		}
		String var13 = "";
		if (Teams.isOnSameTeam(entity)) {
			var13 = "\u00a7b";
		} else {
			var13 = "";
		}
		
        BigDecimal DT = new BigDecimal((int)mc.thePlayer.getDistanceToEntity(entity));
		DT = DT.setScale(1, RoundingMode.HALF_UP);
		int Dis = DT.intValue();
		String var14;
    if (distance.getValue().booleanValue()) {
      var14 = /*"��a[��f" + Dis + "m��a] " + "��f" +*/
          var13 + var12 + tag + " health: " + (int) entity.getHealth() +"   |Distance: "+(int)(entity.getDistanceToEntity(mc.thePlayer))+"m";
			}else {var14 = /*"��a[��f" + Dis + "m��a] " + "��f" +*/
			var13 + var12 + tag + " health: " + (int) entity.getHealth();

	}
		String var15 = "\u00a77HP:" + (int) entity.getHealth();
		GL11.glPushMatrix();
		GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-var11, -var11, var11);
		setGLCap(2896, false);
		setGLCap(2929, false);
		int var16 = mc.fontRendererObj.getStringWidth(var14) / 2;
		setGLCap(3042, true);
		GL11.glBlendFunc(770, 771);
		drawBorderedRectNameTag((float) (-var16 - 8), (float) (-(mc.fontRendererObj.FONT_HEIGHT + 12)),
				(float) (var16 + 1), -5.0F, 1.0F, reAlpha(Color.BLACK.getRGB(), 0.3F),
				reAlpha(Color.BLACK.getRGB(), 0.6F));
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		fr.drawStringWithShadow(var14, -var16 - 4, -(mc.fontRendererObj.FONT_HEIGHT + 8), -1);
		//fr.drawString(var15, -var16 - 4, -(mc.fontRendererObj.FONT_HEIGHT - 1), -1);
		int var17 = (new Color(188, 0, 0)).getRGB();
		if (entity.getHealth() > 20.0F) {
			var17 = -65292;
		}

		float var18 = (float) Math.ceil((double) (entity.getHealth() + entity.getAbsorptionAmount()));
		float var19 = var18 / (entity.getMaxHealth() + entity.getAbsorptionAmount());
		//RenderUtil.drawRect((float) var16 + var19 * 40.0F - 40.0F + 8.0F, 2.0F, (float) (-var16) - 8F, 0.9F, new Color(100, 100, 100, 170).getRGB()); //Color.RED.getRGB()
		GL11.glPushMatrix();
		if(armor.getValue().booleanValue()) {
			int var20 = 0;
			ItemStack[] var24 = entity.inventory.armorInventory;
			int var23 = entity.inventory.armorInventory.length;
	
			ItemStack var21;
			for (int var22 = 0; var22 < var23; ++var22) {
				var21 = var24[var22];
				if (var21 != null) {
					var20 -= 11;
				}
			}
	
			if (entity.getHeldItem() != null) {
				var20 -= 8;
				var21 = entity.getHeldItem().copy();
				if (((ItemStack) var21).hasEffect() && (((ItemStack) var21).getItem() instanceof ItemTool
						|| ((ItemStack) var21).getItem() instanceof ItemArmor)) {
					((ItemStack) var21).stackSize = 1;
				}
	
				renderItemStack((ItemStack) var21, var20, -40);
				var20 += 20;
			}
	
			ItemStack[] var25 = entity.inventory.armorInventory;
			int var28 = entity.inventory.armorInventory.length;
	
			for (var23 = 0; var23 < var28; ++var23) {
				ItemStack var27 = var25[var23];
				if (var27 != null) {
					ItemStack var26 = var27.copy();
					if (var26.hasEffect()
							&& (var26.getItem() instanceof ItemTool || var26.getItem() instanceof ItemArmor)) {
						var26.stackSize = 1;
					}
					renderItemStack(var26, var20, -40);
					var20 += 20;
				}
			}
		}
		
		GL11.glPopMatrix();
		revertAllCaps();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public void revertAllCaps() {
		for (Iterator localIterator = glCapMap.keySet().iterator(); localIterator.hasNext();) {
			int cap = ((Integer) localIterator.next()).intValue();
			revertGLCap(cap);
		}
	}

	public void revertGLCap(int cap) {
		Boolean origCap = (Boolean) glCapMap.get(Integer.valueOf(cap));
		if (origCap != null) {
			if (origCap.booleanValue()) {
				GL11.glEnable(cap);
			} else {
				GL11.glDisable(cap);
			}
		}
	}

	public int reAlpha(int color, float alpha) {
		Color c = new Color(color);
		float r = ((float) 1 / 255) * c.getRed();
		float g = ((float) 1 / 255) * c.getGreen();
		float b = ((float) 1 / 255) * c.getBlue();
		return new Color(r, g, b, alpha).getRGB();
	}

	public void OldrenderItemStack(ItemStack var1, int var2, int var3) {
		GL11.glPushMatrix();
		GL11.glDepthMask(true);
		GlStateManager.clear(256);
		RenderHelper.enableStandardItemLighting();
		mc.getRenderItem().zLevel = -150.0F;
		whatTheFuckOpenGLThisFixesItemGlint();
		mc.getRenderItem().renderItemAndEffectIntoGUI(var1, var2, var3);
		mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, var1, var2, var3);
		mc.getRenderItem().zLevel = 0.0F;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.5D, 0.5D, 0.5D);
		GlStateManager.disableDepth();
		GlStateManager.enableDepth();
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		GL11.glPopMatrix();
	}

	public void whatTheFuckOpenGLThisFixesItemGlint() {
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
	}

	private Map<Integer, Boolean> glCapMap = new HashMap();

	public void setGLCap(int cap, boolean flag) {
		glCapMap.put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
		if (flag) {
			GL11.glEnable(cap);
		} else {
			GL11.glDisable(cap);
		}
	}

	public void drawBorderedRectNameTag(float var1, float var2, float var3, float var4, float var5, int var6,
			int var7) {
		RenderUtil.drawRect(var1, var2, var3, var4, var7);
		float var8 = (float) (var6 >> 24 & 255) / 255.0F;
		float var9 = (float) (var6 >> 16 & 255) / 255.0F;
		float var10 = (float) (var6 >> 8 & 255) / 255.0F;
		float var11 = (float) (var6 & 255) / 255.0F;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(var9, var10, var11, var8);
		GL11.glLineWidth(var5);
		GL11.glBegin(1);
		GL11.glVertex2d((double) var1, (double) var2);
		GL11.glVertex2d((double) var1, (double) var4);
		GL11.glVertex2d((double) var3, (double) var4);
		GL11.glVertex2d((double) var3, (double) var2);
		GL11.glVertex2d((double) var1, (double) var2);
		GL11.glVertex2d((double) var3, (double) var2);
		GL11.glVertex2d((double) var1, (double) var4);
		GL11.glVertex2d((double) var3, (double) var4);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	private void renderItemStack(ItemStack stack, int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.clear(256);
		RenderHelper.enableStandardItemLighting();
		mc.getRenderItem().zLevel = -150.0f;
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);
		mc.getRenderItem().zLevel = 0.0f;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		double s = 0.5;
		GlStateManager.scale(s, s, s);
		GlStateManager.disableDepth();
		renderEnchantText(stack, x, y);
		GlStateManager.enableDepth();
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		GlStateManager.popMatrix();
	}

	private void renderEnchantText(ItemStack stack, int x, int y) {
		int unbreakingLevel2;
		int enchantmentY = y - 24;
		if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
			mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, 16711680);
			return;
		}
		if (stack.getItem() instanceof ItemArmor) {
			int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
			int projectileProtectionLevel = EnchantmentHelper
					.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack);
			int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId,
					stack);
			int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
			int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
			int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			int damage = stack.getMaxDamage() - stack.getItemDamage();
			if (dura.getValue().booleanValue()) {
				mc.fontRendererObj.drawStringWithShadow("" + damage, x * 2, y, 16777215);
			}
			if (protectionLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("prot" + protectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (projectileProtectionLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("proj" + projectileProtectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (blastProtectionLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("bp" + blastProtectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (fireProtectionLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("frp" + fireProtectionLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (thornsLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("th" + thornsLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("unb" + unbreakingLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
		}
		if (stack.getItem() instanceof ItemBow) {
			int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
			int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
			int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
			unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			if (powerLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("pow" + powerLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (punchLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("pun" + punchLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (flameLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("flame" + flameLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel2 > 0) {
				mc.fontRendererObj.drawStringWithShadow("unb" + unbreakingLevel2, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
		}
		if (stack.getItem() instanceof ItemSword) {
			int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
			int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
			int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
			unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			if (sharpnessLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("sh" + sharpnessLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (knockbackLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("kb" + knockbackLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (fireAspectLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("fire" + fireAspectLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel2 > 0) {
				mc.fontRendererObj.drawStringWithShadow("unb" + unbreakingLevel2, x * 2, enchantmentY, -1);
			}
		}
		if (stack.getItem() instanceof ItemTool) {
			int unbreakingLevel22 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
			int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
			int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
			if (efficiencyLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("eff" + efficiencyLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (fortuneLevel > 0) {
				mc.fontRendererObj.drawStringWithShadow("fo" + fortuneLevel, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (silkTouch > 0) {
				mc.fontRendererObj.drawStringWithShadow("silk" + silkTouch, x * 2, enchantmentY, -1);
				enchantmentY += 8;
			}
			if (unbreakingLevel22 > 0) {
				mc.fontRendererObj.drawStringWithShadow("ub" + unbreakingLevel22, x * 2, enchantmentY, -1);
			}
		}
		if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
			mc.fontRendererObj.drawStringWithShadow("god", x * 2, enchantmentY, -1);
		}
	}

	enum HealthMode {
		Hearts, Percentage;
	}
}
