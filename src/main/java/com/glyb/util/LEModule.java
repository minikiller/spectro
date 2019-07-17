package com.glyb.util;

public class LEModule {
    /**
     * '  模块名：LEModule.bas
     * '  函数名：LEMiv
     * '  功能：  用广义逆法求解线性最小二乘问题方程组
     * '  参数：   m    - Integer型变量。系数矩阵的行数， m>=n
     * '           n    - Integer型变量。系数矩阵的列数，n<=m
     * '          dblA  - Double型二维数组，体积维n x n。存放超定方程组系数矩阵；
     * '                  返回时，其对角线存放矩阵的奇异值，其余元素为0。
     * '          dblB  - Double型一维数组，长度为m。存放超定方程组右端常数向量
     * '          dblX  - Double型一维数组，长度为n。返回时，存放超定方程组的最小二乘解。
     * '          dblAP - Double型二维数组，体积维n x m。返回时，存放超定方程组系数矩阵A的广义逆A+。
     * '          dblU  - Double型二维数组，体积维m x m。返回时，存放超定方程组系数矩阵A的奇异值分解式中的
     * '                  左奇异向量U。
     * '          dblV  - Double型二维数组，体积维n x n。返回时，存放超定方程组系数矩阵A的奇异值分解式中的
     * '                  右奇异向量VT。
     * '           ka  - Integer型变量。ka=max(m,n)+1
     * '          eps  - Double型变量。奇异值分解函数中的控制精度参数。
     * '  返回值： Boolean型。False，失败无解；True, 成功
     **/
    public boolean LEMiv(int m, int n, double[][] dblA, double[] dblB, double[] dblX, double[][] dblAP,
                         double[][] dblU, double[][] dblV, int ka, double eps) {
        // ' 局部变量
        int I, J;

        if (!MInv(m, n, dblA, dblAP, dblU, dblV, ka, eps)) {
            return false;
        }

        for (I = 1; I <= n; I++) {
            dblX[I] = 0;
            for (J = 1; J <= m; J++) {
                dblX[I] = dblX[I] + dblAP[I][J] * dblB[J];
            }
        }
        return true;
    }

