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
@Autonomous(name = "Q3AutoFrontOfFieldToBlue", group = "Autonomous")
public class Q3AutoFrontOfFieldToBlue extends LinearOpMode {


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
    double velocityPower = 1550;

    // lift class
    private boolean initialized = false;

    public class runShooter implements InstantFunction{
        @Override
        public void run(){
            shooterLeft.setVelocity(-velocityPower);
            shooterRight.setVelocity(velocityPower);
        }

    }
    public class blockerUp implements InstantFunction{
        @Override
        public void run(){
            blocker.setPosition(0.5);
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

            sleepSeconds(0.2);

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

        Pose2d beginPose = new Pose2d(new Vector2d(56,-12), Math.toRadians(180));
        //this pose assumes the robot starts with the intake facing away from the goal. the shooter will be facing away from the goal

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Action Scrimmage2Auto = drive.actionBuilder(beginPose)
                //shooting position [THIS IS FRONT OF FIELD TO BLUE]
                .strafeToLinearHeading(new Vector2d(53,-15),Math.toRadians(-170))
//go to intake artifacts
                .strafeToLinearHeading(new Vector2d(21,-28), Math.toRadians(-90))

//intakes artifacts
                .strafeTo(new Vector2d(23,-62))
                .strafeTo(new Vector2d(23,-28))
//goes to shooting position
                .strafeToLinearHeading(new Vector2d(45,-18),Math.toRadians(-170))
//goes to intake second rack
                .strafeToLinearHeading(new Vector2d(-5,-28), Math.toRadians(-90))
//intakes artifacts
                .strafeTo(new Vector2d(0,-62))
                .strafeTo(new Vector2d(0,-28))
//goes to shooting position front of field
                .strafeToLinearHeading(new Vector2d(-23,-19), Math.toRadians(-140))

                //goes to intake row 3 of artifacts
                .strafeToLinearHeading(new Vector2d(-30,-28), Math.toRadians(-89))
//intakes artifacts
                .strafeTo(new Vector2d(-27,-52))
                .strafeTo(new Vector2d(-27,-28))
//goes to shooting position near goal
                .strafeToLinearHeading(new Vector2d(-23,-23), Math.toRadians(-140))

//leave pts
                .strafeTo(new Vector2d(-40, -18))

                .build();



        Actions.runBlocking(new SequentialAction(Scrimmage2Auto));








    }

}