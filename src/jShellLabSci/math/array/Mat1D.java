        
// This is the basic Mat1D class of jShellLab.
// Ιt has a lot of operations covering basic mathematical tasks.
// Also, it offers an extensive range of static operations that help to perform conveniently many things.
// Convenient syntax is offered using Groovy's freatures.
// Also, some native optimized C libraries and NVIDIA CUDA support is offered for faster maths.

package jShellLabSci.math.array;

import edu.emory.mathcs.utils.ConcurrencyUtils;
import jshellLabGlobal.Interpreter.GlobalValues;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Future;
import java.util.function.UnaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import static org.bytedeco.javacpp.openblas.CblasNoTrans;
import org.bytedeco.javacpp.openblas;

// keeps the matrix representation as a continues one-dimensional
// Java double [] array
// the data storage is in column-major order
public class Mat1D   {

    public int Nrows, Ncols;
   
    // The storage of data as double [] array. We can manipulate it directly for efficiency.
     public    double[] d;

     // get the data array by reference
     final public  double [] getArray() { return d; } 

    public Mat1D(double [] v, int r, int c) {
     d=v;
     Nrows = r;
     Ncols = c;
    }
     
    
  

public Mat1D(int n, int m)  {  // creates a zero Mat1D 
    d = new double[n*m];
          
    Nrows = n;
    Ncols = m;
}

     
final public DoubleStream stream() {
    return Arrays.stream(d);
}

 
final public DoubleStream parallelstream() {
    return Arrays.stream(d).parallel();
}
        
// multiply using openBLAS
 final public Mat1D multiply( Mat1D that)  {
     
   int Ccolumns = that.Ncols;
   double [] result = new double[Nrows*that.Ncols];
   double alpha=1.0;
   double beta=0.0;
   int lda = Nrows;
   int ldb = Ncols;
   int ldc = Nrows;
    // perform the multiplication using openblas  
   org.bytedeco.javacpp.openblas.cblas_dgemm(openblas.CblasColMajor, CblasNoTrans, CblasNoTrans, Nrows, that.Ncols,  Ncols, alpha, d, lda, that.d, ldb, beta, 
        result, ldc);
 
       // return the resulted matrix
    return new Mat1D(result, Nrows, that.Ncols);
}
 
 
     /*
// multiply using openBLAS
 final public Mat1D test( Mat1D that)  {
     
   int Ccolumns = that.Ncols;
   double [] result = new double[Nrows*that.Ncols];
   double alpha=1.0;
   double beta=0.0;
   int lda = Nrows;
   int ldb = Ncols;
   int ldc = Nrows;
    // perform the multiplication using openblas  
   org.bytedeco.javacpp.openblas.LAPACKE_dgetrf_
           
           //cblas_dgemm(openblas.CblasRowMajor, CblasNoTrans, CblasNoTrans, Nrows, that.Ncols,  Ncols, alpha, d, lda, that.d, ldb, beta, 
    //    result, ldc);
 
       // return the resulted matrix
    return new Mat1D(result, Nrows, that.Ncols);
}
*/

final public int numRows()  { return Nrows; }
final public int numCols() { return Ncols; }
final public int numColumns() { return Ncols; }
 
// returns size as an array of two ints, i.e. int[2]
final public int  [] size() {
    int [] siz = new int[2];
    siz[0] = Nrows;
    siz[1] = Ncols;
    return  siz;
}
	
final public  static int[] size(Mat1D M) {
    return M.size();
}

