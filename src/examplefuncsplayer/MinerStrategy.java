package examplefuncsplayer;

import battlecode.common.*;
import java.util.Random;

strictfp class MinerStrategy {
    static Direction exploreDir = null;
    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runMiner(RobotController rc) throws GameActionException {
        if (exploreDir == null) {
            RobotPlayer.rng.setSeed(rc.getID());
            exploreDir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
        }
        rc.setIndicatorString(exploreDir.toString());
        // Try to mine on squares around us.
        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                // Notice that the Miner's action cooldown is very low.
                // You can mine multiple times per turn!
                while (rc.canMineGold(mineLocation)) {
                    rc.mineGold(mineLocation);
                }
                while (rc.canMineLead(mineLocation) && rc.senseLead(mineLocation)>1) {
                    rc.mineLead(mineLocation);
                }
            }
        }
        int visionRadius = rc.getType().visionRadiusSquared;
        MapLocation[] nearbyLocations = rc.getAllLocationsWithinRadiusSquared(me, visionRadius);

        MapLocation targetLocation=null;
        int distanceToTarget = Integer.MAX_VALUE;

        //for each nearby location
        for(MapLocation tryLocation : nearbyLocations){
            //Is there any resource there?
            if (rc.senseLead(tryLocation) > 1 || rc.senseGold(tryLocation) > 0) {
                //then go there
                int distanceTo = me.distanceSquaredTo(tryLocation);
                if (distanceTo < distanceToTarget) {
                    targetLocation = tryLocation;
                    distanceToTarget = distanceTo;
                }
                break;
            }
        }

        //check if we have a valid targetLocation and then move
        if (targetLocation != null) {
            Direction toMove = me.directionTo(targetLocation);
            if (rc.canMove(toMove)){
                rc.move(toMove);
                System.out.println("Miner moved smartly");
            }
        } else {
            if (rc.canMove(exploreDir)) {
                rc.move(exploreDir);
            } else if (rc.onTheMap(rc.getLocation().add(exploreDir))) {
                exploreDir=exploreDir.opposite();
            }
        }

        // Also try to move randomly.
        int DirIdx = RobotPlayer.rng.nextInt(RobotPlayer.directions.length);
        Direction dir = RobotPlayer.directions[DirIdx];
        if (rc.canMove(dir)) {
            rc.move(dir);
            System.out.println("I moved!");
        }
    }



}