    /**
     * ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
     * '  模块名：MatrixModule.bas
     * '  函数名：MUav
     * '  功能：  用豪斯荷尔德变换及变形QR算法对矩阵进行奇异值分解
     * '  参数：   m    - Integer型变量。系数矩阵的行数， m>=n
     * '           n    - Integer型变量。系数矩阵的列数，n<=m
     * '          dblA  - Double型二维数组，体积维m x n。存放待分解矩阵；
     * '                  返回时，其对角线存放矩阵的奇异值(以非递增次序排列)，其余元素为0。
     * '          dblU  - Double型二维数组，体积维m x m。返回时，存放奇异值分解式中的左奇异向量U。
     * '          dblV  - Double型二维数组，体积维n x n。返回时，存放奇异值分解式中的右奇异向量VT。
     * '           ka  - Integer型变量。ka=max(m,n)+1
     * '          eps  - Double型变量。奇异值分解函数中的控制精度参数。
     * '  返回值： Boolean型。False，失败无解；True, 成功
     * ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
     **/
    public boolean MUav(int m, int n, double[][] dblA, double[][] dblU, double[][] dblV, int ka, double eps) {
//        ' 局部变量
        int I, J, k, L, it;
        int ll, kk, mm, nn, m1, ks;
        double d, dd, t, sm, sm1, em1, sk, ek;
        double b, C, shh;
        double[] fg = new double[2];
        double[] cs = new double[2];

        double[] s = new double[ka];
        double[] e = new double[ka];
        double[] w = new double[ka];

        it = 60;
        k = n;

        if (m - 1 < n) k = m - 1;

        L = m;
        if (n - 2 < m) L = n - 2;

        if (L < 0) L = 0;

        ll = k;

        if (L > k) ll = L;

        if (ll >= 1) {
            for (kk = 1; kk <= ll; kk++) {
                if (kk <= k) {
                    d = 0;
                    for (I = kk; I <= m; I++) {
                        d = d + dblA[I][kk] * dblA[I][kk];
                    }
                    s[kk] = Math.sqrt(d);
                    if (s[kk] != 0) {
                        if (dblA[kk][kk] != 0) {
                            s[kk] = Math.abs(s[kk]);
                            if (dblA[kk][kk] < 0)
                                s[kk] = -s[kk];
                        }
                        for (I = kk; I <= m; I++) {
                            dblA[I][kk] = dblA[I][kk] / s[kk];
                        }
                        dblA[kk][kk] = 1 + dblA[kk][kk];
                    }
                    s[kk] = -s[kk];
                }
                if (n >= kk + 1) {
                    for (J = kk; J <= n; J++) {
                        if ((kk <= k) && (s[kk] != 0)) {
                            d = 0;
                            for (I = kk; I <= m; I++) {
                                d = d + dblA[I][kk] * dblA[I][J];
                            }
                            d = -d / dblA[kk][kk];
                            for (I = kk; I <= m; I++) {
                                dblA[I][J] = dblA[I][J] + d * dblA[I][kk];
                            }
                        }
                        e[J] = dblA[kk][J];
                    }
                }
                if (kk <= k) {
                    for (I = kk; I <= m; I++)
                        dblU[I][kk] = dblA[I][kk];
                }

                if (kk <= L) {
                    d = 0;
                    for (I = kk + 1; I <= n; I++)
                        d = d + e[I] * e[I];
                    e[kk] = Math.sqrt(d);
                    if (e[kk] != 0) {
                        if (e[kk + 1] != 0) {
                            e[kk] = Math.abs(e[kk]);
                            if (e[kk + 1] < 0)
                                e[kk] = -e[kk];
                        }
                        for (I = kk + 1; I <= n; I++)
                            e[I] = e[I] / e[kk];
                        e[kk + 1] = 1 + e[kk + 1];
                    }
                    e[kk] = -e[kk];
                    if ((kk + 1 <= m) && (e[kk] != 0)) {
                        for (I = kk + 1; I <= m; I++)
                            w[I] = 0;
                        for (J = kk + 1; J <= n; J++)
                            for (I = kk + 1; I <= m; I++)
                                w[I] = w[I] + e[J] * dblA[I][J];
                        for (J = kk + 1; J <= n; J++)
                            for (I = kk + 1; I <= m; I++)
                                dblA[I][J] = dblA[I][J] - w[I] * e[J] / e[kk + 1];
                    }
                    for (I = kk + 1; I <= n; I++)
                        dblV[I][kk] = e[I];
                }


            }
        }

        mm = n;
        if (m + 1 < n) mm = m + 1;
        if (k < n) s[k + 1] = dblA[k + 1][k + 1];
        if (m < mm) s[mm] = 0;
        if (L + 1 < mm) e[L + 1] = dblA[L + 1][mm];

        e[mm] = 0;
        nn = m;
        if (m > n) nn = n;
        if (nn >= k + 1) {
            for (J = k + 1; J <= nn; J++) {
                for (I = 1; I < m; I++) {
                    dblU[I][J] = 0;
                }
                dblU[J][J] = 1;
            }
        }
        if (k >= 1) {
            for (ll = 1; ll <= k; ll++) {
                kk = k - ll + 1;
                if ((s[kk] != 0)) {
                    if (nn >= kk + 1) {
                        for (J = kk + 1; J <= nn; J++) {
                            d = 0;
                            for (I = kk; I <= m; I++)
                                d = d + dblU[I][kk] * dblU[I][J] / dblU[kk][kk];

                            d = -d;
                            for (I = kk; I <= m; I++)
                                dblU[I][J] = dblU[I][J] + d * dblU[I][kk];
                        }
                    }
                    for (I = kk; I <= m; I++)
                        dblU[I][kk] = -dblU[I][kk];
                    dblU[kk][kk] = 1 + dblU[kk][kk];
                    if (kk - 1 >= 1) {
                        for (I = 1; I <= kk - 1; I++)
                            dblU[I][kk] = 0;
                    }
                } else {
                    for (I = 1; I <= m; I++) {
                        dblU[I][kk] = 0;
                        dblU[kk][kk] = 1;
                    }
                }
            }
        }
        for (ll = 1; ll <= n; ll++) {
            kk = n - ll + 1;
            if ((kk <= L) && (e[kk] != 0)) {
                for (J = kk + 1; J <= n; J++) {
                    d = 0;
                    for (I = kk + 1; I <= n; I++)
                        d = d + dblV[I][kk] * dblV[I][J] / dblV[kk + 1][kk];
                    d = -d;
                    for (I = kk + 1; I <= n; I++)
                        dblV[I][J] = dblV[I][J] + d * dblV[I][kk];

                }
            }
            for (I = 1; I <= n; I++)
                dblV[I][kk] = 0;
            dblV[kk][kk] = 1;
        }

        for (I = 1; I <= m; I++) {
            for (J = 1; J <= n; J++) {
                dblA[I][J] = 0;
            }
        }
        m1 = mm;
        it = 60;

        while (true) {
            if (mm == 0) {
                Cal1(dblA, e, s, dblV, m, n);
                return true;
            }
            if (it == 0) {
                Cal1(dblA, e, s, dblV, m, n);
                return false;
            }
            kk = mm - 1;
            while ((kk != 0) && (Math.abs(e[kk]) != 0)) {
                d = Math.abs(s[kk]) + Math.abs(s[kk + 1]);
                dd = Math.abs(e[kk]);
                if (dd > eps * d)
                    kk = kk - 1;
                else
                    e[kk] = 0;
            }
            if (kk == mm - 1) {
                kk = kk + 1;
                if (s[kk] < 0) {
                    s[kk] = -s[kk];
                    for (I = 1; I <= n; I++)
                        dblV[I][kk] = -dblV[I][kk];
                }
                while ((kk != m1) && (s[kk] < s[kk + 1])) {
                    d = s[kk];
                    s[kk] = s[kk + 1];
                    s[kk + 1] = d;
                    if (kk < n) {
                        for (I = 1; I <= n; I++) {
                            d = dblV[I][kk];
                            dblV[I][kk] = dblV[I][kk + 1];
                            dblV[I][kk + 1] = d;
                        }
                    }
                    if (kk < m) {
                        for (I = 1; I <= m; I++) {
                            d = dblU[I][kk];
                            dblU[I][kk] = dblU[I][kk + 1];
                            dblU[I][kk + 1] = d;
                        }
                    }
                    kk = kk + 1;
                }
                it = 60;
                mm = mm - 1;
            } else {
                ks = mm;
                while ((ks > kk) && (Math.abs(s[ks]) != 0)) {
                    d = 0;
                    if (ks != mm) d = d + Math.abs(e[ks]);
                    if (ks != kk + 1) d = d + Math.abs(e[ks - 1]);
                    dd = Math.abs(s[ks]);
                    if (dd > eps * d)
                        ks = ks - 1;
                    else
                        s[ks] = 0;
                }
                if (ks == kk) {
                    kk = kk + 1;
                    d = Math.abs(s[mm]);
                    t = Math.abs(s[mm - 1]);
                    if (t > d) d = t;
                    t = Math.abs(e[mm - 1]);
                    if (t > d) d = t;
                    t = Math.abs(s[kk]);
                    if (t > d) d = t;
                    t = Math.abs(e[kk]);
                    if (t > d) d = t;
                    sm = s[mm] / d;
                    sm1 = s[mm - 1] / d;
                    em1 = e[mm - 1] / d;
                    sk = s[kk] / d;
                    ek = e[kk] / d;
                    b = ((sm1 + sm) * (sm1 - sm) + em1 * em1) / 2;
                    C = sm * em1;
                    C = C * C;
                    shh = 0;
                    if ((b != 0) || (C != 0))
                        shh = Math.sqrt(b * b + C);
                    if (b < 0) shh = -shh;
                    shh = C / (b + shh);
                    if ((b != 0) || (C != 0))
                        shh = Math.sqrt(b * b + C);
                    if (b < 0) shh = -shh;
                    shh = C / (b + shh);
                    fg[1] = (sk + sm) * (sk - sm) - shh;
                    fg[2] = sk * ek;
                    for (I = kk; I <= mm - 1; I++) {
                        Cal2(fg, cs);
                        if (I != kk) e[I - 1] = fg[1];
                        fg[1] = cs[1] * s[I] + cs[2] * e[I];
                        e[I] = cs[1] * e[I] - cs[2] * s[I];
                        fg[2] = cs[2] * s[I + 1];
                        s[I + 1] = cs[1] * s[I + 1];
                        if ((cs[1] != 1) || (cs[2] != 0)) {
                            for (J = 1; J <= n; J++) {
                                d = cs[1] * dblV[J][I] + cs[2] * dblV[J][I + 1];
                                dblV[J][I + 1] = -cs[2] * dblV[J][I] + cs[1] * dblV[J][I + 1];
                                dblV[J][I] = d;
                            }
                        }
                        Cal2(fg, cs);
                        s[I] = fg[1];
                        fg[1] = cs[1] * e[I] + cs[2] * s[I + 1];
                        s[I + 1] = -cs[2] * e[I] + cs[1] * s[I + 1];
                        fg[2] = cs[2] * e[I + 1];
                        e[I + 1] = cs[1] * e[I + 1];

                        if (I < m) {
                            if ((cs[1] != 1) || (cs[2] != 0)) {
                                for (J = 1; J <= m; J++) {
                                    d = cs[1] * dblU[J][I] + cs[2] * dblU[J][I + 1];
                                    dblU[J][I + 1] = -cs[2] * dblU[J][I] + cs[1] * dblU[J][I + 1];
                                    dblU[J][I] = d;
                                }
                            }
                        }
                    }
                    e[mm - 1] = fg[1];
                    it = it - 1;
                } else {
                    if (ks == mm) {
                        kk = kk + 1;
                        fg[2] = e[mm - 1];
                        e[mm - 1] = 0;
                        for (ll = kk; ll <= mm - 1; ll++) {
                            I = mm + kk - ll - 1;
                            fg[1] = s[I];
                            Cal2(fg, cs);
                            s[I] = fg[1];
                            if (I != kk) {
                                fg[2] = -cs[2] * e[I - 1];
                                e[I - 1] = cs[1] * e[I - 1];
                            }
                            if (cs[1] != 1 || cs[2] != 0) {
                                for (J = 1; J <= n; J++) {
                                    d = cs[1] * dblV[J][I] + cs[2] * dblV[J][mm];
                                    dblV[J][mm] = -cs[2] * dblV[J][I] + cs[1] * dblV[J][mm];
                                    dblV[J][I] = d;
                                }
                            }
                        }
                    } else {
                        kk = ks + 1;
                        fg[2] = e[kk - 1];
                        e[kk - 1] = 0;
                        for (I = kk; I <= mm; I++) {
                            fg[1] = s[I];
                            Cal2(fg, cs);
                            s[I] = fg[1];
                            fg[2] = -cs[2] * e[I];
                            e[I] = cs[1] * e[I];
                            if ((cs[1] != 1) || (cs[2] != 0)) {
                                for (J = 1; J <= m; J++) {
                                    d = cs[1] * dblU[J][I] + cs[2] * dblU[J][kk - 1];
                                    dblU[J][kk - 1] = -cs[2] * dblU[J][I] + cs[1] * dblU[J][kk - 1];
                                    dblU[J][I] = d;
                                }
                            }
                        }
                    }
                }

            }

        }
//        return true;
    }

