package cn.KiesPro.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.KiesPro.api.events.world.EventMove;
import cn.KiesPro.management.FriendManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import optifine.Reflector;

public class Wrapper {
    public static final byte HOTBAR_DOWN = 1;
    public static final byte HOTBAR_UP = 0;
    private static BlockUtils blockUtils = new BlockUtils();
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static FontRenderer fr() {
        return Minecraft.fontRendererObj;
    }

    public static int width() {
        new ScaledResolution(mc);
        return ScaledResolution.getScaledWidth();
    }

    public static int height() {
        return new ScaledResolution(mc).getScaledHeight();
    }

    public static Block block(BlockPos pos, double offset) {
        return Minecraft.theWorld.getBlockState(pos.add(0.0d, offset, 0.0d)).getBlock();
    }

    public static void position(double x2, double y2, double z2, boolean grounded) {
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x2, y2, z2, grounded));
    }

    public static float pitch() {
        return Minecraft.thePlayer.rotationPitch;
    }

    public static void pitch(float pitch) {
        Minecraft.thePlayer.rotationPitch = pitch;
    }

    public static float yaw() {
        return Minecraft.thePlayer.rotationYaw;
    }

    public static void yaw(float yaw) {
        Minecraft.thePlayer.rotationYaw = yaw;
    }

    public static double x() {
        return Minecraft.thePlayer.posX;
    }

    public static void x(double x2) {
        Minecraft.thePlayer.posX = x2;
    }

    public static double y() {
        return Minecraft.thePlayer.posY;
    }

    public static void y(double y2) {
        Minecraft.thePlayer.posY = y2;
    }

    public static double z() {
        return Minecraft.thePlayer.posZ;
    }

    public static void z(double z2) {
        Minecraft.thePlayer.posZ = z2;
    }

    public static String withColors(String identifier, String input) {
        String output = input;
        output.indexOf(identifier);
        while (output.indexOf(identifier) != -1) {
            output = output.replace(identifier, "¡ì");
            output.indexOf(identifier);
        }
        return output;
    }

    public static void sendMessage(String message, boolean toServer) {
        if (toServer) {
            Minecraft.thePlayer.sendChatMessage(message);
            return;
        }
        Minecraft.thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s", "Null: " + EnumChatFormatting.GRAY, message)));
    }

    public static void msgPlayer(String msg) {
        Minecraft.thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s", "Null: " + EnumChatFormatting.GRAY, msg)));
    }

    public static int windowWidth() {
        new ScaledResolution(mc);
        return ScaledResolution.getScaledWidth();
    }

    public static int windowHeight() {
        return new ScaledResolution(mc).getScaledHeight();
    }

    public static void setMoveSpeed(EventMove eventMove, double d2) {
        throw new Error("Unresolved compilation problems: \n\tCannot make a static reference to the non-static method setX(double) from the type EventMove\n\tCannot make a static reference to the non-static method setZ(double) from the type EventMove\n\tCannot make a static reference to the non-static method setX(double) from the type EventMove\n\tCannot make a static reference to the non-static method setZ(double) from the type EventMove\n");
    }

    public static String capitalize(String line) {
        return String.valueOf(String.valueOf(String.valueOf(Character.toUpperCase(line.charAt(0))))) + line.substring(1);
    }

    public static Entity getEntity(double distance) {
        if (getEntity(distance, 0.0d, 0.0f) == null) {
            return null;
        }
        return (Entity) getEntity(distance, 0.0d, 0.0f)[0];
    }

    public static Object[] getEntity(double distance, double expand, float partialTicks) {
        Entity var2 = mc.getRenderViewEntity();
        Entity entity = null;
        if (var2 == null || Minecraft.theWorld == null) {
            return null;
        }
        mc.mcProfiler.startSection("pick");
        Vec3 var3 = var2.getPositionEyes(0.0f);
        Vec3 var4 = var2.getLook(0.0f);
        Vec3 var5 = var3.addVector(var4.xCoord * distance, var4.yCoord * distance, var4.zCoord * distance);
        Vec3 var6 = null;
        List<Entity> var8 = Minecraft.theWorld.getEntitiesWithinAABBExcludingEntity(var2, var2.getEntityBoundingBox().addCoord(var4.xCoord * distance, var4.yCoord * distance, var4.zCoord * distance).expand(1.0d, 1.0d, 1.0d));
        double var9 = distance;
        for (int var10 = 0; var10 < var8.size(); var10++) {
            Entity var11 = var8.get(var10);
            if (var11.canBeCollidedWith()) {
                float var12 = var11.getCollisionBorderSize();
                AxisAlignedBB var13 = var11.getEntityBoundingBox().expand((double) var12, (double) var12, (double) var12).expand(expand, expand, expand);
                MovingObjectPosition var14 = var13.calculateIntercept(var3, var5);
                if (var13.isVecInside(var3)) {
                    if (0.0d < var9 || var9 == 0.0d) {
                        entity = var11;
                        var6 = var14 == null ? var3 : var14.hitVec;
                        var9 = 0.0d;
                    }
                } else if (var14 != null) {
                    double var15 = var3.distanceTo(var14.hitVec);
                    if (var15 < var9 || var9 == 0.0d) {
                        boolean canRiderInteract = false;
                        if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                            canRiderInteract = Reflector.callBoolean(var11, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }
                        if (var11 != var2.ridingEntity || canRiderInteract) {
                            entity = var11;
                            var6 = var14.hitVec;
                            var9 = var15;
                        } else if (var9 == 0.0d) {
                            entity = var11;
                            var6 = var14.hitVec;
                        }
                    }
                }
            }
        }
        if (var9 < distance && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
        }
        mc.mcProfiler.endSection();
        if (entity == null || var6 == null) {
            return null;
        }
        return new Object[]{entity, var6};
    }

    public static BlockUtils blockUtils() {
        return blockUtils;
    }


    public static void sendPacketInQueue(Packet p2) {
        Minecraft.getMinecraft();
        Minecraft.thePlayer.sendQueue.addToSendQueue(p2);
    }

    public static float getDistanceToEntity(Entity from, Entity to2) {
        return from.getDistanceToEntity(to2);
    }

    public static boolean isWithinFOV(Entity en2, double angle) {
        double angleDifference = angleDifference((double) Minecraft.thePlayer.rotationYaw, getRotationToEntity(en2)[0]);
        if ((angleDifference <= 0.0d || angleDifference >= angle) && ((-(angle * 0.5d)) >= angleDifference || angleDifference >= 0.0d)) {
            return false;
        }
        return true;
    }

    public static double angleDifference(double a2, double b2) {
        return ((((a2 - b2) % 360.0d) + 540.0d) % 360.0d) - 180.0d;
    }

    public static double[] getRotationToEntity(Entity entity) {
        double pX = Minecraft.thePlayer.posX;
        double pY = Minecraft.thePlayer.posY + ((double) Minecraft.thePlayer.getEyeHeight());
        double pZ = Minecraft.thePlayer.posZ;
        double dX = pX - entity.posX;
        double dZ = pZ - entity.posZ;
        double dH = Math.sqrt(Math.pow(dX, 2.0d) + Math.pow(dZ, 2.0d));
        return new double[]{Math.toDegrees(Math.atan2(dZ, dX)) + 90.0d, 90.0d - Math.toDegrees(Math.atan2(dH, pY - (entity.posY + ((double) (entity.height / 2.0f)))))};
    }

    public static float[] faceTarget(Entity target, double p_70625_2_, double p_70625_3_, boolean miss) {
        double var6;
        double var4 = target.posX - Minecraft.thePlayer.posX;
        double var8 = target.posZ - Minecraft.thePlayer.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var10 = (EntityLivingBase) target;
            var6 = (var10.posY + ((double) var10.getEyeHeight())) - (Minecraft.thePlayer.posY + ((double) Minecraft.thePlayer.getEyeHeight()));
        } else {
            var6 = ((target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0d) - (Minecraft.thePlayer.posY + ((double) Minecraft.thePlayer.getEyeHeight()));
        }
        new Random();
        double var14 = (double) MathHelper.sqrt_double((var4 * var4) + (var8 * var8));
        float var12 = ((float) ((Math.atan2(var8, var4) * 180.0d) / 3.141592653589793d)) - 90.0f;
        return new float[]{changeRotation((double) Minecraft.thePlayer.rotationYaw, (double) var12, p_70625_2_), changeRotation((double) Minecraft.thePlayer.rotationPitch, (double) ((float) (((-Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25d : 0.0d), var14)) * 180.0d) / 3.141592653589793d)), p_70625_3_)};
    }

    public static float changeRotation(double p_70663_1_, double p_70663_2_, double p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float((float) (p_70663_2_ - p_70663_1_));
        if (((double) var4) > p_70663_3_) {
            var4 = (float) p_70663_3_;
        }
        if (((double) var4) < (-p_70663_3_)) {
            var4 = (float) (-p_70663_3_);
        }
        return (float) (((double) var4) + p_70663_1_);
    }

    public static Entity rayTrace(float f2, double d2) {
        throw new Error("Unresolved compilation problems: \n\tThe field Minecraft.renderViewEntity is not visible\n\tThe field Minecraft.renderViewEntity is not visible\n\tThe method func_174822_a(double, float) is undefined for the type Entity\n\tThe field Minecraft.renderViewEntity is not visible\n\tThe field Minecraft.renderViewEntity is not visible\n\tThe field Minecraft.renderViewEntity is not visible\n\tThe field Minecraft.renderViewEntity is not visible\n\tThe field Minecraft.renderViewEntity is not visible\n");
    }

    public static Entity findClosestToCross(double range) {
        Entity e2 = null;
        double best = 360.0d;
        Minecraft.getMinecraft();
        for (Entity ent : Minecraft.theWorld.loadedEntityList) {
            if (ent instanceof EntityPlayer) {
                Minecraft.getMinecraft();
                double diffX = ent.posX - Minecraft.thePlayer.posX;
                Minecraft.getMinecraft();
                Minecraft.getMinecraft();
                double difference = Math.abs(angleDifference((double) ((float) (Math.toDegrees(Math.atan2(ent.posZ - Minecraft.thePlayer.posZ, diffX)) - 90.0d)), (double) Minecraft.thePlayer.rotationYaw));
                Minecraft.getMinecraft();
                if (ent != Minecraft.thePlayer) {
                    Minecraft.getMinecraft();
                    if (((double) Minecraft.thePlayer.getDistanceToEntity(ent)) <= range && (ent instanceof EntityPlayer) && !FriendManager.isFriend(ent.getName()) && difference < best) {
                        best = difference;
                        e2 = ent;
                    }
                }
            }
        }
        return e2;
    }

    public static void updatePosition(double x2, double y2, double z2) {
        Minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x2, y2, z2, Minecraft.thePlayer.onGround));
    }

    public static int getStringWidth(String text, int increase) {
        return fr().getStringWidth(text);
    }

    public static int RGBtoHEX(int r2, int g2, int b2, int a2) {
        return (a2 << 24) + (r2 << 16) + (g2 << 8) + b2;
    }

    public static List<EntityLivingBase> loadedEntityList() {
        ArrayList<EntityLivingBase> loadedList = new ArrayList<>();
        loadedList.remove(Minecraft.thePlayer);
        return loadedList;
    }

    public static void sendPacketNoEvents(Packet a2) {
        Minecraft.getNetHandler().getNetworkManager().sendPacket(a2);
    }

    public static boolean isStorage(TileEntity entity) {
        if ((entity instanceof TileEntityChest) || (entity instanceof TileEntityBrewingStand) || (entity instanceof TileEntityDropper) || (entity instanceof TileEntityDispenser) || (entity instanceof TileEntityFurnace) || (entity instanceof TileEntityHopper) || (entity instanceof TileEntityEnderChest)) {
            return true;
        }
        return false;
    }

    public static int toRGBAHex(float r2, float g2, float b2, float a2) {
        return ((((int) (a2 * 255.0f)) & 255) << 24) | ((((int) (r2 * 255.0f)) & 255) << 16) | ((((int) (g2 * 255.0f)) & 255) << 8) | (((int) (b2 * 255.0f)) & 255);
    }

    public static boolean isOnSameTeam(boolean teams, EntityLivingBase e2) {
        if (!teams || !e2.isOnSameTeam(Minecraft.thePlayer)) {
            return false;
        }
        return true;
    }

    public static int changeAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 16777215);
    }

    public static float[] getRotations(Entity target, Entity caster) {
        double x2 = target.posX - caster.posX;
        double y2 = (target.posY + (((double) target.getEyeHeight()) / 1.3d)) - (caster.posY + ((double) caster.getEyeHeight()));
        double z2 = target.posZ - caster.posZ;
        double pos = Math.sqrt((x2 * x2) + (z2 * z2));
        return new float[]{((float) ((Math.atan2(z2, x2) * 180.0d) / 3.141592653589793d)) - 90.0f, (float) (((-Math.atan2(y2, pos)) * 180.0d) / 3.141592653589793d)};
    }

    public static void blink(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        try {
            double endX = ((double) endPos.getX()) + 0.5d;
            double endY = ((double) endPos.getY()) + 1.0d;
            double endZ = ((double) endPos.getZ()) + 0.5d;
            double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            int count = 0;
            while (distance > slack) {
                distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
                if (count <= 120) {
                    double diffX = curX - endX;
                    double diffY = curY - endY;
                    double diffZ = curZ - endZ;
                    double offset = (count & 1) == 0 ? pOffset[0] : pOffset[1];
                    if (diffX < 0.0d) {
                        curX = Math.abs(diffX) > offset ? curX + offset : curX + Math.abs(diffX);
                    }
                    if (diffX > 0.0d) {
                        curX = Math.abs(diffX) > offset ? curX - offset : curX - Math.abs(diffX);
                    }
                    if (diffY < 0.0d) {
                        curY = Math.abs(diffY) > 0.25d ? curY + 0.25d : curY + Math.abs(diffY);
                    }
                    if (diffY > 0.0d) {
                        curY = Math.abs(diffY) > 0.25d ? curY - 0.25d : curY - Math.abs(diffY);
                    }
                    if (diffZ < 0.0d) {
                        curZ = Math.abs(diffZ) > offset ? curZ + offset : curZ + Math.abs(diffZ);
                    }
                    if (diffZ > 0.0d) {
                        curZ = Math.abs(diffZ) > offset ? curZ - offset : curZ - Math.abs(diffZ);
                    }
                    Minecraft.getMinecraft();
                    Minecraft.getMinecraft();
                    Minecraft.getMinecraft();
                    Minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(curX, curY, curZ, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, true));
                    count++;
                } else {
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    public static EntityPlayerSP getPlayer() {
        getMinecraft();
        return Minecraft.thePlayer;
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static WorldClient getWorld() {
        getMinecraft();
        return Minecraft.theWorld;
    }
}