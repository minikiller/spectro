package com.glyb;

public class Config {
    public static long wrapper_handle;
    public static long integrationTime ;
    public static double[] Index=new double[2048];
    public static double[] Origin_data=new double[2048];
    public static double[] spect_data=new double[2048];
    public static double[] Low_Origin_data=new double[400];
    public static double[] High_Origin_data=new double[370];
    public static double[] Low_spect_data=new double[400];
    public static double[] High_spect_data=new double[370];
    public static double[] wave_data=new double[2048];
    public static double[] Dark_data=new double[2048];
    public static long spectrumArrayHandle ;
    public static long spectrometerIndex ;
    public static boolean b_pool ;
    public static long[] DarkNoise=new long[150];
    public static long[] Singal=new long[150];
    public static Integer j1 =0;


    public static double NO  ;
    public static double SO2  ;
    public static double SignalNO ;
    public static double SignalSO2  ;
    public static double DarkNoiseNO  ;
    public static double DarkNoiseSO2  ;
    public static double TNO  ;
    public static double TSO2  ;
    public static double[] Signal_226_1 =new double[20], Signal_226_2  =new double[20];
    public static double[] Signal_286_1 =new double[20] , Signal_286_2 =new double[20] ;

    public static  double Ratio226, Ratio286  ;
    public static double Signal_226, Signal_286  ;

    public static double TSO2toNO  ;
    public static String AString  ; // '================串口发送的指令
    public static boolean bTesting ;
    public static boolean bContinue  ;
    public static boolean bTime  ;
    public static double timeinterval  ;
    public static double timecontinue   ;
    public static String DocFileName ;
    public static Integer numberOfSpectrometers  ;
    public static boolean BTestOK  ;
    public static double[] Low_A=new double[400];//    '低波段的吸光度
    public static double[] High_A=new double[370];
    public static double[] Low_index=new double[400];//     '低波段的波长索引
    public static long[] High_index=new long[370];
    public static double[] Low_length=new double[400];//   '低波段的波长
    public static double[] High_length=new double[370];
    public static double[] length_wave ;
    public static double[] N2_data ;
    public static double[] xishu ;
    public static String pathandname ;
    public static double[] DELT_SO2 ;
    public static double[] DELT_NO ;
 
    public static double[] adjust_x_so2=new double[5];   //'校正so2用  显示结果
    public static double[] adjust_y_so2 =new double[5];  //'校正so2用  实际值
    public static double[] adjust_x_no =new double[5] ;  // '校正no用  显示结果
    public static double[] adjust_y_no =new double[5];   //'校正no用  实际值


    public static double Text4;
}