       // set the reference to double[][] storage of this matrix object to a different array 	
   final  public void setRef(double[] a) {
        d = a;
    }

   
   
   
   
     
    // transform  the data array to a 2-D double[][] Java array representation
final public double [][] toDoubleArray() { 
    double [][] data = new double[Nrows][Ncols];
   
    int cnt=0;
        for (int c=0; c < Ncols; c++ )
            for (int r = 0; r < Nrows; r++)
               data[r][c] = d[cnt++];
    return data;
}

 
    

public Mat1D(int n, int m, double v)  {  // creates a Mat1D filled with the value
   d = new double[n*m];
         
    Nrows = n;
    Ncols = m;
    
    for (int k=0; k<n*m; k++)
        d[k]=v;
}

// a copy constructor
public Mat1D(Mat1D m) {    // matrix to copy
 d = DoubleArray.copy(m.d);   
 
 Nrows = m.Nrows;
 Ncols = m.Ncols;
}

// column major data storage, thus
// (row, col) element is at position: col*Nrows+row
final public double get(int row, int col) {
   return  d[col*Nrows+row];
}
	
// return a reference to the 1-D data representation
final public double[] getRef() {
   return d;
}
	

// column major data storage, thus
// (row, col) element is at position: col*Nrows+row
final  public void set(int row, int col,  Number v) {
    d[col*Nrows+row] = v.doubleValue();
}
	
final public void set(int row, int col, Integer v) {
       d[col*Nrows+row]  = (double) v.intValue();
}




// this method is used to overload the indexing operator e.g.,
// A = rand(8, 9);   a23 = A[2,3]
final public double   getAt(int row, int col)  { 
    return    d[col*Nrows+row];
}



// assuming a column major storage of values
final public void   putAt(int row, int col, double value)  { 
     d[col*Nrows+row] = value;
            
}

public double  getAt(int r) {
    return d[r];
}
 // IntRanges we use the following routine for handling lists of Integers
private  void putAtWithInt(Integer a1, Integer a2, double value) {
    d[a2*Ncols+a1] = value;
}
/* e,g,
 x = rand(12, 15)
 x[2..3, 1..2] = 12.22  // subrange 2 to 3 tows, 1 to 2 cols to 12.22
 x[-1..-1, 3..5] = 35  // cols  3 to 5  to 35
 x[0..1, -1..-1] = -11.11 // rows 0 to 1 to -11.11

 */
        

 final public void  sr(int rowS, int rowE, double value)  {
     for (int rows=rowS; rows <= rowE; rows++ )
       for  (int cols=0; cols < Ncols; cols++)
        d[cols*Nrows+rows]= value;
    }
     
     final  public void  sc( int colsS, int colsE,  double value)  {
            
            for  (int rows=0; rows < Nrows; rows++)
                for (int cols=colsS; cols <= colsE; cols++ )
                    d[cols*Nrows+rows] = value;
        }
        
        final  public void  src(int rowS, int rowE, int colsS, int colsE, double value)  {
            for  (int rows=rowS; rows <= rowE; rows++)
                for (int cols=colsS; cols <= colsE; cols++ )
                    d[cols*Nrows+rows] = value;
        }

        final  public void  src(int rowS, int rowInc,  int rowE, int colsS, int colInc, int colsE, double value)  {
            for  (int rows=rowS; rows <= rowE; rows+=rowInc)
                for (int cols=colsS; cols <= colsE; cols+=colInc )
                    d[cols*Nrows+rows] = value;
        }

        
        final  public void  s(int [] rng, double value)  {
            int rowsS = rng[0];  int rowsE = rng[1];
            int colsS = rng[2]; int colsE = rng[3];
            for  (int rows=rowsS; rows <= rowsE; rows++)
                for (int cols=colsS; cols <= colsE; cols++ )
                    d[cols*Nrows+rows] = value;
        }

 

 
// extracts a submatrix, e.g. m.gc( 2,  12 ) corresponds to Matlab's m(:, 2:12)'
 final public Mat1D  gc(int colLow, int  colHigh)  {
     int rowStart = 0;     int rowEnd =  Nrows-1;   // all rows
     int colStart = colLow;  int  colEnd = colHigh;
     int rowInc = 1;
     int colInc = 1;
     int rowNum = Nrows;    // take all the rows

    if  (colStart <= colEnd)   {    // positive increment
        if (colEnd == -1)  { colEnd = Ncols-1; } // if -1 is specified take all the columns
        int colNum = colEnd-colStart+1;
        Mat1D subMatr = new Mat1D(rowNum, colNum);   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    int  crow = rowStart;  // indexes current row
    int  ccol = colStart;  // indexes current column
    int  rowIdx = 0;  int colIdx = 0;  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol <= colEnd)   {
                subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx += rowInc;
            crow += rowInc;
     } // crow <= rowEnd
 return subMatr;
} // positive increment
  else {  // negative increment
    int  colNum = colEnd-colStart+1;
    Mat1D subMatr = new Mat1D(rowNum, colNum);   // create a Matrix to keep the extracted range
      // fill the created matrix with values
    int  crow = rowStart;  // indexes current row
    int  ccol = colStart;  // indexes current column
    int  rowIdx = 0;  int  colIdx = 0;  // indexes at the new Matrix

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol >= colEnd)   {
                subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
     } // crow <= rowEnd
 return subMatr;   // return the submatrix
   }
   }

