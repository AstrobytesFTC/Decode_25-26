package org.firstinspires.ftc.teamcode;


// RR-specific imports

import com.acmerobotics.dashboard.config.Config;

// Non-RR imports
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantFunction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Pose2d;


@Config
@Autonomous(name = "AtGoalBlueUpdated", group = "Autonomous")
public class AtGoalAtBlueUpdatedQ1 extends LinearOpMode {


    DcMotor lf = null;
    DcMotor lb = null;
    DcMotor rf = null;
    DcMotor rb = null;

    DcMotor transfer = null;

    DcMotor launcher = null;
    DcMotor intake = null;

    // lift class
    private boolean initialized = false;

    public class warmupLaunch75 implements InstantFunction{
        @Override
        public void run(){
            launcher.setPower(-0.75);
        }
    }
    public class warmupLaunch70 implements InstantFunction{
        @Override
        public void run(){
            launcher.setPower(-0.70);
        }
    }
    public class stopLauncher implements InstantFunction{
        @Override
        public void run(){
            launcher.setPower(0);
        }
    }

    public class transferArtifact implements InstantFunction{
        @Override
        public void run(){
            transfer.setPower(-1);

        }
    }

    public class reverseTransferArtifact implements InstantFunction{
        @Override
        public void run(){
            transfer.setPower(0.5);
            sleep(60);

        }
    }

    public class intakeFeed implements InstantFunction{
        @Override
        public void run(){
            intake.setPower(1);


        }
    }
    public class stopintake implements InstantFunction{
        @Override
        public void run(){
            intake.setPower(0);

        }
    }

    public class Shoot implements InstantFunction{
        @Override
        public void run(){

            transfer.setPower(-1);
            sleep(600);
            transfer.setPower(0);
        }
    }
    public class slowNSteady implements InstantFunction{
        @Override
        public void run(){
            intake.setPower(0.4);

        }
    }

    public class stopNSteady implements InstantFunction{
        @Override
        public void run(){
            intake.setPower(0.4);

        }
    }



    public void runOpMode() {
        transfer = hardwareMap.dcMotor.get("transfer");
        launcher = hardwareMap.dcMotor.get("launcher");
        intake = hardwareMap.dcMotor.get("intake");
        Pose2d beginPose = new Pose2d(new Vector2d(-52,-60), Math.toRadians(45));
        //this pose assumes the robot starts with the intake facing away from the goal. the shooter will be facing away from the goal

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        //creating RR path

        // actionBuilder builds from the drive steps passed to it
        //this path moves backwards and turns
        Action path = drive.actionBuilder(beginPose)

                .stopAndAdd(new slowNSteady())
                .lineToX(-20)
                .stopAndAdd(new warmupLaunch75())
                .turn(Math.toRadians(-190))

                .stopAndAdd(new transferArtifact())
                .waitSeconds(1)
                .stopAndAdd(new stopNSteady())
                .stopAndAdd(new Shoot())
                .waitSeconds(0.5)
                .stopAndAdd(new slowNSteady())
                .lineToX(-32)
                .stopAndAdd(new intakeFeed())
                .stopAndAdd(new transferArtifact())

                .waitSeconds(3)
                .stopAndAdd(new stopintake())
                .stopAndAdd(new stopLauncher())
                .turn(Math.toRadians(130))
                .lineToX(20)
                //   .stopAndAdd(new Shoot())
                //   .stopAndAdd(new transferArtifact())
                //   .stopAndAdd(new stopintake())
                .build();
        Action path2 = drive.actionBuilder(beginPose)
//                Heading, -52, 60, 45
                .stopAndAdd(new slowNSteady())
                .lineToX(-20)
                .stopAndAdd(new warmupLaunch75())
                .turn(Math.toRadians(-185))
                .stopAndAdd(new stopNSteady())
                .stopAndAdd(new Shoot())
                .waitSeconds(1)
                .stopAndAdd(new warmupLaunch70())
                .waitSeconds(1)
                .lineToX(-32)
                .stopAndAdd(new transferArtifact())
                .stopAndAdd(new intakeFeed())
                .waitSeconds(3)
                .stopAndAdd(new stopintake())
                .stopAndAdd(new stopLauncher())
                .turn(Math.toRadians(40))
                .strafeTo(new Vector2d(-4,-24))
                .stopAndAdd(new intakeFeed())
                .strafeTo(new Vector2d(-4,-54))
                .strafeTo(new Vector2d(-16,-16))
                .stopAndAdd(new stopintake())
                .turn(Math.toRadians(-40))
                .strafeTo(new Vector2d(-34,-34))
                .build();
        Actions.runBlocking(new SequentialAction(path2));










    }

}