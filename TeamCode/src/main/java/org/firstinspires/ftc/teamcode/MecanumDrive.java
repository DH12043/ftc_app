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

public class MecanumDrive {
/*
    private DcMotor _leftFront;
    private DcMotor _rightFront;
    private DcMotor _leftBack;
    private DcMotor _rightBack;

    private double yAxisValue = 0;
    private double xAxisValue = 0;
    private double stickAngle = 0;
    private double stickDistance = 0;
    private double turnValue = 0;

    public void init() {
        _leftFront = hardwareMap.dcMotor.get("LeftFront");
        _rightFront = hardwareMap.dcMotor.get("RightFront");
        hardwareMap.dcMotor.get("LeftBack");
        hardwareMap.dcMotor.get("RightBack");
    }

    public void loop() {
        yAxisValue = -gamepad1.left_stick_y;
        xAxisValue = gamepad1.left_stick_x;
        stickAngle = Math.atan(yAxisValue/xAxisValue);
        stickDistance = Math.sqrt((yAxisValue * yAxisValue) +   (xAxisValue * xAxisValue));
        turnValue = gamepad1.right_stick_x;

        drive();
        turn();
    }

    private void drive() {
        LeftFront.setPower(yAxisValue);
        RightFront.setPower(xAxisValue);
        LeftBack.setPower(xAxisValue);
        RightBack.setPower(yAxisValue);
        // for example, the point (0.2, 0.5) will slide right 2 feet for every 5 feet forward in the same time that (.3, .4) would slide right 3 feet for every 4 feet forward and the same time that (-.4, -1) would go left 4 feet and 10 feet back.
    }

    private void turn() {
        LeftFront.setPower(turnValue);
        RightFront.setPower(turnValue);
        LeftBack.setPower(turnValue);
        RightBack.setPower(turnValue);
    }
    */
}
