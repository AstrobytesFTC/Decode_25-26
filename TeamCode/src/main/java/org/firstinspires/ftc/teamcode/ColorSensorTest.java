package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Color Sensor Test")
@Disabled
public class ColorSensorTest extends LinearOpMode {

    private NormalizedColorSensor colorSensor;

    public void initSensor() {
        // Make sure "colorsensor" matches your configuration name
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorsensor");
    }

    // This method detects if the color is GREEN or PURPLE
    public String detectColor(Telemetry telemetry) {
        if (colorSensor == null) {
            telemetry.addData("Error", "Color sensor not initialized!");
            return "UNKNOWN";
        }

        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float r = colors.red;
        float g = colors.green;
        float b = colors.blue;
        float a = colors.alpha;

        // Normalize values
        if (a != 0) {
            r /= a;
            g /= a;
            b /= a;
        }

        telemetry.addData("Red", r);
        telemetry.addData("Green", g);
        telemetry.addData("Blue", b);

        // --- Detection thresholds ---
        if (g > r * 1.3 && g > b * 1.3 && g > 0.2) {
            telemetry.addData("Detected", "GREEN");
            return "GREEN";
        }

        if (r > 0.2 && b > 0.2 && g < 0.15) {
            telemetry.addData("Detected", "PURPLE");
            return "PURPLE";
        }

        telemetry.addData("Detected", "UNKNOWN");
        return "UNKNOWN";
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize sensor
        initSensor();

        telemetry.addLine("Color sensor initialized. Waiting for start...");
        telemetry.update();

        // Wait for the game to start
        waitForStart();

        // Main loop
        while (opModeIsActive()) {
            String color = detectColor(telemetry);
            telemetry.addData("Detected Color", color);
            telemetry.update();
        }
    }
}
