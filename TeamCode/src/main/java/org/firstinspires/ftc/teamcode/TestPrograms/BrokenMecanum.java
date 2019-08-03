package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.*;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
public class BrokenMecanum extends OpMode {

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
        telemetry.addData("rightStickX", gamepad1.right_stick_x);
        telemetry.addData("_leftFrontDrive", (getLeftFrontDriveValue()));
        telemetry.addData("_rightFrontDrive", (getRightFrontDriveValue()));
        telemetry.addData("_leftRearDrive", (getLeftRearDriveValue()));
        telemetry.addData("_rightRearDrive", (getRightRearDriveValue()));
        telemetry.addData("FLP", (leftFrontDrivePower()));
        telemetry.addData("FRP", (rightFrontDrivePower()));
        telemetry.addData("LRP", (leftRearDrivePower()));
        telemetry.addData("RRP", (rightRearDrivePower()));
        telemetry.addData("scaleFactor", (scaleFactor()));
    }

    private void drive() {
        _leftFrontDrive.setPower(leftFrontDrivePower());
        _rightFrontDrive.setPower(rightFrontDrivePower());
        _leftRearDrive.setPower(leftRearDrivePower());
        _rightRearDrive.setPower(rightRearDrivePower());
    }

    /*public double limit(double value) {
        return Math.max(-1.0, Math.min(value, 1.0));
    }
    */

    public double scaleFactor() {
        return Math.max
                (Math.max(Math.abs(getRightFrontDriveValue()), Math.abs(getLeftFrontDriveValue())),
                        Math.max( Math.abs(getRightRearDriveValue()), Math.abs(getLeftRearDriveValue())));
    }

    public double getLeftFrontDriveValue() {
        return(-gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x);
    }

    public double getRightFrontDriveValue() {
        return(gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x);
    }

    public double getLeftRearDriveValue() {
        return(-gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x);
    }

    public double getRightRearDriveValue() {
        return(gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x);
    }

    public double leftFrontDrivePower() {
        if (scaleFactor() > 1) {
            return(getLeftFrontDriveValue() / scaleFactor());
        }
        else {
            return (getLeftFrontDriveValue());
        }
    }

    public double rightFrontDrivePower() {
        if (scaleFactor() > 1) {
            return (getRightFrontDriveValue() / scaleFactor());
        }
        else {
            return (getRightFrontDriveValue());
        }
    }

    public double leftRearDrivePower() {
        if (scaleFactor() > 1) {
            return (getLeftRearDriveValue() / scaleFactor());
        }
        else {
            return (getLeftRearDriveValue());
        }
    }

    public double rightRearDrivePower() {
        if (scaleFactor() > 1) {
            return (getRightRearDriveValue() / scaleFactor());
        }
        else {
            return (rightRearDrivePower());
        }
    }
}