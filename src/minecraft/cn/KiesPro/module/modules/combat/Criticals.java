/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.combat;

import java.awt.Color;

import cn.KiesPro.Client;
import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.module.modules.movement.Longjump;
import cn.KiesPro.module.modules.movement.Scaffold;
import cn.KiesPro.module.modules.movement.Speed;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.MoveUtil;
import cn.KiesPro.utils.Stopwatch;
import cn.KiesPro.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S2APacketParticles;

public class Criticals
extends Module {
    public static Mode mode = new Mode("Mode", "mode", (Enum[])CritMode.values(), (Enum)CritMode.Packet);
    private static Numbers<Double> delay = new Numbers<Double>("Delay", "Delay", 20.0, 0.0, 500.0, 1.0);
    private Option<Boolean> AFKJump = new Option<Boolean>("AFKJump", "AFKJump", true);
    private static TimerUtil timer = new TimerUtil();
    private static Stopwatch critStopwatch = new Stopwatch();
    private int waitTicks;
    private int groundTicks;

    public Criticals() {
        super("Criticals", new String[]{"crits", "crit"}, ModuleType.Combat);
        this.setColor(new Color(235, 194, 138).getRGB());
        this.addValues(this.mode, this.delay, this.AFKJump);
    }
    
	private boolean isOnGround(double height) {
		if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
    
    @Override
    public void onEnable() {
    	critStopwatch.reset();
    }

    @EventTarget
    private void onUpdate(EventPreUpdate e) {
    	++waitTicks;
        this.setSuffix(this.mode.getValue());
		if (isOnGround(0.001)) {
			groundTicks++;
		} else if (!mc.thePlayer.onGround) {
			groundTicks = 0;
		}
		if(AFKJump.getValue().booleanValue()) {
			if(Killaura.curTarget != null && !mc.thePlayer.isMoving()) {
				if(mc.thePlayer.onGround) {
					mc.thePlayer.jump();
				}
			}
		}
    }
    
    private boolean canCritNew() {
        return !mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround && !ModuleManager.getModuleByName("Speed").isEnabled() && !ModuleManager.getModuleByName("Flight").isEnabled();
    }
    
    @EventTarget
    public void onPacket(EventPacketSend event) {
		if (event.getPacket() instanceof S2APacketParticles || event.getPacket().toString().contains("S2APacketParticles")) {
			return;
		}
		if (event.getPacket() instanceof C02PacketUseEntity && !(event.getPacket() instanceof S2APacketParticles)
				&& !(event.getPacket() instanceof C0APacketAnimation)) {
			C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
			int ticks = this.delay.getValue().intValue();
			if (ModuleManager.getModuleByName("Aura").isEnabled() && mc.thePlayer.onGround && packet.getAction() == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.isCollidedVertically && this.waitTicks >= ticks && this.groundTicks > 1 && Killaura.curTarget != null) {
				if(mode.getValue() == CritMode.Packet) {
		    		PacketCrits();
		    	}else if (mode.getValue()==CritMode.Jump){
					mc.thePlayer.jump();
				}
				else if(mode.getValue() == CritMode.Test) {
		    		Packet2Crits();
		    	}else if(mode.getValue() == CritMode.Hypixel) {
		    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01625, mc.thePlayer.posZ, false));
		    	}else if(mode.getValue() == CritMode.Hypixel2) {
		    		if(Killaura.curTarget.hurtTime > 8) {
		    		Crit2(new Double[] { 0.0412622959183674D, 0.01D, 0.0412622959183674D, 0.01D, 0.001D });
		    		}
		    	}else if(mode.getValue() == CritMode.Hypixel4) {
		    		if(Killaura.curTarget != null) {
		    			//Packet3Crits(Killaura.curTarget);
		    	        if (ModuleManager.getModuleByName("Aura").isEnabled() && canCritNew()) {
		    	            final double x = mc.thePlayer.posX;
		    	            final double y = mc.thePlayer.posY;
		    	            final double z = mc.thePlayer.posZ;
		    	            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.05, z, false));
		    	            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
		    	            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.012511, z, false));
		    	            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
		    	        }
		    		}
		    	}else if(mode.getValue() == CritMode.NoGround) {
		    		Packet packet2 = event.getPacket();
		    		if(packet2 instanceof C03PacketPlayer) {
		    			((C03PacketPlayer) packet2).onGround = false;
		    		}
		    	}else if(mode.getValue() == CritMode.Hypixel5) {
					boolean shouldCritical;
			        boolean hypixel = true;
			        boolean bl = shouldCritical = mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && (ModuleManager.getModuleByName("Criticals").isEnabled() && Criticals.mode.getValue() == Criticals.CritMode.Hypixel5) && !ModuleManager.getModuleByName("Speed").isEnabled() && this.critStopwatch.elapsed(200L) && mc.thePlayer.hurtTime <= 0;
			        if (shouldCritical) {
			            for (double offset : new double[] {0.05250000001304, 0.00150000001304, 0.01400000001304, 0.00150000001304}) {
			                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
			            }
			            this.critStopwatch.reset();
			        }
		    	}else if(mode.getValue() == CritMode.NMSLOLINE3) {
					boolean hypixel = true;
					boolean shouldCritical = mc.thePlayer.isCollidedVertically
							&& mc.thePlayer.onGround && ModuleManager.getModuleByName("Criticals")
									.isEnabled() && this.critStopwatch.elapsed(200L) && Killaura.curTarget.hurtTime <= 0;
					if (shouldCritical) {
						for (double offset : hypixel ? this.hypixelOffsets : this.offsets) {
							mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
									mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
						}
						this.critStopwatch.reset();
					}
		    	}else if(mode.getValue() == CritMode.SuckMyDick) {
		               if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.isCollidedVertically && Killaura.allowCrits && this.waitTicks >= ticks) {
		                   if (Client.instance.getModuleManager().getModuleByClass(Longjump.class).isEnabled() || Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
		                      return;
		                   }
			            double offset1 = (double)(randomNumber(-9999, 9999) / 10000000);
			            double offset2 = (double)(randomNumber(-9999, 9999) / 1000000000);
			            double[] var5 = new double[]{0.0624218713251234D + offset1, 0.0D, 1.0834773E-5D + offset2, 0.0D};
			            int var6 = var5.length;
	
			            for(int var7 = 0; var7 < var6; ++var7) {
			               double offset = var5[var7];
			               mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
			            }
		            }
		            waitTicks = 0;
		    	}else if(mode.getValue() == CritMode.Autumn) {
		            boolean shouldCritical;
		            boolean hypixel = isOnHypixel();
		            EntityPlayerSP player = mc.thePlayer;
		            boolean bl = shouldCritical = player.isCollidedVertically && player.onGround && ModuleManager.getModuleByName("Criticals").isEnabled() && !ModuleManager.getModuleByName("Speed").isEnabled() && this.critStopwatch.elapsed(200L) && Killaura.curTarget.hurtTime <= 0;
		            if (shouldCritical) {
		                for (double offset : hypixel ? this.hypixelOffsets : this.offsets) {
		                    mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + offset, player.posZ, false));
		                }
		                this.critStopwatch.reset();
		            }
		    	}else if(mode.getValue() == CritMode.PowerX) {
		            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
		                if (!canCrit()) {    
		                    return;        
		                }
		                for (double d : new double[]{0.06142999976873398, 0.0, 0.012511000037193298, 0.0}) {
		                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, true));
		                }
		            }
		    	}else if(mode.getValue() == CritMode.Power) {
		    		if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
		    		    if(!canCrit()) {
		                   return;
		                }
		                double[] v3 = new double[]{0.06142999976873398, 0.0, 0.012511000037193298, 0.0};
		                int n = v3.length;
		                int n2 = 0;
		                while (n2 < n) {
		                    double offset = v3[n2];

		                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + offset, this.mc.thePlayer.posZ, true));
		                    ++n2;
		                }
		    		}
		    	}
			}
		}
    }
    
    private static int randomNumber(int max, int min) {
        return (int)(Math.random() * (double)(max - min)) + min;
     }

    public boolean isOnHypixel() {
        return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.contains("hypixel");
    }
    
    //private final double[] hypixelOffsets = new double[] { 0.05000000074505806, 0.0015999999595806003, 0.029999999329447746, 0.0015999999595806003 };;
    //private final double[] offsets = new double[] { 0.05, 0.0, 0.012511, 0.0 };
    //private final double[] hypixelOffsets = new double[]{0.05000000074505806, 0.0015999999595806003, 0.029999999329447746, 0.0015999999595806003};
    private final double[] hypixelOffsets = new double[]{0.05000000074505806D, 0.0015999999595806003D, 0.029999999329447746D, 0.0015999999595806003D};
    private final double[] offsets = new double[]{0.05, 0.0, 0.012511, 0.0};
    private void Packet3Crits(EntityLivingBase entity) {
        boolean shouldCritical;
        boolean hypixel = true;
        boolean bl = shouldCritical = mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && this.critStopwatch.elapsed(150L) && entity.hurtTime <= 0;
        if (shouldCritical) {
            for (double offset : hypixel ? this.hypixelOffsets : this.offsets) {
                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
            }
            this.critStopwatch.reset();
        }
	}
    
    private boolean canAttack() {
    	return !Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled();
    }

	private boolean canCrit() {
        if (this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater() && !Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
            return true;
        }
        return false;
    }
    
	public void Crit2(Double[] value) {
		NetworkManager var1 = Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager();
		Double curX = Minecraft.getMinecraft().thePlayer.posX;
		Double curY = Minecraft.getMinecraft().thePlayer.posY;
		Double curZ = Minecraft.getMinecraft().thePlayer.posZ;
		if(timer.hasReached(delay.getValue().doubleValue()) && canCrit() && MoveUtil.isOnGround(0.072)) {
			for (Double offset : value) {
				Helper.sendMessage("Try send a packet...");
				var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + offset, curZ, false));
			}
			timer.reset();
		}
	}
    
    private void PacketCrits() {
    	double x = mc.thePlayer.posX;
    	double y = mc.thePlayer.posY;
    	double z = mc.thePlayer.posZ;
    	Helper.sendMessage("Try send a packet...");
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.11, z, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1100013579, z, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0000013579, z, false));
        mc.thePlayer.onCriticalHit(Killaura.curTarget);
    }
    
    private void Packet2Crits() {
    	if(canCrti()) {
        final double x = mc.thePlayer.posX;
        final double y = mc.thePlayer.posY;
        final double z = mc.thePlayer.posZ;
        Helper.sendMessage("Try send a packet...");
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.05, z, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.012511, z, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
    	}
    }

    private boolean canCrti() {
        return !mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround && !ModuleManager.getModuleByName("Speed").isEnabled() && !ModuleManager.getModuleByName("Flight").isEnabled();
    }
    
    static enum CritMode {
        Packet,
		Jump,
        Hypixel,
        Hypixel2,
        Hypixel3,
        Hypixel4,
        Hypixel5,
        NoGround,
        NMSLOLINE3,
        Autumn,
        SuckMyDick,
        PowerX,
        Power,
        Test;
    }

}

