package examplefuncsplayer;

import java.util.Arrays;

import battlecode.common.*;

public class ArchonStrategy {

    static int miners=0, soldiers=0, builders=0;
    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runArchon(RobotController rc) throws GameActionException {
        if (miners<5) {
            buildTowardsLowRubble(rc, RobotType.MINER);
        } else if (soldiers<10) {
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        } else if (builders<1) {
            buildTowardsLowRubble(rc, RobotType.BUILDER);
        } else if (miners < (soldiers/2) && rc.getTeamLeadAmount(rc.getTeam())<5000) {
            buildTowardsLowRubble(rc, RobotType.MINER);
        } else if (builders < soldiers/10) {
            buildTowardsLowRubble(rc, RobotType.BUILDER);
        } else {
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        }
        // Pick a direction to build in.
        //Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
        /*if (RobotPlayer.rng.nextBoolean()) {
            rc.setIndicatorString("Trying to build a miner");
            buildTowardsLowRubble(rc, RobotType.MINER);
        } else {
            rc.setIndicatorString("Trying to build a soldier");
            buildTowardsLowRubble(rc, RobotType.SOLDIER);
        }*/
    }

    static void buildTowardsLowRubble(RobotController rc, RobotType type) throws GameActionException {
        Direction[] dirs = Arrays.copyOf(RobotPlayer.directions, RobotPlayer.directions.length);
        Arrays.sort(dirs, (a, b) -> getRubble(rc, a) - getRubble(rc, b));
        for (Direction d : dirs) {
            if(rc.canBuildRobot(type,d)) {
                rc.buildRobot(type,d);
                switch(type){
                    case MINER: miners+=1;
                    case SOLDIER: soldiers+=1;
                    case BUILDER: builders+=1;
                    default: break;
                }
            }
        }

    }

    static int getRubble(RobotController rc, Direction d) {
        try {
            MapLocation Loc = rc.getLocation().add(d);
            return rc.senseRubble(Loc);
        } catch (GameActionException e) {
            e.printStackTrace();
            return 0;
        }
    }

}


