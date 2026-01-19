package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class FlywheelTunerPF extends OpMode {

    // TWO shooter motors
    public DcMotorEx flywheelLeft;
    public DcMotorEx flywheelRight;

    public double highVelocity = 1500;
    public double lowVelocity  = 900;
    double curTargetVelocity = highVelocity;

    double F = 0;
    double P = 0;

    double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};
    int stepIndex = 1;

    @Override
    public void init() {

        flywheelLeft  = hardwareMap.get(DcMotorEx.class, "leftShooter");
        flywheelRight = hardwareMap.get(DcMotorEx.class, "rightShooter");

        flywheelLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheelRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // IMPORTANT: motors face opposite directions
        flywheelLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheelRight.setDirection(DcMotorSimple.Direction.FORWARD);

        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);

        flywheelLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        flywheelRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        telemetry.addLine("2-Motor Flywheel Tuner Ready");
        telemetry.update();
    }

    @Override
    public void loop() {

        // ---- TOGGLE TARGET VELOCITY ----
        if (gamepad1.yWasPressed()) {
            curTargetVelocity =
                    (curTargetVelocity == highVelocity) ? lowVelocity : highVelocity;
        }

        // ---- STEP SIZE CHANGE ----
        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        // ---- TUNE F ----
        if (gamepad1.dpadLeftWasPressed()) {
            F -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }

        // ---- TUNE P ----
        if (gamepad1.dpadDownWasPressed()) {
            P += stepSizes[stepIndex];
        }
        if (gamepad1.dpadUpWasPressed()) {
            P -= stepSizes[stepIndex];
        }

        // ---- APPLY PIDF ----
        PIDFCoefficients pidf = new PIDFCoefficients(0.2007, 0, 0, 15);
        flywheelLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        flywheelRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        // ---- SET VELOCITY ----
        flywheelLeft.setVelocity(curTargetVelocity);
        flywheelRight.setVelocity(curTargetVelocity);

        // ---- TELEMETRY ----
        double leftVel  = flywheelLeft.getVelocity();
        double rightVel = flywheelRight.getVelocity();
        double avgVel   = (leftVel + rightVel) / 2.0;
        double error    = curTargetVelocity - avgVel;

        telemetry.addData("Target Velocity", curTargetVelocity);
        telemetry.addData("Left Velocity",  String.format("%.1f", leftVel));
        telemetry.addData("Right Velocity", String.format("%.1f", rightVel));
        telemetry.addData("Average Velocity", String.format("%.1f", avgVel));
        telemetry.addData("Error", String.format("%.1f", error));
        telemetry.addLine("-----");
        telemetry.addData("Tuning P", String.format("%.5f", P));
        telemetry.addData("Tuning F", String.format("%.5f", F));
        telemetry.addData("Step Size", stepSizes[stepIndex]);

        telemetry.update();
    }
}
