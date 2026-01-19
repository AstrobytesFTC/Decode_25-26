package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp
@Disabled
public class decodeToBeFinalizedTeleOp extends LinearOpMode {

    DcMotor frontLeftMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backRightMotor = null;
    DcMotor transfer = null;
    DcMotor leftintake = null;
    DcMotor rightintake = null;

    Servo leftfeeder = null;
    Servo rightfeeder = null;
    DcMotor launcher = null;
    Servo deciderofdoom = null;
    NormalizedColorSensor csensor = null;

    double selectedPower = 0.0;

    private String detectGreen() {
        if (csensor == null) {
            return "ERROR: Sensor not initialized";
        }

        NormalizedRGBA colors = csensor.getNormalizedColors();

        float r = colors.red;
        float g = colors.green;
        float b = colors.blue;
        float a = colors.alpha;


        if (a != 0) {
            r /= a;
            g /= a;
            b /= a;
        }

        telemetry.addData("Raw RGB", "R: %.3f  G: %.3f  B: %.3f", r, g, b);


        float total = r + g + b;
        if (total == 0) return "UNKNOWN";
        float redRatio = r / total;
        float greenRatio = g / total;
        float blueRatio = b / total;


        if (greenRatio > 0.45 && greenRatio > redRatio + 0.10 && greenRatio > blueRatio + 0.10 && g > 0.2) {
            return "GREEN";
        }

        return "UNKNOWN";
    }


    @Override
    public void runOpMode() throws InterruptedException {

        // Variables outside loop
        boolean dpadRightWasPressed = false;
        long lastTapTime = 0;
        long doubleTapTimeWindow = 300; // milliseconds allowed between taps

        //Hardwaremap

        //Control Hub
        frontLeftMotor = hardwareMap.dcMotor.get("left_front_drive");
        backLeftMotor = hardwareMap.dcMotor.get("left_back_drive");
        frontRightMotor = hardwareMap.dcMotor.get("right_front_drive");
        backRightMotor = hardwareMap.dcMotor.get("right_back_drive");

        //Expansion Hub
        transfer = hardwareMap.dcMotor.get(("transfer"));

        leftfeeder = hardwareMap.servo.get("leftfeeder");
        rightfeeder = hardwareMap.servo.get("rightfeeder");

        leftintake = hardwareMap.dcMotor.get("leftintake");
        rightintake = hardwareMap.dcMotor.get("rightintake");

        launcher = hardwareMap.dcMotor.get("launcher");
        csensor = hardwareMap.get(NormalizedColorSensor.class, "colorsensor");
        deciderofdoom = hardwareMap.servo.get(("deciderservo"));


//        // Set motor settings
//        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        rightintake.setDirection(DcMotorSimple.Direction.REVERSE);
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
            double frontLeftPower = ((rotY + rotX + rx) / denominator*0.7);
            double backLeftPower = ((rotY - rotX + rx) / denominator*0.7);
            double frontRightPower = ((rotY - rotX - rx) / denominator*0.7);
            double backRightPower = ((rotY + rotX - rx) / denominator*0.7);

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
            float gamePad1LeftTrigger = gamepad1.left_trigger;


            //Gate Servo Movement
//            if(detectGreen().equals("GREEN")){
//                sleep(150);
//                deciderofdoom.setPosition(-0.6);
//            } else if(detectGreen().equals("UNKNOWN")){
//                deciderofdoom.setPosition(0.3);
//            }

            //Transfer Code(Foward and Inverse Code)

            //Forward Commands
            if(gamepad2.y){
                transfer.setPower(-1);
            } else {
                transfer.setPower(0);
            }

            //Inverse Commands
            if(gamepad2.a){
                transfer.setPower(1);
            } else {
                transfer.setPower(0);
            }

            if(gamepad2.dpad_up){
                launcher.setPower(1);
            }


            if(gamepad2.bWasPressed()){
                transfer.setPower(-1);
                sleep(50);

                transfer.setPower(1);
                sleep(66);

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

            //Launcher + Intake Trigger Code






            launcher.setPower(-gamepad2leftTrigger*selectedPower    );
            //Half speed
            rightintake.setPower(gamePad1rightTrigger*0.75);
            leftintake.setPower(gamePad1rightTrigger*0.75);




            //Feeder Commands(Retractable)
            if(gamepad2.right_bumper){
                rightfeeder.setPosition(-1);
                sleep(300);
                rightfeeder.setPosition(1);
            }

            if(gamepad2.left_bumper){
                leftfeeder.setPosition(1);
                sleep(300);
                leftfeeder.setPosition(-1);
            }

            //Telemetry
            telemetry.addData("color detected",detectGreen());
            telemetry.addData("transfer power", transfer.getPower());
            telemetry.addData("power setting", selectedPower);
            telemetry.update();



            //Leftover Code
//
//            if(gamepad1.dpad_left){
//
//                rightfeeder.setPosition(1);
//
//            } else if(gamepad1.dpad_right){
//                //extends
//
//                leftfeeder.setPosition(0.75);
//
//            } else if(gamepad1.dpad_up){
//                //extends
//
//                rightfeeder.setPosition(-1);
//
//            } else if(gamepad1.dpad_down){
//
//                leftfeeder.setPosition(-0.75);
//
//            }

        }


    }
}


