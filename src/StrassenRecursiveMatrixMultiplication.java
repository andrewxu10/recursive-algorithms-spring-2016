
public class StrassenRecursiveMatrixMultiplication {
	
	public static void main (String[] args){
		

		
		Matrix n1 = Matrix.random(4, 4);
		n1.show();
		
		System.out.println();
		
		Matrix n2 = Matrix.random(4, 4);
		n2.show();
		
		System.out.println();
		
		Matrix n3 = multiply(n1,n2);
		n3.show();
		
		System.out.println();
		

		/*System.out.println();
		Matrix n4 = join(n3, n2, 0, n2.M/2);
		n4.show();*/
		
		//Matrix n3 = multiply(n1, n2);
		/*Matrix n3 = join(n1, n2, 0, 0);
		n3.show();*/
		
		/*toMultiply.show();
		System.out.println();
		topLeft.show();
		System.out.println();
		topRight.show();
		System.out.println();
		bottomLeft.show();
		System.out.println();
		bottomRight.show();*/
	}
	
	
	public static Matrix split(Matrix toSplit, int startX, int startY) {
		
		int half = toSplit.M/2;
		Matrix newMatrix = new Matrix(half, half);
		
		for (int b = 0; b < half; b++){
			for (int j = 0; j < half; j++){
				double val = toSplit.getData()[j + startX][b + startY];
			
				newMatrix.setData(j, b, val);
			}
		}
		return newMatrix;
	}
	
	//my mental model of top right and bottom left were reversed - why?
	public static Matrix splitFourths(Matrix toMultiply, int num) {
		int half1 = toMultiply.M/2;

		if (num == 1){ //top left = 1
			Matrix toMultiplyTL = split(toMultiply, 0, 0);
			return toMultiplyTL;
		}
		
		if (num == 2){ //top right = 2
			Matrix toMultiplyBL = split(toMultiply, 0, half1);
			return toMultiplyBL;
		}
		
		if (num == 3){ //bottom left = 3
			Matrix toMultiplyTR = split(toMultiply, half1, 0);
			return toMultiplyTR;
		}
		
		if (num == 4){ //bottom right = 4
			Matrix toMultiplyBR = split(toMultiply, half1, half1);
			return toMultiplyBR;
		}

		return null; //must be 1 - 4
	}
	
