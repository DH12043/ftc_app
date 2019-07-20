package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
public class CRIsilverCraterScoringAuto extends OpMode{
    private DcMotor ShoulderMotor1;
    private DcMotor RightDrive;
    private DcMotor LeftDrive;
    private DcMotor SpinnerMotor;

    private Servo SorterServo;
    private Servo ArmLock;
    private Servo MarkerServo;

    private double startTime;
    private double currentTime;
    private double startDelayTime;

    private NormalizedColorSensor colorSensorLeft;
    private NormalizedColorSensor colorSensorRight;
    private NormalizedColorSensor sampleSensor;

    private int SorterState;
    private double shoulder;
    private int ShoulderOffset;
    private int armPosition = 4;
    private boolean isArmStraightUp = false;
    private boolean isSampleGold = false;
    private boolean scoring = false;
    private boolean sweeperEngaged = true;
    private boolean ArmLocked = true;
    private boolean firstRun = true;
    private boolean firstDelayRun = true;
    private int centerArmPosition = -2150+4800;
    private int crater = 30+4800;
    private int hover = -200+4800;
    private int lander = -3100+4800;
    private int hang = -4800+4800;
    private double fastSpeed = .5;
    private double mediumSpeed = .2;
    private double slowSpeed = .05;
    private double center = .7;
    private double left = .3;
    private double right = 1;
    private double open = 0;

    static final int pi = 45123889*3;
    static final int countsPerMotorRev = 1120;
    static final int distancePerRotation = 4 * 1120 * pi;

    private int path = 0;
    private boolean maybe1 = true;
    private boolean maybe2 = true;
    private boolean rightDriveCorrect = false;
    private boolean leftDriveCorrect = false;

    float[] hsvValues = new float[3];

    private int stageCounter = 0;

    private static final double DRIVE_SPEED_FAST = 1;
    private static final double DRIVE_SPEED_SLOW = 0.25;

    private boolean rightIsYellow;
    private boolean leftIsYellow;
    private boolean isScoringSilver;
    private boolean scoringGold = false;

    public void init() {
        ShoulderMotor1 = hardwareMap.dcMotor.get("ShoulderMotor1");
        ShoulderMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addData("ShoulderMotor1", ShoulderMotor1.getCurrentPosition());
        SpinnerMotor = hardwareMap.dcMotor.get("SpinnerMotor");
        SorterServo = hardwareMap.servo.get("SorterServo");
        ArmLock = hardwareMap.servo.get("ArmLock");
        MarkerServo = hardwareMap.servo.get("MarkerServo");
        LeftDrive = hardwareMap.dcMotor.get("LeftDrive");
        RightDrive = hardwareMap.dcMotor.get("RightDrive");
        sampleSensor = hardwareMap.get(NormalizedColorSensor.class, "sampleSensor");
        colorSensorLeft = hardwareMap.get(NormalizedColorSensor.class, "colorSensorLeft");
        colorSensorRight = hardwareMap.get(NormalizedColorSensor.class, "colorSensorRight");
        LeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry.addData("LeftDrive", LeftDrive.getCurrentPosition());
        telemetry.addData("RightDrive", RightDrive.getCurrentPosition());
        SorterServo.setPosition(.5);
        MarkerServo.setPosition(.1);
        telemetry.update();

        if (colorSensorLeft instanceof SwitchableLight) {
            ((SwitchableLight)colorSensorLeft).enableLight(true);
        }

        if (colorSensorRight instanceof SwitchableLight) {
            ((SwitchableLight)colorSensorRight).enableLight(true);
        }

        if (colorSensorRight instanceof SwitchableLight) {
            ((SwitchableLight)sampleSensor).enableLight(true);
        }
    }

