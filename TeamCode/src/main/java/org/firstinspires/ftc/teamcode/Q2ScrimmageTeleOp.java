package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
public class Q2ScrimmageTeleOp extends LinearOpMode {

    DcMotor frontLeftMotor = null;
    DcMotor frontRightMotor = null;
    DcMotor backLeftMotor = null;
    DcMotor backRightMotor = null;
    DcMotor transfer = null;
    DcMotor intake = null;
    DcMotor shooterRight = null;
    DcMotor shooterLeft = null;
    Servo blocker = null;
    double moveSpeed = 0.65;
    double shootSpeed = 0.9;
    //Function Variables
    double doomSpeed = 0.85;
    int revTime = 1500;
    boolean doomCancelled = false;

    public void sleepSeconds(double seconds) {
        sleep(Math.round(seconds * 1000));
    }
    public void timeTransferAndIntake(double seconds) {
        transfer.setPower(0.8);
        intake.setPower(0.8);
        sleep(Math.round(seconds * 1000)); // seconds → ms
        transfer.setPower(0);
        intake.setPower(0);
    }
    public void reversetimeTransferAndIntake(double seconds) {
        transfer.setPower(-0.8);
        intake.setPower(-0.8);
        sleep(Math.round(seconds * 1000)); // seconds → ms
        transfer.setPower(0);
        intake.setPower(0);
    }
    public boolean emergencyStop() {
        if (gamepad2.b) {  // choose your cancel button
            shooterLeft.setPower(0);
            shooterRight.setPower(0);
            transfer.setPower(0);
            intake.setPower(0);
            return true;
        }
        return false;
    }
    public boolean safeSleep(long ms) {
        long start = System.currentTimeMillis();
        while (opModeIsActive() && System.currentTimeMillis() - start < ms) {
            if (emergencyStop()) return true;
            idle(); // VERY important
        }
        return false;
    }
    public void doomFunction() {
        doomCancelled = false; // reset cancel flag at start
        blocker.setPosition(0.5);
//aDD Servo
        shooterRight.setPower(-doomSpeed);
        shooterLeft.setPower(doomSpeed);

        // 1st Ball
        if (safeSleep(revTime / 1000.0)) return;  // revTime in ms
        if (doomCancelled) return;

        timeTransferAndIntake(0.15);
        if (safeSleep(1.0)) return;
        if (doomCancelled) return;

        // 2nd Ball
        timeTransferAndIntake(0.3);
        if (safeSleep(1.0)) return;
        if (doomCancelled) return;

        // 3rd Ball
        timeTransferAndIntake(0.3);
        if (safeSleep(1.0)) return;
        if (doomCancelled) return;

        // Stop shooter
        shooterLeft.setPower(0);
        shooterRight.setPower(0);
        blocker.setPosition(0);
    }

    public boolean safeSleep(double seconds) {
        long start = System.currentTimeMillis();
        long duration = (long)(seconds * 1000);

        while (opModeIsActive() && System.currentTimeMillis() - start < duration) {
            // Press B to cancel DOOM
            if (gamepad2.b) {
                doomCancelled = true;
                shooterLeft.setPower(0);
                shooterRight.setPower(0);
                transfer.setPower(0);
                intake.setPower(0);
                return true; // exit early
            }
            idle(); // allows other robot functions to continue
        }
        return false;
    }

