package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@TeleOp
public class Q2VelocityTeleOp extends LinearOpMode {

    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;
    DcMotor transfer;
    DcMotor intake;
    DcMotorEx shooterRight;
    DcMotorEx shooterLeft;
    Servo blocker;

    double moveSpeed = 0.65;
    double shootSpeed = 0.9;

    // DOOM settings
    double doomVelocity = 1900;
    double doomSpeed = 0.9;
    int revTime = 2500;

    double waitTime = 1000;

    double transferTime = 0.5;

    // DOOM state machine
    boolean doomActive = false;
    int doomStep = 0;
    long doomTimer = 0;

    // Unified shooter control
//    double shooterVelocity = 0;

    // ================= HELPERS =================

    public void reversetimeTransferAndIntake(double seconds) {
        transfer.setPower(-0.8);
        intake.setPower(-0.8);
        sleep((long)(seconds * 1000));
        transfer.setPower(0);
        intake.setPower(0);
    }

    public boolean safeSleep(double seconds) {
        long start = System.currentTimeMillis();
        long duration = (long)(seconds * 1000);
        while (opModeIsActive() && System.currentTimeMillis() - start < duration) {
            if (gamepad2.b) {  // emergency stop
                doomActive = false;
                shooterLeft.setVelocity(0);
                shooterRight.setVelocity(0);
                transfer.setPower(0);
                intake.setPower(0);
                return true;
            }
            idle();
        }
        return false;
    }

    // ================= OPMODE =================

    @Override
    public void runOpMode() {

        // ===== HARDWARE MAP (UNCHANGED) =====
        frontLeftMotor = hardwareMap.dcMotor.get("frontleft");
        backLeftMotor  = hardwareMap.dcMotor.get("backleft");
        frontRightMotor = hardwareMap.dcMotor.get("frontright");
        backRightMotor  = hardwareMap.dcMotor.get("backright");
        transfer = hardwareMap.dcMotor.get("intake");
        intake = hardwareMap.dcMotor.get("transfer");
        shooterRight = hardwareMap.get(DcMotorEx.class, "rightShooter");
        shooterLeft  = hardwareMap.get(DcMotorEx.class, "leftShooter");
        blocker = hardwareMap.servo.get("blocker");

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);




        waitForStart();

