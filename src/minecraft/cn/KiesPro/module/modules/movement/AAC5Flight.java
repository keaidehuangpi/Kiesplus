package cn.KiesPro.module.modules.movement;


import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.world.EventMove;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S40PacketDisconnect;

public class AAC5Flight extends Module {

    public static Option damageplayer = new Option("Damage", "Damage", true);

    public AAC5Flight() {
        super("HuaYuTingFly", new String[]{}, ModuleType.Movement);
        addValues(damageplayer);
        setSuffix("AAC5");
    }

    int status = 0;

    @EventHandler
    public void onMove(EventMove event) {
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                mc.thePlayer.posX,
                mc.thePlayer.posY,
                mc.thePlayer.posZ,
                false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true));
        mc.gameSettings.keyBindForward.pressed = status != 1;

        int dist = (int) 0.13;
        float yaw = (float) Math.toRadians(mc.thePlayer.rotationYaw);
        float x = (float) (-Math.sin(yaw) * dist);
        float z = (float) (Math.cos(yaw) * dist);
        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);

    }

    @EventHandler
    public void onPacket(EventPacketSend e) {
        Packet packet = e.getPacket();

        if (packet instanceof C03PacketPlayer) {
            ((C03PacketPlayer) packet).onGround = true;
        }

        if (packet instanceof S08PacketPlayerPosLook) {
            e.isCancelled();
            if (status == 0) {
                mc.thePlayer.setPosition(((S08PacketPlayerPosLook) packet).getX(), ((S08PacketPlayerPosLook) packet).getY(), ((S08PacketPlayerPosLook) packet).getZ());
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, ((S08PacketPlayerPosLook) packet).getYaw(), ((S08PacketPlayerPosLook) packet).getPitch(), false));

                float dist = (float) 0.13;
                float yaw = (float) Math.toRadians(mc.thePlayer.rotationYaw);
                float x = (float) (-Math.sin(yaw) * dist);
                float z = (float) (Math.cos(yaw) * dist);
                mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY,
                        mc.thePlayer.posZ,
                        false));
                mc.thePlayer.posX = mc.thePlayer.posX;
                mc.thePlayer.posZ = mc.thePlayer.posZ;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true));
            }

        }


        if (packet instanceof C03PacketPlayer) {
            e.isCancelled();
        }

        if (packet instanceof S40PacketDisconnect) {
            e.isCancelled();
        }

    }

    public void onEnable() {
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true));
        mc.thePlayer.motionX = 0.0D;
        mc.thePlayer.motionZ = 0.0D;
        status = 0;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, 1.7976931348623157E+308, mc.thePlayer.posZ, true));

    }

    public void onDisable() {

        mc.gameSettings.keyBindForward.pressed = false;


        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));


    }

    public void damagePlayer() {
        if (mc.thePlayer.onGround) {
            NetworkManager var2;
            for (int index = 0; index <= 67 + 23 * ((Boolean) damageplayer.getValue() ? 1 : 0); ++index) {
                var2 = mc.thePlayer.sendQueue.getNetworkManager();

                double var3 = mc.thePlayer.posY + 2.535E-9D;
                var2.sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX, var3, mc.thePlayer.posZ, false));
                var2 = mc.thePlayer.sendQueue.getNetworkManager();
                var3 = mc.thePlayer.posY + 1.05E-10D;
                var2.sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX, var3, mc.thePlayer.posZ, false));
                var2 = mc.thePlayer.sendQueue.getNetworkManager();
                var3 = mc.thePlayer.posY + 0.0448865D;
                var2.sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX, var3, mc.thePlayer.posZ, false));
            }

            var2 = mc.getNetHandler().getNetworkManager();
            var2.sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        }

    }


}
