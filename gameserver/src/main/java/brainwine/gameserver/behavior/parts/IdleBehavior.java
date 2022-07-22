package brainwine.gameserver.behavior.parts;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;

import brainwine.gameserver.behavior.Behavior;
import brainwine.gameserver.entity.FacingDirection;
import brainwine.gameserver.entity.npc.Npc;

public class IdleBehavior extends Behavior {
    
    protected int delay = 60;
    protected int duration = 60;
    protected double random = 0.5;
    protected String[] animations = new String[] {"idle"};
    protected long until;
    protected boolean idle;
    protected String currentAnimation;
    protected long animationSetUntil = System.currentTimeMillis();
    
    @JsonCreator
    public IdleBehavior(@JacksonInject Npc entity) {
        super(entity);
        until = getNextUntil();
    }
    
    @Override
    public boolean behave() {
        Random random = ThreadLocalRandom.current();
        long now = System.currentTimeMillis();
        
        if(entity.isOnGround()) {
            if(now > until) {
                idle = !idle;
                until = getNextUntil();
            }
        } else {
            idle = false;
        }
        
        if(idle) {
            if(now > animationSetUntil) {
                int length = animations.length;
                currentAnimation = length == 0 ? "idle" : animations[random.nextInt(length)];
                animationSetUntil = now + (random.nextInt(2) + 1) * 1000;
            }
            
            entity.move(0, 0, currentAnimation);
            
            if(Math.random() < 0.01) {
                entity.setDirection(entity.getDirection() == FacingDirection.WEST ? FacingDirection.EAST : FacingDirection.WEST);
            }
            
            return true;
        }
        
        return false;
    }
    
    protected long getNextUntil() {
        int currentDuration = (idle ? duration : delay) * 1000;
        return (long)(System.currentTimeMillis() + currentDuration + currentDuration * (Math.random() * random));
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public void setRandom(double random) {
        this.random = random;
    }
    
    @JsonSetter("animation")
    public void setAnimations(String... animations) {
        this.animations = animations;
    }
}