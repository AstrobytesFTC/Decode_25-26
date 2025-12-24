package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantFunction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;



// RR-specific imports

@Config
@Autonomous(name = "sampleAtGoalToRedComplexPathing", group = "Autonomous")
public class sampleAtGoalToRedComplexPathing extends LinearOpMode {

    DcMotor transfer =null;

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
            launcher.setPower(-0.7);
        }
    }
    public class reverseshoot implements InstantFunction{
        @Override
        public void run(){
            launcher.setPower(0.4);
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
    public class transferStop implements InstantFunction{
        @Override
        public void run(){
            transfer.setPower(0.0);

        }
    }




    public void runOpMode() {
        transfer = hardwareMap.dcMotor.get("transfer");
        launcher = hardwareMap.dcMotor.get("launcher");
        intake = hardwareMap.dcMotor.get("intake");
        Pose2d beginPose = new Pose2d(new Vector2d(-52,49), Math.toRadians(-45));
        //this pose assumes the robot starts with the intake facing away from the goal. the shooter will be facing away from the goal

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        //creating RR path

        // actionBuilder builds from the drive steps passed to it
        //this path moves backwards and turns
        Action complexpath = drive.actionBuilder(beginPose)
                //Go to shooting position

                .strafeToLinearHeading(new Vector2d(-12,12),Math.toRadians(130))
                .turn(Math.toRadians(-40))
                .strafeTo(new Vector2d(-12,50))
                .strafeToLinearHeading(new Vector2d(-12,12),Math.toRadians(130))
                .turn(Math.toRadians(-40))
                .strafeTo(new Vector2d(12,12))
                .strafeTo(new Vector2d(12,50))
                .strafeToLinearHeading(new Vector2d(12,12),Math.toRadians(130))
                .strafeTo(new Vector2d(-12,12))
                .turn(Math.toRadians(-40))
                .strafeTo(new Vector2d(34,12))
                .strafeTo(new Vector2d(34,50))
                .strafeToLinearHeading(new Vector2d(34,12),Math.toRadians(130))
                .strafeTo(new Vector2d(-12,12))
                .turn(Math.toRadians(-40))
                .strafeTo(new Vector2d(-34,12))

                .build();
        Action path2 = drive.actionBuilder(beginPose)
                .strafeTo(new Vector2d(-15,18))
                .stopAndAdd(new warmupLaunch75())
                .turn(Math.toRadians(195))
                .stopAndAdd(new Shoot())
                .waitSeconds(1)
                .stopAndAdd(new warmupLaunch70())
                .lineToX(-30)
                .stopAndAdd(new transferArtifact())
                .stopAndAdd(new intakeFeed())
                .waitSeconds(2)
                .stopAndAdd(new stopLauncher())
                .lineToX(-20)
                .stopAndAdd(new reverseshoot())
                .turn(Math.toRadians(-35))
                .strafeTo(new Vector2d(-11,22))
                .strafeTo(new Vector2d(-11,48))
                .waitSeconds(1)
                .stopAndAdd(new transferStop())
                .stopAndAdd(new stopintake())
                .strafeTo(new Vector2d(-48,22))
                .build();




        Actions.runBlocking(new SequentialAction(path2));








    }

}

