package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@Autonomous

public class MonstrositySimpleLandCraterAuto extends OpMode{
    private DcMotor ShoulderMotor;
    private DcMotor FR;
    private DcMotor FL;
    private DcMotor BR;
    private DcMotor BL;
    private DcMotor SpinnerMotor;
    private DcMotor HangingMotor;

    private Servo SorterServo;
    private Servo MarkerServo;
    private Servo RightSampleServo;
    private Servo LeftSampleServo;

    NormalizedColorSensor colorSensorLeft;
    NormalizedColorSensor colorSensorRight;
    private NormalizedColorSensor CenterSampleSensor1;
    private NormalizedColorSensor CenterSampleSensor2;
    private NormalizedColorSensor CenterSampleSensor3;
    private NormalizedColorSensor RightSampleSensor1;
    private NormalizedColorSensor RightSampleSensor2;
    private NormalizedColorSensor RightSampleSensor3;

    private double startTime;
    private double currentTime;
    private double startDelayTime;
    private double startWhileTime;

    private int SorterState;
    private double shoulder;
    private int ShoulderOffset;
    private int armPosition = 4;
    private boolean isArmStraightUp = false;
    private boolean centerIsGold = false;
    private boolean rightIsGold = false;
    private boolean leftIsGold = false;
    private boolean scoring = false;
    private boolean sweeperEngaged = true;

    private boolean firstRun = true;
    private boolean firstDelayRun = true;
    private int centerArmPosition = -3050+7200;
    private int crater = 6500;
    private int hover = -600+7200;
    private int lander = -4450+7200;
    private int hang = 0;
    private double fastSpeed = .5;
    private double mediumSpeed = .2;
    private double slowSpeed = .05;
    private double center = .72;
    private double left = .3;
    private double right = 1;
    private double open = 0;

    static final int pi = 33842916/6;
    //static final int countsPerMotorRev = 1120;
    static final int distancePerRotation = 4 * 1120 * pi;

    private int path = 0;

    private boolean FRdriveCorrect = false;
    private boolean FLdriveCorrect = false;
    private boolean BRdriveCorrect = false;
    private boolean BLdriveCorrect = false;

    float[] hsvValues = new float[3];

    private int stageCounter = 0;

    private static final double DRIVE_SPEED_FAST = .85;
    private static final double DRIVE_SPEED_SLOW = 0.35;

    private boolean rightIsYellow;
    private boolean leftIsYellow;
    private boolean isScoringSilver;
    private boolean scoringGold = false;
    private boolean redPlatniumCollected = false;

    public void init() {
        ShoulderMotor = hardwareMap.dcMotor.get("ShoulderMotor");
        ShoulderMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SpinnerMotor = hardwareMap.dcMotor.get("SpinnerMotor");
        HangingMotor = hardwareMap.dcMotor.get("HangingMotor");
        HangingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SorterServo = hardwareMap.servo.get("SorterServo");
        MarkerServo = hardwareMap.servo.get("MarkerServo");
        RightSampleServo = hardwareMap.servo.get("RightSampleServo");
        LeftSampleServo = hardwareMap.servo.get("LeftSampleServo");

        FR = hardwareMap.dcMotor.get("FR");
        FL = hardwareMap.dcMotor.get("FL");
        BR = hardwareMap.dcMotor.get("BR");
        BL = hardwareMap.dcMotor.get("BL");

        CenterSampleSensor1 = hardwareMap.get(NormalizedColorSensor.class, "CenterSampleSensor1");
        CenterSampleSensor2 = hardwareMap.get(NormalizedColorSensor.class, "CenterSampleSensor2");
        CenterSampleSensor3 = hardwareMap.get(NormalizedColorSensor.class, "CenterSampleSensor3");
        RightSampleSensor1 = hardwareMap.get(NormalizedColorSensor.class, "RightSampleSensor1");
        RightSampleSensor2 = hardwareMap.get(NormalizedColorSensor.class, "RightSampleSensor2");
        RightSampleSensor3 = hardwareMap.get(NormalizedColorSensor.class, "RightSampleSensor3");

        colorSensorLeft = hardwareMap.get(NormalizedColorSensor.class, "colorSensorLeft");
        colorSensorRight = hardwareMap.get(NormalizedColorSensor.class, "colorSensorRight");

        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        SorterServo.setPosition(center);
        MarkerServo.setPosition(.1);
        RightSampleServo.setPosition(.1);
        LeftSampleServo.setPosition(.7);

        if (colorSensorLeft instanceof SwitchableLight) {
            ((SwitchableLight)colorSensorLeft).enableLight(true);
        }
        if (colorSensorRight instanceof SwitchableLight) {
            ((SwitchableLight) colorSensorRight).enableLight(true);
        }

        stageCounter = 0;
    }

