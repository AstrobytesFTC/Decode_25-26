package org.firstinspires.ftc.teamcode;


// RR-specific imports

import static java.lang.Thread.sleep;

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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@Config
@Autonomous(name = "Q3AtGoalRed", group = "Autonomous")
public class Q3AutoAtGoalRed extends LinearOpMode {


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
    //    double velocityPower = 1880;

    double velocityPowerFar = 1680;
    double velocityPowerNear = 1411;
    // lift class
    private boolean initialized = false;

    public class runShooter implements InstantFunction{
        @Override
        public void run(){
            shooterLeft.setVelocity(-velocityPowerNear);
            shooterRight.setVelocity(velocityPowerNear);
        }

    }

    public class reduceShooterSpeed implements InstantFunction{
        @Override
        public void run(){
            shooterLeft.setVelocity(-velocityPowerNear);
            shooterRight.setVelocity(velocityPowerNear);
        }

    }

    public class increaseShooterSpeed implements InstantFunction{
        @Override
        public void run(){
            shooterLeft.setVelocity(-velocityPowerFar);
            shooterRight.setVelocity(velocityPowerFar);
        }
    }
    public class blockerUp implements InstantFunction{
        @Override
        public void run(){
            blocker.setPosition(1);
        }

    }
    public class blockerDown implements InstantFunction{
        @Override
        public void run(){
            blocker.setPosition(0.1);
        }

    }
    public class smartFeed implements InstantFunction{
        @Override
        public void run(){
            transfer.setPower(0.8);
            intake.setPower(0.8);

            sleepSeconds(.7);

            transfer.setPower(0);
            intake.setPower(0);
        }

    }
    public class smartIntake implements InstantFunction{
        @Override
        public void run(){
            intake.setPower(0.8);
            transfer.setPower(0.8);
        }

    }
    public class stopSmartIntake implements InstantFunction{
        @Override
        public void run(){
            intake.setPower(0);
            transfer.setPower(0);
        }

    }
    public boolean waitForShooter(DcMotorEx shooter, double target, long timeoutMs) {
        long start = System.currentTimeMillis();

        while (opModeIsActive()
                && System.currentTimeMillis() - start < timeoutMs) {

            double velocity = Math.abs(shooter.getVelocity());

            if (Math.abs(velocity - target) < 45) {
                return true;
            }

            sleep(5); // allow hardware loop
        }
        return false; // timed out
    }

    public class smartFeedNear implements InstantFunction{
        @Override
        public void run(){
            blocker.setPosition(1);
            //telemetry.addLine("NOT DONE");
            if(waitForShooter(shooterLeft, velocityPowerNear,3000)){
                //telemetry.addLine("SHOT");
                transfer.setPower(0.9);
                intake.setPower(0.9);

                sleepSeconds(.7);

                transfer.setPower(0);
                intake.setPower(0);
            }
            //telemetry.addLine("DONEE");
            blocker.setPosition(0.1);
            //telemetry.update();
        }

    }

    public class smartFeedFar implements InstantFunction{
        @Override
        public void run(){
            blocker.setPosition(1);
            //telemetry.addLine("NOT DONE");
            if(waitForShooter(shooterLeft, velocityPowerFar,3000)){
              //  telemetry.addLine("SHOT");
                transfer.setPower(0.8);
                intake.setPower(0.8);

                sleepSeconds(.6);

                transfer.setPower(0);
                intake.setPower(0);
            }
           // telemetry.addLine("DONEE");
            blocker.setPosition(0.1);
            //telemetry.update();
        }

    }
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

      /* PIDFCoefficients pf = new PIDFCoefficients(0.0005, 0, 0, 12.8222);
        shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pf);
        shooterRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pf);*/

        telemetry.addData("Current Velocity", Math.abs(shooterLeft.getVelocity()));


        Pose2d beginPose = new Pose2d(new Vector2d(-56,50), Math.toRadians(-225));
        //this pose assumes the robot starts with the intake facing away from the goal. the shooter will be facing away from the goal

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Action Scrimmage2Auto = drive.actionBuilder(beginPose)

                // First Shooting
                .stopAndAdd(new runShooter())
                .strafeTo(new Vector2d(-20,14))
                .stopAndAdd(new smartFeedNear())


                //Intakes Balls First Row
                .strafeToLinearHeading(new Vector2d(-13, 26), Math.toRadians(90))
                .stopAndAdd(new smartIntake())
                .strafeTo(new Vector2d(-16,43))
                .strafeTo(new Vector2d(-16,26))
                .stopAndAdd(new stopSmartIntake())

                //Lines up to shoot
               // .stopAndAdd(new runShooter())
                .strafeToLinearHeading(new Vector2d(-24, 14), Math.toRadians(135))
                .stopAndAdd(new smartFeedNear())

                // Goes to intake second row of balls
                //.strafeTo(new Vector2d(14,12 ))
                .stopAndAdd(new smartIntake())
                .strafeToLinearHeading(new Vector2d(12,14), Math.toRadians(90))
                .strafeTo(new Vector2d(10,45))
                .strafeTo(new Vector2d(10,17))
                .stopAndAdd(new stopSmartIntake())

                // Lines up for shot
                //.stopAndAdd(new runShooter())
                .strafeToLinearHeading(new Vector2d(-24, 14), Math.toRadians(135))
                .stopAndAdd(new smartFeedNear())
                .stopAndAdd(new increaseShooterSpeed())


                // Goes to intake the third row of balls
                .stopAndAdd(new smartIntake())
                .strafeToLinearHeading(new Vector2d(36, 14), Math.toRadians(90))
                .strafeTo(new Vector2d(35,35))
                .strafeTo(new Vector2d(35,25))
                .stopAndAdd(new stopSmartIntake())

                //Lines up to shoot
                //.stopAndAdd(new runShooter())
                .strafeToLinearHeading(new Vector2d(56, 16), Math.toRadians(155))
                .stopAndAdd(new smartFeedFar())
                //.stop



                //Leave points
                .strafeTo(new Vector2d(38, 20))
                .build();



        Actions.runBlocking(new SequentialAction(Scrimmage2Auto));

        telemetry.update();








    }

}