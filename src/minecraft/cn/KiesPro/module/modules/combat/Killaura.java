/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import cn.KiesPro.Client;
import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.rendering.EventRender3D;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.EventPostUpdate;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.management.FriendManager;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.combat.AntiBot;
import cn.KiesPro.module.modules.combat.Criticals;
import cn.KiesPro.module.modules.movement.Speed;
import cn.KiesPro.module.modules.player.Teams;
import cn.KiesPro.ui.notification.Notification;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.Stopwatch;
import cn.KiesPro.utils.TimerUtil;
import cn.KiesPro.utils.math.MathUtil;
import cn.KiesPro.utils.math.RotationUtil;
import cn.KiesPro.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Killaura
extends Module {
	public static EntityLivingBase target;
	protected Module mainModel;
	TimerUtil kms = new TimerUtil();
	public static float rotationPitch;
	public ArrayList<EntityLivingBase> targets = new ArrayList();
	public ArrayList<EntityLivingBase> attackedTargets = new ArrayList();
	public static EntityLivingBase curTarget = null;
	public static Mode<Enum> espmode = new Mode("ESP", "ESP", (Enum[]) EMode.values(), (Enum) EMode.Box);
	public static Mode<Enum> Priority = new Mode("Priority", "Priority", (Enum[]) priority.values(), (Enum) priority.Health);
	public static Mode<Enum> Rot = new Mode("Rotation", "Rotation", (Enum[]) rotation.values(), (Enum) rotation.Basic);
	public static Mode<Enum> mode = new Mode("Mode", "Mode", (Enum[]) KillauraMode.values(), (Enum) KillauraMode.Switch);
	public static Numbers<Double> aps = new Numbers<Double>("CPS", "CPS", 10.0, 1.0, 20.0, 0.5);
	public static Numbers<Double> maxtargets = new Numbers<Double>("MaxTargets", "MaxTargets", 1.0, 1.0, 4.0, 1.0);
	public static Numbers<Double> crack = new Numbers<Double>("CrackSize", "CrackSize", 1.0, 0.0, 5.0, 1.0);
	public static Numbers<Double> turnspeed = new Numbers<Double>("TurnSpeed", "TurnSpeed", 90.0, 1.0, 360.0, 1.0);
	public static Numbers<Double> reach = new Numbers<Double>("Range", "Range", 4.2, 1.0, 6.0, 0.1);
	public static Option<Boolean> blocking = new Option<Boolean>("Autoblock", "Autoblock", true);
	public static Option<Boolean> death = new Option<Boolean>("Death", "Death", false);
	public static Option<Boolean> players = new Option<Boolean>("Players", "Players", true);
	public static Option<Boolean> animals = new Option<Boolean>("Animals", "Animals", true);
	public static Option<Boolean> mobs = new Option<Boolean>("Mobs", "Mobs", true);
	public static Option<Boolean> invis = new Option<Boolean>("Invisibles", "Invisibles", true);
	public static Option<Boolean> Fakesharp = new Option<Boolean>("Fakesharp", "Fakesharp", true);
	public static Option<Boolean> AttackwithLove= new Option<Boolean>("AttackwithLove", "AttackwithLove", false);
	public static Option<Boolean> Lightingboltvisual= new Option<Boolean>("Lightingboltvisual", "Lightingboltvisual", false);

	public static Option<Boolean> snowballvisual= new Option<Boolean>("snowballvisual", "snowballvisual", false);
	public static Option<Boolean> eggvisual= new Option<Boolean>("eggvisual", "eggvisual", false);
	public static Option<Boolean> stupidbottlevisual= new Option<Boolean>("stupidbottlevisual", "stupidbottlevisual", false);
	//public static Option<Boolean> smallfireballvisual= new Option<Boolean>("smallfireballvisual", "smallfireballvisual", false);
	//public static Option<Boolean> largefireballvisual= new Option<Boolean>("largefireballvisual", "largefireballvisual", false);
	private static long lastMS, lastMS2;
	private TimerUtil test = new TimerUtil();
	public boolean doBlock = false;
	public boolean unBlock = false;
	private long lastMs;
	private float curYaw = 0.0f;
	private float curPitch = 0.0f;
	private int tick = 0;
	private int index;
	private TimerUtil timer = new TimerUtil();
	private final Stopwatch critStopwatch = new Stopwatch();
	public static float[] facing;
	private float[] facing0;
	private float[] facing1;
	private float[] facing2;
	private float[] facing3;
	static boolean allowCrits;
	
	public float[] lastAngles;
	private Vector2f lastAnglesEz = new Vector2f(0.0F, 0.0F);

	public Killaura() {
		super("Aura", new String[] { "ka", "Killaura", "killa" }, ModuleType.Combat);
		this.addValues(this.espmode, this.Priority, this.Rot, this.mode, this.aps, this.reach, this.maxtargets, this.crack, this.turnspeed, this.blocking, this.death, this.players,
				this.animals, this.mobs, this.invis,Fakesharp,AttackwithLove,Lightingboltvisual,snowballvisual/*,smallfireballvisual,largefireballvisual*/,eggvisual,stupidbottlevisual);
	}

	public static double random(double min, double max) {
		Random random = new Random();
		return min + (int) (random.nextDouble() * (max - min));
	}

	private boolean shouldAttack() {
		return this.timer.hasReached((int) (1000 / this.aps.getValue().intValue()));
	}


	@EventTarget
	public void onRender(EventRender3D render) {
		if (Killaura.curTarget == null || this.espmode.getValue() == EMode.None) {
			return;
		}
		Color color = Killaura.curTarget.hurtTime > 0 ? new Color(-1618884) : new Color(-13330213);
		if (Killaura.curTarget != null && this.espmode.getValue() == EMode.Box) {
			this.mc.getRenderManager();
			double x = Killaura.curTarget.lastTickPosX
					+ (Killaura.curTarget.posX - Killaura.curTarget.lastTickPosX) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosX;
			this.mc.getRenderManager();
			double y = Killaura.curTarget.lastTickPosY
					+ (Killaura.curTarget.posY - Killaura.curTarget.lastTickPosY) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosY;
			this.mc.getRenderManager();
			double z = Killaura.curTarget.lastTickPosZ
					+ (Killaura.curTarget.posZ - Killaura.curTarget.lastTickPosZ) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosZ;
			if (Killaura.curTarget instanceof EntityPlayer) {
				final double width = Killaura.curTarget.getEntityBoundingBox().maxX
						- Killaura.curTarget.getEntityBoundingBox().minX;
				final double height = Killaura.curTarget.getEntityBoundingBox().maxY
						- Killaura.curTarget.getEntityBoundingBox().minY + 0.25;
				final float red = Killaura.curTarget.hurtTime > 0 ? 1.0f : 0.0f;
				final float green = Killaura.curTarget.hurtTime > 0 ? 0.2f : 0.5f;
				final float blue = Killaura.curTarget.hurtTime > 0 ? 0.0f : 1.0f;
				final float alpha = 0.2f;
				final float lineRed = Killaura.curTarget.hurtTime > 0 ? 1.0f : 0.0f;
				final float lineGreen = Killaura.curTarget.hurtTime > 0 ? 0.2f : 0.5f;
				final float lineBlue = Killaura.curTarget.hurtTime > 0 ? 0.0f : 1.0f;
				final float lineAlpha = 1.0f;
				final float lineWdith = 2.0f;
				RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue,
						lineAlpha, lineWdith);
			} else {
				final double width = Killaura.curTarget.getEntityBoundingBox().maxX
						- Killaura.curTarget.getEntityBoundingBox().minX + 0.10;
				final double height = Killaura.curTarget.getEntityBoundingBox().maxY
						- Killaura.curTarget.getEntityBoundingBox().minY + 0.25;
				final float red = Killaura.curTarget.hurtTime > 0 ? 1.0f : 0.0f;
				final float green = Killaura.curTarget.hurtTime > 0 ? 0.2f : 0.5f;
				final float blue = Killaura.curTarget.hurtTime > 0 ? 0.0f : 1.0f;
				final float alpha = 0.2f;
				final float lineRed = Killaura.curTarget.hurtTime > 0 ? 1.0f : 0.0f;
				final float lineGreen = Killaura.curTarget.hurtTime > 0 ? 0.2f : 0.5f;
				final float lineBlue = Killaura.curTarget.hurtTime > 0 ? 0.0f : 1.0f;
				final float lineAlpha = 1.0f;
				final float lineWdith = 2.0f;
				RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue,
						lineAlpha, lineWdith);
			}
		} else if(espmode.getValue() == EMode.FlatBox) {
			this.mc.getRenderManager();
			double x = Killaura.curTarget.lastTickPosX
					+ (Killaura.curTarget.posX - Killaura.curTarget.lastTickPosX) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosX;
			this.mc.getRenderManager();
			double y = Killaura.curTarget.lastTickPosY
					+ (Killaura.curTarget.posY - Killaura.curTarget.lastTickPosY) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosY;
			this.mc.getRenderManager();
			double z = Killaura.curTarget.lastTickPosZ
					+ (Killaura.curTarget.posZ - Killaura.curTarget.lastTickPosZ) * this.mc.timer.renderPartialTicks
					- RenderManager.renderPosZ;
			if (Killaura.curTarget instanceof EntityPlayer) {
				x -= 0.5;
				z -= 0.5;
				y += Killaura.curTarget.getEyeHeight() + 0.35 - (Killaura.curTarget.isSneaking() ? 0.25 : 0.0);
				final double mid = 0.5;
				GL11.glPushMatrix();
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				final double rotAdd = -0.25 * (Math.abs(Killaura.curTarget.rotationPitch) / 90.0f);
				GL11.glTranslated(x + mid, y + mid, z + mid);
				GL11.glRotated((double) (-Killaura.curTarget.rotationYaw % 360.0f), 0.0, 1.0, 0.0);
				GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid));
				GL11.glDisable(3553);
				GL11.glEnable(2848);
				GL11.glDisable(2929);
				GL11.glDepthMask(false);
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
				GL11.glLineWidth(2.0f);
				RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
				RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
				GL11.glDisable(2848);
				GL11.glEnable(3553);
				GL11.glEnable(2929);
				GL11.glDepthMask(true);
				GL11.glDisable(3042);
				GL11.glPopMatrix();
			} else {
				final double width = Killaura.curTarget.getEntityBoundingBox().maxZ
						- Killaura.curTarget.getEntityBoundingBox().minZ;
				final double height = 0.1;
				final float red = 0.0f;
				final float green = 0.5f;
				final float blue = 1.0f;
				final float alpha = 0.5f;
				final float lineRed = 0.0f;
				final float lineGreen = 0.5f;
				final float lineBlue = 1.0f;
				final float lineAlpha = 1.0f;
				final float lineWdith = 2.0f;
				RenderUtil.drawEntityESP(x, y + Killaura.curTarget.getEyeHeight() + 0.25, z, width, height, red, green,
						blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWdith);
			}
		}
		
		if(Killaura.curTarget != null && this.espmode.getValue() == EMode.Cylinder) {
			double posX = curTarget.lastTickPosX
					+ (curTarget.posX - curTarget.lastTickPosX) * (double) render.getPartialTicks()
					- RenderManager.renderPosX;
			double posY = curTarget.lastTickPosY
					+ (curTarget.posY - curTarget.lastTickPosY) * (double) render.getPartialTicks()
					- RenderManager.renderPosY;
			double posZ = curTarget.lastTickPosZ
					+ (curTarget.posZ - curTarget.lastTickPosZ) * (double) render.getPartialTicks()
					- RenderManager.renderPosZ;

			if (curTarget.hurtTime > 0) {
				RenderUtil.drawWolframEntityESP(curTarget, (new Color(255, 102, 113)).getRGB(), posX, posY, posZ);
			} else {
				RenderUtil.drawWolframEntityESP(curTarget, (new Color(186, 100, 200)).getRGB(), posX, posY, posZ);
			}
		}else if(espmode.getValue() == EMode.Ganga) {
            mc.getRenderManager();
            double x1 = curTarget.lastTickPosX + (curTarget.posX - curTarget.lastTickPosX) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosX;
            mc.getRenderManager();
            double y1 = curTarget.lastTickPosY + (curTarget.posY - curTarget.lastTickPosY) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosY;
            mc.getRenderManager();
            double z1 = curTarget.lastTickPosZ + (curTarget.posZ - curTarget.lastTickPosZ) * (double)mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            if(curTarget instanceof EntityPlayer) {
               double width1 = curTarget.getEntityBoundingBox().maxX - curTarget.getEntityBoundingBox().minX - 0.35D;
               double height1 = curTarget.getEntityBoundingBox().maxY - curTarget.getEntityBoundingBox().minY;
               float red1 = curTarget.hurtTime > 0?1.0F:0.0F;
               float green1 = curTarget.hurtTime > 0?0.2F:1.0F;
               float blue1 = curTarget.hurtTime > 0?0.0F:0.2F;
               float alpha1 = 0.2F;
               float lineRed1 = curTarget.hurtTime > 0?1.0F:0.0F;
               float lineGreen1 = curTarget.hurtTime > 0?0.2F:1.0F;
               float lineBlue1 = curTarget.hurtTime > 0?0.0F:0.2F;
               float lineAlpha1 = 0.2F;
               float lineWdith1 = 2.0F;
               RenderUtil.drawEntityESP(x1, y1, z1, width1, height1, red1, green1, blue1, 0.2F, lineRed1, lineGreen1, lineBlue1, 0.2F, 2.0F);
            } else {
               double width1 = curTarget.getEntityBoundingBox().maxX - curTarget.getEntityBoundingBox().minX - 0.35D;
               double height1 = curTarget.getEntityBoundingBox().maxY - curTarget.getEntityBoundingBox().minY;
               float red1 = curTarget.hurtTime > 0?1.0F:0.0F;
               float green1 = curTarget.hurtTime > 0?0.2F:1.0F;
               float blue1 = curTarget.hurtTime > 0?0.0F:0.2F;
               float alpha1 = 0.2F;
               float lineRed1 = curTarget.hurtTime > 0?1.0F:0.0F;
               float lineGreen1 = curTarget.hurtTime > 0?0.2F:1.0F;
               float lineBlue1 = curTarget.hurtTime > 0?0.0F:0.2F;
               float lineAlpha1 = 0.2F;
               float lineWdith1 = 2.0F;
               RenderUtil.drawEntityESP(x1, y1, z1, width1, height1, red1, green1, blue1, 0.2F, lineRed1, lineGreen1, lineBlue1, 0.2F, 2.0F);
            }
		}else if(espmode.getValue() == EMode.Hanhan) {
	        Iterator var3 = mc.theWorld.loadedEntityList.iterator();
     		//Color color = Killaura.curTarget.hurtTime > 0 ? new Color(-1618884) : new Color(255,255,255);
  			this.mc.getRenderManager();
  			double x = Killaura.curTarget.lastTickPosX
  					+ (Killaura.curTarget.posX - Killaura.curTarget.lastTickPosX) * this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
  			this.mc.getRenderManager();
  			double y = Killaura.curTarget.lastTickPosY
  					+ (Killaura.curTarget.posY - Killaura.curTarget.lastTickPosY) * this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
  			this.mc.getRenderManager();
  			double z = Killaura.curTarget.lastTickPosZ
  					+ (Killaura.curTarget.posZ - Killaura.curTarget.lastTickPosZ) * this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
  			if (Killaura.curTarget instanceof EntityPlayer) {
  					final double width = Killaura.curTarget.getEntityBoundingBox().maxX
        						- Killaura.curTarget.getEntityBoundingBox().minX;
        				final double height = Killaura.curTarget.getEntityBoundingBox().maxY
        						- Killaura.curTarget.getEntityBoundingBox().minY + 0.25;
        				final float red = Killaura.curTarget.hurtTime > 0 ? 1.0f : 1.0f;
        				final float green = Killaura.curTarget.hurtTime > 0 ? 0.2f : 1.0f;
        				final float blue = Killaura.curTarget.hurtTime > 0 ? 0.0f : 1.0f;
        				final float alpha = 0.2f;
        				final float lineRed = Killaura.curTarget.hurtTime > 0 ? 1.0f : 1.0f;
        				final float lineGreen = Killaura.curTarget.hurtTime > 0 ? 0.2f : 1.0f;
        				final float lineBlue = Killaura.curTarget.hurtTime > 0 ? 0.0f : 1.0f;
        				final float lineAlpha = 0.2f;
        				final float lineWdith = 1.0f;
        				RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue,
        						lineAlpha, lineWdith);
  			} else {
  				final double width = Killaura.curTarget.getEntityBoundingBox().maxX
  						- Killaura.curTarget.getEntityBoundingBox().minX + 0.10;
  				final double height = Killaura.curTarget.getEntityBoundingBox().maxY
  						- Killaura.curTarget.getEntityBoundingBox().minY + 0.25;
  				final float red = Killaura.curTarget.hurtTime > 0 ? 1.0f : 1.0f;
  				final float green = Killaura.curTarget.hurtTime > 0 ? 0.2f : 1.0f;
  				final float blue = Killaura.curTarget.hurtTime > 0 ? 0.0f : 1.0f;
  				final float alpha = 0.2f;
  				final float lineRed = Killaura.curTarget.hurtTime > 0 ? 1.0f : 1.0f;
  				final float lineGreen = Killaura.curTarget.hurtTime > 0 ? 0.2f : 1.0f;
  				final float lineBlue = Killaura.curTarget.hurtTime > 0 ? 0.0f : 1.0f;
  				final float lineAlpha = 0.2f;
  				final float lineWdith = 1.0f;
  				RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue,
  						lineAlpha, lineWdith);
  				}
		}

	}

	private boolean canBlock() {
		if (this.mc.thePlayer.getCurrentEquippedItem() != null
				&& this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
			return true;
		}
		return false;
	}

	public static long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}

	public static boolean hit(long milliseconds) {
		return (getCurrentMS() - lastMS) >= milliseconds;
	}

	public static void revert() {
		lastMS = getCurrentMS();
	}

	@EventTarget
	private void onPreUpdate(EventPreUpdate event) {
		this.setSuffix(this.mode.getValue().toString() == "Switch" ? "POST" : "PRE");
		int crackSize = ((Double)this.crack.getValue()).intValue();
		if (this.mc.thePlayer.getHealth() <= 0 && this.mode.getValue() == KillauraMode.Single && this.targets.size() > 0) {
			++this.index;
		}
		if(death.getValue().booleanValue()) {
			if ((!mc.thePlayer.isEntityAlive()
					|| (mc.currentScreen != null && mc.currentScreen instanceof GuiGameOver))) {
				this.setEnabled(false);
				//Helper.sendMessage("You are dead! Killing has been turned off automatically!");
				Client.instance.getNotificationManager().sendClientMessage("You are dead! Killing has been turned off automatically!", Notification.Type.INFO);
				return;
			}
			if (mc.thePlayer.ticksExisted <= 1) {
				this.setEnabled(false);
				//Helper.sendMessage("You are dead! Killing has been turned off automatically!");
				Client.instance.getNotificationManager().sendClientMessage("You are dead! Killing has been turned off automatically!", Notification.Type.INFO);
				return;
			}
		}
		if (this.mode.getValue() == KillauraMode.Switch && this.targets.size() > 0
				&& this.mc.thePlayer.ticksExisted % 80 == 0) {
			++this.index;
		}
		if (!this.targets.isEmpty()) {
			if (this.index >= this.targets.size()) {
				this.index = 0;
			}

      if (Lightingboltvisual.getValue().booleanValue()) {
        mc.theWorld.spawnEntityInWorld(
            new EntityLightningBolt(
                mc.thePlayer.getEntityWorld(),
                this.targets.get(0).posX,
					this.targets.get(0).posY,
					this.targets.get(0).posZ));
			}


	  if (snowballvisual.getValue().booleanValue()){
		  mc.theWorld.spawnEntityInWorld(new EntitySnowball(mc.thePlayer.getEntityWorld(),this.targets.get(0).posX,this.targets.get(0).posY,this.targets.get(0).posZ));
	  }
	if (eggvisual.getValue().booleanValue()){
		mc.theWorld.spawnEntityInWorld(new EntityEgg(mc.thePlayer.getEntityWorld(),this.targets.get(0).posX,this.targets.get(0).posY,this.targets.get(0).posZ));
	}
      if (stupidbottlevisual.getValue().booleanValue()) {
        mc.theWorld.spawnEntityInWorld(
            new EntityExpBottle(
                mc.thePlayer.getEntityWorld(),
                this.targets.get(0).posX,
                this.targets.get(0).posY,
                this.targets.get(0).posZ));
}
      if (AttackwithLove.getValue().booleanValue()) {
        mc.theWorld.spawnParticle(
            EnumParticleTypes.HEART,
            this.targets.get(0).posX,
            this.targets.get(0).posY,
            this.targets.get(0).posZ,
            this.targets.get(0).posX,
            this.targets.get(0).posY,
            this.targets.get(0).posZ,
            10);
}





		}




		this.doBlock = false;
		this.clear();
		this.findTargets(event);
		this.setCurTarget();
		if (curTarget != null) {
			if(Rot.getValue() == rotation.Nov) {
				Random rand = new Random();
				facing0 = getHypixelRotationsNeededBlock(curTarget.posX, curTarget.posY, curTarget.posZ);
				facing1 = getRotationFromPosition(curTarget.posX, curTarget.posY, curTarget.posZ);
				facing2 = getRotationsNeededBlock(curTarget.posX, curTarget.posY, curTarget.posZ);
				facing3 = getRotations(curTarget);
				int i;
				for (i = 0; i <= 3; i++) {
					switch (this.randomNumber(0, i)) {
					case 0:
						facing = facing0;
					case 1:
						facing = facing1;
					case 2:
						facing = facing2;
					case 3:
						facing = facing3;
					}
				}
				
				if (facing.length >= 0) {
					event.setYaw((facing[0]));
					event.setPitch(facing[1]);
				}
				if (Killaura.curTarget != null) {
					mc.thePlayer.renderYawOffset = facing[0];
					mc.thePlayer.rotationYawHead = facing[0];
				}
			}else if(Rot.getValue() == rotation.Basic) {
				float[] angles = getRotationsGucel(curTarget);
				event.setYaw((angles[0]));
				event.setPitch(angles[1]);
			}else if(Rot.getValue() == rotation.Flux) {
				float[] angles = getRotationAura(curTarget);

				//if (facing.length >= 0) {
					event.setYaw((angles[0]));
					event.setPitch(angles[1]);
				//}
				if (Killaura.curTarget != null) {
					mc.thePlayer.renderYawOffset = angles[0];
					mc.thePlayer.rotationYawHead = angles[0];
				}
			}else if(Rot.getValue() == rotation.Duel) {
				if (!this.targets.isEmpty()) {
                    if (this.index >= this.targets.size()) {
                        this.index = 0;
                    }
                    if (facing.length >= 0) {
                        event.setYaw((getLoserRotation(curTarget)[0]));
                        event.setPitch(getLoserRotation(curTarget)[1]);
                    }
                    if (Killaura.curTarget != null) {
                        mc.thePlayer.renderYawOffset = getLoserRotation(curTarget)[0];
                        mc.thePlayer.rotationYawHead = getLoserRotation(curTarget)[0];
                    }
                }
			}else if(Rot.getValue() == rotation.WatchDog) {
				if (!this.targets.isEmpty()) {
                    if (this.index >= this.targets.size()) {
                        this.index = 0;
                    }
                    if (facing.length >= 0) {
                        event.setYaw((getRotations(curTarget)[0]));
                        event.setPitch(getRotations(curTarget)[1]);
                    }
                    if (Killaura.curTarget != null) {
                        mc.thePlayer.renderYawOffset = getRotations(curTarget)[0];
                        mc.thePlayer.rotationYawHead = getRotations(curTarget)[0];
                    }
                }
			}else if(Rot.getValue() == rotation.Autumn) {
				float[] angles = getEntityRotations(curTarget);
				float[] rot = getRotations2(curTarget);
				//if (facing.length >= 0) {
					event.setYaw((angles[0]));
					event.setPitch(angles[1]);
				//}
				if (Killaura.curTarget != null) {
					mc.thePlayer.renderYawOffset = rot[0];
					mc.thePlayer.rotationYawHead = rot[0];
				}
			}else if(Rot.getValue() == rotation.SkidSense) {
				float[] angles = getNeededRotations(curTarget);
				float[] rot = getRotationsSkidSense(curTarget);
				//if (facing.length >= 0) {
					event.setYaw((angles[0]));
					event.setPitch(angles[1]);
				//}
				if (Killaura.curTarget != null) {
					mc.thePlayer.renderYawOffset = rot[0];
					mc.thePlayer.rotationYawHead = rot[0];
				}
			}else if(Rot.getValue() == rotation.Ex) {
                float[] rotations = getRotationsEx(curTarget);
                float targetYaw = MathHelper.clamp_float(getYawChangeGivenEx(curTarget.posX, curTarget.posZ, this.lastAnglesEz.x) + (float)this.randomNumber(-5, 5), -180.0F, 180.0F);
                
                Random rand = new Random();
                facing0 = getRotationsEx(curTarget);
                facing1 = getNeededRotationsEx(curTarget);
                facing2 = getRotations5Ex(curTarget);
                facing3 = getRotationsEx(curTarget);
                switch (randomNumber(1,4)) {
                     case 1:
                         facing = facing0;
                         break;
                     case 2:
                         facing = facing1;
                         break;
                     case 3:
                         facing = facing2;
                         break;
                     case 4:
                         facing = facing3;
                         break;
                  }
              
                 if(facing.length > 0) {
                     event.setYaw((facing[0]) + (float)rand.nextInt(12) -2.0F);
                     event.setPitch(facing[1] );
                 }
                mc.thePlayer.renderYawOffset = rotations[0];
                mc.thePlayer.rotationYawHead = rotations[0];
			}
			else if(Rot.getValue() == rotation.AAC) {
				mc.thePlayer.rotationYawHead = RotationUtil.getRotationForKillAura(target)[1];
				mc.thePlayer.rotationPitchHead = RotationUtil.getRotationForKillAura(target)[1];
				mc.thePlayer.renderYawOffset = RotationUtil.getRotationForKillAura(target)[0];
				}
			
			 int i2 = 0;
	            while ((double)i2 < (double)crackSize) {
        if (Fakesharp.getValue().booleanValue()) {

          this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT);
          this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT_MAGIC);
					}
	                ++i2;
	            }
		} else {
			this.targets.clear();
			this.attackedTargets.clear();
			this.lastMs = System.currentTimeMillis();
			if (this.unBlock) {
				this.mc.getNetHandler().addToSendQueue((Packet) new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				this.mc.thePlayer.itemInUseCount = 0;
				this.unBlock = false;
			}
		}
	}
	
	   public static float getYawChangeGivenEx(double posX, double posZ, float yaw) {
		      double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
		      double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
		      double yawToEntity;
		      if (deltaZ < 0.0D && deltaX < 0.0D) {
		         yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		      } else if (deltaZ < 0.0D && deltaX > 0.0D) {
		         yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		      } else {
		         yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
		      }

		      return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
		   }
	
	   public static  float[] getRotations5Ex( Entity curTarget) {
		   	 if(curTarget == null)
		            return null;
		   
		        double diffX = curTarget.posX - Minecraft.getMinecraft().thePlayer.posX;
		        double diffZ = curTarget.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		        double diffY;
		        if(curTarget instanceof EntityLivingBase){
		            EntityLivingBase elb = (EntityLivingBase) curTarget;
		            diffY = elb.posY
		                    + (elb.getEyeHeight() - 0.6)
		                    - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer
		                    .getEyeHeight());
		        }else
		            diffY = (curTarget.boundingBox.minY + curTarget.boundingBox.maxY)
		                    / 2.0D
		                    - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer
		                    .getEyeHeight());
		        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0f;
		        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

		        return new float[] { yaw, pitch };
		   }
	   
	   public static float[] getNeededRotationsEx(Entity target) {
	       final Vec3 eyesPos = new Vec3(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.posZ);
	       final double diffX = target.posX - eyesPos.xCoord;
	       final double diffY = target.posY - eyesPos.yCoord;
	       final double diffZ = target.posZ - eyesPos.zCoord;
	       final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
	       final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
	       final float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
	       return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
	   }
	
	   public static float[] getRotationFromPositionEx(double x, double z, double y) {
		      double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		      double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		      double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2D;
		      double dist = (double)MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		      float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
		      float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D));
		      return new float[]{yaw, pitch};
		   }
	   
	public static float[] getRotationsEx(EntityLivingBase ent) {
	      double x = ent.posX;
	      double z = ent.posZ;
	      double y = ent.posY + (double)(ent.getEyeHeight() / 2.0F);
	      return getRotationFromPositionEx(x, z, y);
	   }
	public static float[] getRotation2(EntityLivingBase entity) {
		EntityLivingBase entityLivingBase = entity;
		double diffX = entityLivingBase.posX - Minecraft.thePlayer.posX;
		double diffZ = entityLivingBase.posZ - Minecraft.thePlayer.posZ;
		double diffY = entityLivingBase.posY + (double) entity.getEyeHeight() - (Minecraft.thePlayer.posY + (double) Minecraft.thePlayer.getEyeHeight());
		double X = diffX;
		double Z = diffZ;
		double dist = MathHelper.sqrt_double((double) (X * X + Z * Z));
		float yaw = (float) (Math.atan2((double) diffZ, (double) diffX) * 180 / 3.141592653589) - 90.0f;
		float pitch = (float) (-(Math.atan2((double) diffY, (double) dist) * 180 / 3.141592653589));
		return new float[]{yaw, pitch};
	}
	public static float[] getLoserRotation(Entity target) {
		Minecraft mc = Minecraft.getMinecraft();
        double xDiff = target.posX - mc.thePlayer.posX;
        double yDiff = target.posY - mc.thePlayer.posY - 0.4;
        double zDiff = target.posZ - mc.thePlayer.posZ;
        Minecraft.getMinecraft();

        Minecraft.getMinecraft();
        Minecraft.getMinecraft();

        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(yDiff, dist)) * 180.0 / 3.141592653589793);
        float[] array = new float[2];
        int n = 0;
        Minecraft.getMinecraft();
        float rotationYaw = Minecraft.thePlayer.rotationYaw;
        float n2 = yaw;
        Minecraft.getMinecraft();
        array[n] = rotationYaw + MathHelper.wrapAngleTo180_float(n2 - Minecraft.thePlayer.rotationYaw);
        int n3 = 1;
        Minecraft.getMinecraft();
        float rotationPitch = Minecraft.thePlayer.rotationPitch;
        float n4 = pitch;
        Minecraft.getMinecraft();
        array[n3] = rotationPitch + MathHelper.wrapAngleTo180_float(n4 - Minecraft.thePlayer.rotationPitch);
        return array;
    }
	

	class Angle {
	    private float yaw;
	    private float pitch;

	    public Angle(float yaw, float pitch) {
	        this.yaw = yaw;
	        this.pitch = pitch;
	    }

	    public Angle() {
	        this(0.0f, 0.0f);
	    }

	    public float getYaw() {
	        return this.yaw;
	    }

	    public float getPitch() {
	        return this.pitch;
	    }

	    public void setYaw(float yaw) {
	        this.yaw = yaw;
	    }

	    public void setPitch(float pitch) {
	        this.pitch = pitch;
	    }

	    public Angle constrantAngle() {
	        this.setYaw(this.getYaw() % 360.0f);
	        this.setPitch(this.getPitch() % 360.0f);
	        while (this.getYaw() <= -180.0f) {
	            this.setYaw(this.getYaw() + 360.0f);
	        }
	        while (this.getPitch() <= -180.0f) {
	            this.setPitch(this.getPitch() + 360.0f);
	        }
	        while (this.getYaw() > 180.0f) {
	            this.setYaw(this.getYaw() - 360.0f);
	        }
	        while (this.getPitch() > 180.0f) {
	            this.setPitch(this.getPitch() - 360.0f);
	        }
	        return this;
	    }
	}
	
    public float[] getEntityRotationsNMSL(Entity entityLivingBase, float[] array) {
        float[] rot = getRotations(entityLivingBase);
        Angle smoothAngle = this.smoothAngle(new Angle(rot[0], rot[1]), new Angle(array[0], array[1]));
        return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(smoothAngle.getYaw() - mc.thePlayer.rotationYaw), smoothAngle.getPitch()};
    }
    
    public Angle smoothAngle(Angle Angle1, Angle Angle2) {
        Angle constrantAngle = new Angle(Angle2.getYaw() - Angle1.getYaw(), Angle2.getPitch() - Angle1.getPitch()).constrantAngle();
        constrantAngle.setYaw(Angle2.getYaw() - constrantAngle.getYaw() / 100.0f * 45.0f);
        constrantAngle.setPitch(Angle2.getPitch() - constrantAngle.getPitch() / 100.0f * 45.0f);
        return constrantAngle.constrantAngle();
    }

    public float[] getRotationsNMSL(Entity entity) {
        double xDiff = entity.posX - mc.thePlayer.posX;
        double yDiff = entity.posY - mc.thePlayer.posY;
        double zDiff = entity.posZ - mc.thePlayer.posZ;
        MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float newYaw = (float)Math.toDegrees(-Math.atan(xDiff / zDiff));
        if (zDiff < 0.0 && xDiff < 0.0) {
            newYaw = (float)(90.0 + Math.toDegrees(Math.atan(zDiff / xDiff)));
        } else if (zDiff < 0.0 && xDiff > 0.0) {
            newYaw = (float)(-90.0 + Math.toDegrees(Math.atan(zDiff / xDiff)));
        }
        float newPitch = (float)(-Math.atan2(entity.posY + (double)entity.getEyeHeight() / 0.0 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        if (yDiff >= -0.2 && yDiff <= 0.2) {
            newPitch = (float)(-Math.atan2(entity.posY + (double)entity.getEyeHeight() / HitLocation.CHEST.getOffset() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        } else if (yDiff > -0.2) {
            newPitch = (float)(-Math.atan2(entity.posY + (double)entity.getEyeHeight() / HitLocation.FEET.getOffset() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        } else if (yDiff < 0.2) {
            newPitch = (float)(-Math.atan2(entity.posY + (double)entity.getEyeHeight() / HitLocation.HEAD.getOffset() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        }
        return new float[]{newYaw, newPitch};
    }
	
	public static float[] getNeededRotations(Entity entityIn) {
		double d0 = entityIn.posX - Minecraft.getMinecraft().thePlayer.posX;
		double d1 = entityIn.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double d2 = entityIn.posY - entityIn.getEyeHeight()
		- (Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY
		- (Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxY
		- Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY));
		double d3 = MathHelper.sqrt_double(d0 * d0 * d1 * d1);
		float f = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
		float f1 = (float) (-(Math.atan2(d2, d3) * 180.0D / Math.PI));
		return new float[] { f, f1 };
	}
	
	public static float[] getRotationsSkidSense(Entity entityIn) {
		float yaw = getNeededRotations(entityIn)[0];
		float pitch = getNeededRotations(entityIn)[1];
		float yaw2 = MathHelper.wrapAngleTo180_float(yaw -
		Minecraft.getMinecraft().thePlayer.rotationYaw);
		yaw = Minecraft.getMinecraft().thePlayer.rotationYaw - yaw2;
		return new float[] { yaw, pitch };
	}
	
    public float[] getRotations2(final Entity entity) {
        final double xDiff = entity.posX - mc.thePlayer.posX;
        final double yDiff = entity.posY - mc.thePlayer.posY;
        final double zDiff = entity.posZ - mc.thePlayer.posZ;
        MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float newYaw = (float)Math.toDegrees(-Math.atan(xDiff / zDiff));
        if (zDiff < 0.0 && xDiff < 0.0) {
            newYaw = (float)(90.0 + Math.toDegrees(Math.atan(zDiff / xDiff)));
        }
        else if (zDiff < 0.0 && xDiff > 0.0) {
            newYaw = (float)(-90.0 + Math.toDegrees(Math.atan(zDiff / xDiff)));
        }
        float newPitch = (float)(-Math.atan2(entity.posY + entity.getEyeHeight() / 0.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        if (yDiff >= -0.2 && yDiff <= 0.2) {
            newPitch = (float)(-Math.atan2(entity.posY + entity.getEyeHeight() / HitLocation.CHEST.getOffset() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        }
        else if (yDiff > -0.2) {
            newPitch = (float)(-Math.atan2(entity.posY + entity.getEyeHeight() / HitLocation.FEET.getOffset() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        }
        else if (yDiff < 0.2) {
            newPitch = (float)(-Math.atan2(entity.posY + entity.getEyeHeight() / HitLocation.HEAD.getOffset() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        }
        return new float[] { newYaw, newPitch };
    }
	
	public float[] getEntityRotations(Entity target) {
		double xDiff = target.posX - mc.thePlayer.posX;
		double yDiff = target.posY - mc.thePlayer.posY;
		double zDiff = target.posZ - mc.thePlayer.posZ;
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) ((-Math.atan2(target.posY + (double) target.getEyeHeight() / 0.0
				- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0
				/ 3.141592653589793);
		if (yDiff > -0.2 && yDiff < 0.2) {
			pitch = (float) ((-Math.atan2(target.posY + (double) target.getEyeHeight() / HitLocation.CHEST.getOffset()
					- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0
					/ 3.141592653589793);
		} else if (yDiff > -0.2) {
			pitch = (float) ((-Math.atan2(target.posY + (double) target.getEyeHeight() / HitLocation.FEET.getOffset()
					- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0
					/ 3.141592653589793);
		} else if (yDiff < 0.3) {
			pitch = (float) ((-Math.atan2(target.posY + (double) target.getEyeHeight() / HitLocation.HEAD.getOffset()
					- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff))) * 180.0
					/ 3.141592653589793);
		}
		return new float[] { yaw, pitch };
	}

	public float[] getRotationsRemix(Entity target) {
		double xDiff = target.posX - mc.thePlayer.posX
				+ (double) (MathUtil.randomDouble((float) (-target.width / 2.0f), (float) (target.width / 4.0f)));
		double yDiff = target.posY - mc.thePlayer.posY;
		double zDiff = target.posZ - mc.thePlayer.posZ
				+ (double) (MathUtil.randomDouble((float) (-target.width / 2.0f), (float) (target.width / 4.0f)));
		float yaw = (float) (Math.atan2((double) zDiff, (double) xDiff) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-Math.atan2(
				(double) (target.posY + (double) target.getEyeHeight() / 0.0
						- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight())),
				(double) Math.hypot((double) xDiff, (double) zDiff)) * 180.0 / 3.141592653589793);
		if (yDiff > -0.2 && yDiff < 0.2) {
			pitch = (float) (-Math.atan2(
					(double) (target.posY + (double) target.getEyeHeight() / HitLocation.CHEST.getOffset()
							- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight())),
					(double) Math.hypot((double) xDiff, (double) zDiff)) * 180.0 / 3.141592653589793);
		} else if (yDiff > -0.2) {
			pitch = (float) (-Math.atan2(
					(double) (target.posY + (double) target.getEyeHeight() / HitLocation.FEET.getOffset()
							- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight())),
					(double) Math.hypot((double) xDiff, (double) zDiff)) * 180.0 / 3.141592653589793);
		} else if (yDiff < 0.3) {
			pitch = (float) (-Math.atan2(
					(double) (target.posY + (double) target.getEyeHeight() / HitLocation.HEAD.getOffset()
							- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight())),
					(double) Math.hypot((double) xDiff, (double) zDiff)) * 180.0 / 3.141592653589793);
		}
		return new float[] { yaw, pitch };
	}
	
	public float[] getRotationAura(EntityLivingBase entity) {
        EntityLivingBase entityLivingBase = entity;
        double diffX = entityLivingBase.posX - mc.thePlayer.posX;
        double diffZ = entityLivingBase.posZ - mc.thePlayer.posZ;
        double diffY = entityLivingBase.posY + (double)entity.getEyeHeight() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
        double X = diffX;
        double Z = diffZ;
        double dist = MathHelper.sqrt_double((double)(X * X + Z * Z));
        float yaw = (float)(Math.atan2((double)diffZ, (double)diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-(Math.atan2((double)diffY, (double)dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }
	
	public float[] getRotationsGucel(Entity entity) {
		double xDiff = entity.posX - mc.thePlayer.posX;
		double yDiff = entity.posY - mc.thePlayer.posY;
		double zDiff = entity.posZ - mc.thePlayer.posZ;
		MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float newYaw = (float) Math.toDegrees(-Math.atan(xDiff / zDiff));
		if (zDiff < 0.0 && xDiff < 0.0) {
			newYaw = (float) (90.0 + Math.toDegrees(Math.atan(zDiff / xDiff)));
		} else if (zDiff < 0.0 && xDiff > 0.0) {
			newYaw = (float) (-90.0 + Math.toDegrees(Math.atan(zDiff / xDiff)));
		}
		float newPitch = (float) (-Math.atan2(
				(double) (entity.posY + (double) entity.getEyeHeight() / 0.0
						- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight())),
				(double) Math.hypot((double) xDiff, (double) zDiff)) * 180.0 / 3.141592653589793);
		if (yDiff > -0.25 && yDiff < 0.25) {
			newPitch = (float) (-Math.atan2(
					(double) (entity.posY + (double) entity.getEyeHeight() / HitLocation.CHEST.getOffset()
							- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight())),
					(double) Math.hypot((double) xDiff, (double) zDiff)) * 180.0 / 3.141592653589793);
		} else if (yDiff > -0.25) {
			newPitch = (float) (-Math.atan2(
					(double) (entity.posY + (double) entity.getEyeHeight() / HitLocation.FEET.getOffset()
							- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight())),
					(double) Math.hypot((double) xDiff, (double) zDiff)) * 180.0 / 3.141592653589793);
		} else if (yDiff < 0.25) {
			newPitch = (float) (-Math.atan2(
					(double) (entity.posY + (double) entity.getEyeHeight() / HitLocation.HEAD.getOffset()
							- (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight())),
					(double) Math.hypot((double) xDiff, (double) zDiff)) * 180.0 / 3.141592653589793);
		}
		return new float[] { newYaw, newPitch };
	}

	private static int randomNumber(double min, double max) {
		Random random = new Random();
		return (int) (min + (random.nextDouble() * (max - min)));
	}

	public static float[] getRotationsNeededBlock(double x, double y, double z) {
		double diffX = x - Minecraft.getMinecraft().thePlayer.posX;
		double diffZ = z - Minecraft.getMinecraft().thePlayer.posZ;
		double diffY = y - Minecraft.getMinecraft().thePlayer.posY - 0.2;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

	public static float[] getHypixelRotationsNeededBlock(double x, double y, double z) {
		double diffX = x + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffZ = z + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double diffY = y - Minecraft.getMinecraft().thePlayer.posY - 0.2;

		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - (float) (120 + new Random().nextInt(2))),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

	public static float[] getRotationFromPosition(double x, double z, double y) {
		Minecraft.getMinecraft();
		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		Minecraft.getMinecraft();
		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		Minecraft.getMinecraft();
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 0.4;
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

	public static float[] getRotations(Entity entity) {

		if (entity == null) {
			return null;
		}
		Minecraft.getMinecraft();
		double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		Minecraft.getMinecraft();
		double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double diffY = entity.posY - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight());

		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
		return new float[] { yaw, pitch };
	}

	private void doAttack() {
		int aps = this.aps.getValue().intValue();
		int delayValue = (int) (1000 / this.aps.getValue().intValue() + MathUtil.randomDouble(-2.0, 2.0));

		if ((double) mc.thePlayer.getDistanceToEntity(curTarget) <= this.reach.getValue() + 0.4 && this.tick == 0
				&& this.test.delay(delayValue - 1)) {
			boolean miss = false;
			this.test.reset();

			if (mc.thePlayer.isBlocking() || mc.thePlayer.getHeldItem() != null
					&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword
					&& this.blocking.getValue().booleanValue()) {
				this.mc.getNetHandler().addToSendQueue((Packet) new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				this.unBlock = false;
			}
			if (!mc.thePlayer.isBlocking() && !this.blocking.getValue().booleanValue()
					&& mc.thePlayer.itemInUseCount > 0) {
				mc.thePlayer.itemInUseCount = 0;
			}

			this.attack(miss);
			this.doBlock = true;
			if (!miss) {
				for (Object o : mc.theWorld.loadedEntityList) {
					EntityLivingBase entity;
					if (!(o instanceof EntityLivingBase) || !this.isValidEntity(entity = (EntityLivingBase) o))
						continue;
					this.attackedTargets.add(curTarget);
				}
			}
		}

	}

	private void swap(int slot, int hotbarNum) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
				this.mc.thePlayer);
	}

	@EventTarget
	public void onPost(EventPostUpdate event) {
		this.sortList(targets);
		if (this.curTarget != null && this.shouldAttack()) {
			this.doAttack();
			this.newAttack();
		}
		if (curTarget != null
				&& (mc.thePlayer.getHeldItem() != null
						&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword
						&& this.blocking.getValue().booleanValue() || mc.thePlayer.isBlocking())
				&& this.doBlock) {
			mc.thePlayer.itemInUseCount = mc.thePlayer.getHeldItem().getMaxItemUseDuration();
			this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
					mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
			this.unBlock = true;
		}
	}
	
	private void attack(boolean fake) {
		mc.thePlayer.swingItem();
		if (!fake) {
			this.doBlock = true;
			this.mc.thePlayer.sendQueue.addToSendQueue((Packet) new C02PacketUseEntity(Killaura.curTarget, C02PacketUseEntity.Action.ATTACK));
			if (mc.thePlayer.isBlocking() && this.blocking.getValue().booleanValue()
					&& mc.thePlayer.inventory.getCurrentItem() != null
					&& mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
				this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
						mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
				this.unBlock = true;
			}
			if (!mc.thePlayer.isBlocking() && !this.blocking.getValue().booleanValue()
					&& mc.thePlayer.itemInUseCount > 0) {
				mc.thePlayer.itemInUseCount = 0;
			}
		}
	}

	private void newAttack() {
		if (mc.thePlayer.isBlocking()) {
			for (int i = 0; i <= 2; i++) {
				this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
			}
		}
		if (mc.thePlayer.isBlocking()) {
			for (int i = 0; i <= 2; i++) {
				this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
						mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
			}
		}
		if (mc.thePlayer.isBlocking() && this.timer.delay(100)) {
			for (int i = 0; i <= 2; i++) {
				this.mc.getNetHandler().addToSendQueue((Packet) new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			}
		}
		if (!mc.thePlayer.isBlocking() && !this.blocking.getValue().booleanValue() && mc.thePlayer.itemInUseCount > 0) {
			mc.thePlayer.itemInUseCount = 0;
		}
	}

	private void setCurTarget() {
		if (targets.size() == 0) {
			curTarget = null;
			return;
		}
		curTarget = this.targets.get(index);
	}

	private void clear() {
		curTarget = null;
		this.targets.clear();
		for (EntityLivingBase ent : this.targets) {
			if (this.isValidEntity(ent))
				continue;
			this.targets.remove(ent);
			if (!this.attackedTargets.contains(ent))
				continue;
			this.attackedTargets.remove(ent);
		}
	}

	private void findTargets(EventPreUpdate event) {
		int maxSize = this.mode.getValue() == KillauraMode.Switch ? 4 : 1;
		int maxSize1 = this.maxtargets.getValue().intValue();

		for (Entity o3 : mc.theWorld.loadedEntityList) {
			EntityLivingBase curEnt;
			if (o3 instanceof EntityLivingBase && this.isValidEntity(curEnt = (EntityLivingBase) o3)
					&& !this.targets.contains(curEnt)) {
				this.targets.add(curEnt);
			}
			if (this.targets.size() >= maxSize1)
				break;
		}
		this.targets.sort((o1, o2) -> (int) (o1.getDistanceToEntity(o2) - o2.getDistanceToEntity(o1)));
	}

	private boolean isValidEntity(EntityLivingBase ent) {
		AntiBot ab = (AntiBot) Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
		
		return ent == null ? false
				: (ent == this.mc.thePlayer ? false
						: (ent instanceof EntityPlayer && !this.players.getValue() ? false
								: ((ent instanceof EntityAnimal || ent instanceof EntitySquid || ent instanceof EntitySnowman)
										&& !this.animals.getValue()
												? false
												: ((ent instanceof EntityMob || ent instanceof EntitySlime || ent instanceof EntityVillager
														|| ent instanceof EntityBat) && !this.mobs.getValue()
																? false
																: ((double) this.mc.thePlayer.getDistanceToEntity(
																		ent) > this.reach.getValue() + 0.4
																				? false
																				: (ent instanceof EntityPlayer
																						&& FriendManager
																								.isFriend(ent.getName())
																										? false
																										: (!ent.isDead
																												&& ent.getHealth() > 0.0F
																														? (ent.isInvisible()
																																&& !this.invis
																																		.getValue()
																																				? false
																																				: ab.isEntityBot(
																																						ent) ? false
																																								: (this.mc.thePlayer.isDead
																																										? false
																																										: !(ent instanceof EntityPlayer)
																																												|| !Teams
																																														.isOnSameTeam(
																																																(EntityPlayer) ent)))
																														: false)))))));
	}

	@Override
	public void onEnable() {
		index = 0;
		this.critStopwatch.reset();
		this.curYaw = mc.thePlayer.rotationYaw;
		this.curPitch = mc.thePlayer.rotationPitch;
        if (mc.thePlayer != null) {
            this.lastAngles = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
        }
        if (mc.thePlayer != null) {
            this.lastAnglesEz.x = mc.thePlayer.rotationYaw;
         }
		Client.instance.getNotificationManager().sendClientMessage("enabled Aura", Notification.Type.SUCCESS);
		super.onEnable();
	}


	public static float[] getRotationToEntity(Entity target) {
		double xDiff = target.posX - Minecraft.getMinecraft().thePlayer.posX;
		double yDiff = target.posY - Minecraft.getMinecraft().thePlayer.posY;
		double zDiff = target.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) ((-Math.atan2(
				target.posY + (double) target.getEyeHeight() / 0.0
						- (Minecraft.getMinecraft().thePlayer.posY
								+ (double) Minecraft.getMinecraft().thePlayer.getEyeHeight()),
				Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
		if (yDiff > -0.2 && yDiff < 0.2) {
			pitch = (float) ((-Math.atan2(
					target.posY + (double) target.getEyeHeight() / HitLocation.CHEST.getOffset()
							- (Minecraft.getMinecraft().thePlayer.posY
									+ (double) Minecraft.getMinecraft().thePlayer.getEyeHeight()),
					Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
		} else if (yDiff > -0.2) {
			pitch = (float) ((-Math.atan2(
					target.posY + (double) target.getEyeHeight() / HitLocation.FEET.getOffset()
							- (Minecraft.getMinecraft().thePlayer.posY
									+ (double) Minecraft.getMinecraft().thePlayer.getEyeHeight()),
					Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
		} else if (yDiff < 0.3) {
			pitch = (float) ((-Math.atan2(
					target.posY + (double) target.getEyeHeight() / HitLocation.HEAD.getOffset()
							- (Minecraft.getMinecraft().thePlayer.posY
									+ (double) Minecraft.getMinecraft().thePlayer.getEyeHeight()),
					Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
		}
		return new float[] { yaw, pitch };

	}

	static enum HitLocation {
		AUTO(0.0), HEAD(1.0), CHEST(1.5), FEET(3.5);

		private double offset;

		HitLocation(double offset) {
			this.offset = offset;
		}

		public double getOffset() {
			return this.offset;
		}
	}

	@Override
	public void onDisable() {
		this.targets.clear();
		this.attackedTargets.clear();
		curTarget = null;
		mc.thePlayer.itemInUseCount = 0;
		allowCrits = true;
		mc.thePlayer.renderYawOffset = mc.thePlayer.rotationYaw;
		rotationPitch = 0.0f;
	      if (mc.thePlayer != null) {
	          this.lastAnglesEz.x = mc.thePlayer.rotationYaw;
	       }
		this.curYaw = mc.thePlayer.rotationYaw;
		this.curPitch = mc.thePlayer.rotationPitch;
		Client.instance.getNotificationManager().sendClientMessage("disabled Aura", Notification.Type.SUCCESS);
		super.onDisable();
	}

	private void sortList(List<EntityLivingBase> weed) {
		if (this.Priority.getValue() == priority.Range) {
			weed.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
		}
		if (this.Priority.getValue() == priority.Fov) {
			weed.sort(Comparator.comparingDouble(o -> RotationUtil.getDistanceBetweenAngles(mc.thePlayer.rotationPitch,
					Killaura.getRotationToEntity(o)[0])));
		}
		if (this.Priority.getValue() == priority.Angle) {
			weed.sort((o1, o2) -> {
				float[] rot1 = getRotationToEntity(o1);
				float[] rot2 = getRotationToEntity(o2);
				return (int) (mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
			});
		}
		if (this.Priority.getValue() == priority.Health) {
			weed.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
		}
		if(this.Priority.getValue() == priority.Dynamic) {
			weed.sort(Comparator.comparingInt(o -> (int)(((EntityLivingBase)o).getHealth() + ((EntityLivingBase)o).getDistanceToEntity((Entity)mc.thePlayer) + (mc.thePlayer.rotationYaw - RotationUtil.getRotations((EntityLivingBase)o)[0]) + ((o instanceof EntityPlayer) ? Minecraft.getMinecraft().thePlayer.getTotalArmorValue() : 0))));
		}
		if(this.Priority.getValue() == priority.Armor) {
			weed.sort(Comparator.comparingInt(o -> (o instanceof EntityPlayer) ? Minecraft.getMinecraft().thePlayer.getTotalArmorValue() : ((int)((EntityLivingBase)o).getHealth())));
		}
		if(this.Priority.getValue() == priority.Distance) {
			this.targets.sort(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceToEntity((Entity)entity)));
			this.targets.sort(Comparator.comparing(entity -> entity instanceof EntityPlayer));
		}
	}

	public static float getYawDifference(final float current, final float target) {
		final float rot = (target + 180.0f - current) % 360.0f;
		return rot + ((rot > 0.0f) ? -180.0f : 180.0f);
	}

	private float getYawDifference(float yaw, EntityLivingBase target) {
		return getYawDifference(yaw, getRotationToEntity(target)[0]);
	}

	static enum EMode {
		Box, None, FlatBox, Cylinder, Ganga, Hanhan;
	}

	static enum priority {
		Range, Fov, Angle, Health, Armor, Dynamic, Distance;
	}

	static enum KillauraMode {
		Switch, Single;
	}
	
	static enum rotation {
		Basic, Flux, Nov, Duel, Autumn, WatchDog, SkidSense, Ex , AAC;
	}
}