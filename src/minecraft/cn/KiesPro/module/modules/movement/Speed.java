package cn.KiesPro.module.modules.movement;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import cn.KiesPro.Client;
import cn.KiesPro.api.EventTarget;
import cn.KiesPro.api.events.world.EventMove;
import cn.KiesPro.api.events.world.EventPacketRecieve;
import cn.KiesPro.api.events.world.EventPreUpdate;
import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.ui.notification.Notification;
import cn.KiesPro.utils.BlockUtils;
import cn.KiesPro.utils.Helper;
import cn.KiesPro.utils.MoveUtil;
import cn.KiesPro.utils.PlayerUtil;
import cn.KiesPro.utils.TimeHelper;
import cn.KiesPro.utils.math.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class Speed
extends Module {
	private Mode mode = new Mode("Mode", "mode",Bhopmode.values(), Bhopmode.SlowHop);
	public static Numbers<Double> timerspeed=new Numbers<Double>("timerspeed","timerspeed",1.0,0.1,10.0,0.1);
	public static Numbers<Double> BhoPSpeed = new Numbers<Double>("BHopSpeed", "BhopSpeed", 1.0, 1.0, 15.0, 0.1);
	private Option<Boolean> StairCheck = new Option<Boolean>("Stairs", "Stairs", true);
	private Option<Boolean> ScaffoldCheck = new Option<Boolean>("CheckScaff", "CheckScaff", true);
	private Option<Boolean> Lagback = new Option<Boolean>("LagbackCheck", "LagbackCheck", true);
	private int stage = 0;
	private boolean shouldslow = false;
	boolean collided = false;
	boolean lessSlow;
	double less;
	double stair;
	double a;
	double y;
	double movespeed;
	static double speed = 0.07999999821186066D;
	double distance;
	private double lastDist;
	
	private TargetStrafe targetStrafe;
	
	TimeHelper lastCheck = new TimeHelper();
	TimeHelper timer = new TimeHelper();
	
	public Speed() {
        super("Speed", new String[]{"bhop"}, ModuleType.Movement);
        this.addValues(mode, BhoPSpeed, StairCheck, Lagback,timerspeed);
	}

	@EventTarget
	public void onReceivePacket(EventPacketRecieve event) {
		if(Lagback.getValue().booleanValue()) {
			if (event.getPacket() instanceof S08PacketPlayerPosLook && !ModuleManager.getModuleByName("LagbackCheck").isEnabled()) {
				//Helper.sendMessage("FlagDelector in Speed. Set Disabled.");
				Client.instance.getNotificationManager().sendClientMessage("speed was disabled to prevent flags/errors", Notification.Type.WARNING);
				this.setEnabled(false);
			}
		}
	}
	
	@EventTarget
	public void onEnable() {
		/*if(mode.getValue() == Bhopmode.Hypixel) {
	        this.lessSlow = false;
	        this.less = 0.0;
	        this.stage = 2;
		this.stage = 2;
		mc.timer.timerSpeed = 1.0f;
		}*/
		if(StairCheck.getValue().booleanValue()) {
    		if (this.isStair(mc.theWorld
    				.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock())) {
    			this.setEnabled(false);
    		Helper.sendMessage("Stairs Check!");
    		}
		}
		if(mode.getValue() == Bhopmode.Hypixel4) {
			boolean player = mc.thePlayer == null;
	        this.collided = player ? false : mc.thePlayer.isCollidedHorizontally;
	        this.lessSlow = false;
	        if (mc.thePlayer != null) {
	            this.movespeed = defaultSpeed2();
	        }
	        this.less = (double)Speed.randomNumber(-10000, 0) / 1.0E7;
	        this.less = 0.0;
	        this.stage = 2;
		}else if(mode.getValue() == Bhopmode.Hypixel5) {
	        this.lastDist = 0.0;
	        this.stage = 0;
		}

		Client.instance.getNotificationManager().sendClientMessage("enabled speed", Notification.Type.SUCCESS);
	}
	public void onDisable(){
		mc.timer.timerSpeed=1;
		Client.instance.getNotificationManager().sendClientMessage("disabled speed", Notification.Type.SUCCESS);
	}
	
    public double defaultSpeed2() {
        double baseSpeed = 0.2873;
        Minecraft.getMinecraft();
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            Minecraft.getMinecraft();
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }
    
    public double getBaseMoveSpeed2() {
        double baseSpeed = 0.2875;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (double)(mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
	
	private boolean isStair(Block blockIn) {
		return blockIn instanceof BlockStairs;
	}
	
    private boolean canZoom() {
        if (this.mc.thePlayer.moving() && this.mc.thePlayer.onGround) {
            return true;
        }
        return false;
    }

    @EventTarget
    public void onMotion(EventPreUpdate e) {
    	this.setSuffix(this.mode.getValue());
    	if(mode.getValue() == Bhopmode.HypixelHop) {
            if(mc.thePlayer.isMoving()) {

                if(mc.thePlayer.onGround) {
                    mc.thePlayer.jump();

                    float speed = (float) (PlayerUtil.getSpeed() < 0.56F ? PlayerUtil.getSpeed() * 1.045F : 0.56F);

                    if(mc.thePlayer.onGround && mc.thePlayer.isPotionActive(Potion.moveSpeed))
                        speed *= 1F + 0.13F * (1 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier());

                    MoveUtil.strafe(speed);
                    return;
                }else if(mc.thePlayer.motionY < 0.2D)
                    mc.thePlayer.motionY -= 0.02D;

                MoveUtil.strafe(PlayerUtil.getSpeed() * 1.01889F);
            }else{
                mc.thePlayer.motionX = mc.thePlayer.motionZ = 0D;
            }
    	}
    	if(mode.getValue() == Bhopmode.Hypixel) {
    		if ((Minecraft.thePlayer.moveForward == 0.0F) && (Minecraft.thePlayer.moveStrafing == 0.0F)) {
    	        this.speed = PlayerUtil.defaultSpeed();
    	      }
    	      if ((stage == 1) && (Minecraft.thePlayer.isCollidedVertically) && ((Minecraft.thePlayer.moveForward != 0.0F) || (Minecraft.thePlayer.moveStrafing != 0.0F))) {
    	        this.speed = (1.0D + PlayerUtil.defaultSpeed() - 0.0D);
    	      }
    	      if ((!isInLiquid()) && (stage == 2) && (Minecraft.thePlayer.isCollidedVertically) && (PlayerUtil.isOnGround(0.01D)) && ((Minecraft.thePlayer.moveForward != 0.0F) || (Minecraft.thePlayer.moveStrafing != 0.0F)))
    	      {
    	        Minecraft.getMinecraft();
    	        if (Minecraft.thePlayer.isPotionActive(Potion.jump))
    	        {
    	          Minecraft.getMinecraft();EventMove.setY(Minecraft.thePlayer.motionY = 0.3785D + (Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 0.01D) * 5.0E-4D);
    	        }
    	        else
    	        {
    	          EventMove.setY(Minecraft.thePlayer.motionY = 0.34955D);
    	        }
    	        Minecraft.thePlayer.jump();
    	        this.speed *= 1.0275D;
    	      }
    	      else if (stage == 3)
    	      {
    	        double difference = 0.41D * (this.lastDist - PlayerUtil.defaultSpeed());
    	        this.speed = (this.lastDist - difference);
    	      }
    	      else
    	      {
    	        List collidingList = Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.boundingBox.offset(0.0D, Minecraft.thePlayer.motionY, 0.0D));
    	        if (((collidingList.size() > 0) || (Minecraft.thePlayer.isCollidedVertically)) && (stage > 0)) {
    	          stage = (Minecraft.thePlayer.moveForward != 0.0F) || (Minecraft.thePlayer.moveStrafing != 0.0F) ? 1 : 0;
    	        }
    	        this.speed = (this.lastDist - this.lastDist / 159.0D);
    	      }
    	      this.speed = Math.max(this.speed, PlayerUtil.defaultSpeed());
    	      if (stage > 0)
    	      {
    	        if (BlockUtils.isInLiquid()) {
    	          this.speed = 9.0E-4D;
    	        }
    	        setMotion2(e, this.speed);
    	      }
    	      if ((Minecraft.thePlayer.moveForward != 0.0F) || (Minecraft.thePlayer.moveStrafing != 0.0F)) {
    	        stage += 1;
    	      }
    	}else if(mode.getValue() == Bhopmode.SlowHop) {
	        if(mc.thePlayer.isInWater())
	            return;
	
	        if(PlayerUtil.isMoving()) {
	            if(mc.thePlayer.onGround)
	                mc.thePlayer.jump();
	            else
	                strafe(getSpeed() * 1.011F);
	        }else{
	            mc.thePlayer.motionX = 0D;
	            mc.thePlayer.motionZ = 0D;
	        }
    	}else if(mode.getValue() == Bhopmode.Hypixel2 || mode.getValue() == Bhopmode.Hypixel3) {
	    	if (mc.thePlayer.isMoving()) {
	            final double var7 = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
	            final double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
	            this.distance = Math.sqrt(var7 * var7 + zDist * zDist);
	        }
	        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
	        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
	        this.distance = Math.sqrt((double)(xDist * xDist + zDist * zDist));
    	}else if(mode.getValue() == Bhopmode.Hypixel4) {            
    		double var7 = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        this.distance = Math.sqrt((double)(var7 * var7 + zDist * zDist));
    	       if (this.lastDist > 5.0) {
    	            this.lastDist = 0.0;
    	        }
    	        this.lastDist = Math.sqrt((double)((mc.thePlayer.posX - mc.thePlayer.prevPosX) * (mc.thePlayer.posX - mc.thePlayer.prevPosX) + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (mc.thePlayer.posZ - mc.thePlayer.prevPosZ)));
    	}else if(mode.getValue() == Bhopmode.Hypixel5 || mode.getValue() == Bhopmode.Ayuan) {
    		if(mc.thePlayer.isMoving()) {
                if (mc.thePlayer.isCollidedVertically) {
                    e.setY(mc.thePlayer.posY + 7.435E-4);
                }
    		}
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    	}else if(mode.getValue() == Bhopmode.Ground && this.canSpeed()) {
    		if(mc.thePlayer.isMoving()) {
	            e.setOnground(this.stage == 5);
	            e.setY(mc.thePlayer.posY + this.y + 7.435E-4);
    		}
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    	}else if(mode.getValue() == Bhopmode.Novoline) {
            if (mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
                movespeed *= 2.0;
            }
             double v11 = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
             double v3 = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
             this.lastDist = Math.sqrt(v11 * v11 + v3 * v3);
    	}
    	if(mode.getValue() == Bhopmode.VanillaHop) {
	         if(this.mc.thePlayer.onGround && PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
		            this.mc.thePlayer.jump();
		         } else if(PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
		            PlayerUtil.setSpeed(BhoPSpeed.getValue().doubleValue());
		         }

		         if(!PlayerUtil.MovementInput()) {
		            this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = 0.0D;
		         }
    	}
    	if(mode.getValue() == Bhopmode.AutoJump) {
    		if(this.mc.thePlayer.onGround)
    		 mc.thePlayer.jump();


    	}
    }
	@EventTarget
	private void onUpdate(EventPreUpdate e) {
		mc.timer.timerSpeed = timerspeed.getValue().floatValue();
	}
    
    private void setMotion2(EventPreUpdate e, double speed)
  {
    if (this.tsBoost > 0)
    {
      this.tsBoost -= 1;
      speed *= 1.7234277234D;
    }
    double forward = MovementInput.moveForward;
    double strafe = MovementInput.moveStrafe;
    float yaw = Minecraft.thePlayer.rotationYaw;
    if ((forward == 0.0D) && (strafe == 0.0D))
    {
      EventMove.setX(0.0D);
      EventMove.setZ(0.0D);
    }
    else
    {
      if (forward != 0.0D)
      {
        if (strafe > 0.0D) {
          yaw += (forward > 0.0D ? -45 : 45);
        } else if (strafe < 0.0D) {
          yaw += (forward > 0.0D ? 45 : -45);
        }
        strafe = 0.0D;
        if (forward > 0.0D) {
          forward = 1.0D;
        } else if (forward < 0.0D) {
          forward = -1.0D;
        }
      }
      EventMove.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
      EventMove.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
    }
  }

	@EventTarget
    public void onMove(EventMove e) {
    	if(mode.getValue() == Bhopmode.Hypixel) {
			if (mc.thePlayer.isCollidedHorizontally) {
				this.collided = true;
			}

			if (this.collided) {
				//mc.timer.timerSpeed = 1.0F;
				stage = -1;
			}

			if (this.stair > 0.0D) {
				this.stair -= 0.25D;
			}

			this.less -= this.less > 1.0D ? 0.12D : 0.11D;
			if (this.less < 0.0D) {
				this.less = 0.0D;
			}

			if (!PlayerUtil.isInLiquid() && PlayerUtil.isOnGround(0.01D) && isMoving() && !ModuleManager.getModuleByName("Scaffold").isEnabled()) {
				this.collided = mc.thePlayer.isCollidedHorizontally;
				if (stage >= 0 || this.collided) {
					stage = 0;
					a = 0.4086666D + (double) MoveUtil.getJumpEffect() * 0.1D;
					if (this.stair == 0.0D) {
						mc.thePlayer.jump();
						mc.thePlayer.motionY = a;
						e.setY(mc.thePlayer.motionY);
					}

					++this.less;
					this.lessSlow = this.less > 1.0D && !this.lessSlow;
					if (this.less > 1.12D) {
						this.less = 1.12D;
					}
				}
			}

			this.speed = this.getHypixelSpeed(stage) + 0.0331D;
			this.speed *= 0.91D;
			if (this.stair > 0.0D) {
				this.speed *= 0.66D - (double) MoveUtil.getSpeedEffect() * 0.1D;
			}

			if (stage < 0) {
				this.speed = getBaseMoveSpeed();
			}

			if (this.lessSlow) {
				this.speed *= 0.93D;
			}

			if (PlayerUtil.isInLiquid()) {
				this.speed = 0.12D;
    	}
    	}else if(mode.getValue() == Bhopmode.Sensation) {
			boolean skyWars = true;
			boolean shouldslow = false;
			double val = 0.619D;
			boolean cZg = false;
			boolean cZh = false;
			if (skyWars) {
				if (ModuleManager.getModuleByName("Scaffold").isEnabled()) {
					shouldslow = true;
				} else if (System.currentTimeMillis() - System.currentTimeMillis() >= 350L && shouldslow) {
					shouldslow = false;
				}
				if (PlayerUtil.isMoving2()) {
					++stage;
					if (stage == 1 && MoveUtil.isOnGround(0.001)) {
						double var17 = mc.thePlayer.isPotionActive(Potion.jump) ? 0.4026D : 0.4224D;
						if (mc.thePlayer.isPotionActive(Potion.jump)) {
							var17 += (double) (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1)
									* 0.105D;
						}
						e.setY(mc.thePlayer.motionY = var17);
						if (mc.thePlayer.isPotionActive(Potion.jump)) {
							this.movespeed = PlayerUtil.getBaseMoveSpeed() * 1.4D;
						} else if (mc.theWorld
								.getBlockState(
										new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ))
								.getBlock() instanceof BlockStairs) {
							this.movespeed = PlayerUtil.getBaseMoveSpeed() * 1.0D;
						} else {
							double v12 = (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56D : 1.55D);
							this.movespeed = PlayerUtil.getBaseMoveSpeed() * v12;
						}
					} else {
						java.util.List<AxisAlignedBB> v3 = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
								mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D));
						if (MoveUtil.isOnGround(0.001) || mc.thePlayer.isCollidedVertically
								|| v3.size() > 0 && stage > 0) {
							stage = 0;
						}
						if (val >= (cZg ? 0.64D : 0.66D)) {
							cZh = true;
						}
						if (val <= 0.62D) {
							cZh = false;
						}

						val += cZh ? -0.0025D : 0.012D;
						if (!cZg) {
							val += 0.02D;
						}
						this.movespeed -= (skyWars ? (mc.theWorld
								.getBlockState(
										new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ))
								.getBlock() instanceof BlockStairs ? 0.67D : val) : 0.69D)
								* (movespeed - PlayerUtil.getBaseMoveSpeed());
					}
					this.setMoveSpeed(e, Math.max(PlayerUtil.getBaseMoveSpeed(), movespeed));
				} else {
					this.movespeed = PlayerUtil.getBaseMoveSpeed();
				}
			} else if (MoveUtil.isOnGround(0.001)
					&& (mc.thePlayer.moveForward > 0.0F || mc.thePlayer.moveStrafing != 0.0F)) {
				this.movespeed = 0;
				EventMove.y = mc.thePlayer.motionY = 0.41999998688697815D;
				this.setMoveSpeed(e, this.movespeed = 0.54D + (double) randomFloat(0.0F, 0.03F));
				if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
					this.setMoveSpeed(e, this.movespeed = 0.54D + (double) randomFloat(0.0F, 0.03F)
							+ (double) mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 0.1D);
				}
			}
    	}else if(mode.getValue() == Bhopmode.Hypixel2) {
			if (!PlayerUtil.isInLiquid()) {
				if(StairCheck.getValue().booleanValue()) {
	        		if (this.isStair(mc.theWorld
	        				.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock())) {
						Client.instance.getNotificationManager().sendClientMessage("speed was because of stairs", Notification.Type.WARNING);
						this.setEnabled(false);
	        		//Helper.sendMessage("Stairs Check!");
	        		}
				}
				if (this.canZoom() && this.stage == 2 && !ModuleManager.getModuleByName("Scaffold").isEnabled()) {
					//mc.timer.timerSpeed = 1.0f;
					e.setY(mc.thePlayer.motionY = getJumpBoostModifier(0.40123128));
					double n = 2.149;
					this.movespeed = getBaseMoveSpeed() * n;
					//Helper.sendMessage(String.valueOf(n));
				} else if (this.stage == 3) {
					this.movespeed = this.distance - 0.7095 * (this.distance - getBaseMoveSpeed());
					//this.movespeed = this.distance + diff;
				} else {
					final List collidingList = this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer,
							this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0));
					if ((collidingList.size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
						this.stage = this.mc.thePlayer.moving() ? 1 : 0;
						//mc.timer.timerSpeed = 1.085f;
					}
					//this.movespeed = getBaseMoveSpeed() * 1.00000011920929D;
					this.movespeed = this.distance - this.distance / 159.0D;
				}
				this.movespeed = Math.max(this.movespeed, getBaseMoveSpeed());
				++this.stage;
			}else {
				Client.instance.getNotificationManager().sendClientMessage("speed was disabled to prevent flags/errors", Notification.Type.WARNING);
				this.setEnabled(false);
			}
    	}else if(mode.getValue() == Bhopmode.Novoline) {
					if (this.canZoom() && this.stage == 2 && !ModuleManager.getModuleByName("Scaffold").isEnabled()) {
						//mc.timer.timerSpeed = 1.0f;
		                if (MathHelper.arisRound(mc.thePlayer.posY - (double)((int)mc.thePlayer.posY), 3) == MathHelper.arisRound(0.138, 3)) {
		                    movespeed *= 1.25;
		                }
						e.setY(mc.thePlayer.motionY = getJumpBoostModifier(0.399999986886975));
						/*double n;
						if(mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
							n = 2.0D;
						}else {
							n = 1.25D;
						}*/
						//this.movespeed *= n;
						//movespeed *= getBaseMoveSpeed() * 1.871D;
						//Helper.sendMessage(String.valueOf(n));
					} else if (this.stage == 3) {
						this.movespeed = getBaseMoveSpeed() * 1.6199999475479125D;
						//this.movespeed = this.distance + diff;
					} else {
						final List collidingList = this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer,
								this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0));
						if ((collidingList.size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
							this.stage = this.mc.thePlayer.moving() ? 1 : 0;
							//mc.timer.timerSpeed = 1.085f;
						}
						this.movespeed = getBaseMoveSpeed() * 0.800000011920929D;
						this.movespeed = this.lastDist - this.lastDist / 99.0D;
					}
					this.movespeed = Math.max(this.movespeed, getBaseMoveSpeed());
					setSpeed(e, movespeed);
					++this.stage;
    	}else if(mode.getValue() == Bhopmode.Hypixel3) {
    		EntityPlayerSP player = mc.thePlayer;
    		if(!PlayerUtil.isInLiquid()) {
    			if(mc.thePlayer.isMoving()) {
			switch (this.stage) {
			case 2: {
				if (!player.onGround || !player.isCollidedVertically && !ModuleManager.getModuleByName("Scaffold").isEnabled())
					break;
				//mc.timer.timerSpeed = 1.0f;
				e.y = player.motionY = getJumpBoostModifier(0.40871);
				double n = randomNumber(1.447, 1.449);
				n = 1.44721;
				this.movespeed = getBaseMoveSpeed() * n;
				break;
			}
			case 3: {
				double difference = (double) 0.62f * (this.distance - getBaseMoveSpeed());
				this.movespeed = this.distance + difference;
				break;
			}
			default: {
				if (mc.theWorld.getCollidingBoundingBoxes(player,
						player.getEntityBoundingBox().offset(0.0, player.motionY, 0.0)).size() > 0
						|| player.isCollidedVertically && player.onGround) {
					this.stage = 1;
					// SpeedMod.mc.timer.timerSpeed = 1.085f;
				}
				this.movespeed = this.distance - this.distance / 89.0;
			}
			}
			this.movespeed = Math.max(this.movespeed, getBaseMoveSpeed());
			++this.stage;
    		}
    		}else {
				this.setEnabled(false);
				Helper.sendMessage("Lag for Water!");
    		}
    	}else if(mode.getValue() == Bhopmode.Hypixel4) {
    		if(!ModuleManager.getModuleByName("Scaffold").isEnabled()) {
            if (mc.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
                //mc.timer.timerSpeed = 1.0f;
                this.stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            this.less -= this.less > 1.0 ? 0.12 : 0.11;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!PlayerUtil.isInLiquid() && MoveUtil.isOnGround((double)0.01) && mc.thePlayer.isMoving()) {
                this.collided = mc.thePlayer.isCollidedHorizontally;
                if (this.stage >= 0 || this.collided) {
                    this.stage = 0;
                    double motY = 0.4086666;
                    if (this.stair == 0.0) {
                    	e.y = mc.thePlayer.motionY = getJumpBoostModifier(0.40871);
                    	this.movespeed = getBaseMoveSpeed() * motY;
                    }
                    this.less += 1.0;
                    boolean bl = this.lessSlow = this.less > 1.0 && !this.lessSlow;
                    if (this.less > 1.12) {
                        this.less = 1.12;
                    }
                }
            }
            this.movespeed = this.getHypixelSpeed(this.stage) + 0.0331;
            this.movespeed *= 0.91;
            if (this.stair > 0.0) {
                this.movespeed *= 0.66 - (double)MoveUtil.getSpeedEffect() * 0.1;
            }
            if (this.stage < 0) {
                this.movespeed = this.getBaseMoveSpeed();
            }
            if (this.lessSlow) {
                this.movespeed *= 0.93;
            }
            if (PlayerUtil.isInLiquid()) {
                this.movespeed = 0.12;
            }
            this.setMotion(e, this.movespeed);
            ++this.stage;
            }
    	}else if(mode.getValue() == Bhopmode.Hypixel5) {
    		EntityPlayerSP player = mc.thePlayer;
    		if (player.isMoving()) {
            switch (this.stage) {
            case 2: {
                if (!player.onGround || !player.isCollidedVertically) break;
                //mc.timer.timerSpeed = 1.0f;
                e.y = player.motionY = getJumpBoostModifier(0.40123128);
                double n = 2.149;
                this.movespeed = getBaseMoveSpeed2() * n;
                break;
            }
            case 3: {
                this.movespeed = this.lastDist - 0.7095 * (this.lastDist - getBaseMoveSpeed2());
                break;
            }
            default: {
                if (mc.theWorld.getCollidingBoundingBoxes(player, player.getEntityBoundingBox().offset(0.0, player.motionY, 0.0)).size() > 0 || player.isCollidedVertically && player.onGround) {
                    this.stage = 1;
                }
                this.movespeed = this.lastDist - this.lastDist / 159.0;
            }
        }
        this.movespeed = Math.max(this.movespeed, getBaseMoveSpeed2());
        setSpeed(e, this.movespeed);
        ++stage;
    		}
    	}else if(mode.getValue() == Bhopmode.Ground) {
    		block0 : switch(mode.getValue().toString()) {
    		case "Ground": {
                if (this.canSpeed()) {
                    switch (this.stage) {
                        case 1: {
                            //mc.timer.timerSpeed = 1.0f;
                            this.y = getJumpBoostModifier(0.40001);
                            this.movespeed = getBaseMoveSpeed() * 2.149;
                            break block0;
                        }
                        case 2: {
                            this.y = getJumpBoostModifier(0.381);
                            double difference = 0.66 * (this.lastDist - getBaseMoveSpeed());
                            this.movespeed = this.lastDist - difference;
                            break block0;
                        }
                        case 3: {
                            this.y = getJumpBoostModifier(0.22);
                            this.movespeed = this.lastDist - this.lastDist / 159.0;
                            break block0;
                        }
                        case 4: {
                            this.y = getJumpBoostModifier(0.11);
                            this.movespeed = this.lastDist - this.lastDist / 159.0;
                            break block0;
                        }
                        case 5: {
                            //mc.timer.timerSpeed = 2.0f;
                            this.y = 0.0;
                            this.movespeed = this.lastDist - this.lastDist / 159.0;
                            this.stage = 0;
                        }
                    }
                    break;
                }
                this.y = 0.0;
                this.movespeed = getBaseMoveSpeed();
                this.stage = 0;
                //mc.timer.timerSpeed = 1.0f;
                break;
    		}
    		}
    	setSpeed(e, this.movespeed);
    	++this.stage;
    	}else if(mode.getValue() == Bhopmode.Ayuan) {
    		if(mc.thePlayer.isMoving()) {
	    		switch (mode.getValue().toString()) {
		    		case "Ayuan": {
		            switch (this.stage) {
		            case 2: {
		                if (!mc.thePlayer.onGround || !mc.thePlayer.isCollidedVertically) break;
		                //mc.timer.timerSpeed = 1.0f;
		                e.y = mc.thePlayer.motionY = getJumpBoostModifier((double)0.3999999463558197);
		                this.movespeed *= 2.0;
		                break;
		            }
		            case 3: {
		                double difference = 0.75 * (this.lastDist - getBaseMoveSpeed());
		                this.movespeed = this.lastDist - difference;
		                break;
		            }
		            default: {
		            	if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0 || mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
		                    this.stage = 1;
		                    //mc.timer.timerSpeed = 1.085f;
		                }
		                this.movespeed = this.lastDist - this.lastDist / 159.0;
		            }
		        }
		        this.movespeed = Math.max((double)this.movespeed, (double)getBaseMoveSpeed());
		        break;
		    	}
		    	}
	    		setSpeed(e, this.movespeed);
	    		++stage;
    		}
    	}
			
			if (isMoving() && mode.getValue() == Bhopmode.Hypixel) {
				if (!((TargetStrafe)ModuleManager.getModuleByName("TargetStrafe")).doStrafeAtSpeed(e, this.speed)) {
				this.setMotion(e, this.speed);
				++stage;
			}
		}else if (isMoving() && (mode.getValue() == Bhopmode.Hypixel2 || mode.getValue() == Bhopmode.Hypixel3)) {
			if (!((TargetStrafe)ModuleManager.getModuleByName("TargetStrafe")).doStrafeAtSpeed(e, this.movespeed)) {
				setMoveSpeed(e, this.movespeed);
			}
		}
    }
    
    private boolean canSpeed() {
        Block blockBelow = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock();
        return mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isPressed() && blockBelow != Blocks.stone_stairs && blockBelow != Blocks.oak_stairs && blockBelow != Blocks.sandstone_stairs && blockBelow != Blocks.nether_brick_stairs && blockBelow != Blocks.spruce_stairs && blockBelow != Blocks.stone_brick_stairs && blockBelow != Blocks.birch_stairs && blockBelow != Blocks.jungle_stairs && blockBelow != Blocks.acacia_stairs && blockBelow != Blocks.brick_stairs && blockBelow != Blocks.dark_oak_stairs && blockBelow != Blocks.quartz_stairs && blockBelow != Blocks.red_sandstone_stairs && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ)).getBlock() == Blocks.air;
    }

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
    
    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }
    
	private double getJumpBoostModifier(double baseJumpHeight) {
		if (mc.thePlayer.isPotionActive(Potion.jump)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
			baseJumpHeight += (double) ((float) (amplifier + 1) * 0.1f);
		}
		return baseJumpHeight;
	}

	public static double randomFloat(float min, float max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
	
	public void setMoveSpeed(EventMove event, double speed) {
		MovementInput movementInput = mc.thePlayer.movementInput;
		double forward = movementInput.moveForward;
		double strafe = movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;

		if ((forward == 0.0D) && (strafe == 0.0D)) {
			event.x = 0.0D;
			event.x = 0.0D;
		} else {
			if (forward != 0.0D) {
				event.setMoveSpeed(0.279);
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}

				strafe = 0.0D;

				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}
			}

			event.x = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
			event.z = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
		}
	}
    
    public static float getSpeed() {
        return (float) Math.sqrt(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX + Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ);
    }
    
    public static double getDirection() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;

        if(Minecraft.getMinecraft().thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(Minecraft.getMinecraft().thePlayer.moveForward < 0F)
            forward = -0.5F;
        else if(Minecraft.getMinecraft().thePlayer.moveForward > 0F)
            forward = 0.5F;

        if(Minecraft.getMinecraft().thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if(Minecraft.getMinecraft().thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }
    
    public static void strafe(final float speed) {
        if(!PlayerUtil.isMoving())
            return;

        final double yaw = getDirection();
        Minecraft.getMinecraft().thePlayer.motionX = -Math.sin(yaw) * speed;
        Minecraft.getMinecraft().thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    int tsBoost = 0;
    private void setMotion(EventMove em2, double speed)
    {
      if (this.tsBoost > 0)
      {
        this.tsBoost -= 1;
        speed *= 1.7234277234D;
      }
      double forward = MovementInput.moveForward;
      double strafe = MovementInput.moveStrafe;
      float yaw = Minecraft.thePlayer.rotationYaw;
      if ((forward == 0.0D) && (strafe == 0.0D))
      {
        EventMove.setX(0.0D);
        EventMove.setZ(0.0D);
      }
      else
      {
        if (forward != 0.0D)
        {
          if (strafe > 0.0D) {
            yaw += (forward > 0.0D ? -45 : 45);
          } else if (strafe < 0.0D) {
            yaw += (forward > 0.0D ? 45 : -45);
          }
          strafe = 0.0D;
          if (forward > 0.0D) {
            forward = 1.0D;
          } else if (forward < 0.0D) {
            forward = -1.0D;
          }
        }
        EventMove.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
        EventMove.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
      }
    }
	
	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
		}

		return baseSpeed;
	}

	private double getHypixelSpeed(int stage) {
		double value = MoveUtil.defaultSpeed() + (0.028 * MoveUtil.getSpeedEffect())
				+ (double) MoveUtil.getSpeedEffect() / 15;
		double firstvalue = 0.4145 + (double) MoveUtil.getSpeedEffect() / 12.5;
		double thirdvalue = 0.4045 + (double) MoveUtil.getSpeedEffect() / 12.5;
		double decr = (((double) stage / 500) * 3);

		if (stage == 0) {
			// JUMP
			if (timer.isDelayComplete(300)) {
				timer.reset();
				// mc.timer.timerSpeed = 1.354f;
			}
			if (!lastCheck.isDelayComplete(500)) {
				if (!shouldslow)
					shouldslow = true;
			} else {
				if (shouldslow)
					shouldslow = false;
			}
			value = 0.64 + (MoveUtil.getSpeedEffect() + (0.028 * MoveUtil.getSpeedEffect())) * 0.134;
		} else if (stage == 1) {
			value = firstvalue;
		} else if (stage == 2) {
			value = thirdvalue;
		} else if (stage >= 3) {
			value = thirdvalue - decr;
		}
		if (shouldslow || !lastCheck.isDelayComplete(500) || collided) {
			value = 0.2;
			if (stage == 0)
				value = 0;
		}

		return Math.max(value, shouldslow ? value : MoveUtil.defaultSpeed() + (0.028 * MoveUtil.getSpeedEffect()));
	}

	protected boolean isMoving() {
		return mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F;
	}
	private boolean isInLiquid()
	  {
	    if (Minecraft.thePlayer == null) {
	      return false;
	    }
	    int x2 = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minX);
	    while (x2 < MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxX) + 1)
	    {
	      int z2 = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minZ);
	      while (z2 < MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxZ) + 1)
	      {
	        BlockPos pos = new BlockPos(x2, (int)Minecraft.thePlayer.boundingBox.minY, z2);
	        Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
	        if ((block != null) && (!(block instanceof BlockAir))) {
	          return block instanceof BlockLiquid;
	        }
	        z2++;
	      }
	      x2++;
	    }
	    return false;
	  }
    
    enum Bhopmode {
    	SlowHop,
    	Hypixel,
    	Hypixel2,
    	Hypixel3,
    	Hypixel4,
    	Hypixel5,
    	HypixelHop,
    	Novoline,
    	Ayuan,
    	Ground,
    	Sensation,
    	VanillaHop,
    	AutoJump;
    }
    
}
