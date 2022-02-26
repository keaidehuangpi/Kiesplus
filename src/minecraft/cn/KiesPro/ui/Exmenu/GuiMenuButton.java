package cn.KiesPro.ui.Exmenu;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class GuiMenuButton extends GuiButton {
	
	float scale = 1.0F;
	float targ;

	public GuiMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			ColorManager sb = new ColorManager();
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition - 2 && mouseX < this.xPosition + this.width && mouseY < this.yPosition + mc.fontRendererObj.FONT_HEIGHT + 2;
			this.targ = this.hovered ? 1.8F : 1.0F;
			float diff = (this.targ - this.scale) * 0.6F;
			this.scale = 1.0F + diff;
			GlStateManager.pushMatrix();
			GlStateManager.scale(this.scale, this.scale, this.scale);
			this.mouseDragged(mc, mouseX, mouseY);
			int text = this.hovered ? Colors.getColor(sb.hudColor.red, sb.hudColor.green, sb.hudColor.blue, 255) : -1;
			GlStateManager.pushMatrix();
			float offset = (float) (this.xPosition + this.width / 2) / this.scale;
			GlStateManager.translate(offset, (float) this.yPosition / this.scale, 1.0F);
			drawFancy(-31.0D, -2.0D, 31.0D, 14.0D, Colors.getColor(21));
			drawFancy(-30.0D, -1.0D, 30.0D, 13.0D, Colors.getColor(28));
			rectangle(-29.0D, (double) (-1.0F / this.scale), 31.0D, (double) (-0.5F / this.scale), Colors.getColor(sb.hudColor.red, sb.hudColor.green, sb.hudColor.blue, 255));
			mc.fontRendererObj.drawStringWithShadow(this.displayString, -(mc.fontRendererObj.getStringWidth(this.displayString) / 2.0F), -0.5F, text);
			GlStateManager.popMatrix();
			GlStateManager.popMatrix();
		}
	}
	
	public static void drawFancy(double d, double e, double f2, double f3, int paramColor) {
		float alpha = (float) (paramColor >> 24 & 255) / 255.0F;
		float red = (float) (paramColor >> 16 & 255) / 255.0F;
		float green = (float) (paramColor >> 8 & 255) / 255.0F;
		float blue = (float) (paramColor & 255) / 255.0F;
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glPushMatrix();
		GL11.glEnable(2848);
		GL11.glEnable(2881);
		GL11.glEnable(2832);
		GL11.glEnable(3042);
		GL11.glColor4f(red, green, blue, alpha);
		GL11.glBegin(7);
		GL11.glVertex2d(f2 + 1.300000011920929D, e);
		GL11.glVertex2d(d + 1.0D, e);
		GL11.glVertex2d(d - 1.300000011920929D, f3);
		GL11.glVertex2d(f2 - 1.0D, f3);
		GL11.glEnd();
		GL11.glDisable(2848);
		GL11.glDisable(2881);
		GL11.glDisable(2832);
		GL11.glDisable(3042);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}
	
	public static void rectangle(double left, double top, double right, double bottom, int color) {
		double var5;
		if (left < right) {
			var5 = left;
			left = right;
			right = var5;
		}

		if (top < bottom) {
			var5 = top;
			top = bottom;
			bottom = var5;
		}

		float var11 = (float) (color >> 24 & 255) / 255.0F;
		float var6 = (float) (color >> 16 & 255) / 255.0F;
		float var7 = (float) (color >> 8 & 255) / 255.0F;
		float var8 = (float) (color & 255) / 255.0F;
		WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var6, var7, var8, var11);
		worldRenderer.startDrawingQuads();
		worldRenderer.addVertex(left, bottom, 0.0D);
		worldRenderer.addVertex(right, bottom, 0.0D);
		worldRenderer.addVertex(right, top, 0.0D);
		worldRenderer.addVertex(left, top, 0.0D);
		Tessellator.getInstance().draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	static class Colors {
		public static int getColor(Color color) {
			return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		}

		public static int getColor(int brightness) {
			return getColor(brightness, brightness, brightness, 255);
		}

		public static int getColor(int brightness, int alpha) {
			return getColor(brightness, brightness, brightness, alpha);
		}

		public static int getColor(int red, int green, int blue) {
			return getColor(red, green, blue, 255);
		}

		public static int getColor(int red, int green, int blue, int alpha) {
			int color = 0 | alpha << 24;
			color |= red << 16;
			color |= green << 8;
			color |= blue;
			return color;
		}
	}
	
	class ColorManager {
		private List<ColorObject> colorObjectList = new CopyOnWriteArrayList<ColorObject>();
		public ColorObject fTeam = new ColorObject(0, 255, 0, 255);
		public ColorObject eTeam = new ColorObject(255, 0, 0, 255);
		public ColorObject fVis = new ColorObject(0, 0, 255, 255);
		public ColorObject fInvis = new ColorObject(0, 255, 0, 255);
		public ColorObject eVis = new ColorObject(255, 0, 0, 255);
		public  ColorObject eInvis = new ColorObject(255, 255, 0, 255);
		public ColorObject hudColor = new ColorObject(0, 192, 255, 255);
		public ColorObject xhair = new ColorObject(0, 255, 0, 200);

		public List<ColorObject> getColorObjectList() {
			return colorObjectList;
		}

		public  ColorObject getFriendlyVisible() {
			return fVis;
		}

		public  ColorObject getFriendlyInvisible() {
			return fInvis;
		}

		public  ColorObject getEnemyVisible() {
			return eVis;
		}

		public  ColorObject getEnemyInvisible() {
			return eInvis;
		}

		public ColorObject getHudColor() {
			return hudColor;
		}

		public ColorManager() {
			colorObjectList.add(fVis);
			colorObjectList.add(fInvis);
			colorObjectList.add(eVis);
			colorObjectList.add(eInvis);
			colorObjectList.add(hudColor);
			colorObjectList.add(fTeam);
			colorObjectList.add(eTeam);
			colorObjectList.add(xhair);
		}
	}
	
	class ColorObject {
		public int red;
		public int green;
		public int blue;
		public int alpha;

		public ColorObject(int red, int green, int blue, int alpha) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.alpha = alpha;
		}

		public int getRed() {
			return this.red;
		}

		public int getGreen() {
			return this.green;
		}

		public int getBlue() {
			return this.blue;
		}

		public int getAlpha() {
			return this.alpha;
		}

		public void setRed(int red) {
			this.red = red;
		}

		public void setGreen(int green) {
			this.green = green;
		}

		public void setBlue(int blue) {
			this.blue = blue;
		}

		public void setAlpha(int alpha) {
			this.alpha = alpha;
		}

		public int getColorInt() {
			return Colors.getColor(this.red, this.green, this.blue, this.alpha);
		}

		public void updateColors(int red, int green, int blue, int alpha) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.alpha = alpha;
		}
	}
}