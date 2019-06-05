package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@TeleOp
public class HangingTest extends OpMode {
    private DcMotor HangingMotor;

    private double hangingPower;


    @Override
    public void init() {
        HangingMotor = hardwareMap.dcMotor.get("HangingMotor");
        HangingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addData("HangingMotor", HangingMotor.getCurrentPosition());
        telemetry.update();
    }
    @Override
    public void loop() {
        HangingMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        double hangingPosition = (HangingMotor.getCurrentPosition());
        hangingPower = gamepad1.left_trigger-gamepad1.right_trigger;
        HangingMotor.setPower(hangingPower);
        telemetry.addData("Position",hangingPosition);
        telemetry.addData("Power",hangingPower);
    }
}