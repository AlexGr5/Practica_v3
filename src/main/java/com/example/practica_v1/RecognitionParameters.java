package com.example.practica_v1;

public class RecognitionParameters {

    private int dp;

    private int denominatorOfMinDist;

    private int param1;

    private int param2;

    private int minRadius;

    private int maxRadius;

    public RecognitionParameters()
    {
        ;
    }

    public RecognitionParameters(int DP, int DenominatorOfMinDist, int Param1, int Param2, int MinRadius, int MaxRadius)
    {
        setDp(DP);
        setDenominatorOfMinDist(DenominatorOfMinDist);
        setParam1(Param1);
        setParam2(Param2);
        setMinRadius(MinRadius);
        setMaxRadius(MaxRadius);
    }

    public void SetRecognitionParameters(int DP, int DenominatorOfMinDist, int Param1, int Param2, int MinRadius, int MaxRadius)
    {
        setDp(DP);
        setDenominatorOfMinDist(DenominatorOfMinDist);
        setParam1(Param1);
        setParam2(Param2);
        setMinRadius(MinRadius);
        setMaxRadius(MaxRadius);
    }


    public int getDp() {
        return dp;
    }

    public void setDp(int dp) {
        this.dp = dp;
    }

    public int getDenominatorOfMinDist() {
        return denominatorOfMinDist;
    }

    public void setDenominatorOfMinDist(int denominatorOfMinDist) {
        this.denominatorOfMinDist = denominatorOfMinDist;
    }

    public int getParam1() {
        return param1;
    }

    public void setParam1(int param1) {
        this.param1 = param1;
    }

    public int getParam2() {
        return param2;
    }

    public void setParam2(int param2) {
        this.param2 = param2;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }
}
