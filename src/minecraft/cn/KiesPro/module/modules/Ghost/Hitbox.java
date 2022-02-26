package cn.KiesPro.module.modules.Ghost;

import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;

public class Hitbox extends Module {
    //public static FloatValue size = new FloatValue("HitBox", "Size", 0.3f, 0.1f, 0.4f, 0.1f);
    public static Numbers<Double> size = new Numbers<Double>("Size", "Size", 0.5, 0.1, 1.0, 0.1);
    public Hitbox() {
        //super("HitBox", Category.Ghost, false);
        super("Hitbox", new String[]{"Hitbox"}, ModuleType.Ghost);
        this.addValues(size);
    }
}