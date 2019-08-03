package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@TeleOp
public class VoltageMonitoring extends OpMode {

    private VoltageSensor _batteryVoltage;

    public void init() {
        _batteryVoltage = hardwareMap.voltageSensor.get("Expansion Hub 2");
    }

    public void loop() {

        double voltage = _batteryVoltage.getVoltage();

        telemetry.addData("BatteryVoltage", "%.3f", voltage);
    }
}
