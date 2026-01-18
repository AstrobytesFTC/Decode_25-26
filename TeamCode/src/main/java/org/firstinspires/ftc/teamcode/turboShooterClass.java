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

    // Constructor
    public turboShooterClass(HardwareMap hardwareMap) {
        shooterLeft  = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotorEx.class, "shooterRight");
        blocker = hardwareMap.get(Servo.class, "blocker");
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        // Store instance
        instance = this;
    }

    // -------- Static getters so you can call static methods safely -------- //

    private static turboShooterClass get() {
        return instance;
    }

    // -------------------- ACTION METHODS -------------------- //

    public static InstantFunction shoot() {
        get().shooterLeft.setVelocity(1600);
        get().shooterRight.setVelocity(1600);
        return null;
    }

    public static void blockerUp() {
        get().blocker.setPosition(0.5);
    }

    public static void blockerDown() {
        get().blocker.setPosition(0.1);
    }

    public static void smartFeed() {
        try {
            get().intake.setPower(0.8);
            get().transfer.setPower(0.8);

            sleep(200);

            get().intake.setPower(0);
            get().transfer.setPower(0);
        } catch (InterruptedException e) {}
    }
}
