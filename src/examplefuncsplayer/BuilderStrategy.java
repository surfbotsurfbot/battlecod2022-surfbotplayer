package examplefuncsplayer;

import battlecode.common.*;

import java.util.Random;

strictfp public class BuilderStrategy {

    static void runBuilder(RobotController rc) throws GameActionException {


        // Also try to move randomly.
        int DirIdx = RobotPlayer.rng.nextInt(RobotPlayer.directions.length);
        Direction dir = RobotPlayer.directions[DirIdx];
        if (rc.canMove(dir)) {
            rc.move(dir);
            System.out.println("I moved!");
        }
    }
}
