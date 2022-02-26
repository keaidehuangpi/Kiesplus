package cn.KiesPro.module.modules.movement;



import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import cn.KiesPro.ui.notification.Notification;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cn.KiesPro.Client;
import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.rendering.EventRender2D;
import cn.KiesPro.api.events.rendering.EventRender3D;
import cn.KiesPro.api.events.world.EventPostUpdate;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.MoveUtil;
import cn.KiesPro.utils.MoveUtils;
import cn.KiesPro.utils.PlayerUtil;
import cn.KiesPro.utils.math.RotationUtil;
import cn.KiesPro.utils.render.RenderUtil;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;

public class Scaffold extends Module {
    boolean nbalerted=false;
    ItemStack is;
    private BlockData blockData;
    private timeHelper time = new timeHelper();
    private timeHelper delay = new timeHelper();
    private timeHelper timer2 = new timeHelper();
    private Option<Boolean> spirnt = new Option<>("Sprint","Sprint", false);
    private Option<Boolean> tower = new Option<Boolean>("Tower", "Tower",true);
    private Option<Boolean> movetower = new Option<Boolean>("MoveTower","MoveTower" ,false);
    private Option<Boolean> noSwing = new Option<Boolean>("NoSwing","NoSwing",true);
    private Option<Boolean> KeepRotation = new Option<Boolean>("KeepRotation","KeepRotation",false);
    private Option<Boolean> rotation = new Option<Boolean>("Rotations","Rotations",true);
    private Option<Boolean> esp = new Option<Boolean>("ESP","ESP" ,false);
    public static Option<Boolean> safeWalk = new Option<Boolean>("SafeWalk","SafeWalk", true);
    private Mode mode = new Mode("Priority", "Priority",Smode.values(), Smode.Hypixel);
    private Mode espMode = new Mode("ESP", "ESP",ESPMode.values(), ESPMode.Basic);
    float keepPitchRender, keepYawRender;
    private double olddelay;
    int count, cubeSpoof=-1;
    private BlockPos blockpos;
    private float Disfall;
    private EnumFacing facing;
    private List<Block> blacklisted = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.ender_chest, Blocks.yellow_flower, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.crafting_table, Blocks.snow_layer, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.cactus, Blocks.lever, Blocks.activator_rail, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.furnace, Blocks.ladder, Blocks.oak_fence, Blocks.redstone_torch, Blocks.iron_trapdoor, Blocks.trapdoor, Blocks.tripwire_hook, Blocks.hopper, Blocks.acacia_fence_gate, Blocks.birch_fence_gate, Blocks.dark_oak_fence_gate, Blocks.jungle_fence_gate, Blocks.spruce_fence_gate, Blocks.oak_fence_gate, Blocks.dispenser, Blocks.sapling, Blocks.tallgrass, Blocks.deadbush, Blocks.web, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.nether_brick_fence, Blocks.vine, Blocks.double_plant, Blocks.flower_pot, Blocks.beacon, Blocks.pumpkin, Blocks.lit_pumpkin);
    public static List<Block> blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.ender_chest, Blocks.enchanting_table, Blocks.stone_button, Blocks.wooden_button, Blocks.crafting_table, Blocks.beacon);
    private boolean rotated = false;
    private boolean should = false;
    int slot;
    private float animationY2;
    private ItemStack currentlyHolding;
    static final int[] $SwitchMap$net$minecraft$util$EnumFacing = new int[EnumFacing.values().length];

    public Scaffold() {
        super("Scaffold", new String[]{"Scaffold"}, ModuleType.Movement);
        this.addValues(mode, espMode, tower, movetower,noSwing,esp,rotation,KeepRotation,safeWalk);
    }


    @EventTarget
    private void SAFASF(EventRender3D e) {
        if(this.esp.getValue()) {
            if(espMode.getValue() == ESPMode.Basic) {
                String direction = mc.getRenderViewEntity().getHorizontalFacing().name();
                /* 125:    */
                /* 126:120 */       BlockPos pos2 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
                /* 127:122 */       if ((mc.theWorld.getBlockState(pos2).getBlock() != Blocks.air) && (!mc.gameSettings.keyBindJump.pressed))
                    /* 128:    */       {
                    /* 129:123 */         if (direction.equalsIgnoreCase("NORTH"))
                        /* 130:    */         {
                        /* 131:124 */           BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ - 1.0D);
                        /* 132:125 */           renderChest(pos, Color.WHITE);
                        /* 133:    */         }
                    /* 134:126 */         else if (direction.equalsIgnoreCase("SOUTH"))
                        /* 135:    */         {
                        /* 136:127 */           BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ + 1.0D);
                        /* 137:128 */          renderChest(pos, Color.WHITE);
                        /* 138:    */         }
                    /* 139:129 */         else if (direction.equalsIgnoreCase("WEST"))
                        /* 140:    */         {
                        /* 141:130 */           BlockPos pos = new BlockPos(mc.thePlayer.posX - 1.0D, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
                        /* 142:131 */          renderChest(pos, Color.WHITE);
                        /* 143:    */         }
                    /* 144:132 */         else if (direction.equalsIgnoreCase("EAST"))
                        /* 145:    */         {
                        /* 146:133 */           BlockPos pos = new BlockPos(mc.thePlayer.posX + 1.0D, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
                        /* 147:134 */          renderChest(pos,  Color.WHITE);
                        /* 148:    */         }
                    /* 149:    */       }
                /* 150:    */       else
                    /* 151:    */       {
                    /* 152:137 */         BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
                    /* 153:138 */         renderChest(pos, Color.WHITE);
                    /* 154:    */       }

            }else {
                Color color = new Color(Color.BLACK.getRGB());
                Color color2 = new Color(Color.ORANGE.getRGB());
                double x = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
                double y = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
                double z = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
                double x2 = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
                double y2 = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
                double z2 = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
                x -= 0.65;
                z -= 0.65;
                x2 -= 0.5;
                z2 -= 0.5;
                y += mc.thePlayer.getEyeHeight() + 0.35 - (mc.thePlayer.isSneaking() ? 0.25 : 0.0);
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                final double rotAdd = -0.25 * (Math.abs(mc.thePlayer.rotationPitch) / 90.0f);
                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
                GL11.glLineWidth(2.0f);
                RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y-2, z, x +1.3, y -2, z + 1.3));
                RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x2, y-2, z2, x2+1, y-2, z2 +1));
                if(this.mc.gameSettings.keyBindJump.pressed||Client.instance.getModuleManager().getModuleByName("Speed").isEnabled()&&Minecraft.getMinecraft().thePlayer.moveForward != 0) {
                    GL11.glColor4f(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, 1.0f);
                    RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y-2, z, x +1.3, y -2, z + 1.3));
                }
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }

        }


    }
    public static void renderChest(BlockPos blockPos, Color white) {
        double d0 = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double d1 = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double d2 = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glColor4d(255.0D, 255.0D, 255D, 15.0D);
        RenderGlobal.func_181561_a(new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 1.0D, d2 + 1.0D));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private int width=0;
    @EventTarget
    private void render(EventRender2D e) {
        ScaledResolution res = new ScaledResolution(mc);
        int color = 0;
        if(this.getBlockCount()>=100&&this.getBlockCount()<1000) {
            this.width=7;
            color = new Color(108,195,18).getRGB();
        }if(this.getBlockCount()>=10&&this.getBlockCount()<100) {
            this.width=5;
            color = new Color(255,255,0).getRGB();
        }if(this.getBlockCount()>=0&&this.getBlockCount()<10) {
            this.width=3;
            color = new Color(255, 0,0).getRGB();
        }
         /*if (this.getBlockCount() == 0 && Helper.onServer("hypixel")) {
             Helper.sendMessage("Scaffold Bug out");
             this.setEnabled(false);
         }*/
        if (this.getBlockCount() == 0&&nbalerted==false) {
            Helper.sendMessage("no blocks!");
            Client.instance.getNotificationManager().sendClientMessage("SCAFFOLD:OUT OF BLOCKS!", Notification.Type.ERROR);
            nbalerted=true;
        }
        //mc.fontRendererObj.drawStringWithShadow("    "+this.getBlockCount() + " Blocks", res.getScaledWidth() / 2-this.width , res.getScaledHeight() / 2 + 23-(float)(double)7-(float)(double)5-1, new Color(255,255,255).getRGB());
        mc.fontRendererObj.drawStringWithShadow("   " + this.getBlockCount() + " Blocks", res.getScaledWidth() / 2-this.width , res.getScaledHeight() / 2 + 23-(float)(double)7-(float)(double)5-1, color);
    }

    @EventTarget
    public void onPre(EventPreUpdate event) {
        this.setSuffix(mode.getValue());
        double x = Minecraft.getMinecraft().thePlayer.posX;
        double y = Minecraft.getMinecraft().thePlayer.posY - 1.0;
        double z = Minecraft.getMinecraft().thePlayer.posZ;
        BlockPos underPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();
        BlockPos blockBelow = new BlockPos(x, y, z);
        if (Minecraft.getMinecraft().thePlayer != null) {
            this.blockData = this.getBlockData(blockBelow, blacklistedBlocks);
            if (this.blockData == null) {
                this.blockData = this.getBlockData(blockBelow.offset(EnumFacing.DOWN), blacklistedBlocks);
            }
            if(KeepRotation.getValue().booleanValue()) {
                mc.thePlayer.renderYawOffset = keepYawRender;
                mc.thePlayer.rotationYawHead = keepYawRender;
            }
            if (this.mc.theWorld.getBlockState(blockBelow = new BlockPos(x, y, z)).getBlock() == Blocks.air) {
                if (this.blockData != null) {
                    if(rotation.getValue().booleanValue()) {
                        float[] rot = this.getRotationsBlock(BlockData.position, BlockData.face);
                        event.setPitch(rot[1]);
                        //event.setYaw(keepYawRender=getRotationsBlock(blockData.getPosition(),blockData.getFacing())[0]);
                        //event.setPitch(keepPitchRender=getRotationsBlock(blockData.getPosition(),blockData.getFacing())[1]);
                        event.setYaw(rot[0]);
                    }else {
                        //event.setPitch(79.44f);
                        event.setPitch(87.0f);
                    }
                }
                if (this.tower.getValue().booleanValue() && this.mc.gameSettings.keyBindJump.pressed) {
                    if(this.mode.getValue() == Smode.Hypixel) {
                        if (((Boolean) this.movetower.getValue()).booleanValue()) {
                            if (mc.gameSettings.keyBindJump.pressed) {
                                Minecraft minecraft11 = mc;
                                if (!Minecraft.thePlayer.isPotionActive(Potion.jump)) {
                                    if (isMoving2()) {
                                        if (PlayerUtil.isOnGround(0.76d) && !PlayerUtil.isOnGround(0.75d)) {
                                            Minecraft minecraft12 = mc;
                                            if (Minecraft.thePlayer.motionY > 0.23d) {
                                                Minecraft minecraft13 = mc;
                                                if (Minecraft.thePlayer.motionY < 0.25d) {
                                                    Minecraft minecraft14 = mc;
                                                    EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
                                                    Minecraft minecraft15 = mc;
                                                    Minecraft minecraft16 = mc;
                                                    entityPlayerSP.motionY = ((double) Math.round(Minecraft.thePlayer.posY)) - Minecraft.thePlayer.posY;
                                                }
                                            }
                                        }
                                        if (PlayerUtil.isOnGround(1.0E-4d)) {
                                            Minecraft minecraft17 = mc;
                                            Minecraft.thePlayer.motionY = 0.42993956416514d;
                                            Minecraft minecraft18 = mc;
                                            Minecraft.thePlayer.motionX *= 0.9d;
                                            Minecraft minecraft19 = mc;
                                            Minecraft.thePlayer.motionZ *= 0.9d;
                                        } else {
                                            Minecraft minecraft20 = mc;
                                            double d2 = Minecraft.thePlayer.posY;
                                            Minecraft minecraft21 = mc;
                                            if (d2 >= ((double) Math.round(Minecraft.thePlayer.posY)) - 1.0E-4d) {
                                                Minecraft minecraft22 = mc;
                                                double d3 = Minecraft.thePlayer.posY;
                                                Minecraft minecraft23 = mc;
                                                if (d3 <= ((double) Math.round(Minecraft.thePlayer.posY)) + 1.0E-4d) {
                                                    Minecraft minecraft24 = mc;
                                                    Minecraft.thePlayer.motionY = 0.0d;
                                                }
                                            }
                                        }
                                    } else {
                                        Minecraft minecraft25 = mc;
                                        Minecraft.thePlayer.motionX = 0.0d;
                                        Minecraft minecraft26 = mc;
                                        Minecraft.thePlayer.motionZ = 0.0d;
                                        Minecraft minecraft27 = mc;
                                        Minecraft.thePlayer.jumpMovementFactor = 0.0f;
                                        BlockPos blockBelow2 = new BlockPos(x, y, z);
                                        Minecraft minecraft28 = mc;
                                        if (Minecraft.theWorld.getBlockState(blockBelow2).getBlock() == Blocks.air && this.blockData != null) {
                                            Minecraft minecraft29 = mc;
                                            Minecraft.thePlayer.motionY = 0.4295751556457d;
                                            Minecraft minecraft30 = mc;
                                            Minecraft.thePlayer.motionX *= 0.75d;
                                            Minecraft minecraft31 = mc;
                                            Minecraft.thePlayer.motionZ *= 0.75d;
                                        }
                                    }
                                }
                            }
                        } else if (!isMoving2() && mc.gameSettings.keyBindJump.pressed) {
                            Minecraft minecraft32 = mc;
                            Minecraft.thePlayer.motionX = 0.0d;
                            Minecraft minecraft33 = mc;
                            Minecraft.thePlayer.motionZ = 0.0d;
                            Minecraft minecraft34 = mc;
                            Minecraft.thePlayer.jumpMovementFactor = 0.0f;
                            BlockPos blockBelow3 = new BlockPos(x, y, z);
                            Minecraft minecraft35 = mc;
                            if (Minecraft.theWorld.getBlockState(blockBelow3).getBlock() == Blocks.air && this.blockData != null) {
                                Minecraft minecraft36 = mc;
                                Minecraft.thePlayer.motionY = 0.4196d;
                                Minecraft minecraft37 = mc;
                                Minecraft.thePlayer.motionX *= 0.75d;
                                Minecraft minecraft38 = mc;
                                Minecraft.thePlayer.motionZ *= 0.75d;
                            }
                        }

                    }
                    else if(this.mode.getValue() == Smode.Normal) {
                        if (isAirBlock(underBlock) && this.blockData != null) {
                            mc.thePlayer.motionY = 0.4196;
                            mc.thePlayer.motionX *= 0.75;
                            mc.thePlayer.motionZ *= 0.75;
                        }
                        if(!PlayerUtil.isMoving2()){
                            mc.thePlayer.motionX = 0;
                            mc.thePlayer.motionZ = 0;
                        }
                    }
                }
            }

            if(this.tower.getValue().booleanValue() && this.mc.gameSettings.keyBindJump.pressed) {
                if(this.mode.getValue() == Smode.CubeCraft  ) {
                    mc.thePlayer.setSprinting(false);
                    count ++;
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                    mc.thePlayer.jumpMovementFactor = 0;
                    if(MoveUtil.isOnGround(2))
                        if(count == 1){
                            mc.thePlayer.motionY = 0.41;
                        }else{

                            mc.thePlayer.motionY = 0.47;
                            count = 0;
                        }
                }
                else if(this.mode.getValue() == Smode.Normal) {
                    if (isAirBlock(underBlock) && this.blockData != null) {
                        mc.thePlayer.motionY = 0.4196;
                        mc.thePlayer.motionX *= 0.75;
                        mc.thePlayer.motionZ *= 0.75;
                    }
                    if(!PlayerUtil.isMoving2()){
                        mc.thePlayer.motionX = 0;
                        mc.thePlayer.motionZ = 0;
                    }
                }
            }}
//	        setSpeed();
    }

    public boolean isOnGround(double height) {
        if (!this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0, - height, 0.0)).isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isMoving2() {
        return Minecraft.getMinecraft().thePlayer.moveForward != 0.0f || Minecraft.getMinecraft().thePlayer.moveStrafing != 0.0f;
    }

    public float[] getRotationsBlock(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - Minecraft.getMinecraft().thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - Minecraft.getMinecraft().thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double y = (double)block.getY() + 0.5;
        double d1 = Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }

    @EventTarget
    public void onSafe(EventPostUpdate event) {
        int i;
        for (i = 36; i < 45; ++i) {

            Item item;
            if (!Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = (is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock()) || ((ItemBlock)item).getBlock().getLocalizedName().toLowerCase().contains("chest") || this.blockData == null) continue;
            int currentItem = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 36));
            Minecraft.getMinecraft().thePlayer.inventory.currentItem = i - 36;
            this.currentlyHolding = this.mc.thePlayer.inventory.getStackInSlot(i - 36);
            Minecraft.getMinecraft().playerController.updateController();
            Minecraft.getMinecraft().playerController.onPlayerRightClick(Minecraft.getMinecraft().thePlayer, this.mc.theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem(), BlockData.position, BlockData.face, new Vec3(BlockData.access$2(this.blockData)).addVector(0.5, 0.5, 0.5).add(new Vec3(BlockData.access$3(this.blockData).getDirectionVec()).scale(0.5)));
            if (this.noSwing.getValue().booleanValue()) {
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            } else {
                Minecraft.getMinecraft().thePlayer.swingItem();
            }
            if(this.mode.getValue() == Smode.CubeCraft){
                if(cubeSpoof != currentItem){

                    C09PacketHeldItemChange p = new C09PacketHeldItemChange(currentItem);
                    cubeSpoof = currentItem;
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(p);
                    mc.thePlayer.inventory.currentItem = currentItem;
                    mc.playerController.updateController();
                }else{
                    mc.thePlayer.inventory.currentItem = currentItem;
                    mc.playerController.updateController();
                }
            }else{
                mc.thePlayer.inventory.currentItem = currentItem;
                mc.playerController.updateController();
            }
            return;
        }
        if (this.invCheck()) {
            for (i = 9; i < 36; ++i) {
                Item item;
                if (!Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock()) || ((ItemBlock)item).getBlock().getLocalizedName().toLowerCase().contains("chest")) continue;
                this.swap(i, 7);
                break;
            }
        }

    }
    public void setSpeed() {
        if(this.mode.getValue() == Smode.CubeCraft)
            mc.thePlayer.onGround = false;
        mc.thePlayer.jumpMovementFactor = 0;
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float YAW = mc.thePlayer.rotationYaw;
        double a = (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)));
        double b = (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)));
        double c = Math.abs((a* b));
        double slow = 1-c*5;
        double speed =0.35 + randomNumber(0.01, -0.05);
        speed *= slow;
        MoveUtil.setMotion(speed);
        mc.thePlayer.setSprinting(false);

    }
    public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }
    public static float randomFloat(long seed) {
        seed = System.currentTimeMillis() + seed;
        return 0.3f + (float)new Random(seed).nextInt(70000000) / 1.0E8f + 1.458745E-8f;
    }

    protected void swap(int slot, int hotbarNum) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Minecraft.getMinecraft().thePlayer);
    }

    private boolean invCheck() {
        for (int i = 36; i < 45; ++i) {
            Item item;
            if (!Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock())) continue;
            return false;
        }
        return true;
    }

    private double getDoubleRandom(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private boolean canPlace(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 vec3) {
        if (heldStack.getItem() instanceof ItemBlock) {
            return ((ItemBlock)heldStack.getItem()).canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack);
        }
        return false;
    }