    @Override
    public void runOpMode() {

        frontLeftMotor = hardwareMap.dcMotor.get("frontleft");
        backLeftMotor  = hardwareMap.dcMotor.get("backleft");
        frontRightMotor = hardwareMap.dcMotor.get("frontright");
        backRightMotor  = hardwareMap.dcMotor.get("backright");
        transfer = hardwareMap.dcMotor.get("intake");
        intake = hardwareMap.dcMotor.get("transfer");
        shooterLeft = hardwareMap.dcMotor.get("rightShooter");
        shooterRight = hardwareMap.dcMotor.get("leftShooter");
        blocker = hardwareMap.servo.get("blocker");

        // ✅ CORRECT motor directions for mecanum
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {

            /*
            IMPORTANT NOTE!!!!!
                This is the AstroBytes Scrimmage TeleOp!

                G1 Controls:
                Joysticks - Movement
                Bumpers - Move Speed Control - 100% & 85%
                A - Smart Intake
                B - Smart Outake
                Y - Vanilla Intake
                X - Vanilla Transfer

                G2 Controls:
                DPAD - Shooting Presets & Rumble
                A - FUNCTION OF DOOM!!!
                LT - Reg. Shooting
                OPTIONS - Close Range Setting for FUNCTION OF DOOM!!
                BACK - Far Range Setting for FUNCTION OF DOOM!!
                BUMPERS - BLOCKER
                Figure it out!
             */

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

            //GamePad 1 Commands

            //Movement Speed Settings - G1 - 100% and 85% Speeds
            if(gamepad1.left_bumper) moveSpeed = 1;
            if(gamepad1.right_bumper) moveSpeed = 0.85;

            //Movement - G1
            frontLeftMotor.setPower(frontLeftPower * moveSpeed);
            backLeftMotor.setPower(backLeftPower * moveSpeed);
            frontRightMotor.setPower(frontRightPower * moveSpeed);
            backRightMotor.setPower(backRightPower * moveSpeed);

            //Smart Intake - A, Reverse Smart Outtake - B
            if(gamepad1.a) timeTransferAndIntake(0.2);
            //Reverse
            if(gamepad1.b) reversetimeTransferAndIntake(0.2);

            //Vanilla Intake & Transfer
            if(gamepad1.y){
                intake.setPower(0.65);
                sleep(200);
                intake.setPower(0);
            }
            if(gamepad1.x){
                transfer.setPower(0.8);
                sleep(200);
                transfer.setPower(0);
            }

            //GamePad 2 Commands

            //Shooter Speed Settings - G2
            if(gamepad2.dpad_up){
                shootSpeed = 0.9;
                gamepad2.rumble(50);
            }
            if(gamepad2.dpad_down){
                shootSpeed = 0.65;
                gamepad2.rumble(50);
            }
            if(gamepad2.dpad_right){
                shootSpeed = 0.85;
                gamepad2.rumble(50);
            }
            if(gamepad2.dpad_left){
                shootSpeed = -0.9;
                gamepad2.rumble(50);
            }

            //Adjusting Function Rev Speed & Rev Time
            if(gamepad2.back){
                //Far Range
                doomSpeed = 0.85;
                revTime = 1500;
                gamepad2.rumble(200);
            }
            if(gamepad2.options){
                //Close Range
                doomSpeed = 0.65;
                revTime = 1500;
                gamepad2.rumble(200);
            }

            if(gamepad2.a) doomFunction();
            //B Button to Cancel FUNCTION OF DOOM!!!
            if(gamepad1.b) doomCancelled = true;

            if(gamepad2.left_bumper) blocker.setPosition(0);
            if(gamepad2.right_bumper) blocker.setPosition(0.5);


            //Shooter Code - Left Trigger
            shooterRight.setPower(-gamepad2.left_trigger*shootSpeed);
            shooterLeft.setPower(gamepad2.left_trigger*shootSpeed);


            telemetry.addLine("CONTROLS:");
            telemetry.addLine("G1: Movement, G2: Shooting");
            telemetry.addLine("Smart Intake - A - G1");
            telemetry.addLine("Smart Outake - B - G1");
            telemetry.addLine("Shooting - Left Trigger - G2");
            telemetry.addLine("Shooting Function - Y - G2");
            telemetry.addLine("Press BACK for far range DOOM Function");
            telemetry.addLine("Press OPTIONS for close range DOOM Function");
            telemetry.addData("Movement Speed:",moveSpeed);
            telemetry.addData("Shooting Speed:",shootSpeed);
            telemetry.addData("Function Rev Power",doomSpeed);
            telemetry.addData("Function Rev Time",revTime);
            telemetry.update();




        }
    }
}
