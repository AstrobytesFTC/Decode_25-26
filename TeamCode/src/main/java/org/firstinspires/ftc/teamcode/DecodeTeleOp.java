package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

@TeleOp
@Disabled
public class DecodeTeleOp extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor frontLeftMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backRightMotor = null;
    DcMotor transfer = null;
    DcMotor intake = null;
    DcMotor launcher = null;
//    Servo deciderofdoom = null;
    ColorSensor csensor = null;


    public String detectColor() {
        int r = csensor.red();
        int g = csensor.green();
        int b = csensor.blue();

        // Normalize by total brightness to make detection more consistent
        int total = r + g + b;
        if (total == 0) return "UNKNOWN";

        double rn = (double)r / total;
        double gn = (double)g / total;
        double bn = (double)b / total;

        // Color conditions
        if (rn > 0.45 && gn < 0.3 && bn < 0.3) return "RED";
        if (gn > 0.45 && rn < 0.3 && bn < 0.3) return "GREEN";
        if (bn > 0.45 && rn < 0.3 && gn < 0.3) return "BLUE";
        if (rn > 0.35 && gn > 0.35 && bn < 0.25) return "YELLOW";
        if (bn > 0.35 && rn > 0.35 && gn < 0.3) return "PURPLE";

        return "UNKNOWN";
    }




    double wheelCircumfrence = 326.56;
    //Ticks Per Revolution
    double TPR = 537.7;
    //Centimeters Per Tick
    double CENTIMETERS_PER_TICK = 0.0607;
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;
    private int lastSeenTag = -1;

    public int detectAprilTag() {
        // Initialize only once
        if (visionPortal == null) {
            aprilTag = new AprilTagProcessor.Builder().build();

            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();
        }

        List<AprilTagDetection> detections = aprilTag.getDetections();

        if (!detections.isEmpty()) {
            lastSeenTag = detections.get(0).id;
            return lastSeenTag;  // return detected tag ID
        }

        return -1;  // no tag seen
    }



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
        intake = hardwareMap.dcMotor.get(("intake"));
        launcher = hardwareMap.dcMotor.get("launcher");
        csensor = hardwareMap.colorSensor.get("colorsensor");
        //deciderofdoom = hardwareMap.servo.get(("deciderservo"));


        // Set motor settings
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);







        // Sets IMU

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        waitForStart();

        if (isStopRequested()) return;

        /*IMPORTANT!!!!!!!!!!!

         */


        while (opModeIsActive()) {
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
            double frontLeftPower = ((rotY + rotX + rx) / denominator)*0.5;
            double backLeftPower = ((rotY - rotX + rx) / denominator)*0.5;
            double frontRightPower = ((rotY - rotX - rx) / denominator)*0.5;
            double backRightPower = ((rotY + rotX - rx) / denominator)*0.5;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            //Code for Actions
            /*
            IMPORTANT!!!!!
            using the left trigger warmups launcher
            pressing a makes the intake spin for 2.5 seconds
            and makes the color sensor sense the artifacts color, which the servo will move
            and load the artifact into the transfer
            pressing b triggers the transfer to run for 2.5 secs

             */

            // Warmup Launcher Code
            float leftTrigger = gamepad1.left_trigger;

            if(leftTrigger>0){
                launcher.setPower(-1);
            } else if(leftTrigger == 0){
                launcher.setPower(0);
            }

            // Transfer
            if(gamepad1.a){
                intake.setPower(1);
                sleep(250);
                intake.setPower(0);
            }

            if(gamepad1.b){
                transfer.setPower(1);
                sleep(250);
                transfer.setPower(0);
            }

            //Color Sensor Code

            if (detectColor().equals("Green")){
                //deciderofdoom.setPosition(1);
            }else if (detectColor().equals("Purple")){
                //deciderofdoom.setPosition(-1);
            }

            if (gamepad1.left_bumper) {
                visionPortal.stopStreaming();
            } else if (gamepad1.right_bumper) {
                visionPortal.resumeStreaming();
            }

            //Telemetry
            telemetry.addData("AprilTags",detectAprilTag());
            telemetry.addData("Colors!!!!", detectColor());
            telemetry.update();


        }


    }
}


