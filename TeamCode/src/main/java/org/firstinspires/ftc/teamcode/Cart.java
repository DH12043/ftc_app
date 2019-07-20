package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
@Disabled

public class Cart extends OpMode {
    private DcMotor FR;
    private DcMotor FL;
    private DcMotor BR;
    private DcMotor BL;

    private Servo RightRamp;
    private Servo LeftRamp;

    private double rightSpeed;
    private double leftSpeed;

    @Override
    public void init() {
        RightRamp = hardwareMap.servo.get("RightRamp");
        LeftRamp = hardwareMap.servo.get("LeftRamp");
        FR = hardwareMap.dcMotor.get("FR");
        FL = hardwareMap.dcMotor.get("FL");
        BR = hardwareMap.dcMotor.get("BR");
        BL = hardwareMap.dcMotor.get("BL");
        RightRamp.setPosition(1);
        LeftRamp.setPosition(0);
    }

    @Override
    public void loop() {
        double rightSpeed = -gamepad1.right_stick_y;
        FR.setPower(rightSpeed);
        BR.setPower(rightSpeed);
        double leftSpeed = gamepad1.left_stick_y;
        FL.setPower(leftSpeed);
        BL.setPower(leftSpeed);

        if (gamepad1.left_bumper) {
            RightRamp.setPosition(1);
            LeftRamp.setPosition(0);
        }
        else if (gamepad1.right_bumper) {
            RightRamp.setPosition(.4);
            LeftRamp.setPosition(.6);
        }
    }
}