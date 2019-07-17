package com.glyb.util;

public class Curve {
    double[] x, y;
    double a[][] = new double[20][20];
    static int m;
    double[] b;//'最多取20次的拟合
    int n, I, J;
    double[] xishu;
    double Xmin, Xmax;
    double Ymin, Ymax;
    double Xo, Yo;

    public void My_curvefit(double[] x, double[] y, int TimeOfFit, double[] xishu) {
        Integer Xh;
        m = TimeOfFit + 1;

        n = x.length;
        b = new double[m];
        xishu = new double[m];
        a = new double[][]{};


//        '形成方程组的各元素
        a[1][1] = n;
        for (I = 1; I <= n; I++) {
            b[1] = b[1] + y[I];
        }

        for (J = 2; J <= m; J++) {
            for (I = 1; I <= n; I++) {
                a[1][J] = a[1][J] + Math.pow(x[I], (J - 1));
            }

        }
        for (I = 2; I <= m; I++) {
            for (J = 1; J <= m; J++) {
                for (Xh = 1; J <= n; J++) {
                    a[I][J] = a[I][J] + Math.pow(x[Xh], (I + J - 2));
                    if (J == 1)
                        b[I] = b[I] + Math.pow(x[Xh], (I - 1)) * y[Xh];
                }
            }
        }


        My_fit(a, b, xishu);
    }


    public void My_fit(double[][] a, double[] b, double[] x) {
        n = b.length;
        double TempA;
        int L=0, k, kk, Ii;
        double ChuShu, Sum;

        for (I = 1; I <= n; I++) {
            L = 0;
            kk = 0;
            for (J = I; J <= n; J++) {
//            ' If GetInputState Then DoEvents
                if (a[J][I] == 0)
                    L = L + 1;
            }
            for (J = I; J <= n - L; J++) {
                if (a[J][I] == 0)
                    kk = kk + 1;
                for (k = I; k <= n; k++) {
                    // ' If GetInputState Then DoEvents
                    TempA = a[J][k];
                    a [J][k] = a[n - kk + 1][k];
                    a[n - kk + 1][k] = TempA;
                }
                TempA = b [J];
                b[J] = b[n - kk + 1];
                b[n - kk + 1] = TempA;
            }

        }





        for (Ii = I; Ii <= n - L; Ii++) {
            ChuShu = a[Ii][I];
            for (J = I; J <= n - L; J++) {
                a[Ii][J] = a[Ii][J] / ChuShu;
            }
            b[Ii] = b[Ii] / ChuShu;
        }


        for (Ii = I + 1; Ii <= n - L; Ii++) {
            for (J = I; J <= n; J++) {
                // 'If GetInputState Then DoEvents
                a[Ii][J] = a[Ii][J] - a[I][J];
            }
            b[Ii] = b[Ii] - b[I];
        }

        for (I = 1; I <= n; I++) {
            for (J = 1; J <= I - 1; J++) {
                //'If GetInputState Then DoEvents
                a[I][J] = 0;
            }
        }

        x[n] = b[n] / a[n][n];
        for (I = n - 1; I <= 1; I--) {
            Sum = 0;
            for (J = I + 1; J <= n; J++) {
                //  ' If GetInputState Then DoEvents
                Sum = Sum + a[I][J] * x[J];
            }
            x[I] = (b[I] - Sum) / a[I][I];
        }
    }


}
