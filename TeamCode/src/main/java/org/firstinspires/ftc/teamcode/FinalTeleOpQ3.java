package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp
public class FinalTeleOpQ3 extends LinearOpMode {

    // Drive motors
    DcMotor frontLeftMotor;
    DcMotor backLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backRightMotor;
    DcMotor transfer;
    DcMotor intake;
    DcMotorEx shooterRight;
    DcMotorEx shooterLeft;
    Servo blocker;
    Servo rgbLight;

    private AprilTagProcessor aprilTag;

    private VisionPortal visionPortal;

    // Movement speed modifier
    double moveSpeed = 0.65;
    boolean USE_WEBCAM = true;


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

    DelayAction block;
    boolean closeShot;


    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()

                // The following default settings are available to un-comment and edit as needed.
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        //aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

    }   // end method initAprilTag()

    private void telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addData("Tag ID", detection.id);
                telemetry.addData("Yaw (deg)", "%.1f", detection.ftcPose.yaw);
            } else {
                telemetry.addLine(String.format("Unknown Tag ID %d", detection.id));
            }
        }
        telemetry.update();
    }

    private void blinkColor(){
        rgbLight.setPosition(0.5);
        block.start(200);
        if(block.done()){
            rgbLight.setPosition(0);
        }

    }


    @Override
    public void runOpMode() {

        // Hardware map
        frontLeftMotor = hardwareMap.dcMotor.get("frontleft");
        backLeftMotor  = hardwareMap.dcMotor.get("backleft");
        frontRightMotor = hardwareMap.dcMotor.get("frontright");
        backRightMotor  = hardwareMap.dcMotor.get("backright");
        transfer = hardwareMap.dcMotor.get("intake");
        intake = hardwareMap.dcMotor.get("transfer");
        shooterRight = hardwareMap.get(DcMotorEx.class, "rightShooter");
        shooterLeft  = hardwareMap.get(DcMotorEx.class, "leftShooter");
        blocker = hardwareMap.servo.get("blocker");
        //extra
        Servo blocker2 = hardwareMap.servo.get("bockerservo");
        rgbLight = hardwareMap.get(Servo.class, "blinkin");



        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pf = new PIDFCoefficients(0.0005, 0, 0, 12.8222);

        shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pf);
        shooterRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pf);

        waitForStart();

        while (opModeIsActive()) {

            // Read joystick inputs
            double y  = -gamepad1.left_stick_y;  // forward/backward
            double x  = gamepad1.left_stick_x;   // strafing
            double rx = gamepad1.right_stick_x;  // rotation

            // Normalize the values so no motor exceeds 1
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            // Apply powers
            frontLeftMotor.setPower((y + x + rx) / denominator * moveSpeed);
            backLeftMotor.setPower((y - x + rx) / denominator * moveSpeed);
            frontRightMotor.setPower((y - x - rx) / denominator * moveSpeed);
            backRightMotor.setPower((y + x - rx) / denominator * moveSpeed);

            // Adjust move speed
            if (gamepad1.dpad_up)  moveSpeed = 0.35; // slow
            if (gamepad1.dpad_down) moveSpeed = 0.85; // fast

            if(gamepad2.a) {
                //close
                blinkColor();
                closeShot = true;
                gamepad1.rumble(200);
                pf = new PIDFCoefficients(0.0005, 0, 0, 12.8222);
                shooterLeft.setVelocity(-1550);
                shooterRight.setVelocity(1550);
            } else if(gamepad2.b){
                //close
                blinkColor();
                closeShot = true;
                gamepad1.rumble(200);
                pf = new PIDFCoefficients(0.0005, 0, 0, 12.8222);
                shooterLeft.setVelocity(-1700);
                shooterRight.setVelocity(1700);
            } else if(gamepad2.x){
                //far
                blinkColor();
                closeShot = false;
                gamepad1.rumble(200);
                pf = new PIDFCoefficients(3.0004, 0, 0, 13.103);
                shooterLeft.setVelocity(-1900);
                shooterRight.setVelocity(1900);
            } else if(gamepad2.y){
                //far
                blinkColor();
                closeShot = false;
                gamepad1.rumble(200);
                pf = new PIDFCoefficients(3.0004, 0, 0, 13.103);
                shooterLeft.setVelocity(-2000);
                shooterRight.setVelocity(2000);
            }
//smart outake
            transfer.setPower(-gamepad1.left_trigger);
            intake.setPower(-gamepad1.left_trigger);
//smart intake
            transfer.setPower(gamepad1.right_trigger);
            intake.setPower(gamepad1.right_trigger);

            //reverse shooter
            if(gamepad1.b){
                shooterLeft.setVelocity(500);
                shooterRight.setVelocity(-500);

                sleep(200);

                shooterLeft.setVelocity(0);
                shooterRight.setVelocity(0);
            }


            DelayAction blockDelay = new DelayAction();
//inverse intake
            if(gamepad1.y){
                intake.setPower(-0.8);
                blockDelay.start(200);
                if(blockDelay.done()){
                    intake.setPower(0);
                }
            }

//blocker
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
}

