package io.hearlov.nexus.npc.entity.pathfinder;

import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import io.hearlov.nexus.npc.entity.NexusEntity;

import java.util.Random;

/*
*Simple radius-bounded pathfinder for NPCs.
* Navigates within MAX_RADIUS blocks around firstPosition. * Safely executes movement on MainThread (via PluginTask).
*/
public class NexusPathfinder {

    public static double MAX_RADIUS = 5.0;
    public static double STEP_SIZE = 0.20;
    public static int IDLE_TICKS = 60;
    public static int NEW_GOAL_TICKS = 100;

    public enum State { IDLE, MOVING, RETURNING }

    private final NexusEntity entity;
    private final Random      random = new Random();

    private Vector3 goal;
    private State state = State.IDLE;
    private int idleTimer  = 0;
    private int goalTimer  = 0;

    public static void preSetup(Config config){
        MAX_RADIUS = config.getDouble("MaxRadius");
        STEP_SIZE  = config.getDouble("StepSize");
        IDLE_TICKS = config.getInt("IdleTicks");
        NEW_GOAL_TICKS = config.getInt("NewGoalTicks");
    }

    public NexusPathfinder(NexusEntity entity){
        this.entity = entity;
    }

    public void tick(){
        if (entity.isClosed() || !entity.isAlive()) return;

        switch(state){
            case IDLE -> tickIdle();
            case MOVING -> tickMoving();
            case RETURNING-> tickReturning();
        }
    }

    private void tickIdle(){
        idleTimer++;
        if (idleTimer >= IDLE_TICKS){
            idleTimer = 0;
            pickRandomGoal();
        }
    }

    private void tickMoving() {
        if (goal == null) { state = State.IDLE; return; }

        Vector3 pos = entity.getPosition();
        double dist = pos.distance(goal);

        if(dist <= STEP_SIZE + 0.1){
            entity.setPosition(goal);
            goal  = null;
            state = State.IDLE;
            return;
        }

        if(distanceFromHome(pos) > MAX_RADIUS + 0.5){
            state = State.RETURNING;
            return;
        }

        moveTowards(pos, goal);
        faceTarget(pos, goal);
    }

    private void tickReturning(){
        Vector3 home = entity.getFirstVector();
        Vector3 pos = entity.getPosition();

        if(distanceFromHome(pos) <= STEP_SIZE + 0.1){
            entity.setPosition(home);
            state = State.IDLE;
            return;
        }

        moveTowards(pos, home);
        faceTarget(pos, home);
    }

    private void pickRandomGoal(){
        goalTimer++;

        if (goalTimer < NEW_GOAL_TICKS / IDLE_TICKS) return;
        goalTimer = 0;

        Vector3 home = entity.getFirstVector();
        double angle  = random.nextDouble() * Math.PI * 2;
        double radius = 1.5 + random.nextDouble() * (MAX_RADIUS - 1.5);

        double gx = home.getX() + Math.cos(angle) * radius;
        double gz = home.getZ() + Math.sin(angle) * radius;
        double gy = getSafeY(gx, gz, home.getY());

        Vector3 candidate = new Vector3(gx, gy, gz);

        if(home.distance(candidate) <= MAX_RADIUS){ // Radius control
            goal  = candidate;
            state = State.MOVING;
        }
    }

    private void moveTowards(Vector3 from, Vector3 to){
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);

        if (dist < 0.001) return;

        double nx = dx / dist * STEP_SIZE;
        double nz = dz / dist * STEP_SIZE;

        double newX = from.getX() + nx;
        double newY = getSafeY(newX, from.getZ() + nz, from.getY());
        double newZ = from.getZ() + nz;

        entity.setPosition(new Vector3(newX, newY, newZ));
        entity.updateMovement();
    }

    private void faceTarget(Vector3 from, Vector3 to){
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        float  yaw = (float) (Math.toDegrees(Math.atan2(-dx, dz)));
        entity.setRotation(yaw, 0);
    }

    private double getSafeY(double x, double z, double fallbackY){
        try{
            int bx = (int) Math.floor(x);
            int bz = (int) Math.floor(z);
            int by = entity.getLevel().getHighestBlockAt(bx, bz);
            return by + 1.0; // block up
        }catch(Exception e){
            return fallbackY;
        }
    }

    private double distanceFromHome(Vector3 pos){
        Vector3 home = entity.getFirstVector();
        double  dx = pos.getX() - home.getX();
        double  dz = pos.getZ() - home.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    public void forceReturn(){
        state = State.RETURNING;
        goal  = null;
    }

    @SuppressWarnings("unused")
    public State getState(){ return state; }
}