// column select 
 final public Mat1D  gc( int colL,  int inc, int  colH)   {
     return grc( 0, 1, this.Nrows-1, colL, inc, colH);
 }
 

// row select 
 final public Mat1D  gr( int rowL,  int inc, int  rowH)   {
     return grc(rowL, inc, rowH, 0, 1, this.Ncols-1);
 }
 
 /* extract the columns specified with indices specified with  the array colIndices.
 The new matrix is formed by using all the rows of the original matrix 
 but with using only the specified columns.
 The columns at the new matrix are arranged in the order specified with the array colIndices
 e.g. 
  testMat = M(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
  colIndices  = [3, 1] as int []
   extract3_1cols = testMat.gc( colIndices)
   */
 
  final public Mat1D gc( int [] colIndices)  {
    int  lv = colIndices.length;
    if (lv > Ncols)  // do nothing
      {
        System.out.println("array indices length = "+lv+" is greater than the number of columns of the matrix = "+Ncols);
        return this;
      }
      else {  // dimension of array with column indices to use is correct
      // allocate array
      Mat1D   colFiltered = new Mat1D(Nrows, lv);
      for (int col = 0; col < lv; col++)  {
           int   currentColumn = colIndices[col];  // the specified column
           for (int row = 0; row < Nrows; row++)  // copy the corresponding row
               colFiltered.putAt(row, col, this.getAt(row, currentColumn));
       }  
    
      return colFiltered;    // return the column filtered array
    } // dimension of array with column indices to use is correct
  }
  

  /* extract the rows specified with indices specified with  the array rowIndices.
 The new matrix is formed by using all the columns of the original matrix 
 but with using only the specified rows.
 The rows at the new matrix are arranged in the order specified with the array rowIndices
 e.g. 
 testMat = M(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12; 13 14 15 16; 17 18 19 20")
 rowIndices = [3, 1] as int []
 extract3_1rows = testMat.gr(rowIndices)
   */
  final public Mat1D gr(int [] rowIndices)  {
    int  lv = rowIndices.length;
    if (lv > Nrows)  // do nothing
      {
        System.out.println("array indices length = "+lv+" is greater than the number of rows of the matrix = "+Nrows);
        return this;
      }  
      else {  // dimension of array with column indices to use is correct
      // allocate array
      Mat1D   rowFiltered = new Mat1D(lv,  Ncols);
      for (int row = 0; row <  lv; row++)  {
           int   currentRow = rowIndices[row];  // the specified row
           for (int col = 0; col < Ncols; col++)  // copy the corresponding row
               rowFiltered.putAt(row,col, this.getAt(currentRow, col));
       }  
    
      return rowFiltered;    // return the column filtered array
   } // dimension of array with column indices to use is correct
  }
  

  
