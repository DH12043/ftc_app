package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

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
    public void loop(){

    }
}
