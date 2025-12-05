package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(56, -12, -45))
                //FrontOfFieldBlue
                //Heading 56, -12, 0
//                .lineToX(-25)
//                .turn(Math.toRadians(-130))
//                .waitSeconds(1)
//                .lineToX(-34)
//                .waitSeconds(3)
//                .turn(Math.toRadians(45))
//                .strafeTo(new Vector2d(-14,-22))
//                .strafeTo(new Vector2d(-14,-32))
//                .strafeTo(new Vector2d(-14,-22))
//                .strafeTo(new Vector2d(-25,-12))
//                .turn(Math.toRadians(-50))
//                .lineToX(-34)

                //Code for FrontOfFieldRed
                //Heading 56, 12, 0
//                        .lineToX(-18)
//                        .turn(Math.toRadians(130))
////                        .waitSeconds(2.5)
////                        .waitSeconds(1)
//                        .lineToX(-32)
////                        .waitSeconds(3)
//                        .turn(Math.toRadians(-45))
//                        .strafeTo(new Vector2d(-14,29))
//                        .strafeTo(new Vector2d(-14,38))
//                        .strafeTo(new Vector2d(-14,29))
//                        .strafeTo(new Vector2d(-34,29))
//                        .turn(Math.toRadians(45))
//                        .strafeTo(new Vector2d(-22,15))
//                        .strafeTo(new Vector2d(-34,29))
//                        .strafeTo(new Vector2d(-52,20))


// Code for GoalToRed
//                Heading 52, -49, -45
                .strafeTo(new Vector2d(-15,18))
                .turn(Math.toRadians(180))
                .lineToX(-30)
                .lineToX(-20)
                .turn(Math.toRadians(-50))
                .strafeTo(new Vector2d(-20,22))
                .strafeTo(new Vector2d(-20,48))
                .strafeTo(new Vector2d(-20,22))

// Code for GoalToBlue
//                Heading, -52, 60, 45
//                .lineToX(-20)
//                .turn(Math.toRadians(-190))
//                .waitSeconds(1)
//                .waitSeconds(0.5)
//                .lineToX(-32)
//                .waitSeconds(3)
//                .turn(Math.toRadians(40))
//                .strafeTo(new Vector2d(-5,-24))
//                .strafeTo(new Vector2d(-5,-38))
//                .strafeTo(new Vector2d(-16,-16))
//                .turn(Math.toRadians(-40))
//                .strafeTo(new Vector2d(-34,-34))
//                .strafeTo(new Vector2d(20,-16))

                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.9f)
                .addEntity(myBot)
                .start();




















    }
}