    /**
     * ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
     * '  模块名：MatrixModule.bas
     * '  函数名：Cal1
     * '  功能：  内部过程，供MUav函数调用
     * ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
     */

    private void Cal1(double[][] dblA, double[] e, double[] s, double[][] dblV, int m, int n) {
        int I, J, q;
        double d;
        if (m >= n)
            I = n;
        else
            I = m;
        for (J = 1; J <= I - 1; J++) {
            dblA[J][J] = s[J];
            dblA[J][J + 1] = e[J];
        }
        dblA[I][I] = s[I];
        if (m < n) dblA[I][I + 1] = e[I];
        for (I = 1; I <= n - 1; I++) {
            for (J = I + 1; J <= n; J++) {
                d = dblV[I][J];
                dblV[I][J] = dblV[J][I];
                dblV[J][I] = d;
            }
        }

    }

    /**
     * ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
     * '  模块名：MatrixModule.bas
     * '  函数名：Cal2
     * '  功能：  内部过程，供MUav函数调用
     * ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
     */
    private void Cal2(double[] fg, double[] cs) {
        double R, d;
        if ((Math.abs(fg[1]) + Math.abs(fg[2])) == 0) {
            cs[1] = 1;
            cs[2] = 0;
            d = 0;
        } else {
            d = Math.sqrt((fg[1] * fg[1] + fg[2] * fg[2]));
            if (Math.abs(fg[1]) > Math.abs(fg[2])) {
                d = Math.abs(d);
                if (fg[1] < 0) d = -d;
            }
            if (Math.abs(fg[2]) >= Math.abs(fg[1])) {
                d = Math.abs(d);
                if (fg[2] < 0) d = -d;
            }
            cs[1] = fg[1] / d;
            cs[2] = fg[2] / d;
            R = 1;
            if (Math.abs(fg[1]) > Math.abs(fg[2]))
                R = cs[2];
            else if (cs[1] != 0)
                R = 1 / cs[1];
            fg[1] = d;
            fg[2] = R;
        }

    }

