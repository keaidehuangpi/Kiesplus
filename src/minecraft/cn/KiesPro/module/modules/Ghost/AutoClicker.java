package cn.KiesPro.module.modules.Ghost;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.events.world.EventTick;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.TimeHelper;

import net.minecraft.item.ItemSword;
import org.apache.commons.lang3.RandomUtils;


public class AutoClicker extends Module {
    private TimeHelper timer = new TimeHelper();
    private TimeHelper blocktimer = new TimeHelper();
    private static Numbers<Float> mincps=new Numbers<Float>("mincps","mincps",8f, 1.0f, 20.0f, 1.0f);
    private static Numbers<Float> maxcps=new Numbers<Float>("maxcps","maxcps",12f, 1.0f, 20.0f, 1.0f);
    //public static FloatValue mincps = new FloatValue("AutoClicker", "Min CPS", 8, 1.0f, 20.0f, 1.0f);
    //public static FloatValue maxcps = new FloatValue("AutoClicker", "Max CPS", 12, 1.0f, 20.0f, 1.0f);
   //public static BooleanValue autoblock = new BooleanValue("AutoClicker", "Auto Block", false);
    private Option<Boolean> autoblock = new Option<Boolean>("Autoblock", "Autoblock", false);
    private int delay;

    public AutoClicker() {
        //super("Speed", new String[]{"bhop"}, ModuleType.Movement);
        super("AutoClicker",new String[]{"bhop"}, ModuleType.Ghost);
        this.addValues(mincps,maxcps,autoblock);
    }

    @EventTarget
    public void onEnable() {
        setDelay();

       super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventPreUpdate event) {
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            return;
        }
        if (mc.playerController.curBlockDamageMP != 0F) {
            return;
        }
        if (timer.delay(delay) && mc.gameSettings.keyBindAttack.pressed) {
            mc.gameSettings.keyBindAttack.pressed = false;
            // autoblock
            if (autoblock.getValue() && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit.isEntityAlive()){
                if (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && blocktimer.delay(100)) {
                    mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
                    blocktimer.reset();
                }
            }
            mc.leftClickCounter=0;
            mc.clickMouse();
            mc.gameSettings.keyBindAttack.pressed = true;
            setDelay();
            timer.reset();
        }
    }

    @EventTarget
    public void onTick(EventTick event){
        // 防止最小cps大于最大cps
        if (mincps.getValue() > maxcps.getValue()) {
            mincps.setValue(maxcps.getValue());
        }
    }

    private void setDelay()
    {
        delay = (int) RandomUtils.nextFloat(mincps.getValue(),maxcps.getValue());

    }
}
