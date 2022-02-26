/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package cn.KiesPro.management;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import cn.KiesPro.api.EventBus;
import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.misc.EventKey;
import cn.KiesPro.api.events.rendering.EventRender2D;
import cn.KiesPro.api.events.rendering.EventRender3D;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.Ghost.AutoClicker;

import cn.KiesPro.module.modules.combat.*;
import cn.KiesPro.module.modules.movement.*;
import cn.KiesPro.module.modules.player.*;
import cn.KiesPro.module.modules.render.*;
import cn.KiesPro.module.modules.world.*;
import cn.KiesPro.module.modules.Ghost.*;
//import cn.KiesPro.module.modules.misc.*;

import cn.KiesPro.utils.render.gl.GLUtils;

public class ModuleManager
implements Manager {
    public static List<Module> modules = new ArrayList<Module>();
    private boolean enabledNeededMod = true;
    public boolean nicetry = true;

    @Override
    public void init() {
    	//Combat
    	modules.add(new AntiBot());
    	modules.add(new AutoPot());
    	modules.add(new AutoSword());
    	modules.add(new BowAimBot());
    	modules.add(new Criticals());
    	modules.add(new FastBow());
    	modules.add(new Killaura());
    	modules.add(new Regen());
    	modules.add(new TPAura());
    	//modules.add(new MultiAura());
    	
    	//Player
    	modules.add(new AntiObbyTrap());
    	//modules.add(new Velocity());
        modules.add(new AntiVelocity());
    	modules.add(new AutoGG());
    	modules.add(new AutoPlay());
    	modules.add(new AutoTool());
    	modules.add(new Disabler());
    	modules.add(new FakeName());
    	modules.add(new FastDig());
    	modules.add(new FastUse());
    	modules.add(new Freecam());
    	modules.add(new InvCleaner());
    	modules.add(new Invplus());
    	modules.add(new Killsult());
    	modules.add(new LagbackCheck());
    	modules.add(new MCF());
    	modules.add(new NoFall());
    	modules.add(new PlayerFinder());
    	modules.add(new Teams());
    	modules.add(new Zoot());
    	modules.add(new NoCommand());
        modules.add(new FuckDomcer());
    	
    	//Movement
    	modules.add(new Flight());
    	modules.add(new InvMove());
    	modules.add(new Jesus());
    	modules.add(new Longjump());
    	modules.add(new NoSlow());
    	modules.add(new Scaffold());
    	modules.add(new Speed());
    	modules.add(new Sprint());
    	modules.add(new Step());
    	modules.add(new TargetStrafe());
        modules.add(new Freeze());
    	//modules.add(new AAC5Flight());
    	
    	//Render
    	modules.add(new Animation());
    	//modules.add(new Capes());
        modules.add(new Chams());
        modules.add(new ClickGui());
        modules.add(new ChestESP());
        modules.add(new ESP());
        modules.add(new FullBright());
        modules.add(new HUD());
        modules.add(new ItemPhysic());
        modules.add(new MiniMap());
        modules.add(new Nametags());
        modules.add(new NoHurtCam());
        modules.add(new NoRender());
        modules.add(new PointerESP());
        modules.add(new TabGui());
        modules.add(new TargetHUD());
        modules.add(new Tracers());
        modules.add(new ViewClip());
        modules.add(new Xray());
        modules.add(new EnchantEffect());
        //modules.add(new ModuleIndicator());
        
        
        //World
        modules.add(new AntiVoid());
        modules.add(new AutoArmor());
        modules.add(new Blink());
        modules.add(new ChestStealer());
        modules.add(new FastPlace());
        modules.add(new NoRotate());
        modules.add(new Phase());
        modules.add(new PingSpoof());
        modules.add(new SafeWalk());
        modules.add(new Eagle());
        modules.add(new Timer());
        modules.add(new PacketMotior());
        
        //Ghost
        modules.add(new AutoClicker());
        modules.add(new Hitbox());
        //modules.add(new Reach());
        //problem

        //Client

        //Misc

        this.readSettings();
        for (Module m : modules) {
            m.makeCommand();
        }
        EventBus.getInstance().register(this);
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : modules) {
            if (m.getClass() != cls) continue;
            return m;
        }
        return null;
    }

    public static Module getModuleByName(String name) {
        for (Module m : modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    public Module getAlias(String name) {
        for (Module f : modules) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
            String[] alias = f.getAlias();
            int length = alias.length;
            int i = 0;
            while (i < length) {
                String s = alias[i];
                if (s.equalsIgnoreCase(name)) {
                    return f;
                }
                ++i;
            }
        }
        return null;
    }

    public static List<Module> getModulesInType(ModuleType t) {
        ArrayList<Module> output = new ArrayList<Module>();
        for (Module m : modules) {
            if (m.getType() != t) continue;
            output.add(m);
        }
        return output;
    }

    @EventTarget
    private void onKeyPress(EventKey e) {
        for (Module m : modules) {
            if (m.getKey() != e.getKey()) continue;
            m.setEnabled(!m.isEnabled());
        }
    }

    @EventTarget
    private void onGLHack(EventRender3D e) {
        GlStateManager.getFloat(2982, (FloatBuffer)GLUtils.MODELVIEW.clear());
        GlStateManager.getFloat(2983, (FloatBuffer)GLUtils.PROJECTION.clear());
        GlStateManager.glGetInteger(2978, (IntBuffer)GLUtils.VIEWPORT.clear());
    }

    @EventTarget
    private void on2DRender(EventRender2D e) {
        if (this.enabledNeededMod) {
            this.enabledNeededMod = false;
            for (Module m : modules) {
                if (!m.enabledOnStartup) continue;
                m.setEnabled(true);
            }
        }
    }

    private void readSettings() {
        List<String> binds = FileManager.read("Binds.txt");
        for (String v : binds) {
            String name = v.split(":")[0];
            String bind = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) continue;
            m.setKey(Keyboard.getKeyIndex((String)bind.toUpperCase()));
        }
        List<String> enabled = FileManager.read("Enabled.txt");
        for (String v : enabled) {
            Module m = ModuleManager.getModuleByName(v);
            if (m == null) continue;
            m.enabledOnStartup = true;
        }
        List<String> hide = FileManager.read("Hide.txt");
        for (String v : hide) {
            Module m = ModuleManager.getModuleByName(v);
            if (m == null) continue;
            m.setRemoved(true);
        }
        List<String> vals = FileManager.read("Values.txt");
        for (String v : vals) {
            String name = v.split(":")[0];
            String values = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }
                ((Mode)value).setMode(v.split(":")[2]);
            }
        }
    }
}

