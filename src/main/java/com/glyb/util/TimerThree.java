package com.glyb.util;

import com.glyb.Config;

import static com.glyb.Config.*;

/**
 * 定时器3的功能
 */
public class TimerThree {
    public static void main(String[] args) {
        double[] C;
        double nongdu_NO;
        double[] ca;
        long[] L;
        double[] N2_data, x;
        double[] y, xishu = new double[]{};
        double[] temp_NO, Index = new double[]{};
        String pathandname;
        double[] length_wave;
        int I=0, J=0;
        int n;
        Integer k = 0;
        double[] Nd_NO = new double[]{};
        k = k + 1;

        Nd_NO = Util.preserve(Nd_NO, k);
        for (n = 160; n <= 1800; n++) {
//            if GetInputState   DoEvents
            if ((n >= 161) && (n <= 560)) //'截取短波段数据 200个
            {
                Config.Low_spect_data[n - 161 + 1] = Config.spect_data[n] * (1 - Config.Text4 / 100);
            } else if ((n >= 1339) && (n <= 1708)) //'截取长波段数据 370个
            {
                Config.High_spect_data[n - 1339 + 1] = Config.spect_data[n];
            }
            //'' '' '' '' '' '' '' '' 计算吸光度
            for (n = 1; n <= 400; n++) {
                Low_index[n] = n;
                Low_length[I] = wave_data[I + 160];
                if (Low_spect_data[n] < Math.pow(10, -5)) {
                    Low_spect_data[n] = Low_Origin_data[n];
                }
                Low_A[n] = Math.log(Low_Origin_data[n] / Low_spect_data[n]);
            }

//            '' '' '曲线拟合
            Curve curve=new Curve();
            curve.My_curvefit(Low_index, Low_A, 7, xishu);

            //'标准差分吸光度
            for (I = 1; I <= 400; I++) {
                Config.Low_A[I] = Config.Low_A[I] - (xishu[1] * Math.pow(I, 0) + xishu[2] * Math.pow(I, 1) + xishu[3] * Math.pow(I, 2) + xishu[4] * Math.pow(I, 3) +
                        xishu[5] * Math.pow(I, 4) + xishu[6] * Math.pow(I, 5) + xishu[7] * Math.pow(I, 6) + xishu[8] * Math.pow(I, 7));
            }
            //'==================寻找吸光度峰值并定位
            Integer[] I_sig_index = new Integer[11];
            double[] I_sig = new double[11];
            for (J = 1; J <= 11; J++) {
                I_sig_index[J] = 188 + J;
                I_sig[J] = Low_A[188 + J];
            }
            //'=========排序
            My_qsort(I_sig, I_sig_index);

            //'读取标准差分吸收截面

            if (k == 1)
                //todo check "absorbsection" + Text14.Text + " _DELT_NO.txt";
                pathandname = "absorbsection" + " _DELT_NO.txt";
            else if (Nd_NO[k - 1] >= 250)
                pathandname = " absorbsection " + " 300_D ELT_NO.txt ";
            else if (250 > Nd_NO[k - 1] && Nd_NO[k - 1] >= 150)
                pathandname = " absorbsection " + " 200_D ELT_NO.txt ";
            else if (150 > Nd_NO[k - 1] && Nd_NO[k - 1] >= 75)
                pathandname = " absorbsection " + " 200_D ELT_NO.txt ";
            else if (75 > Nd_NO[k - 1] && Nd_NO[k - 1] >= 30)
                pathandname = " absorbsection " + " 100_D ELT_NO.txt ";
            else if (30 > Nd_NO[k - 1] && Nd_NO[k - 1] >= 0.5)
                pathandname = " absorbsection " + " 50_D ELT_NO.txt ";
            else if (0.5 > Nd_NO[k - 1])
                pathandname = " absorbsection " + " 10_D ELT_NO.txt ";
            else
                pathandname = " absorbsection " + " _DELT_NO.txt ";

            //Label11.Caption = pathandname

            //'====读取标准吸收截面
            //todo  my_read_file(Index, length_wave, DELT_NO, pathandname);
            n = Index.length;
            Integer[] DELT_sig_index = new Integer[11];
            double[] DELT_sig = new double[11];
            for (J = 1; J <= 11; J++) {
                DELT_sig_index[J] = 188 + J;
                DELT_sig[J] = DELT_NO[188 + J];
            }


            //'======排序
            My_qsort(DELT_sig, DELT_sig_index);
            //todo Text17.Text = DELT_sig_index(1)

            //'########################超定方程组计算浓度
            double[][] dblA = new double[400][3];
            double[][] dblAP = new double[3][400];
            double[][] dblU = new double[400][400];
            double[][] dblV = new double[3][3];
            double[] dblB = new double[400];
            double[] dblX = new double[3];

            //'系数矩阵
            for (I = DELT_sig_index[1] - 12; I <= DELT_sig_index[1] + 8; J++) { //'214.89nm
                dblA[I][1] = DELT_NO[I] * Math.pow(10, -22);
            }

            //'常数向量
            for (I = I_sig_index[1] - 12; I <= I_sig_index[1] + 8; J++) {
                dblB[I] = Low_A[I] / 30;
            }

//            '求解
            if (LEMiv(200, 1, dblA, dblB, dblX, dblAP, dblU, dblV, 5, 0.00000000001))
//            ' MsgBox "求解成功！" & Chr$(13) & Chr$(13) & _
//            "c1 = " & Round(dblX(1), 6) * 22.4 * 10 ^ 9 / (6.022 * 10 ^ 23) & Chr$(13) & _
//            "矩阵A+" & Chr$(13) & Chr$(13) & _
//             MatrixToString(3, 8, dblAP, "######0.0000000")

                Nd_NO[k] = (dblX[1] * 1) * 22.4 * Math.pow(10, 9) / (6.022 * Math.pow(10, 23)) * 2.55 + 8;
            if (Nd_NO[k] < 0)
                Nd_NO[k] = 0;
            else
                System.out.println("求解失败！");


//            '' '' 计算结果先进先出存入等长数组
            //todo     200
            if (k >= 6 + 1) {
                for (I = 1; I <= 6; I++) {
                    Nd_NO[I] = Nd_NO[I + 1];
                }
                k = 6;
            }

            //          '做平均避免显示波动过大
            if (k == 1)
                nongdu_NO = Nd_NO[1];
            else if (k >= 2 && k < 6)
                nongdu_NO = My_ave(Nd_NO);
            else
                nongdu_NO = My_mida(Nd_NO);

            if (nongdu_NO < 0.9)
                nongdu_NO = nongdu_NO - 0.5;
            if (nongdu_NO < 0.4)
                nongdu_NO = 0;
            //todo  Text10.Text = Format(nongdu_NO * 30 / 22.4, "0000.0")


        }


    }

/**   ' 函数 My_qsort(a() As Double, b() As Integer) 对输入参数a(),b()进行排序，注：以a()为排序主体，b()随a()动且a,b等长
//            ' 输入参数：a b——为输如数组；
**/
    public static void My_qsort(double[] a, Integer[] B) {
        int M, n, i, J = 0;
        M = a.length;
//        n = LBound(a())
        for (i = 1; J <= M; J++) {
            for (J = i + 1; J <= M; J++) {
                if (a[i] < a[J]) {
                    My_swap(a[i], a[J]);
                    My_swap(B[i], B[J]);
                }
           }
        }
   }

   /**' 函数 My_swap((a As Variant, b As Variant) 对输入参数a,b互换位置运算

           ' 输入参数：a b——为输如；

           '**/
   public  static void  My_swap(Object a , Object B){
       Object t=a;
       a=B;
       B=t;
   }


}
