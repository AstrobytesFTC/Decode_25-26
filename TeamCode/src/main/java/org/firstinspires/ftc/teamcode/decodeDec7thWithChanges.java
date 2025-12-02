package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp
public class decodeDec7thWithChanges extends LinearOpMode {

    DcMotor frontLeftMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backRightMotor = null;
    DcMotor transfer = null;


    DcMotor launcher = null;

    DcMotor vertintake =null;

    double selectedPower = 0.0;
    double moveSpeed = 0.0;





    @Override
    public void runOpMode() throws InterruptedException {

        //Hardwaremap

        //Control Hub
        frontLeftMotor = hardwareMap.dcMotor.get("left_front_drive");
        backLeftMotor = hardwareMap.dcMotor.get("left_back_drive");
        frontRightMotor = hardwareMap.dcMotor.get("right_front_drive");
        backRightMotor = hardwareMap.dcMotor.get("right_back_drive");

        //Expansion Hub
        transfer = hardwareMap.dcMotor.get(("transfer"));

        vertintake = hardwareMap.dcMotor.get("intake");

        launcher = hardwareMap.dcMotor.get("launcher");



        // Sets IMU

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            double vertPower = vertintake.getPower();
            double launchPower = launcher.getPower();

            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.options) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = ((rotY + rotX + rx) / denominator*moveSpeed);
            double backLeftPower = ((rotY - rotX + rx) / denominator*moveSpeed);
            double frontRightPower = ((rotY - rotX - rx) / denominator*moveSpeed);
            double backRightPower = ((rotY + rotX - rx) / denominator*moveSpeed);

            /*
            NOTE:
            This is the code to-be finalized for the TeleOp
            There are Two Controllers
            Gamepad 1 is for movement
            Gamepad 2 is for Inner Mechanisms(Intake, Transfer, Shooter, Gate Servo, Feeder Servos)
             */

            //Movement

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            //Triggers
            float gamepad2leftTrigger = gamepad2.left_trigger;
            float gamePad1rightTrigger = gamepad1.right_trigger;
            float gamePad1leftTrigger = gamepad1.left_trigger;

            if(gamepad2.bWasPressed()){
                transfer.setPower(-1);
                sleep(50);

                transfer.setPower(1);
                sleep(75);

                transfer.setPower(0);
            }

            //Setting Code

            if(gamepad2.dpad_up){
                //Most power
                selectedPower = 0.85;

            } else if(gamepad2.dpad_down){
                //Least Power
                selectedPower = 1;

            } else if(gamepad2.dpad_left){
                //Middle Power
                selectedPower = 0.75;

            } else if(gamepad2.dpad_right){

                selectedPower = -0.85;

            }

            if(gamepad1.left_bumper){
                moveSpeed = 0.7;
            } else if(gamepad1.right_bumper){
                moveSpeed = 0.4;
            }

            if(gamepad2.a){
                transfer.setPower(-1);
            } else if(gamepad2.y){
                transfer.setPower(1);
            } else{
                transfer.setPower(0);
            }

            //Launcher + Intake Trigger Code


            launcher.setPower(-gamepad2leftTrigger*selectedPower);
            //Half speed
            vertintake.setPower(-gamePad1leftTrigger);
            vertintake.setPower(gamePad1rightTrigger);

            if(gamePad1rightTrigger < 0 || gamePad1rightTrigger > 0){
                transfer.setPower(-0.5);
                launcher.setPower(-0.1);
            } else{
                transfer.setPower(0);
                launcher.setPower(0);
            }



            if(gamepad2leftTrigger < 0){
                vertintake.setPower(0.1);
            }



            //Feeder Commands(Retractable)

            //Telemetry

            telemetry.addData("transfer power", transfer.getPower());
            telemetry.addData("power setting", selectedPower);
            telemetry.addData("move speed", moveSpeed);
            telemetry.addData("launcher Power", launchPower);
            telemetry.addData("intake Power", vertPower);
            telemetry.update();





        }


    }
}