    public void loop() {
        currentTime = getRuntime();
        telemetry.addData("Time", currentTime - startTime);
        telemetry.addData("Current Time", currentTime);
        telemetry.addData("Start Time",startTime);


        if (firstRun) {
            startTime = getRuntime();
            FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            ShoulderMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            HangingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            HangingMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            currentTime = getRuntime();
            firstRun = false;

            // Old code for getting landing working
//            HangingMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            HangingMotor.setPower(-1);
//            HangingMotor.setTargetPosition(3654);
//            if (HangingMotor.getCurrentPosition() == 3654) {
//                stageCounter++;
//            }
//            else {
//                stageCounter = 0;
//            }
//            HangingMotor.setTargetPosition(-3654);
//            HangingMotor.setPower(-1);

        }
        else {
            //land
            if (stageCounter == 0) {
                HangingMotor.setTargetPosition(-9690);
                HangingMotor.setPower(-1);
                if (HangingMotor.getCurrentPosition() <= -9660) {
                    HangingMotor.setPower(0);
                    stopAndResetEncoder();
                }
                else {
                    stageCounter = 0;
                }
            }
            if (stageCounter == 1) {
                HangingMotor.setTargetPosition(0);
                HangingMotor.setPower(1);
                stageCounter++;
            }
            if (stageCounter >= 1) {
                if (HangingMotor.getCurrentPosition() >= -5) {
                    HangingMotor.setPower(0);
                }
            }
            //drive toward sample
            if (stageCounter == 2) {
                driveAtSpeed(-10.7, DRIVE_SPEED_SLOW); //-10
            }
            if (stageCounter == 3) {
                stopAndResetEncoder();
            }
            //turn 180 degrees
            if (stageCounter == 4) {
                turn(25.8); //25.8
            }
            if (stageCounter == 5) {
                stopAndResetEncoder();
            }
            // drive towards sample
            if (stageCounter == 6) {
                driveAtSpeed(20, DRIVE_SPEED_SLOW);
            }
            if (stageCounter == 7) {
                stopAndResetEncoder();
            }
            if (stageCounter == 8) {
                shoulderPosition(centerArmPosition);
                SpinnerMotor.setPower(1);
                SorterServo.setPosition(open);
            }
            if (stageCounter == 9) {
                driveAndArm(-10, DRIVE_SPEED_FAST, crater);
            }
            if (stageCounter == 10) {
                stopAndResetEncoder();
            }
            telemetry.addData("Path", path);
            telemetry.addData("StageCounter",stageCounter);
            telemetry.addData("FR",FR.getCurrentPosition());
            telemetry.addData("FL",FL.getCurrentPosition());
            telemetry.addData("BR",BR.getCurrentPosition());
            telemetry.addData("BL",BL.getCurrentPosition());
            telemetry.addData("HangingMotor",HangingMotor.getCurrentPosition());
            telemetry.addData("HangingMotor Power",HangingMotor.getPower());
            telemetry.update();
        }
    }
    // methods
    private void shoulderPosition(int position) {
        ShoulderMotor.setTargetPosition(position+ShoulderOffset);
        ShoulderMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        double encoderPosition = (ShoulderMotor.getCurrentPosition());

        if (armPosition == 0) {
            if (encoderPosition > (hover-200) && encoderPosition <= (crater+ShoulderOffset)) {
                ShoulderMotor.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (centerArmPosition+ShoulderOffset) && encoderPosition <= (hover+ShoulderOffset)) {
                ShoulderMotor.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 2) {
            if (encoderPosition > (hover-400+ShoulderOffset) && encoderPosition <= (hover+ShoulderOffset)) {
                ShoulderMotor.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (centerArmPosition+ShoulderOffset) && encoderPosition <= (hover-400+ShoulderOffset)) {
                ShoulderMotor.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 1) {
            if (encoderPosition > (lander+ShoulderOffset) && encoderPosition <= (lander+200+ShoulderOffset)) {
                ShoulderMotor.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (lander+200+ShoulderOffset) && encoderPosition < (centerArmPosition+ShoulderOffset)) {
                ShoulderMotor.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 3) {
            ShoulderMotor.setPower(fastSpeed);
            telemetry.addData("Shoulder Speed","Fast");
        }
        else if (armPosition == 4) {
            ShoulderMotor.setPower(fastSpeed);
            telemetry.addData("Shoulder Speed","Fast");
        }
        telemetry.addData("Speed", ShoulderMotor.getPower());
        if (encoderPosition > position-10 && encoderPosition < position+10) {
            stageCounter++;
        }
    }

    private void driveAtSpeed(double distance, double power) {
        //FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int finalDriveForwardDistance = (Math.round((int)(distance * (distancePerRotation / 10000000))));
        //FR.setTargetPosition(finalDriveForwardDistance);
        //FL.setTargetPosition(-finalDriveForwardDistance);
        BR.setTargetPosition(finalDriveForwardDistance);
        BL.setTargetPosition(-finalDriveForwardDistance);
        telemetry.addData("Total Counts Necessary", finalDriveForwardDistance);
        //FR.setPower(power);
        //FL.setPower(power);
        BR.setPower(power);
        BL.setPower(power);
        //int FRdrivePosition = FR.getCurrentPosition();
        //int FLdrivePosition = FL.getCurrentPosition();
        int BRdrivePosition = BR.getCurrentPosition();
        int BLdrivePosition = BL.getCurrentPosition();

        //FRdriveCorrect = isPositionCorrect(finalDriveForwardDistance, FRdrivePosition);
        //FLdriveCorrect = isPositionCorrect(-finalDriveForwardDistance, FLdrivePosition);
        BRdriveCorrect = isPositionCorrect(finalDriveForwardDistance, BRdrivePosition);
        BLdriveCorrect = isPositionCorrect(-finalDriveForwardDistance, BLdrivePosition);

        if (BRdriveCorrect && BLdriveCorrect) {
            stageCounter++;
        }
    }

    private void driveAndArm(int distance, double power, int position) {
        //FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int finalDriveForwardDistance = Math.round(distance * (distancePerRotation / 10000000));
        //FR.setTargetPosition(finalDriveForwardDistance);
        //FL.setTargetPosition(-finalDriveForwardDistance);
        BR.setTargetPosition(finalDriveForwardDistance);
        BL.setTargetPosition(-finalDriveForwardDistance);
        telemetry.addData("Total Counts Necessary", finalDriveForwardDistance);
        //FR.setPower(power);
        //FL.setPower(power);
        BR.setPower(power);
        BL.setPower(power);
        //int FRdrivePosition = FR.getCurrentPosition();
        //int FLdrivePosition = FL.getCurrentPosition();
        int BRdrivePosition = BR.getCurrentPosition();
        int BLdrivePosition = BL.getCurrentPosition();

        //FRdriveCorrect = isPositionCorrect(finalDriveForwardDistance, FRdrivePosition);
        //FLdriveCorrect = isPositionCorrect(-finalDriveForwardDistance, FLdrivePosition);
        BRdriveCorrect = isPositionCorrect(finalDriveForwardDistance, BRdrivePosition);
        BLdriveCorrect = isPositionCorrect(-finalDriveForwardDistance, BLdrivePosition);

        ShoulderMotor.setTargetPosition(position+ShoulderOffset);
        ShoulderMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        double encoderPosition = (ShoulderMotor.getCurrentPosition());

        if (armPosition == 0) {
            if (encoderPosition > (hover-200) && encoderPosition <= (crater+ShoulderOffset)) {
                ShoulderMotor.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (centerArmPosition+ShoulderOffset) && encoderPosition <= (hover+ShoulderOffset)) {
                ShoulderMotor.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 2) {
            if (encoderPosition > (hover-400+ShoulderOffset) && encoderPosition <= (hover+ShoulderOffset)) {
                ShoulderMotor.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (centerArmPosition+ShoulderOffset) && encoderPosition <= (hover-400+ShoulderOffset)) {
                ShoulderMotor.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 1) {
            if (encoderPosition > (lander+ShoulderOffset) && encoderPosition <= (lander+200+ShoulderOffset)) {
                ShoulderMotor.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (lander+200+ShoulderOffset) && encoderPosition < (centerArmPosition+ShoulderOffset)) {
                ShoulderMotor.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 3) {
            ShoulderMotor.setPower(fastSpeed);
            telemetry.addData("Shoulder Speed","Fast");
        }
        else if (armPosition == 4) {
            ShoulderMotor.setPower(fastSpeed);
            telemetry.addData("Shoulder Speed","Fast");
        }
        telemetry.addData("Speed", ShoulderMotor.getPower());

        if (BRdriveCorrect && BLdriveCorrect && (encoderPosition > position-10 && encoderPosition < position+10)) {
            stageCounter++;
        }
    }

    private void turn(double turningDistance) {
        double turnPower = .3;
        //FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int finalTurningDistance = (Math.round((int)(turningDistance * (distancePerRotation / 10000000))));
        //FR.setTargetPosition(finalTurningDistance);
        //FL.setTargetPosition(finalTurningDistance);
        BR.setTargetPosition(finalTurningDistance);
        BL.setTargetPosition(finalTurningDistance);
        telemetry.addData("Total Counts Necessary", finalTurningDistance);
        //FR.setPower(turnPower);
        //FL.setPower(turnPower);
        BR.setPower(turnPower);
        BL.setPower(turnPower);
        //int FRdrivePosition = FR.getCurrentPosition();
        //int FLdrivePosition = FL.getCurrentPosition();
        int BRdrivePosition = BR.getCurrentPosition();
        int BLdrivePosition = BL.getCurrentPosition();

        //FRdriveCorrect = isPositionCorrect(finalTurningDistance, FRdrivePosition);
        //FLdriveCorrect = isPositionCorrect(finalTurningDistance, FLdrivePosition);
        BRdriveCorrect = isPositionCorrect(finalTurningDistance, BRdrivePosition);
        BLdriveCorrect = isPositionCorrect(finalTurningDistance, BLdrivePosition);

        if (BRdriveCorrect && BLdriveCorrect) {
            stageCounter++;
        }
    }

    private boolean isPositionCorrect(int targetPosition, int currentPosition) {
        return Math.abs(targetPosition - currentPosition) <= 10;
    }
    private void stopAndResetEncoder() {
        FR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        stageCounter++;
    }
    private void delay(double delaySeconds) {
        currentTime = getRuntime();
        if (firstDelayRun) {
            startDelayTime = getRuntime();
            currentTime = getRuntime();
            firstDelayRun = false;
        }
        else if (currentTime-startDelayTime < delaySeconds) {}
        else {
            stageCounter++;
            firstDelayRun = true;
        }
    }
    private void driveOneWheel(int rightDistance, int leftDistance) {
        FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int finalRightDistance = Math.round(rightDistance * (distancePerRotation / 10000000));
        int finalLeftDistance = Math.round(leftDistance * (distancePerRotation / 10000000));
        FL.setTargetPosition(finalLeftDistance);
        BL.setTargetPosition(finalLeftDistance);
        FR.setTargetPosition(finalRightDistance);
        BR.setTargetPosition(finalRightDistance);
        FR.setPower(1);
        FL.setPower(1);
        BR.setPower(1);
        BL.setPower(1);

        int FRdrivePosition = FR.getCurrentPosition();
        int FLdrivePosition = FL.getCurrentPosition();
        int BRdrivePosition = BR.getCurrentPosition();
        int BLdrivePosition = BL.getCurrentPosition();

        FRdriveCorrect = isPositionCorrect(finalRightDistance, FRdrivePosition);
        FLdriveCorrect = isPositionCorrect(finalLeftDistance, FLdrivePosition);
        BRdriveCorrect = isPositionCorrect(finalRightDistance, BRdrivePosition);
        BLdriveCorrect = isPositionCorrect(finalLeftDistance, BLdrivePosition);

        if (FRdriveCorrect && FLdriveCorrect && BRdriveCorrect && BLdriveCorrect) {
            stageCounter++;
        }
    }
    private void checkSampleSensors() {
        NormalizedRGBA CenterSampleColors1 = CenterSampleSensor1.getNormalizedColors();
        NormalizedRGBA CenterSampleColors2 = CenterSampleSensor2.getNormalizedColors();
        NormalizedRGBA CenterSampleColors3 = CenterSampleSensor3.getNormalizedColors();

        float maxC1 = Math.max(Math.max(Math.max(CenterSampleColors1.red, CenterSampleColors1.green), CenterSampleColors1.blue), CenterSampleColors1.alpha);
        CenterSampleColors1.red   /= maxC1;
        CenterSampleColors1.green /= maxC1;
        CenterSampleColors1.blue  /= maxC1;
        if (75 * (CenterSampleColors1.red + CenterSampleColors1.green - 2 * CenterSampleColors1.blue) >= 24) {
            centerIsGold = true;
        }
        telemetry.addData("goldnessC1", 75 * (CenterSampleColors1.red + CenterSampleColors1.green - 2 * CenterSampleColors1.blue));

        float maxC2 = Math.max(Math.max(Math.max(CenterSampleColors2.red, CenterSampleColors2.green), CenterSampleColors2.blue), CenterSampleColors2.alpha);
        CenterSampleColors2.red   /= maxC2;
        CenterSampleColors2.green /= maxC2;
        CenterSampleColors2.blue  /= maxC2;
        if (75 * (CenterSampleColors2.red + CenterSampleColors2.green - 2 * CenterSampleColors2.blue) >= 24) {
            centerIsGold = true;
        }
        telemetry.addData("goldnessC2", 75 * (CenterSampleColors2.red + CenterSampleColors2.green - 2 * CenterSampleColors2.blue));

        float maxC3 = Math.max(Math.max(Math.max(CenterSampleColors3.red, CenterSampleColors3.green), CenterSampleColors3.blue), CenterSampleColors3.alpha);
        CenterSampleColors3.red   /= maxC3;
        CenterSampleColors3.green /= maxC3;
        CenterSampleColors3.blue  /= maxC3;
        if (75 * (CenterSampleColors3.red + CenterSampleColors3.green - 2 * CenterSampleColors3.blue) >= 24) {
            centerIsGold = true;
        }
        telemetry.addData("goldnessC3", 75 * (CenterSampleColors3.red + CenterSampleColors3.green - 2 * CenterSampleColors3.blue));

        NormalizedRGBA RightSampleColors1 = RightSampleSensor1.getNormalizedColors();
        NormalizedRGBA RightSampleColors2 = RightSampleSensor2.getNormalizedColors();
        NormalizedRGBA RightSampleColors3 = RightSampleSensor3.getNormalizedColors();

        float maxR1 = Math.max(Math.max(Math.max(RightSampleColors1.red, RightSampleColors1.green), RightSampleColors1.blue), RightSampleColors1.alpha);
        RightSampleColors1.red   /= maxR1;
        RightSampleColors1.green /= maxR1;
        RightSampleColors1.blue  /= maxR1;
        if (75 * (RightSampleColors1.red + RightSampleColors1.green - 2 * RightSampleColors1.blue) >= 24) {
            rightIsGold = true;
        }
        telemetry.addData("goldnessR1", 75 * (RightSampleColors1.red + RightSampleColors1.green - 2 * RightSampleColors1.blue));

        float maxR2 = Math.max(Math.max(Math.max(RightSampleColors2.red, RightSampleColors2.green), RightSampleColors2.blue), RightSampleColors2.alpha);
        RightSampleColors2.red   /= maxR2;
        RightSampleColors2.green /= maxR2;
        RightSampleColors2.blue  /= maxR2;
        if (75 * (RightSampleColors2.red + RightSampleColors2.green - 2 * RightSampleColors2.blue) >= 24) {
            rightIsGold = true;
        }
        telemetry.addData("goldnessR2", 75 * (RightSampleColors2.red + RightSampleColors2.green - 2 * RightSampleColors2.blue));

        float maxR3 = Math.max(Math.max(Math.max(RightSampleColors3.red, RightSampleColors3.green), RightSampleColors3.blue), RightSampleColors3.alpha);
        RightSampleColors3.red   /= maxR3;
        RightSampleColors3.green /= maxR3;
        RightSampleColors3.blue  /= maxR3;
        if (75 * (RightSampleColors3.red + RightSampleColors3.green - 2 * RightSampleColors3.blue) >= 24) {
            rightIsGold = true;
        }
        telemetry.addData("goldnessR3", 75 * (RightSampleColors3.red + RightSampleColors3.green - 2 * RightSampleColors3.blue));

        if (centerIsGold) {
            path = 1;
        }
        else if (rightIsGold) {
            path = 2;
        }
        if (!rightIsGold && !centerIsGold && stageCounter == 6) {
            path = 3;
        }
    }
    private void checkSorterSensors() {
        NormalizedRGBA colorsLeft = colorSensorLeft.getNormalizedColors();
        NormalizedRGBA colorsRight = colorSensorRight.getNormalizedColors();

        // display the colors seen by the right color sensor
        Color.colorToHSV(colorsRight.toColor(), hsvValues);
        telemetry.addLine("Right ")
                .addData("r", "%.3f", colorsRight.red)
                .addData("g", "%.3f", colorsRight.green)
                .addData("b", "%.3f", colorsRight.blue);

        // display the colors seen by the left sensor
        Color.colorToHSV(colorsLeft.toColor(), hsvValues);
        telemetry.addLine("Left")
                .addData("r", "%.3f", colorsLeft.red)
                .addData("g", "%.3f", colorsLeft.green)
                .addData("b", "%.3f", colorsLeft.blue);


        // max the values
        float maxRight = Math.max(Math.max(Math.max(colorsRight.red, colorsRight.green), colorsRight.blue), colorsRight.alpha);
        colorsRight.red   /= maxRight;
        colorsRight.green /= maxRight;
        colorsRight.blue  /= maxRight;

        float maxLeft = Math.max(Math.max(Math.max(colorsLeft.red, colorsLeft.green), colorsLeft.blue), colorsLeft.alpha);
        colorsLeft.red   /= maxLeft;
        colorsLeft.green /= maxLeft;
        colorsLeft.blue  /= maxLeft;

        // decide whether or not the right sees yellow so we can act on this info later
        double goldnessRight = 75 * (colorsRight.red + colorsRight.green - 2 * colorsRight.blue);
        rightIsYellow = false;

        if (goldnessRight >= 23) {
            rightIsYellow = true;
        }

        telemetry.addLine("Saw yellow:  ")
                .addData(" right is yellow", rightIsYellow)
                .addData("goldness", "%.3f", goldnessRight);

        // decide
        double goldnessLeft = 75 * (colorsLeft.red + colorsLeft.green - 2 * colorsLeft.blue);
        leftIsYellow = false;

        if (goldnessLeft >= 23) {
            leftIsYellow = true;
        }

        telemetry.addLine("Saw yellow:  ")
                .addData(" left is yellow", leftIsYellow)
                .addData("goldness", "%.3f", goldnessLeft);


        if ((colorsLeft.red > colorsLeft.blue*3.5) || (colorsRight.red > colorsRight.blue*3.5)) {
            redPlatniumCollected = true;
            telemetry.addData("WARNING", "RED PLATNIUM COLLECTED!");
        }
        else {
            redPlatniumCollected = false;
        }
    }
}