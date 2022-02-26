package cn.KiesPro.module.modules.movement;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.MotionUpdateEvent;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Freeze extends Module {
    private double motionX=0.0;
    private double motionY=0.0;
    private double motionZ=0.0;
    private double x=0.0;
    private double y=0.0;
    private double z=0.0;
    public Freeze(){
        super("Freeze", new String[]{"Freeze"}, ModuleType.Movement);
    }

    @Override
    public void onEnable(){
        if (mc.thePlayer==null){
            return;
        }
        x=mc.thePlayer.posX;
        y=mc.thePlayer.posY;
        z=mc.thePlayer.posZ;
        motionX=mc.thePlayer.motionX;
        motionY=mc.thePlayer.motionY;
        motionZ=mc.thePlayer.motionZ;
    }

    @EventTarget
    public void onUpdate(MotionUpdateEvent e){
        mc.thePlayer.motionX=0.0;
        mc.thePlayer.motionY=0.0;
        mc.thePlayer.motionZ=0.0;
        mc.thePlayer.setPositionAndRotation(x,y,z,mc.thePlayer.rotationYaw,mc.thePlayer.rotationPitch);
    }
    @EventTarget
    public void onPacket(EventPacketRecieve e){
        if (e.getPacket() instanceof C03PacketPlayer){
            e.setCancelled(true);
        }
        if (e.getPacket() instanceof S08PacketPlayerPosLook){
            x=((S08PacketPlayerPosLook) e.getPacket()).getX();
            y=((S08PacketPlayerPosLook) e.getPacket()).getY();
            z=((S08PacketPlayerPosLook) e.getPacket()).getZ();
            motionX=0.0;
            motionY=0.0;
            motionZ=0.0;
        }
    }
    public void onPacket(EventPacketSend e){
        if (e.getPacket() instanceof C03PacketPlayer){
            e.setCancelled(true);
        }
        if (e.getPacket() instanceof S08PacketPlayerPosLook){
            x=((S08PacketPlayerPosLook) e.getPacket()).getX();
            y=((S08PacketPlayerPosLook) e.getPacket()).getY();
            z=((S08PacketPlayerPosLook) e.getPacket()).getZ();
            motionX=0.0;
            motionY=0.0;
            motionZ=0.0;
        }
    }

    @Override
    public void onDisable(){
        mc.thePlayer.motionX=motionX;
        mc.thePlayer.motionY=motionY;
        mc.thePlayer.motionZ=motionZ;
        mc.thePlayer.setPositionAndRotation(x,y,z,mc.thePlayer.rotationYaw,mc.thePlayer.rotationPitch);
    }
    }
