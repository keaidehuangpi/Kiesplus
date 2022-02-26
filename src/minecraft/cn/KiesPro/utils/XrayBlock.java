package cn.KiesPro.utils;
public class XrayBlock {
    public double x;
    public double y;
    public double z;
    public String type;

    public XrayBlock(double a, double a2, double a3, String xx) {
        z = a;
        y = a2;
        x = a3;
        type = xx;
    }

    public XrayBlock() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }
}