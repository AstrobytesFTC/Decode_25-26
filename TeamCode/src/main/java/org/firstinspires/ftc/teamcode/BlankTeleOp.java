package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class BlankTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {

        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontleft");
        DcMotor backLeftMotor  = hardwareMap.dcMotor.get("backleft");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontright");
        DcMotor backRightMotor  = hardwareMap.dcMotor.get("backright");

        // ✅ CORRECT motor directions for mecanum
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            double y  = -gamepad1.left_stick_y; // forward/back
            double x  = gamepad1.left_stick_x;  // strafe
            double rx = gamepad1.right_stick_x; // rotate

            double denominator = Math.max(
                    Math.abs(y) + Math.abs(x) + Math.abs(rx), 1
            );

            double frontLeftPower  = (y + x + rx) / denominator;
            double backLeftPower   = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower  = (y + x - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower * 0.7);
            backLeftMotor.setPower(backLeftPower * 0.7);
            frontRightMotor.setPower(frontRightPower * 0.7);
            backRightMotor.setPower(backRightPower * 0.7);
        }
    }
}
