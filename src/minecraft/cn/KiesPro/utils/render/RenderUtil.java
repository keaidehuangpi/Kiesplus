package cn.KiesPro.utils.render;

import java.awt.Color;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import pw.knx.feather.tessellate.Tessellation;
import shadersmod.client.Shaders;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import cn.KiesPro.api.events.rendering.EventRender3D;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.math.Vec2f;
import cn.KiesPro.utils.math.Vec3f;
import cn.KiesPro.utils.render.gl.GLClientState;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.List;


public class RenderUtil {
	public static final Tessellation tessellator;
	private static final List<Integer> csBuffer;
	private static final Consumer<Integer> ENABLE_CLIENT_STATE;
	private static final Consumer<Integer> DISABLE_CLIENT_STATE;
	public static float delta;
	private static Frustum frustrum = new Frustum();

	static {
		tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
		csBuffer = new ArrayList<Integer>();
		ENABLE_CLIENT_STATE = GL11::glEnableClientState;
		DISABLE_CLIENT_STATE = GL11::glEnableClientState;
	}

	public RenderUtil() {
		super();
	}

	// ave
	   public static int rainbow(int delay) {
		      double rainbow = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 10.0D);
		      return Color.getHSBColor((float)((rainbow %= 360.0D) / 360.0D), 0.5F, 1.0F).getRGB();
		   }
	
	public static void pre3D() {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
	}

	public static int width() {
		return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
	}

	public static int height() {
		return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
	}

	public static double interpolation(final double newPos, final double oldPos) {
		return oldPos + (newPos - oldPos) * Helper.mc.timer.renderPartialTicks;
	}
	
    public static void drawImg(ResourceLocation loc, double posX, double posY, double width, double height) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
        float f = 1.0f / (float)width;
        float f1 = 1.0f / (float)height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(posX, posY + height, 0.0).tex(0.0f * f, (0.0f + (float)height) * f1).endVertex();
        worldrenderer.pos(posX + width, posY + height, 0.0).tex((0.0f + (float)width) * f, (0.0f + (float)height) * f1).endVertex();
        worldrenderer.pos(posX + width, posY, 0.0).tex((0.0f + (float)width) * f, 0.0f * f1).endVertex();
        worldrenderer.pos(posX, posY, 0.0).tex(0.0f * f, 0.0f * f1).endVertex();
        tessellator.draw();
    }

	public static class R3DUtils {
		public static void startDrawing() {
			GL11.glEnable((int) 3042);
			GL11.glEnable((int) 3042);
			GL11.glBlendFunc((int) 770, (int) 771);
			GL11.glEnable((int) 2848);
			GL11.glDisable((int) 3553);
			GL11.glDisable((int) 2929);
			Minecraft.getMinecraft().entityRenderer
					.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
		}

		public static void stopDrawing() {
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 3553);
			GL11.glDisable((int) 2848);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
		}

		public static void drawOutlinedBox(AxisAlignedBB box) {
			if (box == null) {
				return;
			}
			Minecraft.getMinecraft().entityRenderer
					.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 1);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
			GL11.glEnd();
		}

		public static void drawBoundingBox(AxisAlignedBB aabb) {
			WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
			Tessellator tessellator = Tessellator.getInstance();
			Minecraft.getMinecraft().entityRenderer
					.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			tessellator.draw();
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			tessellator.draw();
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			tessellator.draw();
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			tessellator.draw();
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			tessellator.draw();
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			tessellator.draw();
		}
		
		public static void drawFilledBox(AxisAlignedBB mask) {
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glEnd();
		}

		public static void drawOutlinedBoundingBox(AxisAlignedBB aabb) {
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 1);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glEnd();
		}
	}

	public static class R2DUtils {
		public static void enableGL2D() {
			GL11.glDisable((int) 2929);
			GL11.glEnable((int) 3042);
			GL11.glDisable((int) 3553);
			GL11.glBlendFunc((int) 770, (int) 771);
			GL11.glDepthMask((boolean) true);
			GL11.glEnable((int) 2848);
			GL11.glHint((int) 3154, (int) 4354);
			GL11.glHint((int) 3155, (int) 4354);
		}

		public static void disableGL2D() {
			GL11.glEnable((int) 3553);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
			GL11.glDisable((int) 2848);
			GL11.glHint((int) 3154, (int) 4352);
			GL11.glHint((int) 3155, (int) 4352);
		}

		public static void draw2DCorner(Entity e, double posX, double posY, double posZ, int color) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(posX, posY, posZ);
			GL11.glNormal3f((float) 0.0f, (float) 0.0f, (float) 0.0f);
			GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
			GlStateManager.scale(-0.1, -0.1, 0.1);
			GL11.glDisable((int) 2896);
			GL11.glDisable((int) 2929);
			GL11.glEnable((int) 3042);
			GL11.glBlendFunc((int) 770, (int) 771);
			GlStateManager.depthMask(true);
			R2DUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
			R2DUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
			R2DUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
			R2DUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
			R2DUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
			R2DUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
			R2DUtils.drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
			R2DUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
			R2DUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
			R2DUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
			R2DUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
			R2DUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
			R2DUtils.drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
			GlStateManager.popMatrix();
		}

		public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
			R2DUtils.enableGL2D();
			GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
			R2DUtils.disableGL2D();
			Gui.drawRect(0, 0, 0, 0, 0);
		}

		public static void drawRect(double x2, double y2, double x1, double y1, int color) {
			R2DUtils.enableGL2D();
			R2DUtils.glColor(color);
			R2DUtils.drawRect(x2, y2, x1, y1);
			R2DUtils.disableGL2D();
		}

		private static void drawRect(double x2, double y2, double x1, double y1) {
			GL11.glBegin((int) 7);
			GL11.glVertex2d((double) x2, (double) y1);
			GL11.glVertex2d((double) x1, (double) y1);
			GL11.glVertex2d((double) x1, (double) y2);
			GL11.glVertex2d((double) x2, (double) y2);
			GL11.glEnd();
		}

		public static void glColor(int hex) {
			float alpha = (float) (hex >> 24 & 255) / 255.0f;
			float red = (float) (hex >> 16 & 255) / 255.0f;
			float green = (float) (hex >> 8 & 255) / 255.0f;
			float blue = (float) (hex & 255) / 255.0f;
			GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
		}

		public static void drawRect(float x, float y, float x1, float y1, int color) {
			R2DUtils.enableGL2D();
			glColor(color);
			R2DUtils.drawRect(x, y, x1, y1);
			R2DUtils.disableGL2D();
		}

		public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
			R2DUtils.enableGL2D();
			glColor(borderColor);
			R2DUtils.drawRect(x + width, y, x1 - width, y + width);
			R2DUtils.drawRect(x, y, x + width, y1);
			R2DUtils.drawRect(x1 - width, y, x1, y1);
			R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
			R2DUtils.disableGL2D();
		}

		public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
			R2DUtils.enableGL2D();
			GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
			R2DUtils.drawHLine(x, x1 - 1.0f, y, borderC);
			R2DUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
			R2DUtils.disableGL2D();
		}

		public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
			R2DUtils.enableGL2D();
			GL11.glShadeModel((int) 7425);
			GL11.glBegin((int) 7);
			glColor(topColor);
			GL11.glVertex2f((float) x, (float) y1);
			GL11.glVertex2f((float) x1, (float) y1);
			glColor(bottomColor);
			GL11.glVertex2f((float) x1, (float) y);
			GL11.glVertex2f((float) x, (float) y);
			GL11.glEnd();
			GL11.glShadeModel((int) 7424);
			R2DUtils.disableGL2D();
		}

		public static void drawHLine(float x, float y, float x1, int y1) {
			if (y < x) {
				float var5 = x;
				x = y;
				y = var5;
			}
			R2DUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
		}

		public static void drawVLine(float x, float y, float x1, int y1) {
			if (x1 < y) {
				float var5 = y;
				y = x1;
				x1 = var5;
			}
			R2DUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
		}

		public static void drawHLine(float x, float y, float x1, int y1, int y2) {
			if (y < x) {
				float var5 = x;
				x = y;
				y = var5;
			}
			R2DUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
		}

		public static void drawRect(float x, float y, float x1, float y1) {
			GL11.glBegin((int) 7);
			GL11.glVertex2f((float) x, (float) y1);
			GL11.glVertex2f((float) x1, (float) y1);
			GL11.glVertex2f((float) x1, (float) y);
			GL11.glVertex2f((float) x, (float) y);
			GL11.glEnd();
		}
	}

	public static int getHexRGB(final int hex) {
		return 0xFF000000 | hex;
	}

	public static void drawCustomImage(int x, int y, int width, int height, ResourceLocation image) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) width, (int) height,
				(float) width, (float) height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
		Gui.drawRect(0, 0, 0, 0, 0);
	}
	
	public static void drawCustomImage(ResourceLocation image, int x, int y, int width, int height) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) width, (int) height,
				(float) width, (float) height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
		Gui.drawRect(0, 0, 0, 0, 0);
	}

	public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1,
			final int col1, final int col2) {
		Gui.drawRect(x, y, x2, y2, col2);
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void pre() {
		GL11.glDisable(2929);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
	}

	public static void post() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glColor3d(1.0, 1.0, 1.0);
	}

	public static void startDrawing() {
		GL11.glEnable(3042);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		Helper.mc.entityRenderer.setupCameraTransform(Helper.mc.timer.renderPartialTicks, 0);
	}

	public static void stopDrawing() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
	}

	public static Color blend(final Color color1, final Color color2, final double ratio) {
		final float r = (float) ratio;
		final float ir = 1.0f - r;
		final float[] rgb1 = new float[3];
		final float[] rgb2 = new float[3];
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		final Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir,
				rgb1[2] * r + rgb2[2] * ir);
		return color3;
	}

	public static void drawLine(final Vec2f start, final Vec2f end, final float width) {
		drawLine(start.getX(), start.getY(), end.getX(), end.getY(), width);
	}

	public static void drawLine(final Vec3f start, final Vec3f end, final float width) {
		drawLine((float) start.getX(), (float) start.getY(), (float) start.getZ(), (float) end.getX(),
				(float) end.getY(), (float) end.getZ(), width);
	}

	public static void drawLine(final float x, final float y, final float x1, final float y1, final float width) {
		drawLine(x, y, 0.0f, x1, y1, 0.0f, width);
	}

	public static void drawLine(final float x, final float y, final float z, final float x1, final float y1,
			final float z1, final float width) {
		GL11.glLineWidth(width);
		setupRender(true);
		setupClientState(GLClientState.VERTEX, true);
		RenderUtil.tessellator.addVertex(x, y, z).addVertex(x1, y1, z1).draw(3);
		setupClientState(GLClientState.VERTEX, false);
		setupRender(false);
	}

	public static void setupClientState(final GLClientState state, final boolean enabled) {
		RenderUtil.csBuffer.clear();
		if (state.ordinal() > 0) {
			RenderUtil.csBuffer.add(state.getCap());
		}
		RenderUtil.csBuffer.add(32884);
		RenderUtil.csBuffer.forEach(enabled ? RenderUtil.ENABLE_CLIENT_STATE : RenderUtil.DISABLE_CLIENT_STATE);
	}

	public static void setupRender(final boolean start) {
		if (start) {
			GlStateManager.enableBlend();
			GL11.glEnable(2848);
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			GlStateManager.blendFunc(770, 771);
			GL11.glHint(3154, 4354);
		} else {
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
			GL11.glDisable(2848);
			GlStateManager.enableDepth();
		}
		GlStateManager.depthMask(!start);
	}

	public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void layeredRect(double x1, double y1, double x2, double y2, int outline, int inline,
			int background) {
		R2DUtils.drawRect(x1, y1, x2, y2, outline);
		R2DUtils.drawRect(x1 + 1, y1 + 1, x2 - 1, y2 - 1, inline);
		R2DUtils.drawRect(x1 + 2, y1 + 2, x2 - 2, y2 - 2, background);
	}

	public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green,
			float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDisable((int) 3553);
		GL11.glEnable((int) 2848);
		GL11.glDisable((int) 2929);
		GL11.glDepthMask((boolean) false);
		GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
		RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glLineWidth((float) lineWdith);
		GL11.glColor4f((float) lineRed, (float) lineGreen, (float) lineBlue, (float) lineAlpha);
		RenderUtil
				.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glDisable((int) 2848);
		GL11.glEnable((int) 3553);
		GL11.glEnable((int) 2929);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glPopMatrix();
	}

	public static void drawBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(1, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		tessellator.draw();
	}

	public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
		float red = 0.003921569F * redRGB;
		float green = 0.003921569F * greenRGB;
		float blue = 0.003921569F * blueRGB;
		GL11.glColor4f(red, green, blue, alpha);
	}
	
    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

	public static void post3D() {
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
	}

    public static void drawRect(float x1, float y1, float x2, float y2, int color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtil.glColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y1);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glVertex2d((double)x1, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtil.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderUtil.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
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
        float var11 = (float)(color >> 24 & 255) / 255.0f;
        float var6 = (float)(color >> 16 & 255) / 255.0f;
        float var7 = (float)(color >> 8 & 255) / 255.0f;
        float var8 = (float)(color & 255) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.color((float)var6, (float)var7, (float)var8, (float)var11);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0).endVertex();
        worldRenderer.pos(right, bottom, 0.0).endVertex();
        worldRenderer.pos(right, top, 0.0).endVertex();
        worldRenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

	public static void drawBorderedRect(final double d, final double e, final double g, final double h, final float l1,
			final int col1, final int col2) {
		Gui.drawRect(d, e, g, h, col2);
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d((double) d, (double) e);
		GL11.glVertex2d((double) d, (double) h);
		GL11.glVertex2d((double) g, (double) h);
		GL11.glVertex2d((double) g, (double) e);
		GL11.glVertex2d((double) d, (double) e);
		GL11.glVertex2d((double) g, (double) e);
		GL11.glVertex2d((double) d, (double) h);
		GL11.glVertex2d((double) g, (double) h);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 255) / 255.0f;
        float f1 = (float)(col1 >> 16 & 255) / 255.0f;
        float f2 = (float)(col1 >> 8 & 255) / 255.0f;
        float f3 = (float)(col1 & 255) / 255.0f;
        float f4 = (float)(col2 >> 24 & 255) / 255.0f;
        float f5 = (float)(col2 >> 16 & 255) / 255.0f;
        float f6 = (float)(col2 >> 8 & 255) / 255.0f;
        float f7 = (float)(col2 & 255) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static Vec3 interpolateRender(final EntityPlayer player) {
        final float part = Minecraft.getMinecraft().timer.renderPartialTicks;
        final double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * part;
        final double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * part;
        final double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * part;
        return new Vec3(interpX, interpY, interpZ);
    }

    /*public static double getAnimationState(double animation, final double finalState, final double speed) {
        final float add = (float)(RenderUtil.delta * speed);
        animation = ((animation < finalState) ? ((animation + add < finalState) ? (animation += add) : finalState) : ((animation - add > finalState) ? (animation -= add) : finalState));
        return animation;
    }*/

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float)((double)delta * speed);
        animation = animation < finalState ? (animation + (double)add < finalState ? (animation += (double)add) : finalState) : (animation - (double)add > finalState ? (animation -= (double)add) : finalState);
        return animation;
    }
    
	public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }
	
    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static double interpolate(double newPos, double oldPos) {
        return oldPos + (newPos - oldPos) * (double)Minecraft.getMinecraft().timer.renderPartialTicks;
    }


    public static void entityESPBox(Entity e, Color color, EventRender3D event) {
        Minecraft.getMinecraft().getRenderManager();
        double posX = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)event.getPartialTicks() - RenderManager.renderPosX;
        Minecraft.getMinecraft().getRenderManager();
        double posY = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)event.getPartialTicks() - RenderManager.renderPosY;
        Minecraft.getMinecraft().getRenderManager();
        double posZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)event.getPartialTicks() - RenderManager.renderPosZ;
        AxisAlignedBB box = AxisAlignedBB.fromBounds((double)(posX - (double)e.width), (double)posY, (double)(posZ - (double)e.width), (double)(posX + (double)e.width), (double)(posY + (double)e.height + 0.2), (double)(posZ + (double)e.width));
        if (e instanceof EntityLivingBase) {
            box = AxisAlignedBB.fromBounds((double)(posX - (double)e.width + 0.2), (double)posY, (double)(posZ - (double)e.width + 0.2), (double)(posX + (double)e.width - 0.2), (double)(posY + (double)e.height + (e.isSneaking() ? 0.02 : 0.2)), (double)(posZ + (double)e.width - 0.2));
        }
        GL11.glLineWidth((float)3.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        RenderUtil.drawOutlinedBoundingBox(box);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
        RenderUtil.drawOutlinedBoundingBox(box);
    }

    public static void drawCircle(double x, double y, double radius, int c) {
        float f2 = (float)(c >> 24 & 255) / 255.0f;
        float f22 = (float)(c >> 16 & 255) / 255.0f;
        float f3 = (float)(c >> 8 & 255) / 255.0f;
        float f4 = (float)(c & 255) / 255.0f;
        GlStateManager.alphaFunc(516, 0.001f);
        GlStateManager.color(f22, f3, f4, f2);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tes = Tessellator.getInstance();
        double i = 0.0;
        while (i < 360.0) {
            double f5 = Math.sin(i * 3.141592653589793 / 180.0) * radius;
            double f6 = Math.cos(i * 3.141592653589793 / 180.0) * radius;
            GL11.glVertex2d((double)((double)f3 + x), (double)((double)f4 + y));
            i += 1.0;
        }
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.alphaFunc(516, 0.1f);
    }
    
    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
        GL11.glPushMatrix();
        cx *= 2.0F;
        cy *= 2.0F;
        float f = (c >> 24 & 0xFF) / 255.0F;
        float f1 = (c >> 16 & 0xFF) / 255.0F;
        float f2 = (c >> 8 & 0xFF) / 255.0F;
        float f3 = (c & 0xFF) / 255.0F;
        float theta = (float) (6.2831852D / num_segments);
        float p = (float) Math.cos(theta);
        float s = (float) Math.sin(theta);
        float x = r *= 2.0F;
        float y = 0.0F;
        enableGL2D();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(2);
        int ii = 0;
        while (ii < num_segments) {
            GL11.glVertex2f(x + cx, y + cy);
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ii++;
        }
        GL11.glEnd();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
        GlStateManager.color(1, 1, 1, 1);
        GL11.glPopMatrix();
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 0.75f, 1f).getRGB();
    }

    public static void drawIcon(float x, float y, int sizex, int sizey, ResourceLocation resourceLocation) {
        GL11.glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc((int)516, (float)0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GL11.glTranslatef((float)x, (float)y, (float)0.0f);
        RenderUtil.drawScaledRect(0, 0, 0.0f, 0.0f, sizex, sizey, sizex, sizey, sizex, sizey);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        GL11.glDisable((int)2848);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
    
    public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        Gui.drawScaledCustomSizeModalRect((int)x, (int)y, (float)u, (float)v, (int)uWidth, (int)vHeight, (int)width, (int)height, (float)tileWidth, (float)tileHeight);
    }

    public static void doGlScissor(int x2, int y2, int width, int height) {
        Minecraft mc2 = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k2 = mc2.gameSettings.guiScale;
        if (k2 == 0) {
            k2 = 1000;
        }
        while (scaleFactor < k2 && mc2.displayWidth / (scaleFactor + 1) >= 320 && mc2.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor(x2 * scaleFactor, mc2.displayHeight - (y2 + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
    }

    public static void drawRect(double d, double e, double f, double g, int color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtil.color(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)f, (double)e);
        GL11.glVertex2d((double)d, (double)e);
        GL11.glVertex2d((double)d, (double)g);
        GL11.glVertex2d((double)f, (double)g);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glPopMatrix();
    } 
    
	  public static void color(int color)
	  {
	    float f = (color >> 24 & 0xFF) / 255.0F;
	    float f1 = (color >> 16 & 0xFF) / 255.0F;
	    float f2 = (color >> 8 & 0xFF) / 255.0F;
	    float f3 = (color & 0xFF) / 255.0F;
	    GL11.glColor4f(f1, f2, f3, f);
	  }

		public static void drawFilledCircle(double x, double y, double r, int c, int id) {
			float f = (float) (c >> 24 & 0xff) / 255F;
			float f1 = (float) (c >> 16 & 0xff) / 255F;
			float f2 = (float) (c >> 8 & 0xff) / 255F;
			float f3 = (float) (c & 0xff) / 255F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(f1, f2, f3, f);
			GL11.glBegin(GL11.GL_POLYGON);
			if (id == 1) {
				GL11.glVertex2d(x, y);
				for (int i = 0; i <= 90; i++) {
					double x2 = Math.sin((i * 3.141526D / 180)) * r;
					double y2 = Math.cos((i * 3.141526D / 180)) * r;
					GL11.glVertex2d(x - x2, y - y2);
				}
			} else if (id == 2) {
				GL11.glVertex2d(x, y);
				for (int i = 90; i <= 180; i++) {
					double x2 = Math.sin((i * 3.141526D / 180)) * r;
					double y2 = Math.cos((i * 3.141526D / 180)) * r;
					GL11.glVertex2d(x - x2, y - y2);
				}
			} else if (id == 3) {
				GL11.glVertex2d(x, y);
				for (int i = 270; i <= 360; i++) {
					double x2 = Math.sin((i * 3.141526D / 180)) * r;
					double y2 = Math.cos((i * 3.141526D / 180)) * r;
					GL11.glVertex2d(x - x2, y - y2);
				}
			} else if (id == 4) {
				GL11.glVertex2d(x, y);
				for (int i = 180; i <= 270; i++) {
					double x2 = Math.sin((i * 3.141526D / 180)) * r;
					double y2 = Math.cos((i * 3.141526D / 180)) * r;
					GL11.glVertex2d(x - x2, y - y2);
				}
			} else {
				for (int i = 0; i <= 360; i++) {
					double x2 = Math.sin((i * 3.141526D / 180)) * r;
					double y2 = Math.cos((i * 3.141526D / 180)) * r;
					GL11.glVertex2f((float) (x - x2), (float) (y - y2));
				}
			}
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		public static void drawBorderRect(double x, double y, double x1, double y1, int color, double lwidth) {
			drawHLine(x, y, x1, y, (float) lwidth, color);
			drawHLine(x1, y, x1, y1, (float) lwidth, color);
			drawHLine(x, y1, x1, y1, (float) lwidth, color);
			drawHLine(x, y1, x, y, (float) lwidth, color);
		}
		
		public static void drawHLine(double x, double y, double x1, double y1, float width, int color) {
			float var11 = (color >> 24 & 0xFF) / 255.0F;
			float var6 = (color >> 16 & 0xFF) / 255.0F;
			float var7 = (color >> 8 & 0xFF) / 255.0F;
			float var8 = (color & 0xFF) / 255.0F;
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(var6, var7, var8, var11);
			GL11.glPushMatrix();
			GL11.glLineWidth(width);
			GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x1, y1);
			GL11.glEnd();

			GL11.glLineWidth(1);

			GL11.glPopMatrix();
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
			GlStateManager.color(1, 1, 1, 1);

		}

		public static void renderBox(Entity entity,double r,double g, double b) {
			Minecraft mc = Minecraft.getMinecraft();
			double x = RenderUtil.interpolate((double)entity.posX, (double)entity.lastTickPosX);
	        double y = RenderUtil.interpolate((double)entity.posY, (double)entity.lastTickPosY);
	        double z = RenderUtil.interpolate((double)entity.posZ, (double)entity.lastTickPosZ);
			GL11.glPushMatrix();
			RenderUtil.pre();
	        GL11.glLineWidth((float)1.0f);
	        GL11.glEnable((int)2848);
	        GL11.glColor3d(r,g,b);
	        mc.getRenderManager();
	        mc.getRenderManager();
	        mc.getRenderManager();
	        RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(
	        		entity.boundingBox.minX
	                - 0.05
	                - entity.posX
	                + (entity.posX - mc.getRenderManager().renderPosX),
	                entity.boundingBox.minY
	                - entity.posY
	                + (entity.posY - mc.getRenderManager().renderPosY),
	                entity.boundingBox.minZ
	                - 0.05
	                - entity.posZ
	                + (entity.posZ - mc.getRenderManager().renderPosZ),
	                entity.boundingBox.maxX
	                + 0.05
	                - entity.posX
	                + (entity.posX - mc.getRenderManager().renderPosX),
	                entity.boundingBox.maxY
	                + 0.1
	                - entity.posY
	                + (entity.posY - mc.getRenderManager().renderPosY),
	                entity.boundingBox.maxZ
	                + 0.05
	                - entity.posZ
	                + (entity.posZ - mc.getRenderManager().renderPosZ)));
	        GL11.glDisable((int)2848);
	        RenderUtil.post();
	        GL11.glPopMatrix();
		}

		public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
			R2DUtils.enableGL2D();
			GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
			R2DUtils.disableGL2D();
			Gui.drawRect(0, 0, 0, 0, 0);
		}
		
	    public static void drawblock(double a2, double a22, double a3, int a4, int a5, float a6) {
	        float a7 = (float)(a4 >> 24 & 255) / 255.0f;
	        float a8 = (float)(a4 >> 16 & 255) / 255.0f;
	        float a9 = (float)(a4 >> 8 & 255) / 255.0f;
	        float a10 = (float)(a4 & 255) / 255.0f;
	        float a11 = (float)(a5 >> 24 & 255) / 255.0f;
	        float a12 = (float)(a5 >> 16 & 255) / 255.0f;
	        float a13 = (float)(a5 >> 8 & 255) / 255.0f;
	        float a14 = (float)(a5 & 255) / 255.0f;
	        GL11.glPushMatrix();
	        GL11.glEnable(3042);
	        GL11.glBlendFunc(770, 771);
	        GL11.glDisable(3553);
	        GL11.glEnable(2848);
	        GL11.glDisable(2929);
	        GL11.glDepthMask(false);
	        GL11.glColor4f(a8, a9, a10, a7);
	        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(a2, a22, a3, a2 + 1.0, a22 + 1.0, a3 + 1.0));
	        GL11.glLineWidth(a6);
	        GL11.glColor4f(a12, a13, a14, a11);
	        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(a2, a22, a3, a2 + 1.0, a22 + 1.0, a3 + 1.0));
	        GL11.glDisable(2848);
	        GL11.glEnable(3553);
	        GL11.glEnable(2929);
	        GL11.glDepthMask(true);
	        GL11.glDisable(3042);
	        GL11.glPopMatrix();
	    }

		public static void drawBorderedCircle(double x, double y, float radius, int outsideC, int insideC) {
			// GL11.glEnable((int)3042);
			GL11.glDisable((int) 3553);
			GL11.glBlendFunc((int) 770, (int) 771);
			GL11.glEnable((int) 2848);
			GL11.glPushMatrix();
			float scale = 0.1f;
			GL11.glScalef((float) 0.1f, (float) 0.1f, (float) 0.1f);
			drawCircle(x *= 10, y *= 10, radius *= 10.0f, insideC);
			// drawUnfilledCircle(x, y, radius, 1.0f, outsideC);
			GL11.glScalef((float) 10.0f, (float) 10.0f, (float) 10.0f);
			GL11.glPopMatrix();
			GL11.glEnable((int) 3553);
			// GL11.glDisable((int)3042);
			GL11.glDisable((int) 2848);
		}

		public static void drawFullCircle(int cx, int cy, double r, int segments, float lineWidth, int part, int c) {
			GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
			r *= 2.0;
			cx *= 2;
			cy *= 2;
			float f2 = (float) (c >> 24 & 255) / 255.0f;
			float f22 = (float) (c >> 16 & 255) / 255.0f;
			float f3 = (float) (c >> 8 & 255) / 255.0f;
			float f4 = (float) (c & 255) / 255.0f;
			GL11.glEnable((int) 3042);
			GL11.glLineWidth((float) lineWidth);
			GL11.glDisable((int) 3553);
			GL11.glEnable((int) 2848);
			GL11.glBlendFunc((int) 770, (int) 771);
			GL11.glColor4f((float) f22, (float) f3, (float) f4, (float) f2);
			GL11.glBegin((int) 3);
			int i = segments - part;
			while (i <= segments) {
				double x = Math.sin((double) i * Math.PI / 180.0) * r;
				double y = Math.cos((double) i * Math.PI / 180.0) * r;
				GL11.glVertex2d((double) ((double) cx + x), (double) ((double) cy + y));
				++i;
			}
			GL11.glEnd();
			GL11.glDisable((int) 2848);
			GL11.glEnable((int) 3553);
			GL11.glDisable((int) 3042);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
		}
		
		public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, float targetHeight, float p_147046_3_,
				float p_147046_4_, EntityLivingBase p_147046_5_) {
			GlStateManager.enableColorMaterial();
			GlStateManager.pushMatrix();
			GlStateManager.translate(p_147046_0_, p_147046_1_, 40.0f);
			GlStateManager.scale(-targetHeight, targetHeight, targetHeight);
			GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
			float var6 = p_147046_5_.renderYawOffset;
			float var7 = p_147046_5_.rotationYaw;
			float var8 = p_147046_5_.rotationPitch;
			float var9 = p_147046_5_.prevRotationYawHead;
			float var10 = p_147046_5_.rotationYawHead;
			GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
			RenderHelper.enableStandardItemLighting();
			GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
			GlStateManager.rotate((-(float) Math.atan(p_147046_4_ / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
			p_147046_5_.renderYawOffset = (float) Math.atan(p_147046_3_ / 40.0f) * -14.0f;
			p_147046_5_.rotationYaw = (float) Math.atan(p_147046_3_ / 40.0f) * -14.0f;
			p_147046_5_.rotationPitch = (-(float) Math.atan(p_147046_4_ / 40.0f)) * 15.0f;
			p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
			p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
			GlStateManager.translate(0.0f, 0.0f, 0.0f);
			RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
			var11.setPlayerViewY(180.0f);
			var11.setRenderShadow(false);
			var11.renderEntityWithPosYaw(p_147046_5_, 0.0, 0.0, 0.0, 0.0f, 1.0f);
			var11.setRenderShadow(true);
			p_147046_5_.renderYawOffset = var6;
			p_147046_5_.rotationYaw = var7;
			p_147046_5_.rotationPitch = var8;
			p_147046_5_.prevRotationYawHead = var9;
			p_147046_5_.rotationYawHead = var10;
			GlStateManager.popMatrix();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.disableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		}

		public static double[] convertTo2D(double x, double y, double z) {
		    double[] arrd;
		    java.nio.FloatBuffer screenCoords = org.lwjgl.BufferUtils.createFloatBuffer((int)3);
		    java.nio.IntBuffer viewport = org.lwjgl.BufferUtils.createIntBuffer((int)16);
		    java.nio.FloatBuffer modelView = org.lwjgl.BufferUtils.createFloatBuffer((int)16);
		    java.nio.FloatBuffer projection = org.lwjgl.BufferUtils.createFloatBuffer((int)16);
		    org.lwjgl.opengl.GL11.glGetFloat((int)2982, (java.nio.FloatBuffer)modelView);
		    org.lwjgl.opengl.GL11.glGetFloat((int)2983, (java.nio.FloatBuffer)projection);
		    org.lwjgl.opengl.GL11.glGetInteger((int)2978, (java.nio.IntBuffer)viewport);
		    boolean result = org.lwjgl.util.glu.GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (java.nio.FloatBuffer)modelView, (java.nio.FloatBuffer)projection, (java.nio.IntBuffer)viewport, (java.nio.FloatBuffer)screenCoords);
		    if (result) {
		        double[] arrd2 = new double[3];
		        arrd2[0] = screenCoords.get(0);
		        arrd2[1] = (float)org.lwjgl.opengl.Display.getHeight() - screenCoords.get(1);
		        arrd = arrd2;
		        arrd2[2] = screenCoords.get(2);
		    } else {
		        arrd = null;
		    }
		    return arrd;
		}

	    public static void drawArc(float n, float n2, double n3, final int n4, final int n5, final double n6, final int n7) {
	        n3 *= 2.0;
	        n *= 2.0f;
	        n2 *= 2.0f;
	        final float n8 = (n4 >> 24 & 0xFF) / 255.0f;
	        final float n9 = (n4 >> 16 & 0xFF) / 255.0f;
	        final float n10 = (n4 >> 8 & 0xFF) / 255.0f;
	        final float n11 = (n4 & 0xFF) / 255.0f;
	        GL11.glDisable(2929);
	        GL11.glEnable(3042);
	        GL11.glDisable(3553);
	        GL11.glBlendFunc(770, 771);
	        GL11.glDepthMask(true);
	        GL11.glEnable(2848);
	        GL11.glHint(3154, 4354);
	        GL11.glHint(3155, 4354);
	        GL11.glScalef(0.5f, 0.5f, 0.5f);
	        GL11.glLineWidth((float)n7);
	        GL11.glEnable(2848);
	        GL11.glColor4f(n9, n10, n11, n8);
	        GL11.glBegin(3);
	        for (int n12 = n5; n12 <= n6; ++n12) {
	            GL11.glVertex2d(n + Math.sin(n12 * 3.141592653589793 / 180.0) * n3, n2 + Math.cos(n12 * 3.141592653589793 / 180.0) * n3);
	        }
	        GL11.glEnd();
	        GL11.glDisable(2848);
	        GL11.glScalef(2.0f, 2.0f, 2.0f);
	        GL11.glEnable(3553);
	        GL11.glDisable(3042);
	        GL11.glEnable(2929);
	        GL11.glDisable(2848);
	        GL11.glHint(3154, 4352);
	        GL11.glHint(3155, 4352);
	    }
	    
	    public static void drawImage(final ResourceLocation resourceLocation, final float n, final float n2, final float n3, final float n4) {
	        drawImage(resourceLocation, (int)n, (int)n2, (int)n3, (int)n4, 1.0f);
	    }
	    
	    public static void drawImage(final ResourceLocation resourceLocation, final int n, final int n2, final int n3, final int n4, final float n5) {
	        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
	        GL11.glDisable(2929);
	        GL11.glEnable(3042);
	        GL11.glDepthMask(false);
	        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	        GL11.glColor4f(1.0f, 1.0f, 1.0f, n5);
	        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
	        Gui.drawModalRectWithCustomSizedTexture(n, n2, 0.0f, 0.0f, n3, n4, (float)n3, (float)n4);
	        GL11.glDepthMask(true);
	        GL11.glDisable(3042);
	        GL11.glEnable(2929);
	    }

		public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width,
				float height, float textureWidth, float textureHeight) {
			float f = 1.0F / textureWidth;
			float f1 = 1.0F / textureHeight;
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldrenderer.pos((double) x, (double) (y + height), 0.0D)
					.tex((double) (u * f), (double) ((v + (float) height) * f1)).endVertex();
			worldrenderer.pos((double) (x + width), (double) (y + height), 0.0D)
					.tex((double) ((u + (float) width) * f), (double) ((v + (float) height) * f1)).endVertex();
			worldrenderer.pos((double) (x + width), (double) y, 0.0D)
					.tex((double) ((u + (float) width) * f), (double) (v * f1)).endVertex();
			worldrenderer.pos((double) x, (double) y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();
			tessellator.draw();
		}

	    public static void drawHollowBox(float x, float y, float x1, float y1, float thickness, int color) {
	        RenderUtil.drawHorizontalLine(x, y, x1, thickness, color);
	        RenderUtil.drawHorizontalLine(x, y1, x1, thickness, color);
	        RenderUtil.drawVerticalLine(x, y, y1, thickness, color);
	        RenderUtil.drawVerticalLine(x1 - thickness, y, y1, thickness, color);
	    }

	    public static void drawHorizontalLine(float x, float y, float x1, float thickness, int color) {
	        RenderUtil.drawRect2(x, y, x1, y + thickness, color);
	    }

	    public static void drawRect2(double x, double y, double x2, double y2, int color) {
	        RenderUtil.drawRect(x, y, x2, y2, color);
	    }

	    public static void drawVerticalLine(float x, float y, float y1, float thickness, int color) {
	        RenderUtil.drawRect2(x, y, x + thickness, y1, color);
	    }

		public static void drawWolframEntityESP(EntityLivingBase entity, int rgb, double posX, double posY, double posZ) {
			GL11.glPushMatrix();
			GL11.glTranslated(posX, posY, posZ);
			GL11.glRotatef(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
			setColor(rgb);
			enableGL3D(1.0F);
			Cylinder c = new Cylinder();
			GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			c.setDrawStyle(100011);
			c.draw(0.5F, 0.5F, entity.height + 0.1F, 18, 1);
			disableGL3D();
			GL11.glPopMatrix();
		}
		
		public static void enableGL3D(float lineWidth) {
			GL11.glDisable(3008);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GL11.glDisable(3553);
			GL11.glDisable(2929);
			GL11.glDepthMask(false);
			GL11.glEnable(2884);
			Shaders.disableLightmap();
			Shaders.disableFog();
			GL11.glEnable(2848);
			GL11.glHint(3154, 4354);
			GL11.glHint(3155, 4354);
			GL11.glLineWidth(lineWidth);
		}

		public static void disableGL3D() {
			GL11.glEnable(3553);
			GL11.glEnable(2929);
			GL11.glDisable(3042);
			GL11.glEnable(3008);
			GL11.glDepthMask(true);
			GL11.glCullFace(1029);
			GL11.glDisable(2848);
			GL11.glHint(3154, 4352);
			GL11.glHint(3155, 4352);
		}
		
		public static void setColor(int colorHex) {
			float alpha = (float) (colorHex >> 24 & 255) / 255.0F;
			float red = (float) (colorHex >> 16 & 255) / 255.0F;
			float green = (float) (colorHex >> 8 & 255) / 255.0F;
			float blue = (float) (colorHex & 255) / 255.0F;
			GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
		}


		   public static void drawRoundRect(double d, double e, double g, double h, float f, int color) {
		      drawRect(d + 1.0D, e, g - 1.0D, h, color);
		      drawRect(d, e + 1.0D, d + 1.0D, h - 1.0D, color);
		      drawRect(d + 1.0D, e + 1.0D, d + 0.5D, e + 0.5D, color);
		      drawRect(d + 1.0D, e + 1.0D, d + 0.5D, e + 0.5D, color);
		      drawRect(g - 1.0D, e + 1.0D, g - 0.5D, e + 0.5D, color);
		      drawRect(g - 1.0D, e + 1.0D, g, h - 1.0D, color);
		      drawRect(d + 1.0D, h - 1.0D, d + 0.5D, h - 0.5D, color);
		      drawRect(g - 1.0D, h - 1.0D, g - 0.5D, h - 0.5D, color);
		   }

		public static void drawRoundedRect(float x, float y, float x1, float y1, float f, int insideC) {
		      R2DUtils.enableGL2D();
		      GL11.glScalef(0.5F, 0.5F, 0.5F);
		      R2DUtils.drawVLine(x *= 2.0F, (y *= 2.0F) + 1.0F, (y1 *= 2.0F) - 2.0F, insideC);
		      R2DUtils.drawVLine((x1 *= 2.0F) - 1.0F, y + 1.0F, y1 - 2.0F, insideC);
		      R2DUtils.drawHLine(x + 2.0F, x1 - 3.0F, y, insideC);
		      R2DUtils.drawHLine(x + 2.0F, x1 - 3.0F, y1 - 1.0F, insideC);
		      R2DUtils.drawHLine(x + 1.0F, x + 1.0F, y + 1.0F, insideC);
		      R2DUtils.drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, insideC);
		      R2DUtils.drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, insideC);
		      R2DUtils.drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, insideC);
		      R2DUtils.drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
		      GL11.glScalef(2.0F, 2.0F, 2.0F);
		      R2DUtils.disableGL2D();
		      Gui.drawRect(0, 0, 0, 0, 0);
		}

		   public static void drawFancy(double d, double e, double f2, double f3, int paramColor) {
			      float alpha = (float)(paramColor >> 24 & 255) / 255.0F;
			      float red = (float)(paramColor >> 16 & 255) / 255.0F;
			      float green = (float)(paramColor >> 8 & 255) / 255.0F;
			      float blue = (float)(paramColor & 255) / 255.0F;
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

		   public static double getEntityRenderX(Entity entity) {
		        return (entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * ((double) Minecraft.getMinecraft().timer.renderPartialTicks))) - RenderManager.renderPosX;
		    }

		    public static double getEntityRenderY(Entity entity) {
		        return (entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * ((double) Minecraft.getMinecraft().timer.renderPartialTicks))) - RenderManager.renderPosY;
		    }

		    public static double getEntityRenderZ(Entity entity) {
		        return (entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * ((double) Minecraft.getMinecraft().timer.renderPartialTicks))) - RenderManager.renderPosZ;
		    }

		    public static void drawCircle(float x, float y, float r, float lineWidth, boolean isFull, int color) {
		        drawCircle(x, y, (double) r, 10, lineWidth, 360, isFull, color);
		    }

		    public static void drawCircle(float cx, float cy, double r, int segments, float lineWidth, int part, boolean isFull, int c) {
		        GL11.glScalef(0.5f, 0.5f, 0.5f);
		        double r2 = r * 2.0d;
		        float cx2 = cx * 2.0f;
		        float cy2 = cy * 2.0f;
		        GL11.glEnable(3042);
		        GL11.glLineWidth(lineWidth);
		        GL11.glDisable(3553);
		        GL11.glEnable(2848);
		        GL11.glBlendFunc(770, 771);
		        GL11.glColor4f(((float) ((c >> 16) & 255)) / 255.0f, ((float) ((c >> 8) & 255)) / 255.0f, ((float) (c & 255)) / 255.0f, ((float) ((c >> 24) & 255)) / 255.0f);
		        GL11.glBegin(3);
		        for (int i = segments - part; i <= segments; i++) {
		            GL11.glVertex2d(((double) cx2) + (Math.sin((((double) i) * 3.141592653589793d) / 180.0d) * r2), ((double) cy2) + (Math.cos((((double) i) * 3.141592653589793d) / 180.0d) * r2));
		            if (isFull) {
		                GL11.glVertex2d((double) cx2, (double) cy2);
		            }
		        }
		        GL11.glEnd();
		        GL11.glDisable(2848);
		        GL11.glEnable(3553);
		        GL11.glDisable(3042);
		        GL11.glScalef(2.0f, 2.0f, 2.0f);
		    }
	}
