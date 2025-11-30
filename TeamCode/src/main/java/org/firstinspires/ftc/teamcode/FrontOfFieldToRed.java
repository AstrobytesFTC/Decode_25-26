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
import org.firstinspires.ftc.teamcode.AtGoalToRed;





@Config
@Autonomous(name = "FrontOfFieldToRed", group = "Autonomous")
public class FrontOfFieldToRed extends LinearOpMode {


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
        Action path = drive.actionBuilder(beginPose)


                .lineToX(-18)
                .stopAndAdd(new warmupLaunch())
                .turn(Math.toRadians(130))
                .stopAndAdd(new transferArtifact())
                .waitSeconds(2.5)
                .stopAndAdd(new Shoot())
                .waitSeconds(1)
                .stopAndAdd(new intakeFeed())
                .stopAndAdd(new transferArtifact())
                .waitSeconds(5)
             //   .stopAndAdd(new Shoot())
             //   .stopAndAdd(new transferArtifact())
             //   .stopAndAdd(new stopintake())
                .build();




        Actions.runBlocking(new SequentialAction(path));








    }

}

