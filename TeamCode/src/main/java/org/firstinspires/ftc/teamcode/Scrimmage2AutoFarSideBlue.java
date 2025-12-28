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
import com.qualcomm.robotcore.hardware.Servo;


@Config
@Autonomous(name = "Scrimmage2AutoFarSideBlue", group = "Autonomous")
public class Scrimmage2AutoFarSideBlue extends LinearOpMode {


    DcMotor frontLeftMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backRightMotor = null;

    DcMotor transfer =null;

   // DcMotor launcher = null;
    DcMotor intake = null;

    DcMotor shooterRight = null;
    DcMotor shooterLeft = null;
    Servo blocker = null;

    // lift class
    private boolean initialized = false;

//    public class warmupLaunch implements InstantFunction{
//        @Override
//        public void run(){
//            launcher.setPower(-0.8);
//        }
//    }

//    public class reverselaunch implements InstantFunction{
//        @Override
//        public void run(){
//            launcher.setPower(0.5);
//        }
//    }
//    public class stopLauncher implements InstantFunction{
//        @Override
//        public void run(){
//            launcher.setPower(0);
//        }
//    }

    public void timeTransferAndIntake(double seconds) {
        transfer.setPower(0.8);
        intake.setPower(-0.8);
        sleep(Math.round(seconds * 1000)); // seconds → ms
        transfer.setPower(0);
        intake.setPower(0);
    }

    public class functionOfDOOM implements InstantFunction{
        @Override
        public void run(){
            blocker.setPosition(0.5);
            // === ULTIMATE FUNCTION OF DOOM ===
            shooterRight.setPower(-0.90);
            shooterLeft.setPower(0.90);

// Spin-up time
            sleep(2000);

// === 1st Ball ===
            timeTransferAndIntake(0.15);
            sleep(1500);

// === 2nd Ball ===
            timeTransferAndIntake(0.25);
            sleep(1500);

// === 3rd Ball ===
            timeTransferAndIntake(0.35);
            sleep(1500);

// Power down shooter
            shooterLeft.setPower(0);
            shooterRight.setPower(0);

            blocker.setPosition(0);
        }
    }
    public class smartIntake implements InstantFunction{
        @Override
        public void run(){
            //Change If needed
            intake.setPower(-0.8);
            transfer.setPower(0.8);
        }
    }
    public class stopSmartIntake implements InstantFunction{
        @Override
        public void run(){
            //Change If needed
            intake.setPower(0);
            transfer.setPower(0);
        }
    }



    public void runOpMode() {


        frontLeftMotor = hardwareMap.dcMotor.get("frontleft");
        backLeftMotor  = hardwareMap.dcMotor.get("backleft");
        frontRightMotor = hardwareMap.dcMotor.get("frontright");
        backRightMotor  = hardwareMap.dcMotor.get("backright");
        transfer = hardwareMap.dcMotor.get("intake");
        intake = hardwareMap.dcMotor.get("transfer");
        shooterLeft = hardwareMap.dcMotor.get("rightShooter");
        shooterRight = hardwareMap.dcMotor.get("leftShooter");
        blocker = hardwareMap.servo.get("blocker");

        Pose2d beginPose = new Pose2d(new Vector2d(65,-16), Math.toRadians(-170));
        //this pose assumes the robot starts with the intake facing away from the goal. the shooter will be facing away from the goal

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        //creating RR path

        // actionBuilder builds from the drive steps passed to it
        //this path moves backwards and turns
//        Action path = drive.actionBuilder(beginPose)
//                .stopAndAdd(new slowNSteady())
//                .lineToX(-25)
////                .stopAndAdd(new warmupLaunch())
//                .turn(Math.toRadians(-125))
//                .stopAndAdd(new Shoot())
//                .waitSeconds(1)
//                .lineToX(-34)
//                .stopAndAdd(new intakeFeed())
//                .stopAndAdd(new transferArtifact())
//                .waitSeconds(3)
//                .turn(Math.toRadians(45))
////                .stopAndAdd(new stopLauncher())
////                .stopAndAdd(new reverselaunch())
//                .waitSeconds(1)
//                .strafeTo(new Vector2d(-12,-22))
//                .waitSeconds(1)
//                .strafeTo(new Vector2d(-12,-33))
//                .strafeTo(new Vector2d(-12,-22))
//                .stopAndAdd(new stopintake())
//                .strafeTo(new Vector2d(-52,-12))
//                .turn(Math.toRadians(-30))
//                .build();
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

                .strafeTo(new Vector2d(58,-16))
                .turn(Math.toRadians(25))
                .stopAndAdd(new functionOfDOOM() )

                .strafeToLinearHeading(new Vector2d(39,-28), Math.toRadians(-90))
                .stopAndAdd(new smartIntake())
                .strafeTo(new Vector2d(39,-56))
                .strafeTo(new Vector2d(39,-28))
                .stopAndAdd(new stopSmartIntake())


                .strafeToLinearHeading(new Vector2d(52,-16),Math.toRadians(-155))
                .stopAndAdd( new functionOfDOOM())

                .strafeTo(new Vector2d(12,-28))
                        .build();

        Action Scrimmage2AutoWithShooting = drive.actionBuilder(beginPose)

                .strafeTo(new Vector2d(56,-16))
                .turn(Math.toRadians(-130))
                //add shooting function
                .strafeToLinearHeading(new Vector2d(35,-28), Math.toRadians(-90))
                //turn on intake
                .strafeTo(new Vector2d(35,-52))
                .strafeTo(new Vector2d(35,-28))
                //intake slow
                .strafeToLinearHeading(new Vector2d(56,-16),Math.toRadians(-150))
                //add shoooting function
                .strafeTo(new Vector2d(12,-28))
                .build();



        Actions.runBlocking(new SequentialAction(Scrimmage2Auto));








    }

}
