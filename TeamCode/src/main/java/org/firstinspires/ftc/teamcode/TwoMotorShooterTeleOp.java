package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Two Motor Adjustable Shooter", group = "Shooter")
public class TwoMotorShooterTeleOp extends LinearOpMode {

    // Shooter motors
    DcMotorEx shooterLeft;
    DcMotorEx shooterRight;

    // Shooter state
    boolean shooterOn = false;
    boolean lastRB = false;

    // Velocity presets (TUNE THESE)
    double closeVel = 1500;
    double midVel   = 1800;
    double farVel   = 2100;

    double targetVelocity = midVel;

    @Override
    public void runOpMode() {

        // Hardware mapping
        shooterLeft  = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");

        // Motor directions (CRITICAL)
        shooterLeft.setDirection(DcMotor.Direction.FORWARD);
        shooterRight.setDirection(DcMotor.Direction.REVERSE);

        // Use encoders
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Float when stopped
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry.addLine("Shooter Ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // -------- TOGGLE SHOOTER --------
            if (gamepad1.right_bumper && !lastRB) {
                shooterOn = !shooterOn;
            }
            lastRB = gamepad1.right_bumper;

            // -------- VELOCITY PRESETS --------
            if (gamepad1.x) {
                targetVelocity = closeVel;
            }
            if (gamepad1.y) {
                targetVelocity = midVel;
            }
            if (gamepad1.b) {
                targetVelocity = farVel;
            }

            // -------- APPLY SHOOTER VELOCITY --------
            if (shooterOn) {
                shooterLeft.setVelocity(targetVelocity);
                shooterRight.setVelocity(targetVelocity);
            } else {
                shooterLeft.setVelocity(0);
                shooterRight.setVelocity(0);
            }

            // -------- TELEMETRY --------
            telemetry.addData("Shooter On", shooterOn);
            telemetry.addData("Target Velocity", targetVelocity);
            telemetry.addData("Left Velocity", shooterLeft.getVelocity());
            telemetry.addData("Right Velocity", shooterRight.getVelocity());
            telemetry.update();
        }
    }
}
