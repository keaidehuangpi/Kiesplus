package cn.KiesPro.module.modules.movement;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import cn.KiesPro.Client;
import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.rendering.EventRender3D;
import cn.KiesPro.api.events.world.EventMove;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.combat.Killaura;
import cn.KiesPro.utils.MoveUtils;
import cn.KiesPro.utils.RotationUtils;
import cn.KiesPro.utils.math.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class TargetStrafe
  extends Module
{
  private static Numbers<Double> radius = new Numbers("Radius", "Radius", Double.valueOf(2.0D), Double.valueOf(1.0D), Double.valueOf(3.0D), Double.valueOf(0.25D));
  private static Numbers<Double> lol = new Numbers("Sides", "Sides", Double.valueOf(9.0D), Double.valueOf(5.0D), Double.valueOf(25.0D), Double.valueOf(1.0D));
  private static Option<Boolean> render = new Option("Render", "Render", Boolean.valueOf(true));
  private static int direction = -1;
  public static float hue;
  private static boolean strafing = false;
  static int preview;
  
  public static void setSpeed(final EventMove moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
      double forward = pseudoForward;
      double strafe = pseudoStrafe;
      float yaw = pseudoYaw;

      if (forward == 0.0 && strafe == 0.0) {
          moveEvent.setZ(0);
          moveEvent.setX(0);
      } else {
          if (forward != 0.0) {
              if (strafe > 0.0) {
                  yaw += ((forward > 0.0) ? -45 : 45);
              } else if (strafe < 0.0) {
                  yaw += ((forward > 0.0) ? 45 : -45);
              }
              strafe = 0.0;
              if (forward > 0.0) {
                  forward = 1.0;
              } else if (forward < 0.0) {
                  forward = -1.0;
              }
          }
          final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
          final double sin = Math.sin(Math.toRadians(yaw + 90.0f));

          moveEvent.setX((forward * moveSpeed * cos + strafe * moveSpeed * sin));
          moveEvent.setZ((forward * moveSpeed * sin - strafe * moveSpeed * cos));
      }
  }
  
  public TargetStrafe()
  {
    super("TargetStrafe", new String[] { "ts", "strafe" }, ModuleType.Movement);
    setColor(new Color(158, 205, 125).getRGB());
    addValues(new Value[] { radius, lol, render });
  }
  
  @EventHandler
  public void onRender3D(EventRender3D e)
  {
    Killaura killaura = (Killaura)Client.getModuleManager().getModuleByClass(Killaura.class);
    if ((((Boolean)render.getValue()).booleanValue()) && (Killaura.target != null))
    {
      Color col = new Color(255, 255, 255);
      if (this.mc.gameSettings.thirdPersonView == 1) {
        col = new Color(149, 255, 147);
      }
      if (this.mc.gameSettings.thirdPersonView == 0) {
        col = new Color(255, 255, 255);
      }
      drawCircle2((EntityLivingBase)Killaura.target, col.getRGB(), e);
    }
  }
  
  @EventHandler
  private void onUpdate(EventPreUpdate event)
  {
    if ((!canStrafe()) && (strafing)) {
      strafing = false;
    }
    if ((Minecraft.thePlayer.isCollidedHorizontally) || (isAboveVoid())) {
      switchDirection();
    }
    if (this.mc.gameSettings.keyBindLeft.isPressed()) {
      direction = 1;
    }
    if (this.mc.gameSettings.keyBindRight.isPressed()) {
      direction = -1;
    }
  }
  
  private void switchDirection()
  {
    direction = direction == 1 ? -1 : 1;
  }
  
  public static void strafe(EventMove event, double moveSpeed)
  {
    Killaura killaura = (Killaura)Client.getModuleManager().getModuleByClass(Killaura.class);
    
    strafing = true;
    if (Killaura.target != null)
    {
      EntityLivingBase target = (EntityLivingBase)Killaura.target;
      float[] rotations = RotationUtils.getRotations(target);
      Minecraft.getMinecraft();
      if (Minecraft.thePlayer.getDistanceToEntity(target) <= ((Double)radius.getValue()).doubleValue()) {
        MoveUtils.setSpeed_2(event, moveSpeed, rotations[0], direction, 0.0D);
      } else {
        MoveUtils.setSpeed_2(event, moveSpeed, rotations[0], direction, 1.0D);
      }
    }
  }
  
  public static void strafe(float speed)
  {
    Minecraft.getMinecraft();
    if (!Minecraft.thePlayer.moving()) {
      return;
    }
    double yaw = getDirection();
    Minecraft.getMinecraft();Minecraft.thePlayer.motionX = (-Math.sin(yaw) * speed);
    Minecraft.getMinecraft();Minecraft.thePlayer.motionZ = (Math.cos(yaw) * speed);
  }
  
  public static double getDirection()
  {
    Minecraft.getMinecraft();float rotationYaw = Minecraft.thePlayer.rotationYaw;
    
    Minecraft.getMinecraft();
    if (Minecraft.thePlayer.moveForward < 0.0F) {
      rotationYaw += 180.0F;
    }
    float forward = 1.0F;
    Minecraft.getMinecraft();
    if (Minecraft.thePlayer.moveForward < 0.0F)
    {
      forward = -0.5F;
    }
    else
    {
      Minecraft.getMinecraft();
      if (Minecraft.thePlayer.moveForward > 0.0F) {
        forward = 0.5F;
      }
    }
    Minecraft.getMinecraft();
    if (Minecraft.thePlayer.moveStrafing > 0.0F) {
      rotationYaw -= 90.0F * forward;
    }
    Minecraft.getMinecraft();
    if (Minecraft.thePlayer.moveStrafing < 0.0F) {
      rotationYaw += 90.0F * forward;
    }
    return Math.toRadians(rotationYaw);
  }
  
  public boolean isAboveVoid()
  {
    for (int i = (int)Math.ceil(Minecraft.thePlayer.posY); i >= 0; i--)
    {
      double var10004 = i;
      if (Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, var10004, Minecraft.thePlayer.posZ)).getBlock() != Blocks.air) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean canStrafe()
  {
    Killaura killaura = (Killaura)Client.getModuleManager().getModuleByClass(Killaura.class);
    return (killaura.isEnabled()) && (Killaura.target != null) && (ModuleManager.getModuleByName("TargetStrafe").isEnabled()) && ((ModuleManager.getModuleByName("Speed").isEnabled()) || (ModuleManager.getModuleByName("Flight").isEnabled()));
  }
  
  public static void drawCircle2(EntityLivingBase entity, int color, EventRender3D e)
  {
    double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * e.getPartialTicks() - RenderManager.renderPosX;
    double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * e.getPartialTicks() - RenderManager.renderPosY;
    double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * e.getPartialTicks() - RenderManager.renderPosZ;
    GL11.glPushMatrix();
    GL11.glTranslated(x, y, z);
    GL11.glRotatef(-entity.width, 0.0F, 1.0F, 0.0F);
    glColor(color);
    enableSmoothLine(1.0F);
    Cylinder c = new Cylinder();
    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
    c.setDrawStyle(100011);
    c.draw(((Double)radius.getValue()).floatValue(), ((Double)radius.getValue()).floatValue() + 0.005F, 0.005F, ((Double)lol.getValue()).intValue(), 1);
    
    disableSmoothLine();
    GL11.glPopMatrix();
  }
  
  public static void glColor(int hex)
  {
    float alpha = (hex >> 24 & 0xFF) / 255.0F;
    float red = (hex >> 16 & 0xFF) / 255.0F;
    float green = (hex >> 8 & 0xFF) / 255.0F;
    float blue = (hex & 0xFF) / 255.0F;
    GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
  }
  
  public static void enableSmoothLine(float width)
  {
    GL11.glDisable(3008);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glDisable(3553);
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    GL11.glEnable(2884);
    GL11.glEnable(2848);
    GL11.glHint(3154, 4354);
    GL11.glHint(3155, 4354);
    GL11.glLineWidth(width);
  }
  
  public static void disableSmoothLine()
  {
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

public final boolean doStrafeAtSpeed(EventMove event, final double moveSpeed) {
        final boolean strafe = canStrafe();

        if (strafe) {
            float[] rotations = RotationUtil.getRotations(Killaura.curTarget);
            if (mc.thePlayer.getDistanceToEntity(Killaura.curTarget) <= radius.getValue()) {
                setSpeed(event, moveSpeed, rotations[0], direction, 0);
            } else {
                setSpeed(event, moveSpeed, rotations[0], direction, 1);
            }
        }

        return strafe;
    }
}