//    private void setBlockAndFacing(BlockPos bp) {
//        if (this.mc.theWorld.getBlockState(bp.add(0, -1, 0)).getBlock() != Blocks.air) {
//            this.blockpos = bp.add(0, -1, 0);
//            this.facing = EnumFacing.UP;
//        } else if (this.mc.theWorld.getBlockState(bp.add(-1, 0, 0)).getBlock() != Blocks.air) {
//            this.blockpos = bp.add(-1, 0, 0);
//            this.facing = EnumFacing.EAST;
//        } else if (this.mc.theWorld.getBlockState(bp.add(1, 0, 0)).getBlock() != Blocks.air) {
//            this.blockpos = bp.add(1, 0, 0);
//            this.facing = EnumFacing.WEST;
//        } else if (this.mc.theWorld.getBlockState(bp.add(0, 0, -1)).getBlock() != Blocks.air) {
//            this.blockpos = bp.add(0, 0, -1);
//            this.facing = EnumFacing.SOUTH;
//        } else if (this.mc.theWorld.getBlockState(bp.add(0, 0, 1)).getBlock() != Blocks.air) {
//            this.blockpos = bp.add(0, 0, 1);
//            this.facing = EnumFacing.NORTH;
//        } else {
//            bp = null;
//            this.facing = null;
//        }
//    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    private int getBlockSlot() {
        for (int i = 36; i < 45; ++i) {
            ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock) || itemStack.stackSize <= 0 || this.blacklisted.stream().anyMatch(e -> e.equals(((ItemBlock)itemStack.getItem()).getBlock()))) continue;
            return i - 36;
        }
        return -1;
    }

    private BlockData getBlockData(BlockPos pos, List list) {
        Disfall = Minecraft.getMinecraft().thePlayer.fallDistance;
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {

            return new BlockData(pos.add(-1, 0, 0), Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Minecraft.getMinecraft().thePlayer.onGround && Minecraft.getMinecraft().thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY - 1.0, Minecraft.getMinecraft().thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.EAST,  this.blockData );
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Minecraft.getMinecraft().thePlayer.onGround && Minecraft.getMinecraft().thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY - 1.0, Minecraft.getMinecraft().thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.WEST, this.blockData );
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Minecraft.getMinecraft().thePlayer.onGround && Minecraft.getMinecraft().thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY - 1.0, Minecraft.getMinecraft().thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Minecraft.getMinecraft().thePlayer.onGround && Minecraft.getMinecraft().thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY - 1.0, Minecraft.getMinecraft().thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.NORTH, this.blockData);
        }
        BlockPos add = pos.add(-1, 0, 0);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add2 = pos.add(1, 0, 0);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add3 = pos.add(0, 0, -1);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add4 = pos.add(0, 0, 1);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        return null;
    }

    public boolean isAirBlock(Block block) {
        return block.getMaterial().isReplaceable() && (!(block instanceof BlockSnow) || block.getBlockBoundsMaxY() <= 0.125);
    }

    public Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.NORTH) {
            return new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() - 0.5);
        }
        if (face == EnumFacing.EAST) {
            return new Vec3((double)pos.getX() + 0.5, pos.getY(), pos.getZ());
        }
        if (face == EnumFacing.SOUTH) {
            return new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() + 0.5);
        }
        if (face == EnumFacing.WEST) {
            return new Vec3((double)pos.getX() - 0.5, pos.getY(), pos.getZ());
        }
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onEnable() {
        keepPitchRender = mc.thePlayer.rotationPitch;
        keepYawRender = mc.thePlayer.rotationYaw;
        super.onEnable();
        this.timer2.reset();
        Client.instance.getNotificationManager().sendClientMessage("enabled scaffold", Notification.Type.SUCCESS);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.getMinecraft().thePlayer.inventory.currentItem));
        mc.timer.timerSpeed = 1.0f;
        nbalerted=false;
        Client.instance.getNotificationManager().sendClientMessage("disabled scaffold", Notification.Type.SUCCESS);
    }


    public class timeHelper {
        private long prevMS = 0L;

        public boolean delay(float milliSec) {
            return (float)(this.getTime() - this.prevMS) >= milliSec;
        }

        public void reset() {
            this.prevMS = this.getTime();
        }

        public long getTime() {
            return System.nanoTime() / 1000000L;
        }

        public long getDifference() {
            return this.getTime() - this.prevMS;
        }

        public void setDifference(long difference) {
            this.prevMS = this.getTime() - difference;
        }
    }

    private static class BlockData {
        public static BlockPos position;
        public static EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face, BlockData blockData) {
            BlockData.position = position;
            BlockData.face = face;
        }

        private BlockPos getPosition() {
            return position;
        }

        private EnumFacing getFacing() {
            return face;
        }

        static BlockPos access$0(BlockData var0) {
            return var0.getPosition();
        }

        static EnumFacing access$1(BlockData var0) {
            return var0.getFacing();
        }

        static BlockPos access$2(BlockData var0) {
            return position;
        }

        static EnumFacing access$3(BlockData var0) {
            return face;
        }
    }

    public static enum Smode{
        Normal,
        Hypixel,
        CubeCraft,
    }

    enum ESPMode {
        ZeroDay,
        Basic;
    }

}

