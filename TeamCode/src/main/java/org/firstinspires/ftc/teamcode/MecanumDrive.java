// Imports

public class MecanumDrive extends OpMode {

    private DcMotor LeftFront;
    private DcMotor RightFront;
    private DcMotor LeftBack;
    private DcMotor RightBack;

    private double yAxisValue = 0;
    private double xAxisValue = 0;
    private double stickAngle = 0;
    private double stickDistance = 0;
    private double turnValue = 0;

    public void init() {
        hardwareMap.DcMotor.get(“LeftFront”);
        hardwareMap.DcMotor.get(“RightFront”);
        hardwareMap.DcMotor.get(“LeftBack”);
        hardwareMap.DcMotor.get(“RightBack”);
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
        FrontLeft.setPower(yAxisValue);
        FrontRight.setPower(xAxisValue);
        BackLeft.setPower(xAxisValue);
        BackRight.setPower(yAxisValue);
        /* for example, the point (0.2, 0.5) will slide right 2 feet for every 5 feet forward in the same time that (.3, .4) would slide right 3 feet for every 4 feet forward and the same time that (-.4, -1) would go left 4 feet and 10 feet back. */
    }

    private void turn() {
        FrontLeft.setPower(turnValue);
        FrontRight.setPower(turnValue);
        BackLeft.setPower(turnValue);
        BackRight.setPower(turnValue);
    }
}