	public static Matrix join(Matrix toAdd, Matrix emptyMatrix, int startX, int startY) {
		//joins two matrices (toAdd to emptyMatrix). toAdd must be half the dimensions of emptyMatrix
		
		
		int length = toAdd.M;
		//merge aka multiply
		for ( int i = 0; i < length; i++ ) {
				for ( int j = 0; j < length; j++ ) {
					
					double val = toAdd.getData()[j][i];
					emptyMatrix.setData(j + startX, i + startY, val);
				}
		}
		return emptyMatrix;
		
	}
	
	

	
	public static Matrix multiply(Matrix toMultiply1, Matrix toMultiply2) {
		
		//new matrix with same size as ones to be multiplied
		Matrix outputMatrix = new Matrix(toMultiply1.M, toMultiply1.M);
		
		if(toMultiply1.M == 1) { //why at the 0th address? ... http://www.sanfoundry.com/java-program-strassen-algorithm/
			
			//base case correct?
			double a = toMultiply1.getData()[0][0];
			double b = toMultiply2.getData()[0][0];
			outputMatrix.setData(0, 0, a*b);
			
		} else {
			
			//split each matrix into 4 quarters
			Matrix topLeft1 = splitFourths(toMultiply1, 1); //A (top left)
			Matrix topRight1 = splitFourths(toMultiply1, 2); //B (top right)
			Matrix bottomLeft1 = splitFourths(toMultiply1, 3); //C (bottom left)
			Matrix bottomRight1 = splitFourths(toMultiply1, 4); //D (bottom right)
			
			Matrix topLeft2 = splitFourths(toMultiply2, 1); //E (top left)
			Matrix topRight2 = splitFourths(toMultiply2, 2); //F (top right)
			Matrix bottomLeft2 = splitFourths(toMultiply2, 3); //G (bottom left)
			Matrix bottomRight2 = splitFourths(toMultiply2, 4); //H (bottom right)
			
			//initialize all 7 mini matrices necessary to create a multiplied table
			Matrix M1 = new Matrix(topLeft1.M, topLeft1.M);
			Matrix M2 = new Matrix(topLeft1.M, topLeft1.M);
			Matrix M3 = new Matrix(topLeft1.M, topLeft1.M);
			Matrix M4 = new Matrix(topLeft1.M, topLeft1.M);
			Matrix M5 = new Matrix(topLeft1.M, topLeft1.M);
			Matrix M6 = new Matrix(topLeft1.M, topLeft1.M);
			Matrix M7 = new Matrix(topLeft1.M, topLeft1.M);
			
			//fill mini-matrices with necessary values calculated from the 8 quarter-matrices
			M1 = multiply(topLeft1.plus(bottomRight1), topLeft2.plus(bottomRight2));
			M2 = multiply(topRight1.plus(bottomRight1), topLeft2);
			M3 = multiply(topLeft1, bottomLeft2.minus(bottomRight2));
			M4 = multiply(bottomRight1, topRight2.minus(topLeft2));
			M5 = multiply(topLeft1.plus(bottomLeft1), bottomRight2);
			M6 = multiply(topRight1.minus(topLeft1), topLeft2.plus(bottomLeft2));
			M7 = multiply(bottomLeft1.minus(bottomRight1), topRight2.plus(bottomRight2));
			
			
			/* a11 = top left (1)
			a12 = bottom left (3)
			a21 = top right (2)
			a22 = bottom right (4) */
			
			/*
            M1 = (A11 + A22)(B11 + B22)
            M2 = (A21 + A22) B11
            M3 = A11 (B12 - B22)
            M4 = A22 (B21 - B11)
            M5 = (A11 + A12) B22
            M6 = (A21 - A11) (B11 + B12)
            M7 = (A12 - A22) (B21 + B22)
			*/
			
			/*P1 = topLeft1.times(topRight2.minus(bottomRight2)); //A(F-H)
			P2 = topLeft1.plus(topLeft1).times(bottomRight2); //(A+B)H
			P3 = bottomLeft1.plus(bottomRight1).times(topLeft2); //(C+D)E
			P4 = bottomRight1.plus(bottomLeft2.minus(topLeft2)); //D(G-E)
			P5 = topLeft1.plus(bottomRight1).times(topLeft2.plus(bottomRight2)); //(A+D)(E+H)
			P6 = topRight1.minus(bottomRight1).times(bottomLeft2.plus(bottomRight2)); //(B-D)(G+H)
			P7 = topLeft1.minus(bottomLeft1).times(topLeft2.plus(topRight2)); //(A-C)(E+F)*/
			
			/*
            C11 = M1 + M4 - M5 + M7
            C12 = M3 + M5
            C21 = M2 + M4
            C22 = M1 - M2 + M3 + M6
            */
			
			//calculate final 4 matrices before joining to get the final product
			Matrix C11 = M1.plus(M4).minus(M5).plus(M7); //top left
			Matrix C12 = M3.plus(M5); //top right
			Matrix C21 = M2.plus(M4); //bottom left
			Matrix C22 = M1.minus(M2).plus(M3).plus(M6); //bottom right
			
			outputMatrix = join(C11, outputMatrix, 0, 0);
			outputMatrix = join(C12, outputMatrix, 0, outputMatrix.M/2);
			outputMatrix = join(C21, outputMatrix, outputMatrix.M/2, 0);
			outputMatrix = join(C22, outputMatrix, outputMatrix.M/2, outputMatrix.M/2);
			
			//outputMatrix.show();
			
		}

		
		//ae + bg // af + bh
		//ce + dg // cf + dh
		
		//outputMatrix.show();
		return outputMatrix;
	}

}
