package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
@Disabled
public class DecodeStarterBot extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor frontLeftMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backRightMotor = null;

    Servo leftfeeder = null;
    Servo rightfeeder = null;
    DcMotor launcher = null;





    double wheelCircumfrence = 326.56;
    //Ticks Per Revolution
    double TPR = 537.7;
    //Centimeters Per Tick
    double CENTIMETERS_PER_TICK = 0.0607;


    // Gradually ramps motor power towards a target
    public void rampMotorPower(DcMotor motor, double targetPower, double rampRate) {
        double currentPower = motor.getPower();

        if (currentPower < targetPower) {
            currentPower += rampRate;
            if (currentPower > targetPower) {
                currentPower = targetPower;
            }
        } else if (currentPower > targetPower) {
            currentPower -= rampRate;
            if (currentPower < targetPower) {
                currentPower = targetPower;
            }
        }

        motor.setPower(currentPower);
    }



    private void encoderDrive( double xcentimeters, double ycentimeters, double timeoutSec) {
        GoBildaPinpointDriver pinpoint= hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");



        // Calculate target ticks
        int xmoveCounts = (int) Math.round(xcentimeters * CENTIMETERS_PER_TICK);
        int ymoveCounts = (int) Math.round(ycentimeters * CENTIMETERS_PER_TICK);

        //Get position values of pinpoint odometry computer
        int FowardCurrent = pinpoint.getEncoderY();
        int StrafeCurrent = pinpoint.getEncoderX();
        //Target Positions
        int newLt = FowardCurrent + xmoveCounts;
        int newSt = StrafeCurrent + ymoveCounts;

        //Add settings for Motors
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set target and run to position for going forward/backward
        frontLeftMotor.setTargetPosition(newLt);
        frontRightMotor.setTargetPosition(newLt);
        backRightMotor.setTargetPosition(newLt);
        backLeftMotor.setTargetPosition(newLt);
        // Set target and run to position for strafing left/right
        frontLeftMotor.setTargetPosition(newSt);
        frontRightMotor.setTargetPosition(newSt);
        backRightMotor.setTargetPosition(newSt);
        backLeftMotor.setTargetPosition(newSt);
        //Set Motor Power for movement. Placed at half-power to preserve Bevel Gears
        frontLeftMotor.setPower(0.5);
        frontRightMotor.setPower(0.5);
        backRightMotor.setPower(0.5);
        backLeftMotor.setPower(0.5);
        //Timeout timer
        runtime.reset();
        while (opModeIsActive() &&
                (runtime.seconds() < timeoutSec) &&
                (frontRightMotor.isBusy() || frontLeftMotor.isBusy() || backRightMotor.isBusy() || backLeftMotor.isBusy())) {
            // Optionally add telemetry

            // allow the loop to be interrupted
            idle();
        }






        // Set power



    }



    @Override
    public void runOpMode() throws InterruptedException {

        //Hardwaremap
        frontLeftMotor = hardwareMap.dcMotor.get("left_front_drive");
        backLeftMotor = hardwareMap.dcMotor.get("left_back_drive");
        frontRightMotor = hardwareMap.dcMotor.get("right_front_drive");
        backRightMotor = hardwareMap.dcMotor.get("right_back_drive");
        leftfeeder = hardwareMap.servo.get("leftfeeder");
        rightfeeder = hardwareMap.servo.get("rightfeeder");
        launcher = hardwareMap.dcMotor.get("launcher");
        GoBildaPinpointDriver pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        // Set motor settings
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);






        // Sets IMU

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        waitForStart();

        if (isStopRequested()) return;

        /*IMPORTANT!!!!!!!!!!!
        This code below is for the TeleOp section of the program
        Pushing A makes the robot transfer
        Holding the left trigger warmups the launcher
        Joysticks are for movement
        *Field Centric Program btw
        U know this already
        *Note: Servos have to be updated, so don't expect the servos to work.
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

            // Warmup Launcher and Servo Transfer
            float leftTrigger = gamepad1.left_trigger;
            if(leftTrigger>0){
                launcher.setPower(1);
            } else if(leftTrigger == 0){
                launcher.setPower(0);
            }

            if(gamepad1.a){
                leftfeeder.setPosition(0.3);
                rightfeeder.setPosition(0.3);
            }









            telemetry.addData("frontLeftMotorPos:", frontLeftMotor.getCurrentPosition());
            telemetry.addData("frontRightMotorPos:", frontRightMotor.getCurrentPosition());
            telemetry.addData("backLeftMotorPos", backLeftMotor.getCurrentPosition());
            telemetry.addData("backRightMotorPos", backRightMotor.getCurrentPosition());

            telemetry.addLine("I have made change");





            telemetry.addLine("pressing dpad Right makes robot go in square");




            telemetry.update();






        }


    }
}


