/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.world;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class Timer
extends Module {
    public Timer() {
        super("Timer", new String[]{"timer"}, ModuleType.World);
        this.addValues(value);
    }
    public static Numbers<Double> value=new Numbers<Double>("value","value",1.0,0.1,10.0,0.1);

    @EventTarget
    public void onDisable(){
        mc.timer.timerSpeed=1;
    }
    @EventTarget
    private void onUpdate(EventPreUpdate e) {
    	this.setSuffix(value.getValue().toString());
    	mc.timer.timerSpeed = value.getValue().floatValue();
    }
}

