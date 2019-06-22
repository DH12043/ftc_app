package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class IRDistance extends OpMode {

    private DistanceSensor sensorRange;

    public void init() {
        hardwareMap.get(DistanceSensor.class, "sensor_range");
    }
    @Override
    public void loop() {
        telemetry.addData("range", String.format("%0.1+f mm", sensorRange.getDistance(DistanceUnit.MM)));
    }
}
