package cn.KiesPro.module.modules.player;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.KiesPro.api.EventHandler;
import cn.KiesPro.api.events.world.EventPacketReceive;
import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.EventPostUpdate;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;


public class Disabler
extends Module {
	public static LinkedHashMap<C03PacketPlayer.C06PacketPlayerPosLook, Long> map2de2 = new LinkedHashMap<C03PacketPlayer.C06PacketPlayerPosLook, Long>();

	public static LinkedList<C0FPacketConfirmTransaction> map2de = new LinkedList<C0FPacketConfirmTransaction>();
	public static LinkedHashMap<C00PacketKeepAlive, Long> map2 = new LinkedHashMap<C00PacketKeepAlive, Long>();
    private Numbers<Double> dadly = new Numbers<Double>("C0FminDelay", "C0FminDelay", 4.0, 0.0, 15.0, 1.0);
    private Numbers<Double> dadly2 = new Numbers<Double>("C0FmaxDelay", "C0FmaxDelay", 6.0, 0.0, 15.0, 1.0);
    private Numbers<Double> dadly22 = new Numbers<Double>("S00BaseDelay", "S00BaseDelay", 210.0, 0.0, 400.0, 10.0);
    private Numbers<Double> dadlyS = new Numbers<Double>("S00minDelay", "S00minDelay", 8.0, 0.0, 15.0, 1.0);
    private Numbers<Double> LagBackPacketSendLimitMin = new Numbers<Double>("LagBackPacketSendLimitMin", "LagBackPacketSendLimitMin", 5.0,
			0.0, 50.0, 1.0);
    private Numbers<Double> LagBackPacketSendLimitMax = new Numbers<Double>("LagBackPacketSendLimitMax", "LagBackPacketSendLimitMax", 7.0,
			0.0, 50.0, 1.0);
    private Numbers<Double> dadly2S = new Numbers<Double>("S00maxDelay", "S00maxDelay", 10.0, 0.0, 15.0, 1.0);
	public int coo = -1;

	public static AtomicBoolean switcher = new AtomicBoolean();

	public static int choose = 2;
	
	public int oldPing = -1;
	
    public Disabler() {
        super("Disabler", new String[]{"disabler"}, ModuleType.World);
        this.addValues(dadly, dadly2, dadly22, dadly2S, dadlyS, LagBackPacketSendLimitMin, LagBackPacketSendLimitMax);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }
    
    @Override
	public void onDisable() {
		while (!map2de.isEmpty()) {
			C0FPacketConfirmTransaction p = map2de.poll();

			mc.getNetHandler().getNetworkManager().sendPacket(p);

		}

		map2.entrySet().stream().forEachOrdered(entry -> {

			mc.getNetHandler().getNetworkManager().sendPacket(entry.getKey());
			map2.remove(entry.getKey());
		});

		map2de2.entrySet().stream().forEachOrdered(entry -> {

			mc.getNetHandler().getNetworkManager().sendPacket(entry.getKey());
			map2de2.remove(entry.getKey());
		});
	}

    @EventHandler
	public void onUpdate(EventPreUpdate e) {
		if (!map2de.isEmpty() && Minecraft.thePlayer.ticksExisted % ThreadLocalRandom.current()
				.nextInt(dadly.getValue().intValue(), dadly2.getValue().intValue()) == 0) {
			C0FPacketConfirmTransaction p = map2de.poll();

			mc.getNetHandler().getNetworkManager().sendPacket(p);
		}
	}
    @EventHandler
    public void UpdatePost(EventPostUpdate e) {
		map2.entrySet().stream().filter(entry -> entry.getValue() <= System.currentTimeMillis())
		.forEachOrdered(entry -> {

			mc.getNetHandler().getNetworkManager().sendPacket(entry.getKey());
			map2.remove(entry.getKey());
		});
		
		map2de2.entrySet().stream().filter(entry -> entry.getValue() <= System.currentTimeMillis())
		.forEachOrdered(entry -> {

			mc.getNetHandler().getNetworkManager().sendPacket(entry.getKey());
			map2de2.remove(entry.getKey());
		});
    }
    @EventHandler
    private void onChat(EventPacketReceive e) {
    	if (e.getPacket() instanceof S0CPacketSpawnPlayer) {
    		map2 = new LinkedHashMap<C00PacketKeepAlive, Long>();

    		map2.clear();

    		map2de.clear();
    		map2de2.clear();

    		coo = -1;

    		switcher.set(false);
    	}
    	 if ( e.getPacket() instanceof S00PacketKeepAlive) {

 			S00_RECE(e);

 		} else if ( e.getPacket() instanceof S08PacketPlayerPosLook) {

 			PAUSE(e);

 		}
    }
    @EventHandler
    private void onChaat(EventPacketSend e) {
    	if (e.getPacket() instanceof C0FPacketConfirmTransaction) {

			C0F_Send(e);

		}
    }
    public void C0F_Send(EventPacketSend e) {

		final C0FPacketConfirmTransaction packet = (C0FPacketConfirmTransaction) e.getPacket();
		if (packet.windowId != 0)
			choose = packet.windowId;

		if (packet.windowId == 0 && Minecraft.thePlayer != null && Minecraft.theWorld != null) {

			if (Disabler.switcher.get()) {

				if (packet.uid < 0) {

					if (ThreadLocalRandom.current().nextBoolean()) {

						packet.windowId = choose;
						packet.uid = (short) Math.abs(packet.uid);

					}

					map2de.offer(packet);

				} else {

					mc.getNetHandler().getNetworkManager().sendPacket(packet);
				}

				e.setCancelled(true);

			}

			if (Math.abs(packet.uid) - Math.abs(coo) == 1)
				switcher.set(true);

			coo = packet.uid;

		}
	}

	public void S00_RECE(EventPacketReceive e) {

		S00PacketKeepAlive ke = (S00PacketKeepAlive) e.getPacket();

		ke.id = ke.id;

		map2.put(new C00PacketKeepAlive(ke.id),
				System.currentTimeMillis() + dadly22.getValue().longValue() + ThreadLocalRandom.current()
						.nextInt(dadlyS.getValue().intValue(), dadly2S.getValue().intValue()));

		e.setCancelled(true);

	}

	public void PAUSE(EventPacketReceive e) {

		try {

		if (!map2de.isEmpty()) {

			int i = 0;

			while (!map2de.isEmpty()) {

				if (i >= ThreadLocalRandom.current().nextInt(LagBackPacketSendLimitMin.getValue().intValue(),
						LagBackPacketSendLimitMax.getValue().intValue()))
					break;

				C0FPacketConfirmTransaction p = map2de.poll();

				mc.getNetHandler().getNetworkManager().sendPacket(p);

				i++;
			}
			
			Thread.dumpStack();


		}
		
		}catch(Throwable c) {
			c.printStackTrace();
		}

	}
}