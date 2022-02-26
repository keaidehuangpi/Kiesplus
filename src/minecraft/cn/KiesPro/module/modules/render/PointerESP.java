/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import java.awt.Color;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.rendering.EventRender2D;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.render.RenderUtil;

public class PointerESP
extends Module {
    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }
    
    public void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public void drawESPCircle(int cx, int cy, double d, int n, int color) {
        GL11.glPushMatrix();
        cx *= 2.0;
        cy *= 2.0;
        double b = 6.2831852 / n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        double x = d *= 2.0;
        double y = 0.0;
        enableGL2D();
        GL11.glScaled(0.5, 0.5, 0.5);
        GlStateManager.color(0,0,0);
        GlStateManager.resetColor();
        RenderUtil.glColor(color);
        GL11.glBegin(2);
        double ii = 0;
        while (ii < n) {
            GL11.glVertex2d(x + cx, y + cy);
            double t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ii++;
        }
        GL11.glEnd();
        GL11.glScaled(2.0, 2.0, 2.0);
        disableGL2D();
        GlStateManager.color(1, 1, 1, 1);
        GL11.glPopMatrix();
    }
	
	public Mode<Enum> mode  = new Mode("Mode", "mode", (Enum[])GuiMode.values(), (Enum)GuiMode.Solid);
	public static Option<Boolean> Player = new Option<Boolean>("Player", "Player", true);
	public static Option<Boolean> Animals = new Option<Boolean>("Animals", "Animals", false);
	public static Option<Boolean> Mobs = new Option<Boolean>("Mobs", "Mobs", false);
	public static Option<Boolean> Invisible = new Option<Boolean>("Invisible", "Invisible", false);
	public static Numbers<Double> r = new Numbers<Double>("R", "R", 160.0, 0.0, 255.0, 5.0);
	public static Numbers<Double> g = new Numbers<Double>("G", "G", 160.0, 0.0, 255.0, 5.0);
	public static Numbers<Double> b = new Numbers<Double>("B", "B", 160.0, 0.0, 255.0, 5.0);
	public static Numbers<Double> alpha = new Numbers<Double>("Alpha", "Alpha", 255.0, 0.0, 255.0, 5.0);
    public PointerESP() {
        super("PointerESP", new String[]{"PointerESP"}, ModuleType.Render);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.addValues(mode, Player, Animals, Mobs, Invisible, r,g,b,alpha);
    }
    
    @EventTarget
    public void onRender2D(EventRender2D event) {
    	ScaledResolution a = new ScaledResolution(mc);
		GlStateManager.pushMatrix();
		int size = 50;
		double xOffset = a.getScaledWidth() / 2 - 24.5;
        double yOffset = a.getScaledHeight() / 2 - 25.2;
        double playerOffsetX = mc.thePlayer.posX;
        double playerOffSetZ = mc.thePlayer.posZ;
        for (Object i : mc.theWorld.getLoadedEntityList()){
			Entity entity = (Entity)i;
			if (Mobs.getValue() && (entity instanceof EntityMob || entity instanceof EntitySlime) && (Invisible.getValue() || !entity.isInvisible())){
				double loaddist = 0.2;
				float pTicks = mc.timer.renderPartialTicks;
				double pos1 = (((entity.posX + (entity.posX - entity.lastTickPosX) * pTicks) - playerOffsetX) * loaddist);
                double pos2 = (((entity.posZ + (entity.posZ - entity.lastTickPosZ) * pTicks) - playerOffSetZ) * loaddist);
                double cos = Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                double sin = Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                double rotY = -(pos2 * cos - pos1 * sin);
                double rotX = -(pos1 * cos + pos2 * sin);
                double var7 = 0 - rotX;
                double var9 = 0 - rotY;
				if (MathHelper.sqrt_double(var7 * var7 + var9 * var9) < size / 2 - 4) {
					double angle = (Math.atan2(rotY - 0, rotX - 0) * 180 / Math.PI);
					double x = ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2;
					double y = ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(x, y, 0);
                    GlStateManager.rotate((float) angle, 0, 0, 1);
                    GlStateManager.scale(1.5, 1.0, 1.0);
					if (mode.getValue() == GuiMode.Solid){
					    drawESPCircle(0, 0, 2.2, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
                        drawESPCircle(0, 0, 1.5, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
                        drawESPCircle(0, 0, 1.0, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
                        drawESPCircle(0, 0, 0.5, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
					} else {
						drawESPCircle(0, 0, 2.2, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
					}
                    GlStateManager.popMatrix();
                }
			}
			
			if (Animals.getValue() && (entity instanceof EntityAnimal || entity instanceof EntitySquid) && (Invisible.getValue() || !entity.isInvisible())){
				double loaddist = 0.2;
				double pTicks = mc.timer.renderPartialTicks;
                double pos1 = (((entity.posX + (entity.posX - entity.lastTickPosX) * pTicks) - playerOffsetX) * loaddist);
                double pos2 = (((entity.posZ + (entity.posZ - entity.lastTickPosZ) * pTicks) - playerOffSetZ) * loaddist);
                double cos = Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                double sin = Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                double rotY = -(pos2 * cos - pos1 * sin);
				double rotX = -(pos1 * cos + pos2 * sin);
				double var7 = 0 - rotX;
				double var9 = 0 - rotY;
				if (MathHelper.sqrt_double(var7 * var7 + var9 * var9) < size / 2 - 4) {
					double angle = (Math.atan2(rotY - 0, rotX - 0) * 180 / Math.PI);
					double x = ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2;
					double y = ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(x, y, 0);
                    GlStateManager.rotate((float) angle, 0, 0, 1);
                    GlStateManager.scale(1.5, 1.0, 1.0);
					if (mode.getValue() == GuiMode.Solid){
					    drawESPCircle(0, 0, 2.2, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
                        drawESPCircle(0, 0, 1.5, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
                        drawESPCircle(0, 0, 1.0, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
                        drawESPCircle(0, 0, 0.5, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
					} else {
						drawESPCircle(0, 0, 2.2, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
					}
                    GlStateManager.popMatrix();
                }
			}
			
			if (Player.getValue() && (entity instanceof EntityPlayer && entity != mc.thePlayer) && (Invisible.getValue() || !entity.isInvisible())){
				double loaddist = 0.2;
				double pTicks = mc.timer.renderPartialTicks;
                double pos1 = (((entity.posX + (entity.posX - entity.lastTickPosX) * pTicks) - playerOffsetX) * loaddist);
                double pos2 = (((entity.posZ + (entity.posZ - entity.lastTickPosZ) * pTicks) - playerOffSetZ) * loaddist);
                double cos = Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
				double sin = Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                double rotY = -(pos2 * cos - pos1 * sin);
                double rotX = -(pos1 * cos + pos2 * sin);
                double var7 = 0 - rotX;
                double var9 = 0 - rotY;
				if (MathHelper.sqrt_double(var7 * var7 + var9 * var9) < size / 2 - 4) {
					double angle = (Math.atan2(rotY - 0, rotX - 0) * 180 / Math.PI);
					double x = ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2;
					double y = ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(x, y, 0);
                    GlStateManager.rotate((float) angle, 0, 0, 1);
                    GlStateManager.scale(1.5, 1.0, 1.0);
                    if (mode.getValue() == GuiMode.Solid){
					    drawESPCircle(0, 0, 2.2, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
                        drawESPCircle(0, 0, 1.5, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
                        drawESPCircle(0, 0, 1.0, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
                        drawESPCircle(0, 0, 0.5, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
					} else {
						drawESPCircle(0, 0, 2.2, 3, new Color(r.getValue().intValue(),g.getValue().intValue(),b.getValue().intValue(),alpha.getValue().intValue()).getRGB());
					}                    
					GlStateManager.popMatrix();
                }
			}
		}
		GlStateManager.popMatrix();
    }
    
    enum GuiMode {
    	Solid,
    	Line;
    }
}

