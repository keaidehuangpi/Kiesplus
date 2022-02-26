/*
 * Decompiled with CFR 0_132.
 */
package cn.KiesPro.module.modules.movement;

import cn.KiesPro.Client;


		import cn.KiesPro.api.EventTarget;
		import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.module.Module;
		import cn.KiesPro.module.ModuleType;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
		import net.minecraft.network.play.server.S08PacketPlayerPosLook;
//import cn.KiesPro.*;
import cn.KiesPro.api.events.world.EventMove;
import cn.KiesPro.api.events.world.EventPacketSend;
import cn.KiesPro.api.events.world.EventPostUpdate;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.ui.notification.Notification;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.Stopwatch;
import cn.KiesPro.utils.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.*;


import cn.KiesPro.api.events.world.EventBlockBB;
import cn.KiesPro.utils.MoveUtils;


public class Flight
extends Module {
	int time = 0;
	private Mode mode = new Mode("Mode", "mode", (Enum[])FlyMode.values(), (Enum)FlyMode.Vanilla);
	private Mode damageMode = new Mode("Damage", "Damage", (Enum[])damageM.values(), (Enum)damageM.Autumn3);
	public Numbers<Double> vanillaspeed = new Numbers<Double>("VanillaSpeed", "VanillaSpeed", 1.0D, 1.0D, 7.0D, 0.1D);
	public Numbers<Double> multiplytime = new Numbers<Double>("MultiplyTime", "ZoomTime", 230.0D, 100.0D, 1500.0D, 10.0D);
	public Numbers<Double> multiplyspeed = new Numbers<Double>("MultiplySpeed", "ZoomSpeed", 4.0, 0.1, 4.0, 0.5);
	private Option Ground11111 = new Option("Ground", "Ground", Boolean.valueOf(false));
	private Option<Boolean> Bobbing = new Option<Boolean>("Bobbing", "Bobbing", true);
	private Option<Boolean> uhc = new Option<Boolean>("UHC", "UHC", false);
	private Option<Boolean> BoostTime = new Option<Boolean>("BoostTime", "FastTimer", true);
	private double movementSpeed;
    int counter, level;
    double y;
    double moveSpeed, lastDist;
    boolean fly;
    boolean tp;
    TimerUtil timer = new TimerUtil();
    Stopwatch stopwatch = new Stopwatch();
	public Flight() {
        super("Flight", new String[]{"fly", "angel"}, ModuleType.Movement);
        this.addValues(mode, damageMode, vanillaspeed, multiplytime, multiplyspeed, BoostTime, Bobbing, Ground11111, uhc);
    }
    
    public static void damage() {
        double offset = 0.0601f;
        NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        int i = 0;
        while ((double)i < (double)getMaxFallDist() / 0.05510000046342611 + 1.0) {
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + (double)0.0601f, z, false));
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + (double)5.0E-4f, z, false));
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + (double)0.005f + 6.01000003516674E-8, z, false));
            ++i;
        }
        netHandler.addToSendQueueSilent(new C03PacketPlayer(true));
    }

    public static float getMaxFallDist() {
        PotionEffect potioneffect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump);
        int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
        return Minecraft.getMinecraft().thePlayer.getMaxFallHeight() + f;
    }
	
	   public void damagePlayerNew() {
		      if (this.mc.thePlayer.onGround) {
		         for(int var1 = 0; var1 <= ((Boolean)this.uhc.getValue() ? 60 : 49); ++var1) {
		            double[] var2 = new double[]{0.05099991337D, 0.06199991337D, 0.0D};
		            double[] var3 = var2;
		            int var4 = var2.length;

		            for(int var5 = 0; var5 < var4; ++var5) {
		               double var6 = var3[var5];
		               this.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY + var6, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, false));
		            }
		         }

		         this.mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, true));
		      }
		   }
	   
		public void damagePlayer(double d) {
			if (d < 1)
				d = 1;
			if (d > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
				d = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

			double offset = 0.0625;
			if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
				for (int i = 0; i <= ((3 + d) / offset); i++) {
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + d) / offset))));
				}
			}
		}
		
		public void TestAutumn3() {
			if (Minecraft.getMinecraft().thePlayer.onGround) {

				double offset = 0.0601f;
				NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
				EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
				double x = player.posX;
				double y = player.posY;
				double z = player.posZ;
				int i = 0;
				while ((double) i < (double) getMaxFallDist() / 0.05510000046342611 + 1.0) {
					netHandler.addToSendQueueSilent(
							new C03PacketPlayer.C04PacketPlayerPosition(x, y + (double) 0.0601f, z, false));
					netHandler.addToSendQueueSilent(
							new C03PacketPlayer.C04PacketPlayerPosition(x, y + (double) 5.0E-4f, z, false));
					netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x,
							y + (double) 0.005f + 6.01000003516674E-8, z, false));
					++i;
				}
				netHandler.addToSendQueueSilent(new C03PacketPlayer(true));

				/*
				 * for (int var1 = 0; var1 <= 8; ++var1) {
				 * 
				 * mc.getNetHandler().getNetworkManager().sendPacket(new
				 * C03PacketPlayer.C04PacketPlayerPosition( mc.thePlayer.posX, mc.thePlayer.posY
				 * + 0.41999998688698D, mc.thePlayer.posZ, false)); //
				 * this.mc.getNetHandler().getNetworkManager().sendPacket(new //
				 * C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.posy +
				 * // 0.10643676261265D, this.mc.thePlayer.posZ, false));
				 * mc.getNetHandler().getNetworkManager().sendPacket(new
				 * C03PacketPlayer.C04PacketPlayerPosition( mc.thePlayer.posX,
				 * mc.thePlayer.posY, mc.thePlayer.posZ, var1 == 8));
				 * 
				 * }
				 */
				// this.mc.getNetHandler().getNetworkManager().sendPacket(new
				// C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.posy,
				// this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw,
				// this.mc.thePlayer.rotationPitch, true));
			}
		}
	
		
	    public void damageFuck() {
	        //if (mc.thePlayer.onGround) {
	            final double offset = 0.060100000351667404;
	            final NetHandlerPlayClient netHandler = mc.getNetHandler();
	            final EntityPlayerSP player = mc.thePlayer;
	            final double x = player.posX;
	            final double y = player.posY;
	            final double z = player.posZ;
	            if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
	            for (int i = 0; i < getMaxFallDist() / 0.05510000046342611 + 1.0; ++i) {
	                netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.060100000351667404, z, false));
	                netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.000000237487257E-4, z, false));
	                netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.004999999888241291 + 6.01000003516674E-8, z, false));
	            }
	            netHandler.addToSendQueueSilent(new C03PacketPlayer(true));
	        }
	    }
		
	    
		private boolean isStair(Block blockIn) {
			return blockIn instanceof BlockSlab;
		}
	
		
	    public void damageuhc() {
	        if (mc.thePlayer.onGround) {
	            double offset = 0.060100000351667404;
	            NetHandlerPlayClient netHandler = mc.getNetHandler();
	            EntityPlayerSP player = mc.thePlayer;
	            double x = player.posX;
	            double y = player.posY;
	            double z = player.posZ;
	            int i = 0;
	            while ((double)i < ((double)getMaxFallDist() + 1.0) / 0.05510000046342611 + 1.0) {
	                netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.060100000351667404, z, false));
	                netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.000000237487257E-4, z, false));
	                netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.004999999888241291 + 6.01000003516674E-8, z, false));
	                ++i;
	            }
	            netHandler.addToSendQueueSilent(new C03PacketPlayer(true));
	        }
	    }
	    
    @Override
    public void onEnable() {
    	stopwatch.reset();
    	this.tp = false;
    	if(mode.getValue() == FlyMode.Disabler) {
			mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + 0.17, mc.thePlayer.posZ, true));
			mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + 0.06, mc.thePlayer.posZ, true));
    	}else if(mode.getValue() == FlyMode.Disabler2) {
			mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, false));
			mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + 0.16, mc.thePlayer.posZ, true));
			mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + 0.06, mc.thePlayer.posZ, true));
    	}
    	if(mode.getValue() == FlyMode.HypixelBoost) {
			time = 0;
			mc.thePlayer.jump();
    	}else if(mode.getValue() == FlyMode.Hypixel) {
    		if(damageMode.getValue() == damageM.Skid) {
    		damagePlayer(1);
    		}else if(damageMode.getValue() == damageM.Normal) {
    		damagePlayerNew();
    		}else if(damageMode.getValue() == damageM.Autumn) {
    		damage();
    		}else if(damageMode.getValue() == damageM.Autumn2) {
        		if (this.isStair(mc.theWorld
        				.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)).getBlock())
        				|| mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) {
        			mc.timer.timerSpeed = 1;
        			this.setEnabled(false);
        			Helper.sendMessage("不要在台阶上开启Zoomfly!已自动关闭!");
        		}else {
    			damageFuck();
    			//Helper.sendMessage("发送 [Damage] 成功");
        		}
    		}
    	level = 0;
    	this.y = 0.0;
    	moveSpeed = 0.1D;
    	counter = 0;
    	lastDist = 0.0D;
        mc.thePlayer.stepHeight = 0.0f;
        mc.thePlayer.motionX = 0.0;
        mc.thePlayer.motionZ = 0.0;
    	}
    }
    
    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (double)((float)(amplifier + 1) * 0.1f);
        }
        return baseJumpHeight;
    }
	public int Height() {
		if (mc.thePlayer.isPotionActive(Potion.jump)) {
			return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
		}
		return 0;
	}
    
	@EventTarget
	public void onReceivePacket(EventPacketRecieve event) {
		if ((this.mode.getValue() == FlyMode.Disabler || this.mode.getValue() == FlyMode.Disabler2)
				&& event.getPacket() instanceof S08PacketPlayerPosLook) {
			Helper.sendMessage("Teleported after lagback.");
			if (!this.tp) {
				this.tp = true;
			}
		}else {
			if(!(mode.getValue() == FlyMode.Vanilla)) {
				if (event.getPacket() instanceof S08PacketPlayerPosLook && !ModuleManager.getModuleByName("LagbackCheck").isEnabled()) {
					Client.instance.getNotificationManager().sendClientMessage("FlagDelector in Fly.", Notification.Type.WARNING);
					//Helper.sendMessage("FlagDelector in Fly. Set Disabled.");
					mc.timer.timerSpeed = 1;
					this.setEnabled(false);
				}
			}
		}
	}
	
    @Override
    public void onDisable() {
    	stopwatch.reset();
    	if(mode.getValue() == FlyMode.HypixelBoost) {
    		time = 0;
    		mc.timer.timerSpeed = 1;
    	}else if(mode.getValue() == FlyMode.Hypixel) {
    		mc.thePlayer.stepHeight = 0.625f;
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + this.y, mc.thePlayer.posZ);
    	}else if(mode.getValue() == FlyMode.Vanilla) {
    		mc.thePlayer.motionX = 0.0;
    		mc.thePlayer.motionZ = 0.0;
    	}
    	mc.timer.timerSpeed = 1;
    }
    
    @EventTarget
    private void onUpdate(EventPreUpdate e) {
    	this.setSuffix(mode.getValue());
    	if(Bobbing.getValue().booleanValue()) {
    		mc.thePlayer.cameraYaw = 0.105f;
    		//mc.thePlayer.cameraYaw = (float)(0.09090908616781235 * 1);
    	}
    	if(mode.getValue() == FlyMode.HypixelBoost) {
    		mc.thePlayer.motionY = 0;
    		time++;
    		mc.timer.timerSpeed = 1.16F;
    		mc.thePlayer.onGround = true;
    		if(time >= 2) {
    			time = 0;
    			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0054231231323, mc.thePlayer.posZ);
    		}
    	}else if(mode.getValue() == FlyMode.Vanilla) {
            this.mc.thePlayer.motionY = this.mc.thePlayer.movementInput.jump ? vanillaspeed.getValue().intValue() : (this.mc.thePlayer.movementInput.sneak ? -vanillaspeed.getValue().intValue() : 0.0); // 1.0
            if (this.mc.thePlayer.moving()) {
                this.mc.thePlayer.setSpeed(vanillaspeed.getValue().intValue());
            } else {
                this.mc.thePlayer.setSpeed(0.0);
            }
    	}else if(mode.getValue() == FlyMode.Hypixel) {
            if (this.BoostTime.getValue().booleanValue()) {
                mc.timer.timerSpeed = !stopwatch.elapsed(((Double)this.multiplytime.getValue()).longValue()) ? ((Double)this.multiplyspeed.getValue()).floatValue() : 1.0f;
            }
            if (this.level > 2) {
                mc.thePlayer.motionY = 0.0;
            }
            if (this.level <= 2) return;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.003, mc.thePlayer.posZ);
            switch (++this.counter) {
                case 1: {
                    this.y *= -0.949999988079071;
                    break;
                }
                case 2: 
                case 3: 
                case 4: {
                    this.y += 3.25E-4;
                    break;
                }
                case 5: {
                    this.y += 5.0E-4;
                    this.counter = 0;
                }
            }
            e.setY(mc.thePlayer.posY + this.y);
            if (this.level <= 2) return;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.003, mc.thePlayer.posZ);
    	}else if(mode.getValue() == FlyMode.Disabler) {
    		switch(mode.getValue().toString()) {
	    		case "Disabler": {
	    			break;
	    		}
    		}
    	}else if(mode.getValue() == FlyMode.Disabler2) {
    		switch(mode.getValue().toString()) {
	    		case "Disabler2": {
	    			break;
	    		}
    		}
    	}
    }
    
    @EventTarget
    private void onPost(EventPostUpdate e) {
    	if(mode.getValue() == FlyMode.Hypixel) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt((double)(xDist * xDist + zDist * zDist));
    	}
    }
    
    public void DamageX() {
        double offset = 0.060100000351667404;
        NetHandlerPlayClient netHandler = mc.getNetHandler();
        EntityPlayerSP player = mc.thePlayer;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        int i = 0;
        while ((double)i < (double)getMaxFallDist() / 0.05510000046342611 + 1.0) {
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.060100000351667404, z, false));
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.000000237487257E-4, z, false));
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.004999999888241291 + 6.01000003516674E-8, z, false));
            ++i;
        }
        netHandler.addToSendQueueSilent(new C03PacketPlayer(true));
    }
    
    @EventTarget
    private void onMove(EventMove e) {
    	if(mode.getValue() == FlyMode.Hypixel) {
    		if(!fly) {
	    		EntityPlayerSP player = mc.thePlayer;
	    		GameSettings gameSettings = mc.gameSettings;
	    		switch (this.mode.getValue().toString()) {
	    		case "Hypixel": {
	    			if (!player.isMoving()) break;
		    			switch (this.level) {
		    			case 0: {
		    				if (!mc.thePlayer.onGround || !mc.thePlayer.isCollidedVertically) break;
		    				if(uhc.getValue().booleanValue()) {
		    					damageuhc();
		    				}else {
		    					DamageX();
		    				}
		    				//TestAutumn3();
		    				this.moveSpeed = 0.45;
		    				break;
		    			}
		    			case 1: {
		    				if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
		    					e.y = player.motionY = getJumpBoostModifier(0.39999994);
		    				}
		    				this.moveSpeed *= 2.149;
		    				break;
		    			}
		    			case 2: {
		    				this.moveSpeed = 1.17;
		    				break;
		    			}
		    			default: {
		    				this.moveSpeed = this.lastDist - this.lastDist / 159.0;
		    			}
	    			}
	    			setSpeed(e, Math.max(this.moveSpeed, getBaseMoveSpeed()));
	    			++this.level;
	    			break;
	    			}
	    		}
    		}
    	}else if(mode.getValue()==FlyMode.Verusbroken){

			mc.gameSettings.keyBindJump.pressed=false;
			if (mc.thePlayer.onGround&&MoveUtils.isMoving()){
				mc.thePlayer.jump();
				MoveUtils.strafe(0.48F);

			}else {
				MoveUtils.strafe();
			}


		} else if(mode.getValue() == FlyMode.Disabler) {
    		switch (mode.getValue().toString()) {
	    		case "Disabler": {
		    		GameSettings gameSettings = mc.gameSettings;
					mc.thePlayer.motionY = 0.0;
					e.y = 0.0;
					if (this.tp) {
						if (gameSettings.keyBindJump.isKeyDown()) {
							mc.thePlayer.motionY = 2.0;
							e.y = 2.0;
						} else if (gameSettings.keyBindSneak.isKeyDown()) {
							mc.thePlayer.motionY = -2.0;
							e.y = -2.0;
						}
						setSpeed(e, 2.0);
						break;
					}
					setSpeed(e, 0.0);
					break;
	    		}
    		}
    	}else if(mode.getValue() == FlyMode.Disabler2) {
    		GameSettings gameSettings = mc.gameSettings;
    		switch (mode.getValue().toString()) {
	    		case "Disabler2": {
	    			mc.thePlayer.motionY = 0.0;
	    			e.y = 0.0;
	    			if (this.tp) {
	    				if (gameSettings.keyBindJump.isKeyDown()) {
	    					mc.thePlayer.motionY = 2.0;
	    					e.y = 2.0;
	    				} else if (gameSettings.keyBindSneak.isKeyDown()) {
	    					mc.thePlayer.motionY = -2.0;
	    					e.y = -2.0;
	    				}
	    				setSpeed(e, 2.0);
	    				break;
	    			}
	    			setSpeed(e, 0.0);
	    			break;
	    		}
    		}
    	}
    }

	/*@EventTarget
	public void OnBlockBB(EventBlockBB e){
		if (e instanceof BlockAir)
	}*/

    
    public void setSpeed(EventMove moveEvent, double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
    }

    public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                yaw += (float)(forward > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
	
	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2875;
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
			baseSpeed *= 1.0 + 0.2
					* (double) (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}
		return baseSpeed;
	}
    
    @EventTarget
    private void onPacket(EventPacketSend e) {
    	Packet packet = e.getPacket();
    	if(mode.getValue() == FlyMode.HypixelBoost) {
			if(packet instanceof C03PacketPlayer) {
				((C03PacketPlayer) packet).onGround = false;
			}
    	}else if(mode.getValue() == FlyMode.Hypixel && this.level == 0) {
    		e.setCancelled(true);
    	}else if((mode.getValue() == FlyMode.Disabler || mode.getValue() == FlyMode.Disabler2) && e.getPacket() instanceof C03PacketPlayer && !this.tp) {
    		e.setCancelled(true);
    	}
    }

    enum damageM {
    	Autumn,
    	Skid,
    	Normal, Autumn2, Autumn3;
    }
    
    enum FlyMode {
    	Vanilla,
    	HypixelBoost,
    	Hypixel,
		Verusbroken,
    	Disabler,
    	Disabler2;
    }
}

