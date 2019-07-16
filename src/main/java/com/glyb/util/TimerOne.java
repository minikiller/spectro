package com.glyb.util;

public class TimerOne {
    private static Long length_dnl,length_dnh;
    /**
     * 函数 My_Del_Nois2(c() As Double,l() As Double, wname As String, DIME As Integer, ca() As Double) 对输入序列dnl() As Double, dnh() As Double进行一维重构序列c
     * ' 输入参数：c()——小波分解系数序列；
     * '          l()——各层分解系数长度；
     * '          wname——小波名称；
     * '          DIME——小波分解层数。
     * ' 输出参数：ca——去噪后输出的重构序列；
     **/
    private static void My_Del_Nois2(Double[] sign_in, Long[] L, String wname, Integer DIME, Double[] ca) {
        Double[] cvl, cvh, cva, dnl, dnh;
        Double[] lp_d=new Double[]{}, hp_d=new Double[]{}, lp_r=new Double[]{}, hp_r=new Double[]{};
        Double[] lp_d_dbl, hp_d_dbl, lp_r_dbl, hp_r_dbl;
        Double[] conv_out, sign_out, C;
        Double[] Upspl_out;
        Double[] Mtrim_out;
        Double[] DeNois_out;
        Double[] var;
        Double[] Mean;
        Double[] median;
        Double[] std;
        Double[] threshold;
        Long length_sign;
        Long length_recon;
        Long length_sign_in;

        int M =sign_in.length;
        C =new Double[M];
        Util.CopyArrDbl2(C, sign_in, 1L, Long.valueOf(M * 8));

        Util.My_Getfilters(wname, lp_d, hp_d, lp_r, hp_r);                           //   '提取四种滤波器
        int length_filter = lp_r.length;
        lp_d_dbl=new Double[length_filter];
        hp_d_dbl=new Double[length_filter];
        lp_r_dbl=new Double[length_filter] ;
        hp_r_dbl=new Double[length_filter];
        for (int i = 1; i < lp_d.length; i++) {
            lp_d_dbl[i] =  lp_d[i];                    // '将四种滤波器变成double型
            hp_d_dbl[i] =  hp_d[i];
            lp_r_dbl[i] =  lp_r[i];
            hp_r_dbl[i] =  hp_r[i];
        }

        for (int kkk = 1; kkk < DIME; kkk++) {
            length_sign_in = Long.valueOf(C.length);
            if (kkk == 1) {
                length_dnl = L[2 * ((DIME - kkk) + 1)];
                length_dnh = L[2 * ((DIME - kkk) + 1) + 1];
                dnl=new Double[length_dnl.intValue()];
                dnh=new Double[length_dnh.intValue()];

                 Util.CopyArr(dnl, C, length_dnl * 8, 1L, (length_sign_in - length_dnl - length_dnh) + 1);


                Util.CopyArr(dnh, C, length_dnh * 8, 1L, (length_sign_in - length_dnh) + 1);
                C=new Double[(int) (length_sign_in - length_dnl - length_dnh)];      //'提取系数后，将输入系数数组截尾
                length_sign_in = Long.valueOf(C.length);
            }

            else{
                length_dnl = L[2 * ((DIME - kkk) + 1)];
                length_dnh = L[2 * ((DIME - kkk) + 1) + 1];
                dnh=new Double[length_dnh.intValue()];


                Util.CopyArr(dnh, C, length_dnh * 8, 1L, (length_sign_in - length_dnh) + 1);
                C=new Double[(int) (length_sign_in - length_dnl - length_dnh)];              //'提取系数后，将输入系数数组截尾
            }
        }
         
    }



}
