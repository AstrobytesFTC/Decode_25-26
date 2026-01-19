package org.firstinspires.ftc.teamcode;


// RR-specific imports

import com.acmerobotics.dashboard.config.Config;

// Non-RR imports
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantFunction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@Config
@Disabled
@Autonomous(name = "Q2AutoAtGoalRed", group = "Autonomous")
public class Q2AutoAtGoalRed extends LinearOpMode {


    DcMotor frontLeftMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backRightMotor = null;

    DcMotor transfer =null;

    // DcMotor launcher = null;
    DcMotor intake = null;

    DcMotorEx shooterRight = null;
    DcMotorEx shooterLeft = null;
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
    public void sleepSeconds(double seconds) {
        sleep((long)(seconds * 1000));
    }
    public void timeTransferAndIntake(double seconds) {
        transfer.setPower(0.8);
        intake.setPower(-0.8);
        sleep(Math.round(seconds * 1000)); // seconds → ms
        transfer.setPower(0);
        intake.setPower(0);
    }
    public boolean waitForShooter(DcMotorEx shooter, double target, long timeoutMs) {
        long start = System.currentTimeMillis();

        while (opModeIsActive()
                && System.currentTimeMillis() - start < timeoutMs) {

            double velocity = Math.abs(shooter.getVelocity());
//Dependable, keep if close
            if (Math.abs(velocity - target) < 30) {
                return true;
            }

            sleep(10); // allow hardware loop
        }
        return false; // timed out
    }

    public class functionOfDOOM implements InstantFunction{
        @Override
        public void run(){
            boolean shootControl = false;
            double doomVelocity = 1420;
            double transfertime = 0.3;

            blocker.setPosition(0.1);
            sleep(1000);  // 500ms
            blocker.setPosition(0.5);

            shooterRight.setVelocity(1300);
            shooterLeft.setVelocity(-1300);
            transfer.setPower(-0.3);
            intake.setPower(0.3);

            sleep(500);

            shooterRight.setVelocity(0);
            shooterLeft.setVelocity(0);
            transfer.setPower(0);
            intake.setPower(0);

            shooterRight.setVelocity(doomVelocity);
            shooterLeft.setVelocity(-doomVelocity);

            intake.setPower(0.3);

            if (waitForShooter(shooterRight, doomVelocity, 1500)) {
                transfer.setPower(0.65);
                sleepSeconds(transfertime - 0.15);
                transfer.setPower(-0.4);
                sleepSeconds(transfertime - 0.15);
                transfer.setPower(0);

            }

            if (waitForShooter(shooterLeft, doomVelocity, 4000)) {
                transfer.setPower(0.65);
                intake.setPower(0.2);
                sleepSeconds(transfertime);
//                transfer.setPower(-0.4);
//                sleepSeconds(transfertime - 0.15);
                transfer.setPower(0);
                intake.setPower(0.3);
            }

            if (waitForShooter(shooterLeft, doomVelocity, 4000)) {
                transfer.setPower(1);
                intake.setPower(0.7);
                sleepSeconds(transfertime + 0.1);
                transfer.setPower(0);
            }
            doomVelocity = 0;
            shooterRight.setVelocity(0);
            shooterLeft.setVelocity(0);
            intake.setPower(0);
        }
    }

    public class fastFunctionOfDOOM implements InstantFunction{
        @Override
        public void run(){
            boolean shootControl = false;
            double doomVelocity = 1400;
            double transfertime = 0.3;

            blocker.setPosition(0.1);
            sleep(1000);  // 500ms
            blocker.setPosition(0.5);

            shooterRight.setVelocity(1300);
            shooterLeft.setVelocity(-1300);
            transfer.setPower(-0.3);
            intake.setPower(0.3);

            sleep(800);

            shooterRight.setVelocity(0);
            shooterLeft.setVelocity(0);
            transfer.setPower(0);
            intake.setPower(0);

            shooterRight.setVelocity(doomVelocity);
            shooterLeft.setVelocity(-doomVelocity);

            intake.setPower(0.3);

            if (waitForShooter(shooterRight, doomVelocity, 1500)) {
                transfer.setPower(0.65);
                sleepSeconds(transfertime - 0.15);
                transfer.setPower(-0.4);
                sleepSeconds(transfertime - 0.15);
                transfer.setPower(0);
            }

            if (waitForShooter(shooterLeft, doomVelocity, 4000)) {
                transfer.setPower(0.65);
                intake.setPower(0.2);
                sleepSeconds(transfertime);
//                transfer.setPower(-0.4);
//                sleepSeconds(transfertime - 0.15);
                transfer.setPower(0);
                intake.setPower(0.3);
            }

            if (waitForShooter(shooterLeft, doomVelocity, 4000)) {
                transfer.setPower(1);
                intake.setPower(0.7);
                sleepSeconds(transfertime + 0.1);
                transfer.setPower(0);
            }
            doomVelocity = 0;
            intake.setPower(0);
        }
    }
    public class stopLauncher implements InstantFunction {
        @Override
        public void run() {
            //Change If needed
            //shooterRight.setVelocity(-300);
            //shooterLeft.setVelocity(-300);
            //sleepSeconds(0.2);
            shooterRight.setVelocity(-300);
            shooterLeft.setVelocity(300);
        }
    }