    /**
     * ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
     * '  模块名：MatrixModule.bas
     * '  函数名：MInv
     * '  功能：  求矩阵的广义逆
     * '  参数：   m    - Integer型变量。系数矩阵的行数， m>=n
     * '           n    - Integer型变量。系数矩阵的列数，n<=m
     * '          dblA  - Double型二维数组，体积维m x n。存放待分解矩阵；
     * '                  返回时，其对角线存放矩阵的奇异值(以非递增次序排列)，其余元素为0。
     * '          dblAP - Double型二维数组，体积维n x m。返回时存放矩阵的广义逆。
     * '          dblU  - Double型二维数组，体积维m x m。返回时，存放奇异值分解式中的左奇异向量U。
     * '          dblV  - Double型二维数组，体积维n x n。返回时，存放奇异值分解式中的右奇异向量VT。
     * '           ka  - Integer型变量。ka=max(m,n)+1
     * '          eps  - Double型变量。奇异值分解函数中的控制精度参数。
     * '  返回值： Boolean型。False，失败无解；True, 成功
     * ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
     */
    private boolean MInv(int m, int n, double[][] dblA, double[][] dblAP, double[][] dblU, double[][] dblV, int ka, double eps) {
        int I, J, k, L;
        if (!MUav(m, n, dblA, dblU, dblV, ka, eps)) {
            return false;
        }
        J = n;
        if (m < n) J = m;
        J = J - 1;
        k = 0;
        while (k <= J) {
            if (dblA[k + 1][k + 1] == 0)
                break;
            k = k + 1;
        }

        k = k - 1;
        for (I = 0; I <= n - 1; I++) {
            for (J = 0; J <= m - 1; J++) {
                dblAP[I + 1][J + 1] = 0;
                for (L = 0; L <= k; L++) {
                    dblAP[I + 1][J + 1] = dblAP[I + 1][J + 1] + dblV[L + 1][I + 1] * dblU[J + 1][L + 1] / dblA[L + 1][L + 1];
                }
            }
        }
        return true;
    }

}
