package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.List;

@TeleOp(name = "TeleOp AutoTurn", group = "Main")
public class sampleQ3TestingCamera extends LinearOpMode {

    DcMotor frontLeftMotor,backLeftMotor,backRightMotor,frontRightMotor;
    AprilTagProcessor aprilTag;
    VisionPortal visionPortal;
    Servo rgb;

    @Override
    public void runOpMode() {

        // ---------------- MOTOR SETUP ----------------
        frontLeftMotor = hardwareMap.dcMotor.get("frontleft");
        backLeftMotor  = hardwareMap.dcMotor.get("backleft");
        frontRightMotor = hardwareMap.dcMotor.get("frontright");
        backRightMotor  = hardwareMap.dcMotor.get("backright");
        rgb = hardwareMap.servo.get(("blinkin"));

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // ---------------- APRILTAG SETUP ----------------
        aprilTag = new AprilTagProcessor.Builder().build();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();

        telemetry.addLine("Ready.");
        telemetry.update();
        waitForStart();

        // ---------------- TELEOP LOOP ----------------
        while (opModeIsActive()) {

            // Manual drive
            double x = -gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;

            drive(x, y, turn);

            // Press X to auto-turn
            if (gamepad1.x) {
                turnToYawZero();
            }

            telemetry.update();
        }
    }

    // --------------- AUTO TURN FUNCTION ---------------
    public void turnToYawZero() {
        List<AprilTagDetection> det = aprilTag.getDetections();

        if (det.size() == 0) {
            telemetry.addLine("No tag!");
            drive(0, 0, 0);
            return;
        }

        double yaw = det.get(0).ftcPose.yaw;
        double kP = 0.02;
        double turn = -yaw * kP;

        turn = Math.max(Math.min(turn, 0.5), -0.5);

        if (Math.abs(yaw) < 2) {
            telemetry.addLine("Aligned!");
            drive(0, 0, 0);
            rgb.setPosition(0.5);
            return;
        }

        drive(0, 0, turn);
        telemetry.addData("Yaw", yaw);
        telemetry.addData("TurnPower", turn);
    }

    // --------------- MECANUM DRIVE ---------------
    public void drive(double x, double y, double turn) {
        double lf = y + x + turn;
        double rf = y - x - turn;
        double lr = y - x + turn;
        double rr = y + x - turn;

        frontLeftMotor.setPower(lf);
        frontRightMotor.setPower(rf);
        backLeftMotor.setPower(lr);
        backRightMotor.setPower(rr);
    }
}
