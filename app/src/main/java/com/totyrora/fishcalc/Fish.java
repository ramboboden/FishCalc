package com.totyrora.fishcalc;

/**
 * Created by thomasb on 2015-05-30.
 */

public class Fish {

    private double len;     // cm
    private double weight;  // g
    private double k;
    private int[] kArray;
    private int spices;


    public Fish(int startLen, int startSpices, int[] initArr) {
        kArray = initArr;
        setLen(startLen);
        setSpices(startSpices);
        calcWeight();
    }

   public void setLen (double inLen) {
       len = inLen;
       calcWeight();
   };

    public void setSpices (int selSpices) {
        spices = selSpices;
        k = (double)kArray[selSpices]/100;
        calcWeight();
    };

    public void calcLen (int inWeight) {
        //TODO later fix the reverse
        weight = (double) inWeight * 1000;
        // calcLen()
    }

    private void calcWeight () {
        weight = Math.pow(len, 3) * k / 100;
    }

    public int getSpices () {return spices;}

    public double getWeight() {return weight;}

    public double getWeightHi() {return weight*1.1;}

    public double getWeightLo() {return weight*0.9;}

    public double getLen() {return len;}

    public String weightToString() {
        return String.format("%.2f", weight/1000);

    }

}
