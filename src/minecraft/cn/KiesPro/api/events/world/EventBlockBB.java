package cn.KiesPro.api.events.world;
import cn.KiesPro.api.Event;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class EventBlockBB
        extends Event{

    public  EventBlockBB(BlockPos blockPos,Block block,AxisAlignedBB boundingbox){
    int x=blockPos.getX();
    int y=blockPos.getY();
    int z=blockPos.getZ();
    }
    public  EventBlockBB(BlockPos blockPos,Block block){
    int x=blockPos.getX();
    int y=blockPos.getY();
    int z=blockPos.getZ();
    }
}
