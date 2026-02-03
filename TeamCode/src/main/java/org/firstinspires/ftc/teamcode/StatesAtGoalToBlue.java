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
@Autonomous(name = "AtGoalTOBlueWithChanges", group = "Autonomous")
public class StatesAtGoalToBlue extends LinearOpMode {


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
    public class blockerDown implements InstantFunction {
        @Override
        public void run() {
            blocker.setPosition(0.2);
        }

    }
    public class smartFeed implements InstantFunction{
        @Override
        public void run(){
            transfer.setPower(0.8);
            intake.setPower(0.8);

            sleepSeconds(.6);

            transfer.setPower(0);
            intake.setPower(0);
        }

    }
    public class smartIntake implements InstantFunction {
        @Override
        public void run() {
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
//Dependable, keep if close
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
            blocker.setPosition(0.2);
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
            blocker.setPosition(0.2);
            //telemetry.update();
        }

    }

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

//    public class stopLauncher implements InstantFunction {
//        @Override
//        public void run() {
//            //Change If needed
//            //shooterRight.setVelocity(-300);
//            //shooterLeft.setVelocity(-300);
//            //sleepSeconds(0.2);
//            shooterRight.setVelocity(-300);
//            shooterLeft.setVelocity(300);
//        }
//    }
//
//    public class stopLauncher1 implements InstantFunction{
//        @Override
//        public void run(){
//            //Change If needed
//            //shooterRight.setVelocity(-300);
//            //shooterLeft.setVelocity(-300);
//            //sleepSeconds(0.2);
//            shooterRight.setVelocity(0);
//            shooterLeft.setVelocity(0);
//
//        }
//    }


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

        telemetry.addData("Current Velocity", Math.abs(shooterLeft.getVelocity()));

        Pose2d beginPose = new Pose2d(new Vector2d(-52,-60), Math.toRadians(-140));
        //this pose assumes the robot starts with the intake facing away from the goal. the shooter will be facing away from the goal

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Action Scrimmage2Auto = drive.actionBuilder(beginPose)
                .stopAndAdd(new runShooter())
                .strafeToLinearHeading(new Vector2d(-23,-27), Math.toRadians(-135))

                .stopAndAdd(new smartFeedNear())
                //goes to intake artifacts row 1


                .strafeToLinearHeading(new Vector2d(-15,-40), Math.toRadians(-90))
                .stopAndAdd(new smartIntake())
//intakes artifacts

                .strafeTo(new Vector2d(-15,-66))

                .stopAndAdd(new stopSmartIntake())

//goes to shooting position
//                .stopAndAdd(new runShooter())
                .strafeToLinearHeading(new Vector2d(-23,-27), Math.toRadians(-135))
                .stopAndAdd(new smartFeedNear())
                //goes to intake second row of artifacts
                .stopAndAdd(new smartIntake())
                .strafeToLinearHeading(new Vector2d(10,-40), Math.toRadians(-90))


//intakes artifacts row 2

                .strafeTo(new Vector2d(10,-64))
                .stopAndAdd(new stopSmartIntake())

//goes to shooting position

                .strafeToLinearHeading(new Vector2d(-23,-27), Math.toRadians(-135))
                .stopAndAdd(new smartFeedNear())
                .stopAndAdd(new increaseShooterSpeed())

                //goes to intake third row of artifacts
                .stopAndAdd(new smartIntake())
                .strafeToLinearHeading(new Vector2d(35,-52), Math.toRadians(-90))

                //intakes artifacts

                .strafeTo(new Vector2d(35,-65))

                .stopAndAdd(new stopSmartIntake())

//goes to shooting position

                .strafeToLinearHeading(new Vector2d(62,-33),Math.toRadians(-160))
                .stopAndAdd(new smartFeedFar())
                //shoots
                .strafeTo(new Vector2d(38,-25))



                .build();





        Actions.runBlocking(new SequentialAction(Scrimmage2Auto));

        telemetry.update();








    }

}