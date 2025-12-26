package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;



@TeleOp
public class Q2Testing extends LinearOpMode {
    DcMotor frontLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor backRightMotor = null;
    DcMotor shooterRight = null;
    DcMotor shooterLeft = null;
    DcMotor transfer = null;
    DcMotor intake = null;
    Servo blocker = null;
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        frontLeftMotor = hardwareMap.dcMotor.get("frontleft");
        backLeftMotor = hardwareMap.dcMotor.get("backleft");
        frontRightMotor = hardwareMap.dcMotor.get("frontright");
        backRightMotor = hardwareMap.dcMotor.get("backright");
        shooterRight = hardwareMap.dcMotor.get("rightShooter");
        shooterLeft = hardwareMap.dcMotor.get("leftShooter");
        transfer = hardwareMap.dcMotor.get("intake");
        intake = hardwareMap.dcMotor.get("transfer");
        blocker = hardwareMap.servo.get("blocker");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            double shootSpeed = 1;
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // This button choice was made Sso that it is hard to hit on accident,
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

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower*0.7);
            backLeftMotor.setPower(backLeftPower*0.7);
            frontRightMotor.setPower(frontRightPower*0.7);
            backRightMotor.setPower(backRightPower*0.7);



//            if(gamepad1.a){
//                frontLeftMotor.setPower(1);
//            } else if(gamepad1.b){
//                //Reverse
//                frontRightMotor.setPower(1);
//            } else if (gamepad1.y) {
//                //Reverse
//                backLeftMotor.setPower(1);
//            } else if(gamepad1.x){
//                //reverse
//                backRightMotor.setPower(1);
//            }

            if(gamepad1.left_bumper){
                blocker.setPosition(0.5);

            } else if(gamepad1.right_bumper){
                blocker.setPosition(0.3);

            }

            if(gamepad1.dpad_up){
                shootSpeed = 1;
            } else if(gamepad1.dpad_down){
                shootSpeed = 0.8;
            }

            intake.setPower(gamepad1.left_trigger);

            if(gamepad1.y){
                transfer.setPower(1);
            } else if(gamepad1.b){
                transfer.setPower(-1);
            }else {
                transfer.setPower(0);
            }

            //G2

            shooterLeft.setPower(gamepad1.right_trigger*shootSpeed);
            shooterRight.setPower(gamepad1.right_trigger*shootSpeed);

        }
    }
}