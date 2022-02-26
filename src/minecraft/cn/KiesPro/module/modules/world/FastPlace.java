/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.world;

import java.awt.Color;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventTick;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.client.Minecraft;

public class FastPlace
extends Module {
    public static Numbers<Double> delay=new Numbers<Double>("delay","delay",0.0,0.0,1.0,1.0);
    public FastPlace() {
        super("FastPlace", new String[]{"fplace", "fc"}, ModuleType.World);
        this.setColor(new Color(226, 197, 78).getRGB());
        this.addValues(delay);

    }

    @EventTarget
    private void onTick(EventTick e) {
        this.mc.rightClickDelayTimer = delay.getValue().intValue();
    }
}

