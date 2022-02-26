package cn.KiesPro.module.modules.combat;

import java.util.Iterator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.rendering.EventRender3D;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;

public class MultiAura extends Module {
    private Numbers<Double> cps = new Numbers("CPS", "CPS",14.0,1,20,1);
    private Numbers<Double> HurtTime = new Numbers("HurtTime","HurtTime", 10.0,1,20,1);
    private Numbers<Double> targets = new Numbers("MaxTargets","MaxTargets", 5.0,1,10,1);
    private Numbers<Double> reach = new Numbers("Range","Range", 4.1,1,8,0.1);
    private Option<Boolean> armorbreak = new Option("ArmorBreaker","ArmorBreaker", false);
    private Option<Boolean> swing = new Option("Swing","Swing", true);
    private static Option<Boolean> blocking = new Option("AutoBlock", "AutoBlock",true);
    private Option<Boolean> players = new Option("Players", "Players", true);
    private Option<Boolean> animals = new Option("Animals","Animals", false);
    private Option<Boolean> villager = new Option("Villager","Villager", false);
    private Option<Boolean> invis = new Option("Invisibles", "Invisibles",false);
    private Option<Boolean> mobs = new Option("Mobs","Mobs", true);

    public Entity target;
    private static boolean isBlocking;
    public MultiAura() {
        super("MultiAura",new String[]{}, ModuleType.Combat);
        this.addValues(cps,HurtTime,targets,reach,swing,blocking,players,animals,villager,invis,mobs,armorbreak);
    }
    @Override
    public void onDisable() {
        if (blocking.getValue()) {
            this.stopBlocking();
        }
    }
    @EventHandler
    public void onUpdate(EventPreUpdate e) {

        for (Iterator<Entity> entities = this.mc.theWorld.loadedEntityList.iterator(); entities.hasNext();) {
            Object theObject = entities.next();
            if ((theObject instanceof EntityLivingBase)) {
                EntityLivingBase entity = (EntityLivingBase)theObject;
                if (!(entity instanceof EntityPlayerSP)) {
                    if ((this.mc.thePlayer.getDistanceToEntity(entity) <= reach.getValue()) && (entity.isEntityAlive())) {
                        if (blocking.getValue() && !mc.thePlayer.isBlocking()) {
                            this.startBlocking(armorbreak.getValue(),entity);
                        }
                        if (canFight(entity)) {
                            if (swing.getValue()) {
                                this.mc.thePlayer.swingItem();
                            } else {
                                mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                            }
                            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                        }
                    }
                }
            }
        }
    }
    private void startBlocking(boolean interact,EntityLivingBase e) {
        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            isBlocking = true;
            if (interact) {
                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e,C02PacketUseEntity.Action.INTERACT));
                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e,C02PacketUseEntity.Action.ATTACK));
            }

           // KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem());


            //mc.playerController.sendUseItem(mc.thePlayer,mc.theWorld,mc.thePlayer.getHeldItem());
        }
    }

    private void stopBlocking() {
        if (isBlocking && !Mouse.isButtonDown(1)) {
            isBlocking = false;
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            Minecraft.playerController.onStoppedUsingItem(mc.thePlayer);
            mc.thePlayer.itemInUseCount = 0;
        }
    }
    private boolean canFight(EntityLivingBase e) {
        if (!e.isEntityAlive())
            return false;
        if (!(e.hurtTime <= HurtTime.getValue()))
            return false;
        if (players.getValue() && e instanceof EntityPlayer)
            return true;
        if (animals.getValue() && e instanceof EntityAnimal)
            return true;
        if (mobs.getValue() && e instanceof EntityMob)
            return true;
        if (villager.getValue() && e instanceof EntityVillager)
            return true;
        if (invis.getValue() && e.isInvisible())
            return true;

        return false;
    }
    @EventHandler
    public void on3D(EventRender3D e) {
        for (Iterator<Entity> entities = this.mc.theWorld.loadedEntityList.iterator(); entities.hasNext();) {
            Object theObject = entities.next();
            if ((theObject instanceof EntityLivingBase)) {
                EntityLivingBase entity = (EntityLivingBase)theObject;
                if (!(entity instanceof EntityPlayerSP)) {
                    if ((this.mc.thePlayer.getDistanceToEntity(entity) <= reach.getValue()) && (entity.isEntityAlive()) && canFight(entity)) {
                        GL11.glBlendFunc(770, 771);
                        GL11.glEnable(3042);
                        GL11.glEnable(2848);
                        GL11.glLineWidth(2.0f);
                        GL11.glDisable(3553);
                        GL11.glDisable(2929);
                        GL11.glDepthMask(false);
                        GL11.glColor4f(0f / 255, 0f / 255, 255f / 255, 144.0f / 255);

                        double renderPosX = RenderManager.renderPosX;
                        double renderPosY = RenderManager.renderPosY;
                        double renderPosZ = RenderManager.renderPosZ;
                        double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) e.getPartialTicks() - renderPosX;
                        double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) e.getPartialTicks() - renderPosY;
                        double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) e.getPartialTicks() - renderPosZ;
                        AxisAlignedBB box = AxisAlignedBB.fromBounds(posX - (double) entity.width - entity.getCollisionBorderSize() + 0.3, posY, posZ - (double) entity.width - entity.getCollisionBorderSize() + 0.3, posX + (double) entity.width + entity.getCollisionBorderSize() - 0.3, posY + (double) entity.height + 0.2, posZ + (double) entity.width + entity.getCollisionBorderSize() - 0.3);

                        RenderUtil.drawOutlinedBoundingBox(box);
                        GL11.glDisable(2848);
                        GL11.glEnable(3553);
                        GL11.glEnable(2929);
                        GL11.glDepthMask(true);
                        GL11.glDisable(3042);
                    }
                }
            }
        }
    }
}