    public class stopLauncher1 implements InstantFunction{
        @Override
        public void run(){
            //Change If needed
            //shooterRight.setVelocity(-300);
            //shooterLeft.setVelocity(-300);
            //sleepSeconds(0.2);
            shooterRight.setVelocity(0);
            shooterLeft.setVelocity(0);

        }
    }

//    public class functionOfDOOM implements InstantFunction{
//        @Override
//        public void run(){
//            blocker.setPosition(0.5);
//            // === ULTIMATE FUNCTION OF DOOM === <has to be replaced>
//            shooterRight.setPower(-0.90);
//            shooterLeft.setPower(0.90);
//
//// Spin-up time
//            sleep(2000);
//
//// === 1st Ball ===
//            timeTransferAndIntake(0.15);
//            sleep(1500);
//
//// === 2nd Ball ===
//            timeTransferAndIntake(0.25);
//            sleep(1500);
//
//// === 3rd Ball ===
//            timeTransferAndIntake(0.35);
//            sleep(1500);
//
    //// Power down shooter
//            shooterLeft.setPower(0);
//            shooterRight.setPower(0);
//
//            blocker.setPosition(0);
//        }
//    }
    public class smartIntake implements InstantFunction{
        @Override
        public void run(){
            //Change If needed
            transfer.setPower(0.6);
            intake.setPower(0.75);
            shooterRight.setVelocity(-1450);
            shooterLeft.setVelocity(1450);
        }
    }
    public class stopSmartIntake implements InstantFunction{
        @Override
        public void run(){
            //Change If needed
            intake.setPower(0);
            transfer.setPower(0);
            shooterRight.setVelocity(0);
            shooterLeft.setVelocity(0);
        }
    }




    public void runOpMode() {


        frontLeftMotor = hardwareMap.dcMotor.get("frontleft");
        backLeftMotor  = hardwareMap.dcMotor.get("backleft");
        frontRightMotor = hardwareMap.dcMotor.get("frontright");
        backRightMotor  = hardwareMap.dcMotor.get("backright");
        transfer = hardwareMap.dcMotor.get("intake");
        intake = hardwareMap.dcMotor.get("transfer");
        shooterLeft  = hardwareMap.get(DcMotorEx.class, "leftShooter");
        shooterRight = hardwareMap.get(DcMotorEx.class, "rightShooter");
        blocker = hardwareMap.servo.get("blocker");

        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Pose2d beginPose = new Pose2d(new Vector2d(-54,50), Math.toRadians(-225));
        //this pose assumes the robot starts with the intake facing away from the goal. the shooter will be facing away from the goal

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Action Scrimmage2Auto = drive.actionBuilder(beginPose)
                .strafeTo(new Vector2d(-13,12))
                .stopAndAdd(new functionOfDOOM())
//                .waitSeconds(2)
                .turnTo(Math.toRadians(-270))
                .stopAndAdd(new smartIntake())
                .strafeTo(new Vector2d(-13,32))
                .strafeTo(new Vector2d(-13,48))
                .stopAndAdd(new stopSmartIntake())
                .strafeTo(new Vector2d(-13,12))
                .turnTo(Math.toRadians(142))
                .stopAndAdd(new fastFunctionOfDOOM())
//                .waitSeconds(0.8)
                .stopAndAdd(new stopLauncher())
                .strafeTo(new Vector2d(5,12))
                .stopAndAdd(new stopLauncher1())
                .build();



        Actions.runBlocking(new SequentialAction(Scrimmage2Auto));








    }

}