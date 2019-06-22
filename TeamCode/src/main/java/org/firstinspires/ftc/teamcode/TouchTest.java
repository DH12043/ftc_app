package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp
public class TouchTest extends OpMode {
    TouchSensor touchSensor;
    TouchSensor touchSensor2;
    // Use Digital Ports - Blue is Even, White is Odd, Black is Common and must connenct to both, Red is unused
    // For REV, use odd ports only
    @Override
    public void init() {
        touchSensor = hardwareMap.touchSensor.get("touchSensor");
        touchSensor2 = hardwareMap.touchSensor.get("touchSensor2");
    }
    public void loop() {
        if (touchSensor.isPressed()) {
            telemetry.addData("touchSensor", "Is Pressed");
        } else {
            telemetry.addData("touchSensor", "Is Not Pressed");
        }
        telemetry.addData("Value", touchSensor.getValue());

        if (touchSensor2.isPressed()) {
            telemetry.addData("touchSensor2", "Is Pressed");
        } else {
            telemetry.addData("touchSensor2", "Is Not Pressed");
        }
        telemetry.addData("Value", touchSensor2.getValue());

        telemetry.update();
    }

}
