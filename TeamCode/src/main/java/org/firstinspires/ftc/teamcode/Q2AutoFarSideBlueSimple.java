package org.firstinspires.ftc.teamcode;


// RR-specific imports

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
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@Config
@Autonomous(name = "Q2AutoFarSideBlueSimple", group = "Autonomous")
public class Q2AutoFarSideBlueSimple extends LinearOpMode {


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
            double doomVelocity = 1680;
            double transfertime = 0.3;

            blocker.setPosition(0.1);
            sleep(1000);  // 500ms
            blocker.setPosition(0.6);

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

            if (waitForShooter(shooterRight, doomVelocity, 3000)) {
                sleepSeconds(0.2);
                transfer.setPower(0.65);
                sleepSeconds(transfertime - 0.15);
                transfer.setPower(-0.4);
                sleepSeconds(transfertime - 0.15);
                transfer.setPower(0);
            }

            if (waitForShooter(shooterLeft, doomVelocity, 3000)) {
                sleepSeconds(0.2);
                transfer.setPower(0.65);
                sleepSeconds(transfertime);
                transfer.setPower(0);
            }

            if (waitForShooter(shooterLeft, doomVelocity, 3000)) {
                transfer.setPower(0.8);
                intake.setPower(0.7);
                sleepSeconds(transfertime + 0.3);
                transfer.setPower(0);
            }
            doomVelocity = 0;
            shooterRight.setVelocity(0);
            shooterLeft.setVelocity(0);
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
            shooterRight.setVelocity(-1000);
            shooterLeft.setVelocity(1000);
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
                .turn(Math.toRadians(-1))
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

                .turn(Math.toRadians(26))
                .stopAndAdd(new functionOfDOOM() )
                .lineToX(40)
                .stopAndAdd(new stopLauncher())
                .waitSeconds(1)
                .stopAndAdd(new stopLauncher1())

//                .stopAndAdd(new smartIntake())
//                .strafeToLinearHeading(new Vector2d(36 ,-28), Math.toRadians(-90))
//                .stopAndAdd(new smartIntake())
//                .strafeTo(new Vector2d(39,-75))
//                .strafeTo(new Vector2d(39,-32))
//                .strafeToLinearHeading(new Vector2d(52,-25),Math.toRadians(-145))
//                .stopAndAdd(new stopSmartIntake())
//                .stopAndAdd( new functionOfDOOM())
//                .stopAndAdd(new stopLauncher())
//
//                .strafeTo(new Vector2d(40,-28))
//                .stopAndAdd(new stopLauncher1())
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