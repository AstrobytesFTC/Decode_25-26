package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.acmerobotics.roadrunner.InstantFunction;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class turboShooterClass {

    // Static reference so you can call turboShooterClass.shoot()
    private static turboShooterClass instance;

    public DcMotorEx shooterRight;
    public DcMotorEx shooterLeft;
    public Servo blocker;
    public DcMotorEx transfer, intake;

    public class turboShooter implements InstantFunction{
        @Override
        public void run(){

        }
        public void shoot(int velocity){
            shooterRight.setVelocity(-velocity);
            shooterLeft.setVelocity(velocity);
        }
        public void servoUp(){
            blocker.setPosition(0.5);
        }
        public void servoDown(){
            blocker.setPosition(0.1);
        }
        public void smartFeed() throws InterruptedException {
            intake.setPower(0.8);
            transfer.setPower(0.8);

            sleep(200);

            intake.setPower(0);
            transfer.setPower(0);
        }
        public void smartIntake(){
            intake.setPower(0.8);
            transfer.setPower(0.8);
        }
        public void stopSmartIntake(){
            intake.setPower(0);
            transfer.setPower(0);
        }
    }
}
