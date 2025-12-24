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


@Config
@Autonomous(name = "sampleFrontOfFieldRedComplexPathing", group = "Autonomous")
public class sampleFrontOfFieldRedComplexPathing extends LinearOpMode {


    DcMotor lf = null;
    DcMotor lb = null;
    DcMotor rf = null;
    DcMotor rb = null;

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

    public class reverseLaunch implements InstantFunction{
        @Override
        public void run(){
            launcher.setPower(0.5);

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
        Pose2d beginPose = new Pose2d(new Vector2d(56,12), Math.toRadians(0));
        //this pose assumes the robot starts with the intake facing away from the goal. the shooter will be facing away from the goal

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        //creating RR path

        // actionBuilder builds from the drive steps passed to it
        //this path moves backwards and turns
        Action complexfar = drive.actionBuilder(beginPose)
                .strafeToLinearHeading(new Vector2d(40,12),Math.toRadians(170))
                .strafeTo(new Vector2d(56,12))
                .turn(Math.toRadians(-80))
                .strafeTo(new Vector2d(35,12))
                .strafeTo(new Vector2d(35,50))
                .strafeToLinearHeading(new Vector2d(35,12),Math.toRadians(170))
                .strafeTo(new Vector2d(56,12))
                .strafeToLinearHeading(new Vector2d(12,12),Math.toRadians(90))
                .strafeTo(new Vector2d(12,50))
                .strafeToLinearHeading(new Vector2d(12,12), Math.toRadians(170))
                .strafeTo(new Vector2d(56,12))
                .strafeTo(new Vector2d(56,35))
                .build();
        Action complexclose = drive.actionBuilder(beginPose)
                .strafeToLinearHeading(new Vector2d(-12,12),Math.toRadians(140))
                .turn(Math.toRadians(-50))
                .strafeTo(new Vector2d(-12,50))
                .strafeToLinearHeading(new Vector2d(-12,12),Math.toRadians(-220))
                .turn(Math.toRadians(-50))
                .strafeTo(new Vector2d(12,12))
                .strafeTo(new Vector2d(12,50))
                .strafeToLinearHeading(new Vector2d(12,12),Math.toRadians(-220))
                .strafeTo(new Vector2d(-12,12))
                .turn(Math.toRadians(-50))
                .strafeTo(new Vector2d(35,12))
                .strafeTo(new Vector2d(35,50))
                .strafeToLinearHeading(new Vector2d(35,12),Math.toRadians(-220))
                .strafeTo(new Vector2d(-12,12))
                .turn(Math.toRadians(35))
                .strafeTo(new Vector2d(-38,12))

                .build();




        Actions.runBlocking(new SequentialAction(complexfar));








    }

}