    public void loop() {
        NormalizedRGBA sampleColors = sampleSensor.getNormalizedColors();

        // max the values
        float max = Math.max(Math.max(Math.max(sampleColors.red, sampleColors.green), sampleColors.blue), sampleColors.alpha);
        sampleColors.red   /= max;
        sampleColors.green /= max;
        sampleColors.blue  /= max;
        int sampleColor = sampleColors.toColor();

        if (75 * (sampleColors.red + sampleColors.green - 2 * sampleColors.blue) >= 25) {
            isSampleGold = true;
        }
        telemetry.addData("goldness", 75 * (sampleColors.red + sampleColors.green - 2 * sampleColors.blue));
        NormalizedRGBA colorsLeft = colorSensorLeft.getNormalizedColors();
        NormalizedRGBA colorsRight = colorSensorRight.getNormalizedColors();

        // display the colors seen by the right color sensor
        Color.colorToHSV(colorsRight.toColor(), hsvValues);
        telemetry.addLine("right ")
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
        int colorRight = colorsRight.toColor();

        float maxLeft = Math.max(Math.max(Math.max(colorsLeft.red, colorsLeft.green), colorsLeft.blue), colorsLeft.alpha);
        colorsLeft.red   /= maxLeft;
        colorsLeft.green /= maxLeft;
        colorsLeft.blue  /= maxLeft;
        int colorLeft = colorsLeft.toColor();

        // decide whether or not the right sees yellow so we can act on this info later
        double goldnessRight = 75 * (colorsRight.red + colorsRight.green - 2 * colorsRight.blue);
        rightIsYellow = false;

        if (goldnessRight >= 25) {
            rightIsYellow = true;
        }

        telemetry.addLine("Saw yellow:  ")
                .addData(" right is yellow", rightIsYellow)
                .addData("goldness", "%.3f", goldnessRight);

        // decide
        double goldnessLeft = 75 * (colorsLeft.red + colorsLeft.green - 2 * colorsLeft.blue);
        leftIsYellow = false;

        if (goldnessLeft >= 25) {
            leftIsYellow = true;
        }

        telemetry.addLine("Saw yellow:  ")
                .addData(" left is yellow", leftIsYellow)
                .addData("goldness", "%.3f", goldnessLeft);

        currentTime = getRuntime();
        telemetry.addData("Time", currentTime - startTime);
        telemetry.addData("Current Time", currentTime);
        telemetry.addData("Start Time",startTime);
        if (firstRun == true) {
            startTime = getRuntime();
            RightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            LeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            ShoulderMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            currentTime = getRuntime();
            firstRun = false;
        }
        else {
            if (stageCounter == 0) {
                driveAtSpeed(-19, DRIVE_SPEED_FAST);
            }
            if (stageCounter == 1) {
                stopAndResetEncoder();
            }
            if (stageCounter == 2) {
                driveAtSpeed(-2, DRIVE_SPEED_SLOW);
            }
            if (stageCounter == 3) {
                stopAndResetEncoder();
            }
            if (isSampleGold && path == 0 && stageCounter == 4) {
                path = 1;
                stageCounter = 5;
            }
            else if (!isSampleGold && path == 0 && stageCounter == 4) {
                maybe1 = false;
                stageCounter = 5;
            }
            if (path == 1) {
                // if mineral is in the center
                if (stageCounter == 5) {
                    driveAtSpeed(-4, DRIVE_SPEED_FAST);
                }
                if (stageCounter == 6) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 7) {
                    shoulderPosition(centerArmPosition);
                    SpinnerMotor.setPower(-1);
                    SorterServo.setPosition(open);
                }
                if (stageCounter == 8) {
                    driveAndArm(20, DRIVE_SPEED_FAST, crater);
                }
                if (stageCounter == 9) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 10) {
                    driveAtSpeed(-18, DRIVE_SPEED_FAST);

                    if (LeftDrive.getCurrentPosition() < 800) {
                        SorterServo.setPosition(center);
                    }
                }
                if (stageCounter == 11) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 12) {
                    SorterServo.setPosition(center);
                    driveAndArm(10, DRIVE_SPEED_FAST, lander);
                }
                if (stageCounter == 13) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 14) {
                    if (!leftIsYellow && !rightIsYellow) {
                        SorterServo.setPosition(open); // check Position in old teleop
                        isScoringSilver = true;
                    }
                    else if(leftIsYellow && !rightIsYellow) {
                        SorterServo.setPosition(right); // check old teleop
                        isScoringSilver = true;
                    }
                    else if(!leftIsYellow && rightIsYellow) {
                        SorterServo.setPosition(left);
                        isScoringSilver = true;
                    }
                    else {
                        SorterServo.setPosition(center);
                    }
                    delay(1);
                }
                if (stageCounter == 15) {
                    if (leftIsYellow || rightIsYellow) {
                        scoringGold = true;
                    }
                    else {
                        scoringGold = false;
                    }
                    stageCounter++;
                }
                if (scoringGold) {
                    if (stageCounter == 16) {
                        driveOneWheel(10, 0);
                    }
                    if (stageCounter == 17) {
                        stopAndResetEncoder();
                    }
                    if (stageCounter == 18) {
                        if (leftIsYellow && rightIsYellow) {
                            SorterServo.setPosition(open); // check Position in old teleop
                            telemetry.addData("Yellow","Both");
                        }
                        else if(!leftIsYellow && rightIsYellow) {
                            SorterServo.setPosition(right); // check old teleop
                            telemetry.addData("Yellow","Right");
                        }

                        else if(leftIsYellow && !rightIsYellow) {
                            SorterServo.setPosition(left);
                            telemetry.addData("Yellow","Left");
                        }
                        else {
                            SorterServo.setPosition(center);
                            telemetry.addData("Yellow","Neither");
                        }
                        delay(1);
                    }
                    if (stageCounter == 19) {
                        driveOneWheel(-10, 0);
                    }
                    if (stageCounter == 20) {
                        stopAndResetEncoder();
                    }
                }
                else {
                    if (stageCounter == 16) {
                        stageCounter = 21;
                    }
                }
                if (stageCounter == 21) {
                    driveAndArm(9, DRIVE_SPEED_FAST, crater);
                    shoulderPosition(crater);
                    SorterServo.setPosition(open);
                }
                if (stageCounter == 22) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 23) {
                    driveAtSpeed(-14, DRIVE_SPEED_FAST);

                    if (LeftDrive.getCurrentPosition() < 600) {
                        SorterServo.setPosition(center);
                    }
                }
                if (stageCounter == 24) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 25) {
                    SorterServo.setPosition(center);
                    driveAndArm(13, DRIVE_SPEED_FAST, lander);
                }
                if (stageCounter == 26) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 27) {
                    if (!leftIsYellow && !rightIsYellow) {
                        SorterServo.setPosition(open); // check Position in old teleop
                        isScoringSilver = true;
                    }
                    else if(leftIsYellow && !rightIsYellow) {
                        SorterServo.setPosition(right); // check old teleop
                        isScoringSilver = true;
                    }
                    else if(!leftIsYellow && rightIsYellow) {
                        SorterServo.setPosition(left);
                        isScoringSilver = true;
                    }
                    else {
                        SorterServo.setPosition(center);
                    }
                    delay(1);
                }
                if (stageCounter == 28) {
                    if (leftIsYellow || rightIsYellow) {
                        scoringGold = true;
                    }
                    else {
                        scoringGold = false;
                    }
                    stageCounter++;
                }
                if (scoringGold) {
                    if (stageCounter == 29) {
                        driveOneWheel(10, 0);
                    }
                    if (stageCounter == 30) {
                        stopAndResetEncoder();
                    }
                    if (stageCounter == 31) {
                        if (leftIsYellow && rightIsYellow) {
                            SorterServo.setPosition(open); // check Position in old teleop
                            telemetry.addData("Yellow","Both");
                        }
                        else if(!leftIsYellow && rightIsYellow) {
                            SorterServo.setPosition(right); // check old teleop
                            telemetry.addData("Yellow","Right");
                        }

                        else if(leftIsYellow && !rightIsYellow) {
                            SorterServo.setPosition(left);
                            telemetry.addData("Yellow","Left");
                        }
                        else {
                            SorterServo.setPosition(center);
                            telemetry.addData("Yellow","Neither");
                        }
                        delay(1);
                    }
                    if (stageCounter == 32) {
                        driveOneWheel(-10, 0);
                    }
                    if (stageCounter == 33) {
                        stopAndResetEncoder();
                    }
                }
                else {
                    if (stageCounter == 29) {
                        stageCounter = 34;
                    }
                }
                if (stageCounter == 34) {
                    shoulderPosition(crater);
                    SorterServo.setPosition(open);
                }
                if (stageCounter == 35) {
                    driveAtSpeed(-13, DRIVE_SPEED_FAST);
                }
                if (stageCounter == 36) {
                    stopAndResetEncoder();
                }
            }
            if (path == 0) {
                if (stageCounter == 5) {
                    driveAtSpeed(6, DRIVE_SPEED_FAST);
                }
                if (stageCounter == 6) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 7) {
                    turn(-6);
                }
                if (stageCounter == 8) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 9) {
                    driveAtSpeed(-9, DRIVE_SPEED_FAST);
                }
                if (stageCounter == 10) {
                    driveAtSpeed(-11, DRIVE_SPEED_SLOW);
                }
                if (stageCounter == 11) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 12 && isSampleGold == true && path == 0) {
                    path = 2;
                    stageCounter = 13;
                }
                else if (stageCounter == 12 && !isSampleGold == true && path == 0) {
                    maybe2 = false;
                    stageCounter = 13;
                }
            }
            if (path == 2) {
                // second route
                if (stageCounter == 13) {
                    driveAndArm(-4, DRIVE_SPEED_FAST, centerArmPosition);
                }
                if (stageCounter == 14) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 15) {
                    driveAtSpeed(14, DRIVE_SPEED_FAST);
                }
                if (stageCounter == 16) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 17) {
                    turn(7);
                }
                if (stageCounter == 18) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 19) {
                    driveAndArm(6, DRIVE_SPEED_FAST, hover);
                    SpinnerMotor.setPower(-1);
                    SorterServo.setPosition(open);
                }
                if (stageCounter == 20) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 21) {
                    driveAndArm(-10, DRIVE_SPEED_FAST, crater);
                }
                if (stageCounter == 22) {
                    stopAndResetEncoder();
                    SorterServo.setPosition(center);
                }
                if (stageCounter == 23) {
                    driveAndArm(16, DRIVE_SPEED_FAST, lander);
                }
                if (stageCounter == 24) {
                    stopAndResetEncoder();
                }
            }
            if (path == 0 && maybe1 == false && maybe2 == false) {
                path = 3;
            }
            if (path == 3) {
                if (stageCounter == 13) {
                    driveAtSpeed(10, DRIVE_SPEED_FAST);
                }
                if (stageCounter == 14) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 15) {
                    turn(15);
                }
                if (stageCounter == 16) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 17) {
                    driveAndArm(-24, DRIVE_SPEED_FAST, centerArmPosition);
                    SorterServo.setPosition(open);
                }
                if (stageCounter == 18) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 19) {
                    driveAtSpeed(25, DRIVE_SPEED_FAST);
                }
                if (stageCounter == 20) {
                    stopAndResetEncoder();
                }
                if (stageCounter == 21) {
                    turn(-8);
                }
                if (stageCounter == 22) {
                    stopAndResetEncoder();
                }
                if (stageCounter== 23) {
                    driveAndArm(6, DRIVE_SPEED_FAST, hover);
                }
            }
            telemetry.addData("isSampleGold", isSampleGold);
            telemetry.addData("Path", path);
            telemetry.addData("Maybe1", maybe1);
            telemetry.addData("Maybe2", maybe2);
            telemetry.addData("StageCounter",stageCounter);
            telemetry.addData("Right",RightDrive.getCurrentPosition());
            telemetry.addData("Left",LeftDrive.getCurrentPosition());
            telemetry.update();
        }
    }
    // methods
    private void shoulderPosition(int position) {
        ShoulderMotor1.setTargetPosition(position+ShoulderOffset);
        ShoulderMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        double encoderPosition = (ShoulderMotor1.getCurrentPosition());

        if (armPosition == 0) {
            if (encoderPosition > (hover-200) && encoderPosition <= (crater+ShoulderOffset)) {
                ShoulderMotor1.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (centerArmPosition+ShoulderOffset) && encoderPosition <= (hover+ShoulderOffset)) {
                ShoulderMotor1.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor1.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 2) {
            if (encoderPosition > (hover-400+ShoulderOffset) && encoderPosition <= (hover+ShoulderOffset)) {
                ShoulderMotor1.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (centerArmPosition+ShoulderOffset) && encoderPosition <= (hover-400+ShoulderOffset)) {
                ShoulderMotor1.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor1.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 1) {
            if (encoderPosition > (lander+ShoulderOffset) && encoderPosition <= (lander+200+ShoulderOffset)) {
                ShoulderMotor1.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (lander+200+ShoulderOffset) && encoderPosition < (centerArmPosition+ShoulderOffset)) {
                ShoulderMotor1.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor1.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 3) {
            ShoulderMotor1.setPower(fastSpeed);
            telemetry.addData("Shoulder Speed","Fast");
        }
        else if (armPosition == 4) {
            ShoulderMotor1.setPower(fastSpeed);
            telemetry.addData("Shoulder Speed","Fast");
        }
        telemetry.addData("Speed", ShoulderMotor1.getPower());
        if (encoderPosition > position-7 && encoderPosition < position+7) {
            stageCounter++;
        }
    }

    private void driveAtSpeed(int distance, double power) {
        RightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int finalDriveForwardDistance = Math.round(distance * (distancePerRotation / 10000000));
        RightDrive.setTargetPosition(finalDriveForwardDistance);
        LeftDrive.setTargetPosition(-finalDriveForwardDistance);
        telemetry.addData("Total Counts Necessary", finalDriveForwardDistance);
        RightDrive.setPower(power);
        LeftDrive.setPower(power);
        int rightDrivePosition = RightDrive.getCurrentPosition();
        int leftDrivePosition = LeftDrive.getCurrentPosition();

        rightDriveCorrect = isPositionCorrect(finalDriveForwardDistance, rightDrivePosition);
        leftDriveCorrect = isPositionCorrect(-finalDriveForwardDistance, leftDrivePosition);

        if (rightDriveCorrect && leftDriveCorrect) {
            stageCounter++;
        }
    }

    private void driveAndArm(int distance, double power, int position) {
        RightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int finalDriveForwardDistance = Math.round(distance * (distancePerRotation / 10000000));
        RightDrive.setTargetPosition(finalDriveForwardDistance);
        LeftDrive.setTargetPosition(-finalDriveForwardDistance);
        telemetry.addData("Total Counts Necessary", finalDriveForwardDistance);
        RightDrive.setPower(power);
        LeftDrive.setPower(power);
        int rightDrivePosition = RightDrive.getCurrentPosition();
        int leftDrivePosition = LeftDrive.getCurrentPosition();
        rightDriveCorrect = isPositionCorrect(finalDriveForwardDistance, rightDrivePosition);
        leftDriveCorrect = isPositionCorrect(-finalDriveForwardDistance, leftDrivePosition);

        ShoulderMotor1.setTargetPosition(position+ShoulderOffset);
        ShoulderMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        double encoderPosition = (ShoulderMotor1.getCurrentPosition());

        if (armPosition == 0) {
            if (encoderPosition > (hover-200) && encoderPosition <= (crater+ShoulderOffset)) {
                ShoulderMotor1.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (centerArmPosition+ShoulderOffset) && encoderPosition <= (hover+ShoulderOffset)) {
                ShoulderMotor1.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor1.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 2) {
            if (encoderPosition > (hover-400+ShoulderOffset) && encoderPosition <= (hover+ShoulderOffset)) {
                ShoulderMotor1.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (centerArmPosition+ShoulderOffset) && encoderPosition <= (hover-400+ShoulderOffset)) {
                ShoulderMotor1.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor1.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 1) {
            if (encoderPosition > (lander+ShoulderOffset) && encoderPosition <= (lander+200+ShoulderOffset)) {
                ShoulderMotor1.setPower(slowSpeed);
                telemetry.addData("Shoulder Speed","Slow");
            }
            else if (encoderPosition > (lander+200+ShoulderOffset) && encoderPosition < (centerArmPosition+ShoulderOffset)) {
                ShoulderMotor1.setPower(mediumSpeed);
                telemetry.addData("Shoulder Speed","Medium");
            }
            else {
                ShoulderMotor1.setPower(fastSpeed);
                telemetry.addData("Shoulder Speed","Fast");
            }
        }
        else if (armPosition == 3) {
            ShoulderMotor1.setPower(fastSpeed);
            telemetry.addData("Shoulder Speed","Fast");
        }
        else if (armPosition == 4) {
            ShoulderMotor1.setPower(fastSpeed);
            telemetry.addData("Shoulder Speed","Fast");
        }
        telemetry.addData("Speed", ShoulderMotor1.getPower());

        if (rightDriveCorrect && leftDriveCorrect && (encoderPosition > position-7 && encoderPosition < position+7)) {
            stageCounter++;
        }
    }

    private void turn(int turningDistance) {
        LeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int finalTurningDistance = Math.round(turningDistance * (distancePerRotation / 10000000));
        LeftDrive.setTargetPosition(finalTurningDistance);
        RightDrive.setTargetPosition(finalTurningDistance);
        telemetry.addData("Total Counts Necessary", finalTurningDistance);
        telemetry.addData("Current Position", LeftDrive.getCurrentPosition());
        LeftDrive.setPower(1);
        RightDrive.setPower(1);
        int rightDrivePosition = RightDrive.getCurrentPosition();
        int leftDrivePosition = LeftDrive.getCurrentPosition();

        rightDriveCorrect = isPositionCorrect(finalTurningDistance, rightDrivePosition);
        leftDriveCorrect = isPositionCorrect(finalTurningDistance, leftDrivePosition);

        if (rightDriveCorrect && leftDriveCorrect) {
            stageCounter++;
        }
    }

    private boolean isPositionCorrect(int targetPosition, int currentPosition) {
        return Math.abs(targetPosition - currentPosition) <= 8;
    }
    private void stopAndResetEncoder() {
        RightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        stageCounter++;
    }
    private void delay(int delaySeconds) {
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
        LeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int finalRightDistance = Math.round(rightDistance * (distancePerRotation / 10000000));
        int finalLeftDistance = Math.round(leftDistance * (distancePerRotation / 10000000));
        LeftDrive.setTargetPosition(finalLeftDistance);
        RightDrive.setTargetPosition(finalRightDistance);
        LeftDrive.setPower(1);
        RightDrive.setPower(1);
        int rightDrivePosition = RightDrive.getCurrentPosition();
        int leftDrivePosition = LeftDrive.getCurrentPosition();

        rightDriveCorrect = isPositionCorrect(finalRightDistance, rightDrivePosition);
        leftDriveCorrect = isPositionCorrect(finalLeftDistance, leftDrivePosition);

        if (rightDriveCorrect && leftDriveCorrect) {
            stageCounter++;
        }
    }
}