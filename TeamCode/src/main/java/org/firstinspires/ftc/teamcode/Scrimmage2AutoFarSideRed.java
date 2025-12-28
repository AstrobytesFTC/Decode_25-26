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
@Autonomous(name = "Scrimmage2AutoFarSideRed", group = "Autonomous")
public class Scrimmage2AutoFarSideRed extends LinearOpMode {


    DcMotor lf = null;
    DcMotor lb = null;
    DcMotor rf = null;
    DcMotor rb = null;

    DcMotor transfer =null;

    DcMotor launcher = null;
    DcMotor intake = null;

    // lift class
    private boolean initialized = false;

    public class warmupLaunch implements InstantFunction{
        @Override
        public void run(){
            launcher.setPower(-0.8);
        }
    }

    public class reverselaunch implements InstantFunction{
        @Override
        public void run(){
            launcher.setPower(0.5);
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
            intake.setPower(0.2);

        }
    }



    public void runOpMode() {
        transfer = hardwareMap.dcMotor.get("transfer");
        launcher = hardwareMap.dcMotor.get("launcher");
        intake = hardwareMap.dcMotor.get("intake");
        Pose2d beginPose = new Pose2d(new Vector2d(60,16), Math.toRadians(-45));
        //this pose assumes the robot starts with the intake acing away from the goal. the shooter will be facing away from the goal

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        //creating RR path

        // actionBuilder builds from the drive steps passed to it
        //this path moves backwards and turns
        Action path = drive.actionBuilder(beginPose)
                .stopAndAdd(new slowNSteady())
                .lineToX(-25)
                .stopAndAdd(new warmupLaunch())
                .turn(Math.toRadians(-125))
                .stopAndAdd(new Shoot())
                .waitSeconds(1)
                .lineToX(-34)
                .stopAndAdd(new intakeFeed())
                .stopAndAdd(new transferArtifact())
                .waitSeconds(3)
                .turn(Math.toRadians(45))
                .stopAndAdd(new stopLauncher())
                .stopAndAdd(new reverselaunch())
                .waitSeconds(1)
                .strafeTo(new Vector2d(-12,-22))
                .waitSeconds(1)
                .strafeTo(new Vector2d(-12,-33))
                .strafeTo(new Vector2d(-12,-22))
                .stopAndAdd(new stopintake())
                .strafeTo(new Vector2d(-52,-12))
                .turn(Math.toRadians(-30))
                .build();
        Action path2 = drive.actionBuilder(beginPose)
                .lineToX(-25)
                .turn(Math.toRadians(-130))
                .waitSeconds(1)
                .lineToX(-34)
                .waitSeconds(3)
                .turn(Math.toRadians(45))
                .strafeTo(new Vector2d(-12,-22))
                .strafeTo(new Vector2d(-12,-32))
                .strafeTo(new Vector2d(-12,-22))
                .strafeTo(new Vector2d(-25,-12))
                .turn(Math.toRadians(-50))
                .lineToX(-34)
                .build();

        Action path3 = drive.actionBuilder(beginPose)

                .lineToX(-25)
                .turn(Math.toRadians(-130))
                .waitSeconds(1)
                .lineToX(-34)
                .waitSeconds(3)
                .turn(Math.toRadians(45))
                .strafeTo(new Vector2d(-14,-22))
                .strafeTo(new Vector2d(-14,-40))
                .strafeTo(new Vector2d(-14,-22))
                .strafeTo(new Vector2d(-52,-12))
                .turn(Math.toRadians(-30))

                .build();

        Action path4 = drive.actionBuilder(beginPose)
                .strafeToLinearHeading(new Vector2d(56,-16),Math.toRadians(-170))
                .strafeTo(new Vector2d(35,-28))
                .turn(Math.toRadians(80))
                .strafeTo(new Vector2d(35,-52))
                .strafeTo(new Vector2d(35,-28))
                .strafeToLinearHeading(new Vector2d(56,-16),Math.toRadians(-170))
                .strafeTo(new Vector2d(12,-28))
                .turn(Math.toRadians(80))
                .strafeTo(new Vector2d(12,-52))
                .strafeTo(new Vector2d(12,-28))
                .strafeToLinearHeading(new Vector2d(56,-16),Math.toRadians(-170))
                .strafeTo(new Vector2d(-11,-28))
                .turn(Math.toRadians(80))
                .strafeTo(new Vector2d(-11,-52))
                .strafeTo(new Vector2d(-11,-28))
                .strafeToLinearHeading(new Vector2d(-34,-20),Math.toRadians(-120))
                .strafeTo(new Vector2d(-50, -16))

                .build();

        Action Scrimmage2Auto = drive.actionBuilder(beginPose)

                .strafeToLinearHeading(new Vector2d(56,16),Math.toRadians(170))
                .strafeTo(new Vector2d(35,28))
                .turn(Math.toRadians(-80))
                .strafeTo(new Vector2d(35,52))
                .strafeTo(new Vector2d(35,28))
                .strafeToLinearHeading(new Vector2d(56,16),Math.toRadians(170))
                .waitSeconds(1)
                .strafeTo(new Vector2d(12,28))
                .build();

        Action Scrimmage2AutoWithShooting = drive.actionBuilder(beginPose)

                .strafeToLinearHeading(new Vector2d(56,-16),Math.toRadians(-170))
                .waitSeconds(0.5)
                .strafeTo(new Vector2d(35,-28))
                .turn(Math.toRadians(80))
                .strafeTo(new Vector2d(35,-52))
                .strafeTo(new Vector2d(35,-28))
                .strafeToLinearHeading(new Vector2d(56,-16),Math.toRadians(-170))
                .waitSeconds(0.5)
                .strafeTo(new Vector2d(12,-28))
                .build();



        Actions.runBlocking(new SequentialAction(Scrimmage2Auto));








    }

}
