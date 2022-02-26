package cn.KiesPro.module.modules.render;

import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class Animation extends Module {
    /**
     * ItemRenderer.java & EntityLivingBase.java �?
     */
    public static Mode mode;
    public static Option Smooth;
    public static Option DamageAni;
    public static Option LeftHand;
    public static Option EveryThingBlock;
    public static Numbers swingtimes;
    public static Numbers x;
    public static Numbers y;
    public static Numbers swingx;
    public static Numbers swingy;
    public static Numbers swingz;
    public static Numbers z;
    public static Numbers Speed;
    public static Numbers<Double> swingspeed = new Numbers<Double>("SwingSpeed", "SwingSpeed", 1.0, 0.1, 1.0, 0.1);

    static {
        mode = new Mode("Mode", "Mode", renderMode.values(), renderMode.Swang);
        Smooth = new Option("Smoothhit", "Smoothhit", false);
        DamageAni = new Option("DamageAnimations", "DamageAnimations", false);
        EveryThingBlock = new Option("EveryThingBlock", "EveryThingBlock", false);
        swingtimes = new Numbers("Swingtimes", "Swingtimes", 6.0D, 1.0D, 64.0D, 1.0D);
        x = new Numbers("X", "X", 0.0D, -1.0D, 1.0D, 0.1D);
        y = new Numbers("Y", "Y", 0.0D, -1.0D, 1.0D, 0.1D);
        swingx = new Numbers("Swingx", "Swingx", 0.0D, -1.0D, 1.0D, 0.1D);
        swingy = new Numbers("Swingy", "Swingy", 0.0D, -1.0D, 1.0D, 0.1D);
        swingz = new Numbers("Swingz", "Swingz", 0.0D, -1.0D, 1.0D, 0.1D);
        z = new Numbers("Z", "Z", 0.0D, -1.0D, 1.0D, 0.1D);
        Speed = new Numbers("Speed", "Speed", 10.0D, 1.0D, 50.0D, 1.0D);
    }

    public Animation() {
        super("Animations", new String[]{"BlockHitanimations"}, ModuleType.Render);
        this.addValues(new Value[]{mode, x, y, z, Speed, swingspeed, DamageAni, EveryThingBlock, swingtimes, swingx, swingy, swingz, Smooth});
    }

    public enum renderMode {
        Swang,
        Swank,
        Swing,
        Swong,
        SwAing,
        Remix,
        Custom,
        Null,
        Old,
        Jello,
        Punch,
        Rotate,
        Winter,
        Circle,
        Exhibition,
        Russia;
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        this.setSuffix(this.mode.getValue());
    }
}
