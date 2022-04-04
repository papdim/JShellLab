package jShellLabSci.math.array;

import static jShellLabSci.math.array.Matrix.*; 

public class LQResults {

    public double  [][] L;
    public double [][] getL() { return  L; }
    public double [][] Q;
    public double [][] getQ() { return Q; }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (L != null)
         sb.append("L  = "+ printArray(L));
        else
         sb.append("L = null \n");
        if (Q != null)
         sb.append("Q = "+ printArray(Q));
        else
         sb.append("Q = null");   
        
        return sb.toString();
    }
}
