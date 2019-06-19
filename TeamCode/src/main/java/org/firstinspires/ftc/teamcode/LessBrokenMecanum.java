package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.*;

@TeleOp
public class LessBrokenMecanum extends OpMode {

    private DcMotor _leftFrontDrive;
    private DcMotor _rightFrontDrive;
    private DcMotor _leftRearDrive;
    private DcMotor _rightRearDrive;

    @Override
    public void init() {
        _leftFrontDrive = hardwareMap.dcMotor.get("LeftFront");
        _rightFrontDrive = hardwareMap.dcMotor.get("RightFront");
        _leftRearDrive = hardwareMap.dcMotor.get("LeftBack");
        _rightRearDrive = hardwareMap.dcMotor.get("RightBack");
    }
    @Override
    public void loop() {
        drive();

        telemetry.addData("leftStickX", gamepad1.left_stick_x);
        telemetry.addData("leftStickY", gamepad1.left_stick_y);
        telemetry.addData("_leftFrontDrive", (limit(gamepad1.left_stick_x - gamepad1.left_stick_y)));
        telemetry.addData("_rightFrontDrive", (limit(gamepad1.left_stick_x + gamepad1.left_stick_y)));
        telemetry.addData("_leftRearDrive", (limit(-gamepad1.left_stick_x - gamepad1.left_stick_y)));
        telemetry.addData("_rightRearDrive", (limit(-gamepad1.left_stick_x + gamepad1.left_stick_y)));
    }

    private void drive() {
        _leftFrontDrive.setPower(limit(gamepad1.left_stick_x - gamepad1.left_stick_y));
        _rightFrontDrive.setPower(limit(gamepad1.left_stick_x + gamepad1.left_stick_y));
        _leftRearDrive.setPower(limit(-gamepad1.left_stick_x - gamepad1.left_stick_y));
        _rightRearDrive.setPower(limit(-gamepad1.left_stick_x + gamepad1.left_stick_y));
    }

    public double limit(double value) {
        return Math.max(-1.0, Math.min(value, 1.0));
    }
}