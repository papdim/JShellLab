# JShellLab: Easy and effective MATLAB-like scientific programming with Java JShell 



## Installation

The installation of JShellLab is very simple: 

  * *Step 1* Download and unzip the .zip download.
  
  * *Step 2* Run the proper script for your platform, i.e. the .sh scripts for Unix like systems and the .bat scripts for Windows. 


## Building with Netbeans and ant

The JShellLab.zip download contains both the sources and all the relevant libraries to build JShellLab with ant.

To build JShellLab with Netbeans, simply specify the corresponding project to Netbeans.



## Building with Gradle and support for fat jar that supports the DeepLearning4j library and Apache Spark

To build JShellLab with *Gradle*, unzip the zipped file, go to the root folder where the contents are unzipped and type: 

 * gradle clean 
 
 * gradle jar
 
 
 The executable should be placed in the build/libs folder
 
 You can also build a fat jar, the JShellLab-all.jar, that supports the DeepLearning4j effective Java library for Deep Learning (it provides very fast native numerical processing operations). Also, it supports Apache Spark for Big Data Processing.
 The Gradle command is:
 
  * gradle fatjar



a fat jar is produced with all the DeepLearning4j and Apache Spark functionality!

Therefore we can run Deep Learning and Apache Spark algorithms in scripting mode,
with either Groovy or the JShell of Java 9+.

Note that F7 executes the selected script code with the JShell.


## Project Summary

The JShellLab environment aims to provide a MATLAB/Scilab like scientific computing platform that is supported by a scripting engine implemented in Groovy language. The JShellLab user can work either with a MATLAB-like command console, or with a flexible editor based on the rsyntaxarea  component, that offers more convenient code development. JShellLab supports extensive plotting facilities and can exploit effectively a lot of powerful Java scientific libraries, as JLAPACK , Apache Common Maths , EJML , MTJ , NUMAL translation to Java , Numerical Recipes Java translation , Colt etc. 

JShellLab scripts can be easily combined with MATLAB, thus the scientist can combine the flexibility of Groovy with the computational effectiveness of MATLAB.



## JShellLab Developer

Stergios Papadimitriou

International Hellenic University

Dept of Informatics 

Kavala, Greece

email: sterg@cs.ihu.gr
