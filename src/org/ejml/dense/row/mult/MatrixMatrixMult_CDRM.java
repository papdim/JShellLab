/*
 * Copyright (c) 2009-2020, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Efficient Java Matrix Library (EJML).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ejml.dense.row.mult;

import org.ejml.MatrixDimensionException;
import org.ejml.data.CMatrixRMaj;
import org.ejml.dense.row.CommonOps_CDRM;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Generated;

/**
 * <p>Matrix multiplication routines for complex row matrices in a row-major format.</p>
 * 
 *
 * <p>DO NOT MODIFY.  Automatically generated code created by GeneratorMatrixMatrixMult_CDRM</p>
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.mult.GeneratorMatrixMatrixMult_CDRM")
@SuppressWarnings("Duplicates")
public class MatrixMatrixMult_CDRM {
    public static void mult_reorder(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( a.numCols == 0 || a.numRows == 0 ) {
            CommonOps_CDRM.fill(c,0,0);
            return;
        }
        float realA,imagA;

        int indexCbase= 0;
        int strideA = a.getRowStride();
        int strideB = b.getRowStride();
        int strideC = c.getRowStride();
        int endOfKLoop = b.numRows*strideB;

        for( int i = 0; i < a.numRows; i++ ) {
            int indexA = i*strideA;

            // need to assign c.data to a value initially
            int indexB = 0;
            int indexC = indexCbase;
            int end = indexB + strideB;

                realA = a.data[indexA++];
                imagA = a.data[indexA++];

            while( indexB < end ) {
                float realB = b.data[indexB++];
                float imgB = b.data[indexB++];

                c.data[indexC++] = realA*realB - imagA*imgB;
                c.data[indexC++] = realA*imgB + imagA*realB;
            }

            // now add to it
            while( indexB != endOfKLoop ) { // k loop
                indexC = indexCbase;
                end = indexB + strideB;

                realA = a.data[indexA++];
                imagA = a.data[indexA++];

                while( indexB < end ) { // j loop
                    float realB = b.data[indexB++];
                    float imgB = b.data[indexB++];

                    c.data[indexC++] += realA*realB - imagA*imgB;
                    c.data[indexC++] += realA*imgB + imagA*realB;
                }
            }
            indexCbase += strideC;
        }
    }


    public static void mult_small(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int aIndexStart = 0;
        int indexC = 0;

        int strideA = a.getRowStride();
        int strideB = b.getRowStride();

        for( int i = 0; i < a.numRows; i++ ) {
            for( int j = 0; j < b.numCols; j++ ) {
                float realTotal = 0;
                float imgTotal = 0;

                int indexA = aIndexStart;
                int indexB = j*2;
                int end = indexA + strideA;
                while( indexA < end ) {
                    float realA = a.data[indexA++];
                    float imagA = a.data[indexA++];

                    float realB = b.data[indexB];
                    float imgB = b.data[indexB+1];

                    realTotal += realA*realB - imagA*imgB;
                    imgTotal += realA*imgB + imagA*realB;

                    indexB += strideB;
                }

                c.data[indexC++] = realTotal;
                c.data[indexC++] = imgTotal;
            }
            aIndexStart += strideA;
        }
    }


    public static void multTransA_reorder(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( a.numCols == 0 || a.numRows == 0 ) {
            CommonOps_CDRM.fill(c,0,0);
            return;
        }
        float realA,imagA;

        for( int i = 0; i < a.numCols; i++ ) {
            int indexC_start = i*c.numCols*2;

            // first assign R
            realA = a.data[i*2];
            imagA = a.data[i*2+1];
            int indexB = 0;
            int end = indexB+b.numCols*2;
            int indexC = indexC_start;
            while( indexB<end ) {
                float realB = b.data[indexB++];
                float imagB = b.data[indexB++];
                c.data[indexC++] = realA*realB + imagA*imagB;
                c.data[indexC++] = realA*imagB - imagA*realB;
            }
            // now increment it
            for( int k = 1; k < a.numRows; k++ ) {
            realA = a.getReal(k,i);
            imagA = a.getImag(k,i);
                end = indexB+b.numCols*2;
                indexC = indexC_start;
                // this is the loop for j
                while( indexB<end ) {
                    float realB = b.data[indexB++];
                    float imagB = b.data[indexB++];
                    c.data[indexC++] += realA*realB + imagA*imagB;
                    c.data[indexC++] += realA*imagB - imagA*realB;
                }
            }
        }
    }

    public static void multTransA_small(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;

        for( int i = 0; i < a.numCols; i++ ) {
            for( int j = 0; j < b.numCols; j++ ) {
                int indexA = i*2;
                int indexB = j*2;
                int end = indexB + b.numRows*b.numCols*2;

                float realTotal = 0;
                float imagTotal = 0;

                // loop for k
                for(; indexB < end; indexB += b.numCols*2 ) {
                    float realA = a.data[indexA];
                    float imagA = a.data[indexA+1];
                    float realB = b.data[indexB];
                    float imagB = b.data[indexB+1];
                    realTotal += realA*realB + imagA*imagB;
                    imagTotal += realA*imagB - imagA*realB;
                    indexA += a.numCols*2;
                }

                c.data[indexC++] = realTotal;
                c.data[indexC++] = imagTotal;
            }
        }
    }

    public static void multTransB(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;
        int aIndexStart = 0;

        for( int xA = 0; xA < a.numRows; xA++ ) {
            int end = aIndexStart + b.numCols*2;
            int indexB = 0;
            for( int xB = 0; xB < b.numRows; xB++ ) {
                int indexA = aIndexStart;

                float realTotal = 0;
                float imagTotal = 0;

                while( indexA<end ) {
                    float realA = a.data[indexA++];
                    float imagA = a.data[indexA++];
                    float realB = b.data[indexB++];
                    float imagB = b.data[indexB++];
                    realTotal += realA*realB + imagA*imagB;
                    imagTotal += imagA*realB - realA*imagB;
                }

                c.data[indexC++] = realTotal;
                c.data[indexC++] = imagTotal;
            }
            aIndexStart += a.numCols*2;
        }
    }

    public static void multTransAB(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;

        for( int i = 0; i < a.numCols; i++ ) {
            int indexB = 0;
            for( int j = 0; j < b.numRows; j++ ) {
                int indexA = i*2;
                int end = indexB + b.numCols*2;

                float realTotal = 0;
                float imagTotal = 0;

                for( ;indexB<end; ) {
                    float realA = a.data[indexA];
                    float imagA = -a.data[indexA+1];
                    float realB = b.data[indexB++];
                    float imagB = -b.data[indexB++];
                    realTotal += realA*realB - imagA*imagB;
                    imagTotal += realA*imagB + imagA*realB;
                    indexA += a.numCols*2;
                }

                c.data[indexC++] = realTotal;
                c.data[indexC++] = imagTotal;
            }
        }
    }

    public static void multTransAB_aux(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c , @Nullable float []aux)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( aux == null ) aux = new float[ a.numRows*2 ];

        if( a.numCols == 0 || a.numRows == 0 ) {
            CommonOps_CDRM.fill(c,0,0);
            return;
        }
        int indexC = 0;
        for( int i = 0; i < a.numCols; i++ ) {
            int indexA = i*2;
            for( int k = 0; k < b.numCols; k++ ) {
                aux[k*2]   = a.data[indexA];
                aux[k*2+1] = a.data[indexA+1];
                indexA += a.numCols*2;
            }

            for( int j = 0; j < b.numRows; j++ ) {
                int indexAux = 0;
                int indexB = j*b.numCols*2;
                float realTotal = 0;
                float imagTotal = 0;

                for( int k = 0; k < b.numCols; k++ ) {
                    float realA = aux[indexAux++];
                    float imagA = -aux[indexAux++];
                    float realB = b.data[indexB++];
                    float imagB = -b.data[indexB++];
                    realTotal += realA*realB - imagA*imagB;
                    imagTotal += realA*imagB + imagA*realB;
                }
                c.data[indexC++] = realTotal;
                c.data[indexC++] = imagTotal;
            }
        }
    }

    public static void multAdd_reorder(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( a.numCols == 0 || a.numRows == 0 ) {
            return;
        }
        float realA,imagA;

        int indexCbase= 0;
        int strideA = a.getRowStride();
        int strideB = b.getRowStride();
        int strideC = c.getRowStride();
        int endOfKLoop = b.numRows*strideB;

        for( int i = 0; i < a.numRows; i++ ) {
            int indexA = i*strideA;

            // need to assign c.data to a value initially
            int indexB = 0;
            int indexC = indexCbase;
            int end = indexB + strideB;

                realA = a.data[indexA++];
                imagA = a.data[indexA++];

            while( indexB < end ) {
                float realB = b.data[indexB++];
                float imgB = b.data[indexB++];

                c.data[indexC++] += realA*realB - imagA*imgB;
                c.data[indexC++] += realA*imgB + imagA*realB;
            }

            // now add to it
            while( indexB != endOfKLoop ) { // k loop
                indexC = indexCbase;
                end = indexB + strideB;

                realA = a.data[indexA++];
                imagA = a.data[indexA++];

                while( indexB < end ) { // j loop
                    float realB = b.data[indexB++];
                    float imgB = b.data[indexB++];

                    c.data[indexC++] += realA*realB - imagA*imgB;
                    c.data[indexC++] += realA*imgB + imagA*realB;
                }
            }
            indexCbase += strideC;
        }
    }


    public static void multAdd_small(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int aIndexStart = 0;
        int indexC = 0;

        int strideA = a.getRowStride();
        int strideB = b.getRowStride();

        for( int i = 0; i < a.numRows; i++ ) {
            for( int j = 0; j < b.numCols; j++ ) {
                float realTotal = 0;
                float imgTotal = 0;

                int indexA = aIndexStart;
                int indexB = j*2;
                int end = indexA + strideA;
                while( indexA < end ) {
                    float realA = a.data[indexA++];
                    float imagA = a.data[indexA++];

                    float realB = b.data[indexB];
                    float imgB = b.data[indexB+1];

                    realTotal += realA*realB - imagA*imgB;
                    imgTotal += realA*imgB + imagA*realB;

                    indexB += strideB;
                }

                c.data[indexC++] += realTotal;
                c.data[indexC++] += imgTotal;
            }
            aIndexStart += strideA;
        }
    }


    public static void multAddTransA_reorder(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( a.numCols == 0 || a.numRows == 0 ) {
            return;
        }
        float realA,imagA;

        for( int i = 0; i < a.numCols; i++ ) {
            int indexC_start = i*c.numCols*2;

            // first assign R
            realA = a.data[i*2];
            imagA = a.data[i*2+1];
            int indexB = 0;
            int end = indexB+b.numCols*2;
            int indexC = indexC_start;
            while( indexB<end ) {
                float realB = b.data[indexB++];
                float imagB = b.data[indexB++];
                c.data[indexC++] += realA*realB + imagA*imagB;
                c.data[indexC++] += realA*imagB - imagA*realB;
            }
            // now increment it
            for( int k = 1; k < a.numRows; k++ ) {
            realA = a.getReal(k,i);
            imagA = a.getImag(k,i);
                end = indexB+b.numCols*2;
                indexC = indexC_start;
                // this is the loop for j
                while( indexB<end ) {
                    float realB = b.data[indexB++];
                    float imagB = b.data[indexB++];
                    c.data[indexC++] += realA*realB + imagA*imagB;
                    c.data[indexC++] += realA*imagB - imagA*realB;
                }
            }
        }
    }

    public static void multAddTransA_small(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;

        for( int i = 0; i < a.numCols; i++ ) {
            for( int j = 0; j < b.numCols; j++ ) {
                int indexA = i*2;
                int indexB = j*2;
                int end = indexB + b.numRows*b.numCols*2;

                float realTotal = 0;
                float imagTotal = 0;

                // loop for k
                for(; indexB < end; indexB += b.numCols*2 ) {
                    float realA = a.data[indexA];
                    float imagA = a.data[indexA+1];
                    float realB = b.data[indexB];
                    float imagB = b.data[indexB+1];
                    realTotal += realA*realB + imagA*imagB;
                    imagTotal += realA*imagB - imagA*realB;
                    indexA += a.numCols*2;
                }

                c.data[indexC++] += realTotal;
                c.data[indexC++] += imagTotal;
            }
        }
    }

    public static void multAddTransB(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;
        int aIndexStart = 0;

        for( int xA = 0; xA < a.numRows; xA++ ) {
            int end = aIndexStart + b.numCols*2;
            int indexB = 0;
            for( int xB = 0; xB < b.numRows; xB++ ) {
                int indexA = aIndexStart;

                float realTotal = 0;
                float imagTotal = 0;

                while( indexA<end ) {
                    float realA = a.data[indexA++];
                    float imagA = a.data[indexA++];
                    float realB = b.data[indexB++];
                    float imagB = b.data[indexB++];
                    realTotal += realA*realB + imagA*imagB;
                    imagTotal += imagA*realB - realA*imagB;
                }

                c.data[indexC++] += realTotal;
                c.data[indexC++] += imagTotal;
            }
            aIndexStart += a.numCols*2;
        }
    }

    public static void multAddTransAB(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;

        for( int i = 0; i < a.numCols; i++ ) {
            int indexB = 0;
            for( int j = 0; j < b.numRows; j++ ) {
                int indexA = i*2;
                int end = indexB + b.numCols*2;

                float realTotal = 0;
                float imagTotal = 0;

                for( ;indexB<end; ) {
                    float realA = a.data[indexA];
                    float imagA = -a.data[indexA+1];
                    float realB = b.data[indexB++];
                    float imagB = -b.data[indexB++];
                    realTotal += realA*realB - imagA*imagB;
                    imagTotal += realA*imagB + imagA*realB;
                    indexA += a.numCols*2;
                }

                c.data[indexC++] += realTotal;
                c.data[indexC++] += imagTotal;
            }
        }
    }

    public static void multAddTransAB_aux(CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c , @Nullable float []aux)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( aux == null ) aux = new float[ a.numRows*2 ];

        if( a.numCols == 0 || a.numRows == 0 ) {
            return;
        }
        int indexC = 0;
        for( int i = 0; i < a.numCols; i++ ) {
            int indexA = i*2;
            for( int k = 0; k < b.numCols; k++ ) {
                aux[k*2]   = a.data[indexA];
                aux[k*2+1] = a.data[indexA+1];
                indexA += a.numCols*2;
            }

            for( int j = 0; j < b.numRows; j++ ) {
                int indexAux = 0;
                int indexB = j*b.numCols*2;
                float realTotal = 0;
                float imagTotal = 0;

                for( int k = 0; k < b.numCols; k++ ) {
                    float realA = aux[indexAux++];
                    float imagA = -aux[indexAux++];
                    float realB = b.data[indexB++];
                    float imagB = -b.data[indexB++];
                    realTotal += realA*realB - imagA*imagB;
                    imagTotal += realA*imagB + imagA*realB;
                }
                c.data[indexC++] += realTotal;
                c.data[indexC++] += imagTotal;
            }
        }
    }

    public static void mult_reorder(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( a.numCols == 0 || a.numRows == 0 ) {
            CommonOps_CDRM.fill(c,0,0);
            return;
        }
        float realA,imagA;
        float realTmp,imagTmp;
        int indexCbase= 0;
        int strideA = a.getRowStride();
        int strideB = b.getRowStride();
        int strideC = c.getRowStride();
        int endOfKLoop = b.numRows*strideB;

        for( int i = 0; i < a.numRows; i++ ) {
            int indexA = i*strideA;

            // need to assign c.data to a value initially
            int indexB = 0;
            int indexC = indexCbase;
            int end = indexB + strideB;

            realTmp = a.data[indexA++];
            imagTmp = a.data[indexA++];
            realA = realAlpha*realTmp - imagAlpha*imagTmp;
            imagA = realAlpha*imagTmp + imagAlpha*realTmp;

            while( indexB < end ) {
                float realB = b.data[indexB++];
                float imgB = b.data[indexB++];

                c.data[indexC++] = realA*realB - imagA*imgB;
                c.data[indexC++] = realA*imgB + imagA*realB;
            }

            // now add to it
            while( indexB != endOfKLoop ) { // k loop
                indexC = indexCbase;
                end = indexB + strideB;

            realTmp = a.data[indexA++];
            imagTmp = a.data[indexA++];
            realA = realAlpha*realTmp - imagAlpha*imagTmp;
            imagA = realAlpha*imagTmp + imagAlpha*realTmp;

                while( indexB < end ) { // j loop
                    float realB = b.data[indexB++];
                    float imgB = b.data[indexB++];

                    c.data[indexC++] += realA*realB - imagA*imgB;
                    c.data[indexC++] += realA*imgB + imagA*realB;
                }
            }
            indexCbase += strideC;
        }
    }


    public static void mult_small(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int aIndexStart = 0;
        int indexC = 0;

        int strideA = a.getRowStride();
        int strideB = b.getRowStride();

        for( int i = 0; i < a.numRows; i++ ) {
            for( int j = 0; j < b.numCols; j++ ) {
                float realTotal = 0;
                float imgTotal = 0;

                int indexA = aIndexStart;
                int indexB = j*2;
                int end = indexA + strideA;
                while( indexA < end ) {
                    float realA = a.data[indexA++];
                    float imagA = a.data[indexA++];

                    float realB = b.data[indexB];
                    float imgB = b.data[indexB+1];

                    realTotal += realA*realB - imagA*imgB;
                    imgTotal += realA*imgB + imagA*realB;

                    indexB += strideB;
                }

                c.data[indexC++] = realAlpha*realTotal - imagAlpha*imgTotal;
                c.data[indexC++] = realAlpha*imgTotal + imagAlpha*realTotal;
            }
            aIndexStart += strideA;
        }
    }


    public static void multTransA_reorder(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( a.numCols == 0 || a.numRows == 0 ) {
            CommonOps_CDRM.fill(c,0,0);
            return;
        }
        float realA,imagA;
        float realTmp,imagTmp;

        for( int i = 0; i < a.numCols; i++ ) {
            int indexC_start = i*c.numCols*2;

            // first assign R
            realTmp = a.data[i*2];
            imagTmp = a.data[i*2+1];
            realA = realAlpha*realTmp + imagAlpha*imagTmp;
            imagA = realAlpha*imagTmp - imagAlpha*realTmp;
            int indexB = 0;
            int end = indexB+b.numCols*2;
            int indexC = indexC_start;
            while( indexB<end ) {
                float realB = b.data[indexB++];
                float imagB = b.data[indexB++];
                c.data[indexC++] = realA*realB + imagA*imagB;
                c.data[indexC++] = realA*imagB - imagA*realB;
            }
            // now increment it
            for( int k = 1; k < a.numRows; k++ ) {
            realTmp = a.getReal(k,i);
            imagTmp = a.getImag(k,i);
            realA = realAlpha*realTmp + imagAlpha*imagTmp;
            imagA = realAlpha*imagTmp - imagAlpha*realTmp;
                end = indexB+b.numCols*2;
                indexC = indexC_start;
                // this is the loop for j
                while( indexB<end ) {
                    float realB = b.data[indexB++];
                    float imagB = b.data[indexB++];
                    c.data[indexC++] += realA*realB + imagA*imagB;
                    c.data[indexC++] += realA*imagB - imagA*realB;
                }
            }
        }
    }

    public static void multTransA_small(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;

        for( int i = 0; i < a.numCols; i++ ) {
            for( int j = 0; j < b.numCols; j++ ) {
                int indexA = i*2;
                int indexB = j*2;
                int end = indexB + b.numRows*b.numCols*2;

                float realTotal = 0;
                float imagTotal = 0;

                // loop for k
                for(; indexB < end; indexB += b.numCols*2 ) {
                    float realA = a.data[indexA];
                    float imagA = a.data[indexA+1];
                    float realB = b.data[indexB];
                    float imagB = b.data[indexB+1];
                    realTotal += realA*realB + imagA*imagB;
                    imagTotal += realA*imagB - imagA*realB;
                    indexA += a.numCols*2;
                }

                c.data[indexC++] = realAlpha*realTotal - imagAlpha*imagTotal;
                c.data[indexC++] = realAlpha*imagTotal + imagAlpha*realTotal;
            }
        }
    }

    public static void multTransB(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;
        int aIndexStart = 0;

        for( int xA = 0; xA < a.numRows; xA++ ) {
            int end = aIndexStart + b.numCols*2;
            int indexB = 0;
            for( int xB = 0; xB < b.numRows; xB++ ) {
                int indexA = aIndexStart;

                float realTotal = 0;
                float imagTotal = 0;

                while( indexA<end ) {
                    float realA = a.data[indexA++];
                    float imagA = a.data[indexA++];
                    float realB = b.data[indexB++];
                    float imagB = b.data[indexB++];
                    realTotal += realA*realB + imagA*imagB;
                    imagTotal += imagA*realB - realA*imagB;
                }

                c.data[indexC++] = realAlpha*realTotal - imagAlpha*imagTotal;
                c.data[indexC++] = realAlpha*imagTotal + imagAlpha*realTotal;
            }
            aIndexStart += a.numCols*2;
        }
    }

    public static void multTransAB(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;

        for( int i = 0; i < a.numCols; i++ ) {
            int indexB = 0;
            for( int j = 0; j < b.numRows; j++ ) {
                int indexA = i*2;
                int end = indexB + b.numCols*2;

                float realTotal = 0;
                float imagTotal = 0;

                for( ;indexB<end; ) {
                    float realA = a.data[indexA];
                    float imagA = -a.data[indexA+1];
                    float realB = b.data[indexB++];
                    float imagB = -b.data[indexB++];
                    realTotal += realA*realB - imagA*imagB;
                    imagTotal += realA*imagB + imagA*realB;
                    indexA += a.numCols*2;
                }

                c.data[indexC++] = realAlpha*realTotal - imagAlpha*imagTotal;
                c.data[indexC++] = realAlpha*imagTotal + imagAlpha*realTotal;
            }
        }
    }

    public static void multTransAB_aux(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c , @Nullable float []aux)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( aux == null ) aux = new float[ a.numRows*2 ];

        if( a.numCols == 0 || a.numRows == 0 ) {
            CommonOps_CDRM.fill(c,0,0);
            return;
        }
        int indexC = 0;
        for( int i = 0; i < a.numCols; i++ ) {
            int indexA = i*2;
            for( int k = 0; k < b.numCols; k++ ) {
                aux[k*2]   = a.data[indexA];
                aux[k*2+1] = a.data[indexA+1];
                indexA += a.numCols*2;
            }

            for( int j = 0; j < b.numRows; j++ ) {
                int indexAux = 0;
                int indexB = j*b.numCols*2;
                float realTotal = 0;
                float imagTotal = 0;

                for( int k = 0; k < b.numCols; k++ ) {
                    float realA = aux[indexAux++];
                    float imagA = -aux[indexAux++];
                    float realB = b.data[indexB++];
                    float imagB = -b.data[indexB++];
                    realTotal += realA*realB - imagA*imagB;
                    imagTotal += realA*imagB + imagA*realB;
                }
                c.data[indexC++] = realAlpha*realTotal - imagAlpha*imagTotal;
                c.data[indexC++] = realAlpha*imagTotal + imagAlpha*realTotal;
            }
        }
    }

    public static void multAdd_reorder(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( a.numCols == 0 || a.numRows == 0 ) {
            return;
        }
        float realA,imagA;
        float realTmp,imagTmp;
        int indexCbase= 0;
        int strideA = a.getRowStride();
        int strideB = b.getRowStride();
        int strideC = c.getRowStride();
        int endOfKLoop = b.numRows*strideB;

        for( int i = 0; i < a.numRows; i++ ) {
            int indexA = i*strideA;

            // need to assign c.data to a value initially
            int indexB = 0;
            int indexC = indexCbase;
            int end = indexB + strideB;

            realTmp = a.data[indexA++];
            imagTmp = a.data[indexA++];
            realA = realAlpha*realTmp - imagAlpha*imagTmp;
            imagA = realAlpha*imagTmp + imagAlpha*realTmp;

            while( indexB < end ) {
                float realB = b.data[indexB++];
                float imgB = b.data[indexB++];

                c.data[indexC++] += realA*realB - imagA*imgB;
                c.data[indexC++] += realA*imgB + imagA*realB;
            }

            // now add to it
            while( indexB != endOfKLoop ) { // k loop
                indexC = indexCbase;
                end = indexB + strideB;

            realTmp = a.data[indexA++];
            imagTmp = a.data[indexA++];
            realA = realAlpha*realTmp - imagAlpha*imagTmp;
            imagA = realAlpha*imagTmp + imagAlpha*realTmp;

                while( indexB < end ) { // j loop
                    float realB = b.data[indexB++];
                    float imgB = b.data[indexB++];

                    c.data[indexC++] += realA*realB - imagA*imgB;
                    c.data[indexC++] += realA*imgB + imagA*realB;
                }
            }
            indexCbase += strideC;
        }
    }


    public static void multAdd_small(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int aIndexStart = 0;
        int indexC = 0;

        int strideA = a.getRowStride();
        int strideB = b.getRowStride();

        for( int i = 0; i < a.numRows; i++ ) {
            for( int j = 0; j < b.numCols; j++ ) {
                float realTotal = 0;
                float imgTotal = 0;

                int indexA = aIndexStart;
                int indexB = j*2;
                int end = indexA + strideA;
                while( indexA < end ) {
                    float realA = a.data[indexA++];
                    float imagA = a.data[indexA++];

                    float realB = b.data[indexB];
                    float imgB = b.data[indexB+1];

                    realTotal += realA*realB - imagA*imgB;
                    imgTotal += realA*imgB + imagA*realB;

                    indexB += strideB;
                }

                c.data[indexC++] += realAlpha*realTotal - imagAlpha*imgTotal;
                c.data[indexC++] += realAlpha*imgTotal + imagAlpha*realTotal;
            }
            aIndexStart += strideA;
        }
    }


    public static void multAddTransA_reorder(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( a.numCols == 0 || a.numRows == 0 ) {
            return;
        }
        float realA,imagA;
        float realTmp,imagTmp;

        for( int i = 0; i < a.numCols; i++ ) {
            int indexC_start = i*c.numCols*2;

            // first assign R
            realTmp = a.data[i*2];
            imagTmp = a.data[i*2+1];
            realA = realAlpha*realTmp + imagAlpha*imagTmp;
            imagA = realAlpha*imagTmp - imagAlpha*realTmp;
            int indexB = 0;
            int end = indexB+b.numCols*2;
            int indexC = indexC_start;
            while( indexB<end ) {
                float realB = b.data[indexB++];
                float imagB = b.data[indexB++];
                c.data[indexC++] += realA*realB + imagA*imagB;
                c.data[indexC++] += realA*imagB - imagA*realB;
            }
            // now increment it
            for( int k = 1; k < a.numRows; k++ ) {
            realTmp = a.getReal(k,i);
            imagTmp = a.getImag(k,i);
            realA = realAlpha*realTmp + imagAlpha*imagTmp;
            imagA = realAlpha*imagTmp - imagAlpha*realTmp;
                end = indexB+b.numCols*2;
                indexC = indexC_start;
                // this is the loop for j
                while( indexB<end ) {
                    float realB = b.data[indexB++];
                    float imagB = b.data[indexB++];
                    c.data[indexC++] += realA*realB + imagA*imagB;
                    c.data[indexC++] += realA*imagB - imagA*realB;
                }
            }
        }
    }

    public static void multAddTransA_small(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numRows ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numCols != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;

        for( int i = 0; i < a.numCols; i++ ) {
            for( int j = 0; j < b.numCols; j++ ) {
                int indexA = i*2;
                int indexB = j*2;
                int end = indexB + b.numRows*b.numCols*2;

                float realTotal = 0;
                float imagTotal = 0;

                // loop for k
                for(; indexB < end; indexB += b.numCols*2 ) {
                    float realA = a.data[indexA];
                    float imagA = a.data[indexA+1];
                    float realB = b.data[indexB];
                    float imagB = b.data[indexB+1];
                    realTotal += realA*realB + imagA*imagB;
                    imagTotal += realA*imagB - imagA*realB;
                    indexA += a.numCols*2;
                }

                c.data[indexC++] += realAlpha*realTotal - imagAlpha*imagTotal;
                c.data[indexC++] += realAlpha*imagTotal + imagAlpha*realTotal;
            }
        }
    }

    public static void multAddTransB(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numCols != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numRows != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;
        int aIndexStart = 0;

        for( int xA = 0; xA < a.numRows; xA++ ) {
            int end = aIndexStart + b.numCols*2;
            int indexB = 0;
            for( int xB = 0; xB < b.numRows; xB++ ) {
                int indexA = aIndexStart;

                float realTotal = 0;
                float imagTotal = 0;

                while( indexA<end ) {
                    float realA = a.data[indexA++];
                    float imagA = a.data[indexA++];
                    float realB = b.data[indexB++];
                    float imagB = b.data[indexB++];
                    realTotal += realA*realB + imagA*imagB;
                    imagTotal += imagA*realB - realA*imagB;
                }

                c.data[indexC++] += realAlpha*realTotal - imagAlpha*imagTotal;
                c.data[indexC++] += realAlpha*imagTotal + imagAlpha*realTotal;
            }
            aIndexStart += a.numCols*2;
        }
    }

    public static void multAddTransAB(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        int indexC = 0;

        for( int i = 0; i < a.numCols; i++ ) {
            int indexB = 0;
            for( int j = 0; j < b.numRows; j++ ) {
                int indexA = i*2;
                int end = indexB + b.numCols*2;

                float realTotal = 0;
                float imagTotal = 0;

                for( ;indexB<end; ) {
                    float realA = a.data[indexA];
                    float imagA = -a.data[indexA+1];
                    float realB = b.data[indexB++];
                    float imagB = -b.data[indexB++];
                    realTotal += realA*realB - imagA*imagB;
                    imagTotal += realA*imagB + imagA*realB;
                    indexA += a.numCols*2;
                }

                c.data[indexC++] += realAlpha*realTotal - imagAlpha*imagTotal;
                c.data[indexC++] += realAlpha*imagTotal + imagAlpha*realTotal;
            }
        }
    }

    public static void multAddTransAB_aux(float realAlpha , float imagAlpha , CMatrixRMaj a , CMatrixRMaj b , CMatrixRMaj c , @Nullable float []aux)
    {
        if( a == c || b == c )
            throw new IllegalArgumentException("Neither 'a' or 'b' can be the same matrix as 'c'");
        else if( a.numRows != b.numCols ) {
            throw new MatrixDimensionException("The 'a' and 'b' matrices do not have compatible dimensions");
        } else if( a.numCols != c.numRows || b.numRows != c.numCols ) {
            throw new MatrixDimensionException("The results matrix does not have the desired dimensions");
        }

        if( aux == null ) aux = new float[ a.numRows*2 ];

        if( a.numCols == 0 || a.numRows == 0 ) {
            return;
        }
        int indexC = 0;
        for( int i = 0; i < a.numCols; i++ ) {
            int indexA = i*2;
            for( int k = 0; k < b.numCols; k++ ) {
                aux[k*2]   = a.data[indexA];
                aux[k*2+1] = a.data[indexA+1];
                indexA += a.numCols*2;
            }

            for( int j = 0; j < b.numRows; j++ ) {
                int indexAux = 0;
                int indexB = j*b.numCols*2;
                float realTotal = 0;
                float imagTotal = 0;

                for( int k = 0; k < b.numCols; k++ ) {
                    float realA = aux[indexAux++];
                    float imagA = -aux[indexAux++];
                    float realB = b.data[indexB++];
                    float imagB = -b.data[indexB++];
                    realTotal += realA*realB - imagA*imagB;
                    imagTotal += realA*imagB + imagA*realB;
                }
                c.data[indexC++] += realAlpha*realTotal - imagAlpha*imagTotal;
                c.data[indexC++] += realAlpha*imagTotal + imagAlpha*realTotal;
            }
        }
    }

}
