package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp(name="finalTeleOpQ3", group="Main")
public class FinalTeleOpQ3 extends LinearOpMode {

    // ---------- Motors ----------
    DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;
    DcMotor transfer, intake;
    DcMotorEx shooterRight, shooterLeft;

    // ---------- Servos ----------
    Servo blocker, rgbLight;

    // ---------- Vision ----------
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    // ---------- Misc ----------
    double moveSpeed = 0.65;
    boolean closeShot = false;

    // ---------- Blink vars ----------
    long lastBlink = 0;
    boolean lightState = false;

    // ---------- DelayAction helper ----------
    public class DelayAction {
        ElapsedTime timer = new ElapsedTime();
        boolean active = false;
        double delay;

        public void start(double delayMs) {
            active = true;
            delay = delayMs;
            timer.reset();
        }

        public boolean done() {
            return active && timer.milliseconds() > delay;
        }

        public void stop() {
            active = false;
        }
    }

    DelayAction blockDelay = new DelayAction(); // only 1 object, not created in loop

    @Override
    public void runOpMode() {

        // ---------- Hardware Map ----------
        frontLeftMotor  = hardwareMap.dcMotor.get("frontleft");
        backLeftMotor   = hardwareMap.dcMotor.get("backleft");
        frontRightMotor = hardwareMap.dcMotor.get("frontright");
        backRightMotor  = hardwareMap.dcMotor.get("backright");

        transfer = hardwareMap.dcMotor.get("transfer");
        intake   = hardwareMap.dcMotor.get("intake");

        shooterRight = hardwareMap.get(DcMotorEx.class, "rightShooter");
        shooterLeft  = hardwareMap.get(DcMotorEx.class, "leftShooter");

        blocker = hardwareMap.servo.get("blocker");
        rgbLight = hardwareMap.servo.get("blinkin");

        // ---------- Motor directions ----------
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        // ---------- Shooter PID ----------
        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pf = new PIDFCoefficients(0.0005, 0, 0, 12.8222);
        shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pf);
        shooterRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pf);

        // ---------- Initialize AprilTag ----------
        aprilTag = new AprilTagProcessor.Builder().build();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();

        telemetry.addLine("Ready.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            // ---------- Mecanum Drive ----------
            double y  = -gamepad1.left_stick_y;
            double x  = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            frontLeftMotor.setPower((y + x + rx) / denominator * moveSpeed);
            backLeftMotor.setPower((y - x + rx) / denominator * moveSpeed);
            frontRightMotor.setPower((y - x - rx) / denominator * moveSpeed);
            backRightMotor.setPower((y + x - rx) / denominator * moveSpeed);

            // ---------- Move speed adjust ----------
            if (gamepad1.dpad_up) moveSpeed = 0.35; // slow
            if (gamepad1.dpad_down) moveSpeed = 1;  // fast

            // ---------- Shooter presets ----------
            if(gamepad2.a) {
                closeShot = true;
                gamepad2.rumble(200);
                shooterLeft.setVelocity(-1550);
                shooterRight.setVelocity(1550);
            } else if(gamepad2.b){
                closeShot = true;
                gamepad2.rumble(200);
                shooterLeft.setVelocity(-1700);
                shooterRight.setVelocity(1700);
            } else if(gamepad2.x){
                closeShot = false;
                gamepad2.rumble(200);
                shooterLeft.setVelocity(-1900);
                shooterRight.setVelocity(1900);
            } else if(gamepad2.y){
                closeShot = false;
                gamepad2.rumble(200);
                shooterLeft.setVelocity(-2000);
                shooterRight.setVelocity(2000);
            }

            // ---------- Intake / Transfer ----------
            double intakePower = gamepad1.right_trigger - gamepad1.left_trigger;
            intake.setPower(intakePower);
            transfer.setPower(intakePower);

            // ---------- Reverse Shooter ----------
            if(gamepad1.b){
                shooterLeft.setVelocity(500);
                shooterRight.setVelocity(-500);
                sleep(200);
                shooterLeft.setVelocity(0);
                shooterRight.setVelocity(0);
            }

            // ---------- Auto-turn ----------
            if(gamepad2.right_bumper){
                turnToYawZero();
            }

            // ---------- Intake Invert / Delay ----------
            if(gamepad1.y){
                intake.setPower(-0.8);
                blockDelay.start(200);
                if(blockDelay.done()){
                    intake.setPower(0);
                }
            }

            // ---------- Blocker ----------
            if(gamepad1.left_bumper){
                blocker.setPosition(0.5);
                blockDelay.start(500);
                if(blockDelay.done()){
                    blocker.setPosition(0.1);
                }
            } else{
                blocker.setPosition(0.1);
            }

            telemetry.addData("Close Shot?",closeShot);
            telemetry.update();
        }
    }

    // ---------- Auto-turn using AprilTag ----------
    public void turnToYawZero() {
        List<AprilTagDetection> det = aprilTag.getDetections();
        if(det.size() == 0){
            telemetry.addLine("No tag!");
            return;
        }

        double yaw = det.get(0).ftcPose.yaw;
        double kP = 0.02;
        double turn = -yaw * kP;
        turn = Math.max(Math.min(turn, 0.5), -0.5);

        if(Math.abs(yaw) < 2){
            telemetry.addLine("Aligned!");
            drive(0,0,0);
            blinkLight(0.5,200);
            return;
        }

        drive(0,0,turn);
        telemetry.addData("Yaw", yaw);
        telemetry.addData("TurnPower", turn);
    }

    // ---------- Mecanum drive method ----------
    public void drive(double x, double y, double turn){
        double lf = y + x + turn;
        double rf = y - x - turn;
        double lr = y - x + turn;
        double rr = y + x - turn;

        frontLeftMotor.setPower(lf);
        frontRightMotor.setPower(rf);
        backLeftMotor.setPower(lr);
        backRightMotor.setPower(rr);
    }

    // ---------- Blinkin LED blink ----------
    public void blinkLight(double colorPos, long speedMs){
        long now = System.currentTimeMillis();
        if(now - lastBlink > speedMs){
            lightState = !lightState;
            lastBlink = now;
        }

        if(lightState){
            rgbLight.setPosition(colorPos);
        } else{
            rgbLight.setPosition(0.0);
        }
    }
}
