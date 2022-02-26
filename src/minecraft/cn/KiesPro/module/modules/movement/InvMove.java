package cn.KiesPro.module.modules.movement;

import org.lwjgl.input.Keyboard;

import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

public class InvMove extends Module{

	public InvMove() {
		super("InvMove", new String[] {}, ModuleType.Movement);
	}
	
	@EventTarget
	public void onUpdate(EventPreUpdate event) {
		if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
			KeyBinding[] key = { this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindSprint, this.mc.gameSettings.keyBindJump };
			KeyBinding[] array;
			for (int length = (array = key).length, i = 0; i < length; ++i) {
				KeyBinding b = array[i];
				KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
			}
		}
	}
}
