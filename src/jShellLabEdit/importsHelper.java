// helps to perform conveniently imports for basic application types
package jShellLabEdit;

import jshellLabGlobal.Interpreter.GlobalValues;
  // Java standard UI and graphics support

public class importsHelper {
    

 public static void  injectJavaSwing()   {
      String  javaSwingStr = "import  java.awt.* ; \n"+ 
    "import  java.awt.event.*;\n"+
    "import  javax.swing.*; \n"+
    "import  javax.swing.event.*; \n";
    
    GlobalValues.globalEditorPane.setText(javaSwingStr + GlobalValues.globalEditorPane.getText());
    }
 
 public static void  injectApacheCommonMaths()   {
    }
 
 public static void injectJShellLabSciImports() {
    String allImportsStr =  "import static jShellLabSci.math.array.BasicDSP.*; \n"+   
                "import jShellLabSci.math.array.Vec; \n"+  // Vector class
                    "import static jShellLabSci.math.array.Vec.*; \n"+  
                    "import jShellLabSci.math.array.Matrix; \n"+  // Matrix class
                    "import static jShellLabSci.math.array.Matrix.*; \n"+  
                    "import jShellLabSci.math.array.CCMatrix; \n"+  // Matrix class
                    "import static jShellLabSci.math.array.CCMatrix.*; \n"+  
                    "import jShellLabSci.math.array.Sparse; \n"+  // Sparse Matrix class
                    "import static jShellLabSci.math.array.Sparse.*; \n"+  
                    "import jShellLabSci.math.array.JILapack; \n"+  // JLapack Matrix class
                    "import static jShellLabSci.math.array.JILapack.*; \n"+  
                    "import Jama.*;\n"+
                    "import numal.*;\n"+    // numerical analysis library routines
                   "import static jShellLabSci.math.plot.plot.*;\n"+     // plotting routines 
                
                   "import java.awt.*; \n"+
                   "import javax.swing.*; \n"+   // Java standard UI and graphics support
                   "import java.awt.event.*; \n"+ 
                   "import java.text.DecimalFormat; \n"+
                   "import static jShellLabSciCommands.BasicCommands.*;\n"+  // support for GroovySci's console commands
                   "import  static  jShellLabSci.math.array.DoubleArray.*;\n"+
                  " import JSci.maths.*; \n"+
                  "import JSci.maths.wavelet.*; \n"+
                  "import JSci.maths.wavelet.daubechies2.*; \n"+
                  "import jShellLabSci.math.array.*; \n"+
                   "import jShellLabSci.FFT.ApacheFFT;  \n"+
                    "import static jShellLabSci.FFT.ApacheFFT.*; \n"+
                "import NR.*; \n"+
                "import static NR.gaussj.*;  \n"+
                "import com.nr.sp.*; \n"+
                "import static java.lang.Math.*;  \n";    // standard Java math routines, allows calling directly e.g sin(9.8) instead of Math.sin(9.8)
                
    GlobalValues.globalEditorPane.setText(allImportsStr + GlobalValues.globalEditorPane.getText());
 }

 public static void injectBasicPlotsImports() {
    String basicPlotsImportsStr =  "import static jShellLabSci.math.plot.plot.*;\n"+     // plotting routines 
                  "import java.awt.*; \n"+
                   "import javax.swing.*; \n";   // Java standard UI and graphics support
                   
    GlobalValues.globalEditorPane.setText(basicPlotsImportsStr  + GlobalValues.globalEditorPane.getText());
 }

  // the NUMAL library related staff
  public static void injectNumAl() {
    String  numAlImports  =    "import  java.util.Vector ; \n"+
                    "import  numal.Algebraic_eval.*; \n"+
                    "import  numal.Analytic_eval.*; \n"+
                    "import  numal.Analytic_problems.*; \n"+
                    "import  numal.Approximation.*; \n"+
                    "import  numal.Basic.*; \n"+
                    "import  numal.FFT.*; \n"+
                    "import  numal.Linear_algebra.*; \n"+
                    "import  numal.Special_functions.*; \n"+
                    "import java.text.DecimalFormat;";
    
    GlobalValues.globalEditorPane.setText(numAlImports + GlobalValues.globalEditorPane.getText());
  }
  




}
