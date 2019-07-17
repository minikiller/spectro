package com.glyb.util;

import com.glyb.Normal;

public class TimerOne {
    public static void main(String[] args) {
        Long n;
        double[] C={} ,ca={} ;
        long[] L={};

//        '========================去噪
        double[] spect_data= Normal.getSpectralData();
        Util.My_Dwt2(spect_data, "sym15", 3, C, L);
        Util.My_Del_Nois2(C, L, "sym15", 3, ca);
    }
}
