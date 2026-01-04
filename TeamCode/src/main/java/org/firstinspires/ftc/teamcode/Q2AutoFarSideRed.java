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
@Autonomous(name = "Q2AutoFarSideRed", group = "Autonomous")
public class Q2AutoFarSideRed extends LinearOpMode {


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
        transfer.setPower(0.9);
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
            intake.setPower(-0.9);
            transfer.setPower(0.9);
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

        Pose2d beginPose = new Pose2d(new Vector2d(60,16), Math.toRadians(-180));
        //this pose assumes the robot starts with the intake facing away from the goal. the shooter will be facing away from the goal

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        //creating RR path
        Action complexfar = drive.actionBuilder(beginPose)
//                .strafeToLinearHeading(new Vector2d(40,12),Math.toRadians(170))
//                .strafeTo(new Vector2d(56,12))
////                .turn(Math.toRadians(-80))
////                .strafeTo(new Vector2d(35,12))
////                .strafeTo(new Vector2d(35,50))
////                .strafeToLinearHeading(new Vector2d(35,12),Math.toRadians(170))
////                .strafeTo(new Vector2d(56,12))
////                .strafeTo(new Vector2d(56,35))
                .build();
        Action Auto = drive.actionBuilder(beginPose)
                .strafeToLinearHeading(new Vector2d(56,16),Math.toRadians(150))


                .strafeToLinearHeading(new Vector2d(35,34),Math.toRadians(90))


                .strafeTo(new Vector2d(35,49))
                .strafeTo(new Vector2d(35,28))
                .strafeToLinearHeading(new Vector2d(56,16),Math.toRadians(150))
                .waitSeconds(1)
                .strafeTo(new Vector2d(12,28))
//                .stopAndAdd(new smartIntake())
//                .strafeTo(new Vector2d(39, 56))
//                .strafeTo(new Vector2d(39, 28))
//                .stopAndAdd(new stopSmartIntake())
//
//                .strafeToLinearHeading(new Vector2d(52, 16), Math.toRadians(135))
//                .stopAndAdd(new functionOfDOOM())
//
//                .strafeTo(new Vector2d(12, 28))
                .build();

        Actions.runBlocking(new SequentialAction(Auto));








    }

}
