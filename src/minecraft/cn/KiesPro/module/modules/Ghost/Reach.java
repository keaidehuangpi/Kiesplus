package cn.KiesPro.module.modules.Ghost;

import java.util.Random;

import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.misc.EventClickMouse;
import cn.KiesPro.api.events.world.EventAttack;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Reach extends Module {
    protected Random rand = new Random();
    public double currentRange = 3.0D;
    public long lastAttack = 0L;
    protected long lastMS = -1L;
    private Option combo = new Option("Combo", "Combo", Boolean.valueOf(false));
    private Numbers max = new Numbers("MaxRange", "MaxRange", Double.valueOf(4.0D), Double.valueOf(3.0D), Double.valueOf(8.0D), Double.valueOf(0.1D));
    private Numbers min = new Numbers("MinRange", "MinRange", Double.valueOf(3.0D), Double.valueOf(3.0D), Double.valueOf(8.0D), Double.valueOf(0.1D));

    public Reach() {
        super("Reach", new String[]{"Reach"}, ModuleType.Ghost);
        this.addValues(new Value[]{this.max, this.min, this.combo});
    }

    public boolean hasTimePassedMS(long MS) {
        return this.getCurrentMS() >= this.lastMS + MS;
    }

    public void updatebefore() {
        this.lastMS = this.getCurrentMS();
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    @EventHandler
    public void Attack(EventAttack event) {
        this.lastAttack = System.currentTimeMillis();
    }

    @EventHandler
    public void Update(EventPreUpdate event) {
        if(this.isEnabled()) {
            if(this.hasTimePassedMS(2000L)) {
                double rangeMin = ((Double)this.min.getValue()).doubleValue();
                double rangeMax = ((Double)this.max.getValue()).doubleValue();
                double rangeDiff = rangeMax - rangeMin;
                if(rangeDiff < 0.0D) {
                    return;
                }

                this.currentRange = rangeMin + this.rand.nextDouble() * rangeDiff;
                this.updatebefore();
            }

        }
    }

    @EventHandler
    public void Click(EventClickMouse event) {
        if(this.isEnabled()) {
            if(!((Boolean)this.combo.getValue()).booleanValue() || System.currentTimeMillis() - this.lastAttack <= 300L) {
                Object[] objects = Wrapper.getEntity(this.currentRange, 0.0D, 0.0F);
                if(objects != null) {
                    Wrapper.mc.objectMouseOver = new MovingObjectPosition((Entity)objects[0], (Vec3)objects[1]);
                    Wrapper.mc.pointedEntity = (Entity)objects[0];
                }
            }
        }
    }
}
