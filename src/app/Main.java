package app;


public class Main {

	public static void main(String[] args) {
		Animation animation = null;
//      uncomment the option that you wish to execute.
		//animation = new _1_BasicAnimation(); // modify class Animation.java to demonstrate a less jumpy animation
		//animation = new _2_SmootherAnimationSeveralWindows();
		//animation = new _3_MultipleCirclesAnimation();
		//animation = new _4_TemporalFunctionDemo();
		//animation = new _5_ClockAnimation();
		animation = new _6_YourOwnAnimation();
		animation.start();
		//int[][] M1 = new M1[][] = {{-s1*Math.sin(alpha), 1, 0, _0_Constants.WINDOW_WIDTH},
		//		{-s1 * Math.cos(alpha), 0, -1, _0_Constants.WINDOW_HEIGHT}};
		//printMatrix(matrix);
	}
	/*public static void main(String[] args) {

		int[][] matrix = { { 3, 2, 1, 7 },
				{ 9, 11, 5, 4 },
				{ 6, 0, 13, 17 },
				{ 7, 21, 14, 15 } };
		printMatrix(matrix);*/
	}