/* extract the columns specified with true values at the array  colIndices.
 The new matrix is formed by using all the rows of the original matrix 
 but with using only the specified columns.
 e.g. 
 testMat = M(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 colIndices = [true, false, true, false] as boolean []
 extract0_2cols = testMat.gc(colIndices)
   */
  final public Mat1D gc(boolean [] colIndices)   {   
    int  lv = colIndices.length; 
    if (lv != Ncols)  // do nothing
      {
        System.out.println("array indices length = "+lv+" is not the number of columns of the matrix = "+Ncols);
        return this;
      }
      else {  // dimension of array with column indices to use is correct
        // count the number of trues
        int  ntrues = 0;
        for (int k=0; k<Ncols; k++)
          if (colIndices[k]==true)  
            ntrues++;
        
      // allocate array
      Mat1D  colFiltered = new Mat1D(Nrows, ntrues);
      int   currentColumn=0;
      for (int col = 0; col<Ncols; col++)  {
         if (colIndices[col])   { // copy the corresponding column
             for (int row=0; row<Nrows; row++) 
               colFiltered.putAt(row, currentColumn, this.getAt(row,col));
             currentColumn++;
         }  // copy the corresponding column
      }        
    
      return colFiltered;    // return the column filtered array
      } // dimension of array with column indices to use is correct
  }
  
  
    
