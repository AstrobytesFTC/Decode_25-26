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
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


@Config
@Autonomous(name = "Q2AutoFarSideBlue", group = "Autonomous")
public class Q2AutoFarSideBlue extends LinearOpMode {


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
    public class functionOfDOOM implements InstantFunction{
        @Override
        public void run(){

            double doomVelocity = 1800;
            double transfertime = 0.3;


            boolean shootControl = false;

            blocker.setPosition(0.1);
            sleep(500);  // 500ms
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

            shooterRight.setVelocity(-doomVelocity);
            shooterLeft.setVelocity(doomVelocity);

            // Shoot 1
            double currentVelocity = shooterLeft.getVelocity();
            shootControl = true;
            while (shootControl) {
                if (Math.abs(currentVelocity - doomVelocity) < 40) {
                    transfer.setPower(0.65);
                    //intake.setPower(0.8);
                    // new code
                    sleepSeconds(transfertime - 0.15);
                    transfer.setPower(-0.4);
                    sleepSeconds(transfertime - 0.15);
                    transfer.setPower(0);
                    shootControl = false;
                    telemetry.addLine("Shot 1 completed");
                    telemetry.update();
//                double doomTimer = System.currentTimeMillis();

                }
            }
            // Shoot 2
            shootControl = true;
            currentVelocity = shooterLeft.getVelocity();
            while(shootControl) {
                if (Math.abs(currentVelocity - doomVelocity) < 40) {
                    transfer.setPower(0.65);
                    sleepSeconds(transfertime);
                    transfer.setPower(0);
                    shootControl = false;
                    telemetry.addLine("Shot 2 completed");
                    telemetry.update();
                    //intake.setPower(0.8);
//                double doomTimer = System.currentTimeMillis();
//                doomStep++;
                }
            }

            // Shoot 3
            shootControl = true;
            currentVelocity = shooterLeft.getVelocity();
            while(shootControl) {
                if (Math.abs(currentVelocity - doomVelocity) < 40) {
                    transfer.setPower(0.7);
                    intake.setPower(0.7);
                    sleepSeconds(transfertime+0.1);
                    transfer.setPower(0);
                    shootControl = false;
                    telemetry.addLine("Shot 3 completed");
                    telemetry.update();
                    //intake.setPower(0.8);
//                double doomTimer = System.currentTimeMillis();
//                doomStep++;
                }
            }
            doomVelocity = 0;
            shooterRight.setVelocity(0);
            shooterLeft.setVelocity(0);
            intake.setPower(0);
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

                .turn(Math.toRadians(20))
                .stopAndAdd(new functionOfDOOM() )

                .strafeToLinearHeading(new Vector2d(36 ,-28), Math.toRadians(-90))
                .stopAndAdd(new smartIntake())
                .strafeTo(new Vector2d(39,-56))
                .strafeTo(new Vector2d(39,-28))
                .strafeToLinearHeading(new Vector2d(52,-16),Math.toRadians(-145))
                .stopAndAdd(new stopSmartIntake())
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
