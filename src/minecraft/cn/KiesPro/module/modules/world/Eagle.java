/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.world;

import java.awt.*;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.MotionUpdateEvent;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.BlockPos;

public class Eagle
extends Module {

    public Eagle() {
        super("Eagle", new String[]{"eagle", "parkour"}, ModuleType.World);
        this.setColor(new Color(198, 253, 191).getRGB());

    }


    @EventTarget
    public void onUpdate(MotionUpdateEvent e) {
        mc.gameSettings.keyBindSneak.pressed=(mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY-1, mc.thePlayer.posZ)));

    }

    @Override
    public void onDisable(){
        if (mc.thePlayer==null){
            return;
        }
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)){
            mc.gameSettings.keyBindSneak.pressed=false;
        }
    }
}

