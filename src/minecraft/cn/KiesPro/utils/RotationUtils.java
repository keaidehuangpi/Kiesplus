package cn.KiesPro.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils
{
  public static float[] getBlockRotations(int x, int y, int z, EnumFacing facing)
  {
    Minecraft mc = Minecraft.getMinecraft();
    EntitySnowball temp = new EntitySnowball(Minecraft.theWorld);
    temp.posX = (x + 0.5D);
    temp.posY = (y + 0.5D);
    temp.posZ = (z + 0.5D);
    return getAngles(temp);
  }
  
  public static float[] getAngles(Entity e)
  {
    Minecraft mc = Minecraft.getMinecraft();
    return new float[] { getYawChangeToEntity(e) + Minecraft.thePlayer.rotationYaw, getPitchChangeToEntity(e) + Minecraft.thePlayer.rotationPitch };
  }
  
  public static float[] getRotations(Entity entity)
  {
    if (entity == null) {
      return null;
    }
    Minecraft.getMinecraft();
    double diffX = entity.posX - Minecraft.thePlayer.posX;
    Minecraft.getMinecraft();
    double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
    double diffY;
    double diffx;
    if ((entity instanceof EntityLivingBase))
    {
      EntityLivingBase elb = (EntityLivingBase)entity;
      Minecraft.getMinecraft();
      Minecraft.getMinecraft();
      diffY = elb.posY + (elb.getEyeHeight() - 0.2D) - (Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight());
    }
    else
    {
      Minecraft.getMinecraft();
      Minecraft.getMinecraft();
      diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight());
    }
    double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
    float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
    float pitch = (float)(-Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D) - 60.0F;
    return new float[] { yaw, pitch };
  }
  
  public static float getYawChangeToEntity(Entity entity)
  {
    Minecraft mc = Minecraft.getMinecraft();
    double deltaX = entity.posX - Minecraft.thePlayer.posX;
    double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
    double yawToEntity = (deltaZ < 0.0D) && (deltaX > 0.0D) ? -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0D) && (deltaX < 0.0D) ? 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(-Math.atan(deltaX / deltaZ));
    return MathHelper.wrapAngleTo180_float(-Minecraft.thePlayer.rotationYaw - (float)yawToEntity);
  }
  
  public static float getPitchChangeToEntity(Entity entity)
  {
    Minecraft mc = Minecraft.getMinecraft();
    double deltaX = entity.posX - Minecraft.thePlayer.posX;
    double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
    double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - 0.4D - Minecraft.thePlayer.posY;
    double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
    double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
    return -MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)pitchToEntity);
  }
  
  public static float[] getRotationFromPosition(double x, double z, double y)
  {
    Minecraft.getMinecraft();
    double xDiff = x - Minecraft.thePlayer.posX;
    Minecraft.getMinecraft();
    double zDiff = z - Minecraft.thePlayer.posZ;
    Minecraft.getMinecraft();
    double yDiff = y - Minecraft.thePlayer.posY - 0.8D;
    double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
    float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) + 90.0F;
    float pitch = (float)(-Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D) + 90.0F;
    return new float[] { yaw, pitch };
  }
  
  public static float getDistanceBetweenAngles(float angle1, float angle2)
  {
    float angle3 = Math.abs(angle1 - angle2) % 360.0F;
    if (angle3 > 180.0F) {
      angle3 = 0.0F;
    }
    return angle3;
  }
  
  public static float[] getRotations(Vec3 position)
  {
    return getRotations(Minecraft.thePlayer.getPositionVector().addVector(0.0D, Minecraft.thePlayer.getEyeHeight(), 0.0D), position);
  }
  
  public static float[] getRotations(Vec3 origin, Vec3 position)
  {
    Vec3 difference = position.subtract(origin);
    double distance = difference.flat().lengthVector();
    float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0F;
    float pitch = (float)-Math.toDegrees(Math.atan2(difference.yCoord, distance));
    return new float[] { yaw, pitch };
  }
  
  public static float[] getRotations(BlockPos pos)
  {
    return getRotations(Minecraft.thePlayer.getPositionVector().addVector(0.0D, Minecraft.thePlayer.getEyeHeight(), 0.0D), new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D));
  }
  
  public static float[] getBowAngles(Entity entity)
  {
    double xDelta = entity.posX - entity.lastTickPosX;
    double zDelta = entity.posZ - entity.lastTickPosZ;
    Minecraft.getMinecraft();
    double d = Minecraft.thePlayer.getDistanceToEntity(entity);
    d -= d % 0.8D;
    double xMulti = 1.0D;
    double zMulti = 1.0D;
    boolean sprint = entity.isSprinting();
    xMulti = d / 0.8D * xDelta * (sprint ? 1.25D : 1.0D);
    zMulti = d / 0.8D * zDelta * (sprint ? 1.25D : 1.0D);
    Minecraft.getMinecraft();
    double x = entity.posX + xMulti - Minecraft.thePlayer.posX;
    Minecraft.getMinecraft();
    double z = entity.posZ + zMulti - Minecraft.thePlayer.posZ;
    Minecraft.getMinecraft();
    Minecraft.getMinecraft();
    double y = Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
    Minecraft.getMinecraft();
    double dist = Minecraft.thePlayer.getDistanceToEntity(entity);
    float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0F;
    float pitch = (float)Math.toDegrees(Math.atan2(y, dist));
    return new float[] { yaw, pitch };
  }
  
  public static float normalizeAngle(float angle)
  {
    return (angle + 360.0F) % 360.0F;
  }
  
  public static float getTrajAngleSolutionLow(float d3, float d1, float velocity)
  {
    float g = 0.006F;
    float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0F * d1 * (velocity * velocity));
    return (float)Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (g * d3)));
  }
  
  public static Vec3 getEyesPos()
  {
    return new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
  }
  
  public static float[] getRotationsBlock(BlockPos pos)
  {
    Minecraft mc = Minecraft.getMinecraft();
    double d0 = pos.getX() - Minecraft.thePlayer.posX;
    double d1 = pos.getY() - (Minecraft.thePlayer.posY + Minecraft.thePlayer.getEyeHeight());
    double d2 = pos.getZ() - Minecraft.thePlayer.posZ;
    double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
   // float f2 = (float)(MathHelper(d2, d0) * 180.0D / 3.141592653589793D) - 90.0F;
    float f1 = (float)-Math.toDegrees(Math.atan2(d3, d1));
    return new float[] { f1 };
  }
  
  public static float[] getNeededRotations(Vec3 vec)
  {
    Vec3 eyesPos = getEyesPos();
    double diffX = vec.xCoord - eyesPos.xCoord + 0.5D;
    double diffY = vec.yCoord - eyesPos.yCoord + 0.5D;
    double diffZ = vec.zCoord - eyesPos.zCoord + 0.5D;
    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
    float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
    float pitch = (float)(-Math.atan2(diffY, diffXZ) * 180.0D / 3.141592653589793D);
    float[] arrf = new float[2];
    arrf[0] = MathHelper.wrapAngleTo180_float(yaw);
    arrf[1] = (Minecraft.getMinecraft().gameSettings.keyBindJump.pressed ? 90.0F : MathHelper.wrapAngleTo180_float(pitch));
    return arrf;
  }
  
  public static void faceVectorPacketInstant(Vec3 vec)
  {
    float[] rotations = getNeededRotations(vec);
    Minecraft.getMinecraft();
    Minecraft.getMinecraft();
    Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], Minecraft.thePlayer.onGround));
  }
}
