/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.movement;

import java.awt.Color;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.FoodStats;

public class Sprint
extends Module {
    private Option<Boolean> omni = new Option<Boolean>("Omni-Directional", "omni", true);

    public Sprint() {
        super("Sprint", new String[]{"run"}, ModuleType.Movement);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.addValues(this.omni);
    }

    @EventTarget
    private void onUpdate(EventPreUpdate event) {
        if (this.mc.thePlayer.getFoodStats().getFoodLevel() > 6 && this.omni.getValue() != false ? this.mc.thePlayer.moving() : this.mc.thePlayer.moveForward > 0.0f) {
            this.mc.thePlayer.setSprinting(true);
        }
    }
}

