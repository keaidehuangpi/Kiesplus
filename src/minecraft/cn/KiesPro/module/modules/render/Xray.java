/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.render;

import com.google.common.collect.Lists;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.rendering.EventBlockRenderSide;
import cn.KiesPro.api.events.rendering.EventRender3D;
import cn.KiesPro.api.events.rendering.EventSetOpaqueCube;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.XrayBlock;
import cn.KiesPro.utils.render.RenderUtil;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class Xray
extends Module {
	public static final HashSet<Integer> blockIDs = new HashSet();
	public List<Integer> KEY_IDS = Lists.newArrayList(10, 11, 8, 9, 14, 15, 16, 21, 41, 42, 46, 48, 52, 56, 57, 61, 62,
			73, 74, 84, 89, 103, 116, 117, 118, 120, 129, 133, 137, 145, 152, 153, 154);
	public static Numbers<Double> OPACITY = new Numbers<Double>("OPACITY", "OPACITY", 160.0, 0.0, 255.0, 5.0);

	public Option<Boolean> ESP = new Option<Boolean>("ESP", "ESP", true);
	public Option<Boolean> AnotherESP = new Option<Boolean>("BlockAnotherESP", "BlockAnotherESP", false);

	public CopyOnWriteArrayList list = new CopyOnWriteArrayList<>();

	public static Option<Boolean> cave = new Option<Boolean>("Cave", "Cave", true);
	
	private Option<Boolean> diamond = new Option<Boolean>("Diamond", "Diamond", true);
	private Option<Boolean> gold = new Option<Boolean>("Gold", "Gold", true);
	private Option<Boolean> lapis = new Option<Boolean>("Lapis", "Lapis", false);
	private Option<Boolean> emerald = new Option<Boolean>("Emerald", "Emerald", false);
	private Option<Boolean> iron = new Option<Boolean>("Iron", "Iron", true);
	private Option<Boolean> red = new Option<Boolean>("Red", "Red", true);
	private Option<Boolean> coal = new Option<Boolean>("Coal", "Coal", true);
    public Xray() {
        super("Xray", new String[]{"xrai", "oreesp"}, ModuleType.Render);
        this.setColor(Color.GREEN.getRGB());
        this.addValues(cave, OPACITY, ESP, AnotherESP, diamond, gold, lapis, emerald, iron, red, coal);
    }

	@Override
	public void onEnable() {
		blockIDs.clear();
		// Client.xray = true;
		try {
			for (Integer o : KEY_IDS) {
				blockIDs.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		mc.renderGlobal.loadRenderers();
	}

	@Override
	public void onDisable() {
		// Client.xray = false;
		mc.renderGlobal.loadRenderers();
		list.clear();
	}

	@EventTarget
	public void onEvent(EventRender3D event) {
		Iterator x = list.iterator();

		while (x.hasNext()) {
			XrayBlock x1 = (XrayBlock) x.next();
			if (x1.type.contains("Diamond") && diamond.getValue().booleanValue())
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(30, 144, 255),
						new Color(30, 144, 255).getRGB(), 2f);
			else if (x1.type.contains("Iron") && iron.getValue().booleanValue())
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(184, 134, 11),
						new Color(184, 134, 11).getRGB(), 2f);
			else if (x1.type.contains("Gold") && gold.getValue().booleanValue())
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(255, 255, 0), new Color(255, 255, 0).getRGB(),
						2f);
			else if (x1.type.contains("Lapis") && lapis.getValue().booleanValue())
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(75, 111, 255), new Color(75, 111, 255).getRGB(),
						2f);
			else if (x1.type.contains("Emerald") && emerald.getValue().booleanValue())
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 255, 0), new Color(0, 255, 0).getRGB(),
						2f);
			else if (x1.type.contains("Red") && red.getValue().booleanValue()) {
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(255, 0, 0), new Color(255, 0, 0).getRGB(),
						2f);
			} else {
				if(coal.getValue().booleanValue())
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 0, 0), new Color(0, 0, 0).getRGB(), 2f);
			}

		}
	}

	@EventTarget
	public void onEvent(EventSetOpaqueCube event) {
		event.setCancelled(true);
	}

	@EventTarget
	public void onEvent(EventBlockRenderSide e) {
		e.getState().getBlock();
		if (!cave.getValue() && containsID(Block.getIdFromBlock(e.getState().getBlock()))
				&& !(e.getSide() == EnumFacing.DOWN && e.minY > 0.0D ? true
						: (e.getSide() == EnumFacing.UP && e.maxY < 1.0D ? true
								: (e.getSide() == EnumFacing.NORTH && e.minZ > 0.0D ? true
										: (e.getSide() == EnumFacing.SOUTH && e.maxZ < 1.0D ? true
												: (e.getSide() == EnumFacing.WEST && e.minX > 0.0D ? true
														: (e.getSide() == EnumFacing.EAST && e.maxX < 1.0D ? true
																: !e.getWorld().getBlockState(e.pos).getBlock()
																		.isOpaqueCube()))))))) {
			e.setToRender(true);
		} else {
			if (!cave.getValue()) {
				e.setCancelled(true);
			}
		}
		if (!ESP.getValue())
			return;

		if ((e.getSide() == EnumFacing.DOWN && e.minY > 0.0D ? true
				: (e.getSide() == EnumFacing.UP && e.maxY < 1.0D ? true
						: (e.getSide() == EnumFacing.NORTH && e.minZ > 0.0D ? true
								: (e.getSide() == EnumFacing.SOUTH && e.maxZ < 1.0D ? true
										: (e.getSide() == EnumFacing.WEST && e.minX > 0.0D ? true
												: (e.getSide() == EnumFacing.EAST && e.maxX < 1.0D ? true
														: !e.getWorld().getBlockState(e.pos).getBlock()
																.isOpaqueCube()))))))) {
			if (mc.theWorld.getBlockState(e.getPos().offset(e.getSide(), -1)).getBlock() instanceof BlockOre
					|| mc.theWorld.getBlockState(e.getPos().offset(e.getSide(), -1))
							.getBlock() instanceof BlockRedstoneOre) {
				final float xDiff = (float) (mc.thePlayer.posX - e.pos.getX());
				final float yDiff = 0;
				final float zDiff = (float) (mc.thePlayer.posZ - e.pos.getZ());
				float dis = MathHelper.sqrt_float(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
				if (dis > 50)
					return;
				XrayBlock x = new XrayBlock(Math.round(e.pos.offset(e.getSide(), -1).getZ()),
						Math.round(e.pos.offset(e.getSide(), -1).getY()),
						Math.round(e.pos.offset(e.getSide(), -1).getX()),
						mc.theWorld.getBlockState(e.pos.offset(e.getSide(), -1)).getBlock().getUnlocalizedName());
				if (!list.contains(x)) {
					list.add(x);
					if (AnotherESP.getValue()) {
						for (EnumFacing e1 : EnumFacing.values()) {
							XrayBlock x1 = new XrayBlock(Math.round(e.pos.offset(e.getSide(), -1).offset(e1, 1).getZ()),
									Math.round(e.pos.offset(e.getSide(), -1).offset(e1, 1).getY()),
									Math.round(e.pos.offset(e.getSide(), -1).offset(e1, 1).getX()),
									mc.theWorld.getBlockState(e.pos.offset(e.getSide(), -1).offset(e1, 1)).getBlock()
											.getUnlocalizedName());
							boolean b = false;
							if (x.type.contains("Diamond") && x1.type.contains("Diamond")) {
								b = true;
							} else if (x.type.contains("Iron") && x1.type.contains("Iron")) {
								b = true;
							} else if (x.type.contains("Gold") && x1.type.contains("Gold")) {
								b = true;
							} else if (x.type.contains("Red") && x1.type.contains("Red")) {
								b = true;
							} else if (x.type.contains("Coal") && x1.type.contains("Coal")) {
								b = true;
							}
							if (b) {
								if (!list.contains(x1))
									list.add(x1);
							}
						}
					}
				}
			}
		}

	}

	public static int getColor(int red, int green, int blue) {
		return getColor(red, green, blue, 255);
	}

	public static int getColor(int red, int green, int blue, int alpha) {
		int color = 0;
		color |= alpha << 24;
		color |= red << 16;
		color |= green << 8;
		color |= blue;
		return color;
	}

	public boolean containsID(int id) {
		return blockIDs.contains(id);
	}

	public int getOpacity() {
		return OPACITY.getValue().intValue();
	}
}