/* extract the rows specified with true values at the array rowIndices.
 The new matrix is formed by using all the columns of the original matrix 
 but with using only the specified rows.
 e.g. 
 testMat = M(" 1.0 2.0 3.0 ; 5.0 6.0 7.0 ; 8 9 10 ; 11 12 13")
 rowIndices = [false, true, false, true] as boolean[] 
 extract1_3rows = testMat.gr(rowIndices)
   */
  
  final  public Mat1D gr(boolean [] rowIndices) {
    int   lv = rowIndices.length;
    if (lv != Nrows)  // do nothing
      {
        System.out.println("array indices length = "+lv+" is not the number of rows of the matrix = "+Nrows);
        return this;
      }
      else {  // dimension of array with row indices to use is correct
        // count the number of trues
        int  ntrues = 0;
        for (int k = 0; k < Nrows; k++)
          if (rowIndices[k])  
            ntrues++;
        
      // allocate array
       Mat1D  rowFiltered = new Mat1D(ntrues, Ncols);
       int currentRow=0;
       for (int row = 0; row < Nrows; row++) 
          if (rowIndices[row])  {  // copy the corresponding row
            for (int col = 0; col < Ncols; col++)
               rowFiltered.putAt(currentRow, col, this.getAt(row,col));
               currentRow++;
          }
        return rowFiltered;
      }  // dimension of array with row indices to use is correct 
          
    }
    
  
 final public Mat1D  gr( int rowL,  int  rowH)  {
     int  rowStart = rowL;  int  rowEnd = rowH;
     int  colStart = 0;   int  colEnd =  Ncols - 1;   // all columns
     int  colNum = Ncols;
     int  colInc = 1;
     

if (rowStart <= rowEnd) {   // positive increment
    int  rowInc = 1;
    if (rowEnd == -1) { rowEnd = Nrows-1; }  // if -1 is specified take all the rows
    int  rowNum = rowEnd-rowStart+1;
    Mat1D  subMatr = new Mat1D(rowNum, colNum);   // create a Mat to keep the extracted range
      // fill the created matrix with values
    int  crow = rowStart;  // indexes current row
    int  ccol = colStart;
    int rowIdx =0;  int  colIdx = 0;  // indexes at the new Mat1D
    while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol <= colEnd )   { 
                subMatr.putAt(rowIdx, colIdx,   getAt(crow,ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
       } // crow <= rowEnd
return subMatr;   // return the submatrix

} // rowStart <= rowEnd
else { // rowStart > rowEnd
    int  rowInc = -1;
    int  rowNum = rowStart-rowEnd+1;
    Mat1D  subMatr = new Mat1D(rowNum, colNum);   // create a Mat to keep the extracted range
      // fill the created matrix with values
    int  crow = rowStart;  // indexes current row at the source matrix
    int  ccol = colStart;
    int  rowIdx =0;  int  colIdx = 0;  // indexes at the new Mat
    while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol <= colEnd)   {
                subMatr.putAt(rowIdx, colIdx, getAt(crow, ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
       }

return subMatr;   // return the submatrix

} // rowStart > rowEnd

}
 
 
 
 
 
 

 // extracts a submatrix, e.g. m.grc( 2,  12, 4,   8 )  corresponds to Matlab's m(2:12, 4:8)'
final public  Mat1D  grc(int rowLow,  int rowHigh, int  colLow,  int  colHigh)  {
     int  rowStart = rowLow;     int  rowEnd =  rowHigh;
     int  colStart = colLow;    int  colEnd = colHigh;
     int  rowInc = 1;
     if  (rowHigh < rowLow) rowInc = -1;
     int colInc = 1;
     if (colHigh < colLow) colInc = -1;

        int  rowNum = (int)Math.floor((rowEnd-rowStart) / rowInc)+1;
        int  colNum = (int) Math.floor( (colEnd-colStart) / colInc)+1;
        Mat1D  subMatr = new Mat1D(rowNum, colNum);   // create a Mat1D to keep the extracted range

    if  (rowStart <= rowEnd && colStart <= colEnd)   {    // positive increment at rows and columns
        int  crow = rowStart;  // indexes current row
        int  ccol = colStart;  // indexes current column
        int  rowIdx = 0;  int  colIdx = 0;  // indexes at the new Mat1D
            while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol <= colEnd)   {
                subMatr.putAt(rowIdx, colIdx, getAt(crow, ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
     } // crow <= rowEnd
  return subMatr;
} // positive increment
  else if  (rowStart >= rowEnd && colStart <= colEnd)   {
      // fill the created matrix with values
    int  crow = rowStart;  // indexes current row
    int  ccol = colStart;  // indexes current column
    int  rowIdx = 0;  int colIdx = 0;  // indexes at the new Mat1D

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol <= colEnd)   {
                subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
     } // crow <= rowEnd
 return subMatr;   // return the submatrix
   }
else if  (rowStart <= rowEnd && colStart >= colEnd)   {
      // fill the created matrix with values
    int  crow = rowStart;  // indexes current row
    int  ccol = colStart;  // indexes current column
    int  rowIdx = 0; int  colIdx = 0;  // indexes at the new Mat1D

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol >= colEnd)   {
                subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
     } // crow <= rowEnd
 return subMatr;   // return the submatrix
   }
else {
      // fill the created matrix with values
    int  crow = rowStart;  // indexes current row
    int  ccol = colStart;  // indexes current column
    int  rowIdx = 0;  int  colIdx = 0;  // indexes at the new Mat1D

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol >= colEnd)   {
                subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
     } // crow > rowEnd
 return subMatr;   // return the submatrix
   }

   }




 
 // extracts a submatrix, e.g. m.grc( 2, 3, 12, 4, 2,  8 )  corresponds to Matlab's m(2:3:12, 4:2:8)'
  final public Mat1D  grc(int rowLow, int  rowInc,  int rowHigh,  int  colLow,  int  colInc, int  colHigh)  {
      int  rowStart = rowLow;     int  rowEnd =  rowHigh;
    int  colStart = colLow;   int colEnd = colHigh;

        int   rowNum = (int) Math.floor((rowEnd-rowStart) / rowInc)+1;
        int   colNum = (int) Math.floor( (colEnd-colStart) / colInc)+1;
       
    Mat1D   subMatr = new Mat1D(rowNum, colNum);   // create a Mat1D to keep the extracted range

    if  (rowStart <= rowEnd && colStart <= colEnd)   {    // positive increment at rows and columns
        int   crow = rowStart;  // indexes current row
        int   ccol = colStart;  // indexes current column
        int   rowIdx = 0;  int  colIdx = 0;  // indexes at the new Mat1D
            while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol <= colEnd)   {
                subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
     } // crow <= rowEnd
 return subMatr;
} // positive increment
  else if  (rowStart >= rowEnd && colStart <= colEnd)   {
      // fill the created matrix with values
    int  crow = rowStart;  // indexes current row
    int  ccol = colStart;  // indexes current column
    int  rowIdx = 0; int  colIdx = 0;  // indexes at the new Mat1D

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol <= colEnd)   {
                subMatr.putAt(rowIdx,colIdx, this.getAt(crow, ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
     } // crow <= rowEnd
 return subMatr;   // return the submatrix
   }
else if  (rowStart <= rowEnd && colStart >= colEnd)   {
      // fill the created matrix with values
    int   crow = rowStart;  // indexes current row
    int   ccol = colStart;  // indexes current column
    int   rowIdx = 0;  int  colIdx = 0;  // indexes at the new Mat1D

           while  ( crow <= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol >= colEnd)   {
                subMatr.putAt(rowIdx, colIdx, this.getAt(crow,ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
     } // crow <= rowEnd
 return subMatr;   // return the submatrix
   }
else {
      // fill the created matrix with values
    int   crow = rowStart;  // indexes current row
    int   ccol = colStart;   // indexes current column
    int   rowIdx = 0;  int  colIdx = 0;  // indexes at the new Mat1D

           while  ( crow >= rowEnd )   {
          ccol = colStart;  colIdx = 0;
          while  (ccol >= colEnd)   {
                subMatr.putAt(rowIdx, colIdx, this.getAt(crow,ccol));
                colIdx++;
                ccol += colInc;
               }
            rowIdx++;
            crow += rowInc;
     } // crow > rowEnd
 return subMatr;   // return the submatrix
   }

   }



 
// extracts a specific row, take all columns, e.g. m.gr(2) corresponds to Matlab's m(2, :)'
final public Mat1D gr(int row)  {
    
      int   colStart = 0;     int  colEnd =  Ncols-1;   // all columns
      int   rowNum = 1;    int  colNum = colEnd-colStart+1;
    Mat1D   subMatr = new Mat1D(rowNum, colNum);   // create a Mat1D to keep the extracted range
      // fill the created matrix with values
    int  ccol = colStart;
    while  (ccol <= colEnd)   {
          subMatr.putAt(0, ccol, this.getAt(row,ccol));
          ccol++;
         }

     return subMatr;
}


// extracts a specific column, take all rows, e.g. m.gc( 2) corresponds to Matlab's m(:,2:)'  
 final public Mat1D gc( int col )  { 
    
     int  rowStart = 0;     int  rowEnd =  Nrows-1;   // all rows
     int  colNum = 1;      int   rowNum = rowEnd-rowStart+1;
    Mat1D   subMatr = new Mat1D(rowNum, colNum);   // create a Mat1D to keep the extracted range
      // fill the created matrix with values
    int  crow = rowStart;
    while  (crow <= rowEnd)   {
          subMatr.putAt(crow-rowStart, 0, this.getAt(crow,col));
          crow++;
         }

     return subMatr;
}

     

  

 final public Mat1D plus(Mat1D v2) {
        
   Mat1D r = new Mat1D(Nrows, Ncols);
   for (int k=0; k<Nrows*Ncols; k++)
       r.d[k] = d[k]+v2.d[k];
   
   return r;
    }
 
 
 final public Mat1D plus(double x) {
        
   Mat1D r = new Mat1D(Nrows, Ncols);
   for (int k=0; k<Nrows*Ncols; k++)
       r.d[k] = d[k]+x;
   
   return r;
    }


 
final public double sum() {
    double  sm = 0.0;
    for (int k=0; k<Nrows*Ncols; k++)
        sm += d[k];
    return sm;
        
}
 
 
 final public Mat1D minus(double x) {
        
   Mat1D r = new Mat1D(Nrows, Ncols);
   for (int k=0; k<Nrows*Ncols; k++)
       r.d[k] = d[k]-x;
   
   return r;
    }
 
 
 
 final public Mat1D multiply(double x) {
        
   Mat1D r = new Mat1D(Nrows, Ncols);
   for (int k=0; k<Nrows*Ncols; k++)
       r.d[k] = d[k]*x;
   
   return r;
    }

 final public Mat1D plus(double [] v2) {
        
   Mat1D r = new Mat1D(Nrows, Ncols);
   for (int k=0; k<Nrows*Ncols; k++)
       r.d[k] = d[k]+v2[k];
   
   return r;
    }

 
 final public Mat1D minus(Mat1D v2) {
        
   Mat1D r = new Mat1D(Nrows, Ncols);
   for (int k=0; k<Nrows*Ncols; k++)
       r.d[k] = d[k]-v2.d[k];
   
   return r;
    }

 final public Mat1D minus(double [] v2) {
        
   Mat1D r = new Mat1D(Nrows, Ncols);
   for (int k=0; k<Nrows*Ncols; k++)
       r.d[k] = d[k]-v2[k];
   
   return r;
    }

 
 // assuming column major storage
 final static public Mat1D fill1d(int nrows, int ncols, double value) {
	double[] o = new double[nrows*ncols];
                   int pos=0;
	for (int c = 0; c < ncols; c++)
	  for (int r = 0; r < nrows; r++)
                         o[pos++] = value;
		
        return new Mat1D(o, nrows, ncols);
	}

 final public Mat1D sin() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.sin(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
 final public Mat1D cos() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.cos(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
 
 
 final public Mat1D ceil() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.ceil(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
  
 final public Mat1D floor() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.floor(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
  
 final public Mat1D round() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.round(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
 final public Mat1D tan() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.tan(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
 
  
 final public Mat1D sqrt() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.sqrt(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
 
 final public Mat1D atan() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.atan(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
 
 final public Mat1D sinh() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.sinh(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
 final public Mat1D cosh() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.cosh(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
 
 final public Mat1D tanh() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.tanh(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
 final static public Mat1D rand1d(int nrows,int ncols) {
     double [] ra = new double[nrows*ncols];
          for (int k=0; k<nrows*ncols; k++)
            ra[k] = Math.random();
    return new Mat1D(ra, nrows, ncols);      
 }
 
 
 final static public Mat1D ones1d(int nrows,int ncols) {
     double [] ra = new double[nrows*ncols];
          for (int k=0; k<nrows*ncols; k++)
            ra[k] = 1.0;
    return new Mat1D(ra, nrows, ncols);      
 }
 
 
 
 final static public Mat1D zeros1d(int nrows,int ncols) {
     double [] ra = new double[nrows*ncols];
          for (int k=0; k<nrows*ncols; k++)
            ra[k] = Math.random();
    return new Mat1D(ra, nrows, ncols);      
 }
 
 
 // in place routines
 final public Mat1D isin() {
          for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.sin(d[k]);
    return this;      
 }
 
 final public Mat1D icos() {
           for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.cos(d[k]);
    return this;     
 }
 
 
 
 final public Mat1D iceil() {
           for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.ceil(d[k]);
    return this;     
 }
 
 
 
 final public Mat1D ifloor() {
           for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.floor(d[k]);
    return this;     
 }
 
 final public Mat1D iround() {
           for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.cos(d[k]);
    return this;     
 }
  
 
 final public Mat1D itan() {
           for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.cos(d[k]);
    return this;     
 }
 
 
 
  final public Mat1D isqrt() {
           for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.sqrt(d[k]);
    return this;     
 }
 
 
 final public Mat1D iatan() {
           for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.atan(d[k]);
    return this;     
 }
 
 final public Mat1D isinh() {
           for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.sinh(d[k]);
    return this;     
 }
 
 
 final public Mat1D itanh() {
           for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.tanh(d[k]);
    return this;     
 }
 
 
 final public Mat1D irand() {
           for (int k=0; k<Nrows*Ncols; k++)
            d[k] = Math.random();
    return this;     
 }
  
 
  // map Java 8 lambda function
  final  public Mat1D  map(UnaryOperator <Double>myfun) {
    double [] dm = new double[Nrows*Ncols];
    
      for (int elem = 0; elem < Nrows*Ncols; elem++)
          dm[elem] = myfun.apply(d[elem]);
      
      return new Mat1D(dm, Nrows, Ncols);
 }
  
  //SOS
  
  
   final  public void mapf(computeFunction cf) {
       int idx=0;
       for (int i = 0; i < Nrows; i++)
        for (int j=0; j < Ncols; j++)
          d[idx] = cf.f(d[idx++]);   // apply the compute function to the map
 }
 
   
  
  final public Mat1D  pmapf(computeFunction cf)  {
      
      
      final double [] dm = new double[Nrows*Ncols];
      
     int rN = Ncols;
     int  nthreads = ConcurrencyUtils.getNumberOfThreads();
  
  // the matrix is split by row regions and the mutliplication in each such region
  // will be performed in parallel by a seperate thread
     nthreads = Math.min(nthreads, rN);
  
     Future<?>[] futures = new Future[nthreads];
            
      int   colsPerThread = (int)(rN / nthreads)+1;  // how many rows the thread processes

      int threadId = 0;  // the current threadId
  while (threadId < nthreads)  {  // for all threads 
        // define the column region for the current thread
      final int  firstCol = threadId * colsPerThread;   // start of columns range 
      final int  lastCol =   threadId == nthreads-1? rN: firstCol+colsPerThread;  // end of columns range
        
 futures[threadId] = GlobalValues.execService.submit(() -> {
     int  a = firstCol;   // the first column of the matrix that this thread processes
     int idx=a*Nrows;
     while (a < lastCol) {  // the last column of the matrix that this thread processes
         for (int j=0; j < Nrows; j++)
             d[idx] = cf.f(d[idx++]);
         a++;
     }    } // run
      );
        threadId++;
        
  }  // for all threads

  ConcurrencyUtils.waitForCompletion(futures);
  
  
  return new Mat1D(dm,Nrows,Ncols);
  }
  
  
 
  
 final public Mat1D abs() {
     double [] ra = new double[Nrows*Ncols];
          for (int k=0; k<Nrows*Ncols; k++)
            ra[k] = Math.abs(d[k]);
    return new Mat1D(ra, Nrows, Ncols);      
 }
 
 
    @Override
    public String   toString() {
     if (d != null) {
   StringBuilder  sb = new StringBuilder();
   String  formatString = "0.";
   for (int k = 0; k < jShellLabSci.PrintFormatParams.vecDigitsPrecision; k++) 
       formatString += "0";
    DecimalFormat digitFormat = new DecimalFormat(formatString);
    digitFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
    
     int  mxElems = d.length;
     String  moreElems = "";
     if (mxElems > jShellLabSci.PrintFormatParams.vecMxElemsToDisplay )  {
          // vector has more elements than we can display
         mxElems = jShellLabSci.PrintFormatParams.vecMxElemsToDisplay;
         moreElems = " .... ";
     }
    int  i=0;
    int  colnum=0, rownum=0;
     while (i < mxElems) {
       sb.append(digitFormat.format(getAt(rownum, colnum))+"  ");
        i++;
        colnum++;
        if (colnum==Ncols){
            colnum=0;
            rownum++;
           sb.append("\n");
            
        }
       }
     sb.append(moreElems+"\n");
     
 
   return sb.toString(); 
     }
     else return "";
}
}