        while (opModeIsActive()) {

            // ================= DRIVE =================
            double y  = -gamepad1.left_stick_y;
            double x  = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            frontLeftMotor.setPower((y + x + rx) / denominator * moveSpeed);
            backLeftMotor.setPower((y - x + rx) / denominator * moveSpeed);
            frontRightMotor.setPower((y - x - rx) / denominator * moveSpeed);
            backRightMotor.setPower((y + x - rx) / denominator * moveSpeed);

            if (gamepad1.left_bumper) moveSpeed = 0.35;
            if (gamepad1.right_bumper) moveSpeed = 0.85;

            // ================= SMART INTAKE =================
            if (gamepad1.a) {
                transfer.setPower(0.6);
                intake.setPower(0.75);
                shooterRight.setVelocity(-1450);
                shooterLeft.setVelocity(1450);

                sleep(300);

                intake.setPower(0);
                transfer.setPower(0);
                shooterRight.setVelocity(0);
                shooterLeft.setVelocity(0);

                // ================= SMART OUTAKE =================
            } else if (gamepad1.b) {

                reversetimeTransferAndIntake(0.2);
            }

            // ============INVERSE TRANSFER===============
            if(gamepad2.y){
                transfer.setPower(-0.3);
                intake.setPower(0.7);
                sleep(250);
                transfer.setPower(0);
                intake.setPower(0);
            }

            // ================= DOOM SETTINGS =================
            if (gamepad2.dpad_up) {
                doomVelocity = 1980;//85 Shoot from Far
                gamepad2.rumble(200);
                revTime = 2500;
                waitTime = 1250;
                transferTime = 0.3;
            }
            if (gamepad2.dpad_down) {
                doomVelocity = 1863;//80 Shoot from Far
                gamepad2.rumble(200);
                revTime = 2500;
                waitTime = 1500;
                transferTime = 0.3;
            }
            if (gamepad2.dpad_left) {
                doomVelocity = 1760;// 65 Shoot from Close
                gamepad2.rumble(200);
                revTime = 2500;
                waitTime = 1250;
                transferTime = 0.3;
            }
            if (gamepad2.dpad_right) {
                doomVelocity = 1630;//70 Shoot from Close
                gamepad2.rumble(200);
                revTime = 2500;
                waitTime = 1250;
                transferTime = 0.3;
            }

            // ================= DOOM START =================
            if (gamepad2.a && !doomActive) {
                doomActive = true;
                doomStep = 0;
                doomTimer = System.currentTimeMillis();
               // shooterVelocity = doomVelocity;
                telemetry.addLine("DOOM START");
                telemetry.update();
            }

            // ================= DOOM CANCEL =================
            if (gamepad2.b) {
                doomActive = false;
                doomStep = 0;
                doomVelocity = 0;
                shooterRight.setVelocity(0);
                shooterLeft.setVelocity(0);
                transfer.setPower(0);
                intake.setPower(0);
                telemetry.addLine("DOOM CANCELED");
                telemetry.update();
            }

            // ================= DOOM STATE MACHINE =================
            if (doomActive) {
                long now = System.currentTimeMillis();
                double currentVelocity = shooterLeft.getVelocity();

                switch (doomStep) {

                    case 0: //reverse transfer and reg intake for 1.5 secs and start the REV
                        telemetry.addLine("DOOM Reverse IntakeTrans");
                        telemetry.update();

                        shooterRight.setVelocity(1200);
                        shooterLeft.setVelocity(-1200);
                        transfer.setPower(-0.3);
                        intake.setPower(0.3);


                        blocker.setPosition(0.1);
                        if (safeSleep(0.5)) return;  // 500ms
                        blocker.setPosition(0.5);
                        if (safeSleep(0.25)) return;  // 500ms

//                        shooterLeft.setPower(0);
//                        shooterRight.setPower(0);
                        shooterRight.setVelocity(0);
                        shooterLeft.setVelocity(0);
                        transfer.setPower(0);
                        intake.setPower(0);

                        telemetry.addLine(" DOOM - Start REV");
                        telemetry.update();
                        shooterRight.setVelocity(-doomVelocity);
                        shooterLeft.setVelocity(doomVelocity);
                        intake.setPower(0.15);
//                        doomTimer = System.currentTimeMillis();
                        doomStep++; // go to normal rev step next
                        break;

                    case 1: // Shoot 1
                            if(Math.abs(currentVelocity - doomVelocity) < 50) {
                           // if (System.currentTimeMillis() - doomTimer >= revTime) {
                                telemetry.addLine(" Doom SHOOT 1");
                                telemetry.addData("Right Shooter Velocity",shooterRight.getVelocity());
                                telemetry.addData("Left Shooter Velocity",shooterLeft.getVelocity());
                                telemetry.update();
                                transfer.setPower(0.65);
                                //intake.setPower(0.8);
                                // new code
                                if (safeSleep(transferTime-.1)) return;
                                transfer.setPower(0);
                                doomTimer = System.currentTimeMillis();
                                doomStep++;
                            }
                        break;

                    /*case 2: // stop

                        if (System.currentTimeMillis() - doomTimer >= 100) {
                            telemetry.addLine("DOOM STEP 2 - stop transfer");
                            telemetry.update();
                            transfer.setPower(0);
                            //intake.setPower(0);
                            doomTimer = System.currentTimeMillis();
                            doomStep++;
                        }
                        break;
*/
                    case 2: // Shoot 2
                        if(Math.abs(currentVelocity - doomVelocity) < 50){
//                        if (System.currentTimeMillis() - doomTimer >= waitTime) {
                            telemetry.addLine("DOOM - Shoot 2");
                            //telemetry.addData("rightShooter Tpr",shooterRight.getCurrentPosition());
                            //telemetry.addData("leftShooter Tpr",shooterLeft.getCurrentPosition());
                            telemetry.addData("Right Shooter Velocity",shooterRight.getVelocity());
                            telemetry.addData("Left Shooter Velocity",shooterLeft.getVelocity());
                            telemetry.update();
                            transfer.setPower(0.65);
                            if (safeSleep(transferTime)) return;
                            transfer.setPower(0);
                            //intake.setPower(0.8);
                            doomTimer = System.currentTimeMillis();
                            doomStep++;
                        }
                        break;

/*                    case 4: // stop
                        if (System.currentTimeMillis() - doomTimer >= 100) {
                            telemetry.addLine("DOOM STEP 4 - STOP");
                            telemetry.update();
                            transfer.setPower(0);
                            //intake.setPower(0);
                            doomTimer = System.currentTimeMillis();
                            doomStep++;
                        }
                        break;
*/
                    case 3: // SHOOT 3
                        if(Math.abs(currentVelocity - doomVelocity) < 50){
//                        if (System.currentTimeMillis() - doomTimer >= waitTime) {
                            telemetry.addLine("DOOM - SHOOT 3");
                            //telemetry.addData("rightShooter Tpr",shooterRight.getCurrentPosition());
                            //telemetry.addData("leftShooter Tpr",shooterLeft.getCurrentPosition());
                            telemetry.addData("Right Shooter Velocity",shooterRight.getVelocity());
                            telemetry.addData("Left Shooter Velocity",shooterLeft.getVelocity());
                            telemetry.update();
                            transfer.setPower(0.7);
                            intake.setPower(0.7);
                            if (safeSleep(transferTime+.1)) return;
                            transfer.setPower(0);
//                            doomTimer = System.currentTimeMillis();
                            doomStep++;
                        }
                        break;

/*                    case 6: // stop
                        if (System.currentTimeMillis() - doomTimer >= 100) {
                            telemetry.addLine("DOOM STEP 6 - STOP ");
                            telemetry.update();
                            transfer.setPower(0);
                            intake.setPower(0);
                            doomTimer = System.currentTimeMillis();
                            doomStep++;
                        }
                        break;
*/
                    case 4: // finish
                        telemetry.addLine("DOOM FINISHED");
                        telemetry.update();
                        doomVelocity = 0;
                        shooterRight.setVelocity(0);
                        shooterLeft.setVelocity(0);
                        intake.setPower(0);
                        doomActive = false;
                        doomStep = -1; // reset for next activation
                        break;
                }



            }

            // ================= MANUAL SHOOTING =================
//            if (!doomActive) {
//                shooterPower = gamepad2.left_trigger * shootSpeed;
//            }
            //shooterRight.setPower(-gamepad2.left_trigger*0.7);
            //shooterLeft.setPower(gamepad2.left_trigger*0.7);



            // ================= DOOM FUNCTION SHOOT CONTROL(DO NOT TOUCH) =================
            if(doomStep == 1){
                //Safe Measure insuring rev on step 1
                shooterRight.setVelocity(doomVelocity);
                shooterLeft.setVelocity(-doomVelocity);
            }


            // ================= BLOCKER =================
            if (gamepad2.left_bumper) blocker.setPosition(0.2);
            if (gamepad2.right_bumper) blocker.setPosition(0.5);

            telemetry.addData("DOOM Active", doomActive);
            telemetry.addData("DOOM Step", doomStep);
            telemetry.addData("Doom Velocity", doomVelocity);
//            telemetry.addData("Shooter Velocity", shooterVelocity);
            telemetry.addData("wait time", waitTime);
            telemetry.addData("Transfer Time",transferTime);
            //Pos
            telemetry.addData("current Velocity",shooterLeft.getVelocity());

            telemetry.update();
        }
    }
}
