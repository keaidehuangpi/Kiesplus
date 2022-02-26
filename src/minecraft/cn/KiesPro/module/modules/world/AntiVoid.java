package cn.KiesPro.module.modules.world;

import java.awt.Color;

import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class AntiVoid
        extends Module {
    public AntiVoid() {
        super("AntiFall", new String[]{"novoid", "antifall"}, ModuleType.World);
        setColor(new Color(223, 233, 233).getRGB());
        addValues(new Value[]{this.onlyVoid});
    }

    public double getDistanceToFall() {
        double distance = 0.0D;
        for (double i = Minecraft.thePlayer.posY; i > 0.0D; i -= 1.0D) {
            Block block = getBlockWithBlockPos(new BlockPos(Minecraft.thePlayer.posX, i, Minecraft.thePlayer.posZ));
            if ((block.getMaterial() != Material.air) && (block.isFullCube()) && (block.isCollidable())) {
                distance = i;
                break;
            }
        }
        double distancetofall = Minecraft.thePlayer.posY - distance - 1.0D;
        return distancetofall;
    }

    public Block getBlockWithBlockPos(BlockPos blockPos) {
        return Minecraft.theWorld.getBlockState(blockPos).getBlock();
    }

    private boolean isBlockUnder() {
        for (int i = (int) (Minecraft.thePlayer.posY - 1.0D); i > 0; i--) {
            BlockPos pos = new BlockPos(Minecraft.thePlayer.posX, i, Minecraft.thePlayer.posZ);
            if (!(Minecraft.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }

    private Option<Boolean> onlyVoid = new Option("onlyVoid", "onlyVoid", Boolean.valueOf(true));

    @EventHandler
    private void onUpdate(EventPacketSend e) {
        if (((e.getPacket() instanceof C03PacketPlayer)) && (Minecraft.thePlayer.fallDistance >= 5.0F) && (
                (!((Boolean) this.onlyVoid.getValue()).booleanValue()) || (Minecraft.thePlayer.posY < 0.0D) || ((Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, 0.0D, 0.0D).expand(0.0D, 0.0D, 0.0D)).isEmpty()) && (Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, -10002.25D, 0.0D).expand(0.0D, -10003.75D, 0.0D)).isEmpty())))) {
            ((C03PacketPlayer) e.getPacket()).y += 9.0D;
        }
    }
}
