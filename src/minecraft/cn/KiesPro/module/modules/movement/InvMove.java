package cn.KiesPro.module.modules.movement;

import cn.KiesPro.api.events.rendering.EventRender2D;
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

	public void onRender2D(EventRender2D event) {
		if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
			mc.gameSettings.keyBindForward.pressed=(Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
			mc.gameSettings.keyBindBack.pressed=(Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
			mc.gameSettings.keyBindLeft.pressed=(Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
			mc.gameSettings.keyBindRight.pressed=(Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));

				mc.gameSettings.keyBindJump.pressed=(Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));

		}
	}
}
