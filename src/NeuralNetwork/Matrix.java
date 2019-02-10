package NeuralNetwork;

import java.util.ArrayList;
import java.util.Random;

public class Matrix {

    // return n-by-n identity matrix I
    public static double[][] identity(int n) {
        double[][] a = new double[n][n];
        for (int i = 0; i < n; i++)
            a[i][i] = 1;
        return a;
    }

    // return x^T y
    public static double dot(double[] x, double[] y) {
        if (x.length != y.length) throw new RuntimeException("Illegal vector dimensions.");
        double sum = 0.0;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];
        return sum;
    }

    // return B = A^T
    public static double[][] transpose(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        double[][] b = new double[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                b[j][i] = a[i][j];
        return b;
    }

    // return c = a + b
    public static double[][] add(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                c[i][j] = a[i][j] + b[i][j];
        return c;
    }

    // return c = a - b
    public static double[][] subtract(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                c[i][j] = a[i][j] - b[i][j];
        return c;
    }

    // return c = a * b
    public static double[][] multiply(double[][] a, double[][] b) {
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++)
                    c[i][j] += a[i][k] * b[k][j];
        return c;
    }

    // matrix-vector multiplication (y = A * x)
    public static double[] multiply(double[][] a, double[] x) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += a[i][j] * x[j];
        return y;
    }

    // vector-matrix multiplication (y = x^T A)
    public static double[] multiply(double[] x, double[][] a) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != m) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                y[j] += a[i][j] * x[i];
        return y;
    }
    
    public static double[][] multiply(double[][] a, double b) {
        int m = a.length;
        int n = a[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
            	a[i][j] *= b;
            }
        }
        return a;
    }
    
    public static double[][] add(double[][] a, double b) {
        int m = a.length;
        int n = a[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
            	a[i][j] += b;
            }
        }
        return a;
    }
    
    public static double[][] subtract(double[][] a, double b) {
        int m = a.length;
        int n = a[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
            	a[i][j] -= b;
            }
        }
        return a;
    }
    
    public static void print(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        for (int i = 0; i < m; i++) {
        	System.out.println("");
            for (int j = 0; j < n; j++) {
            	System.out.print(a[i][j] +", ");
            }
        }
    }
    
    public static double[][] ElementMult(double[][] a, double[][] b) {
    	if(a.length == b.length && a[0].length == b[0].length) {
    		double[][] matrix = new double[a.length][b[0].length];
    		for(int i = 0; i < a.length;i++) {
    			for(int j=0; j<a[0].length ;j++){
    				matrix[i][j] = a[i][j] * b[i][j];
    			}
    		}
        	return matrix;
    	}
    	else {
    		double[][] matrix = new double[a.length][b[0].length];

    		for(int i = 0; i < b[0].length; i++) {
    			for(int j = 0; j < a.length; j++) {
    				//System.out.println("\n"+i+", "+j);
    				matrix[j][i] = b[0][i] * a[j][0];

    				//Matrix.print(matrix);


    			}
    		}
    		return matrix;
    	}

    }

    public static double Sum(double[][] a) {
    	double sum = 0;
        int m = a.length;
        int n = a[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sum += a[i][j];
            }
        }
        return sum;
    }
    
    public static ArrayList<double[][]> SpliceWeights(ArrayList<double[][]> type1, ArrayList<double[][]> type2, Random rand){
    	ArrayList<double[][]> splicedList = new ArrayList<double[][]>();
    	for(int i = 0; i < type1.size(); i++) {
    		double[][] weight1 = type1.get(i);
    		double[][] weight2 = type2.get(i);
    		
    		int indexOneLength = weight1.length;
    		int indexTwoLength = weight1[0].length;
    		double[][] weightspliced = new double[indexOneLength][indexTwoLength];
    		for(int k = 0; k < indexOneLength; k++) {
    			for(int m = 0; m < indexTwoLength; m++) {
    				double choice1 = weight1[k][m];
    				double choice2 = weight2[k][m];
    				int chooser = rand.nextInt(100);
    				
    				if(chooser > rand.nextInt(80)) {
    					weightspliced[k][m] = choice1;
    				}
    				else {
    					weightspliced[k][m] = choice2;
    				}
    			}
    		}
    		splicedList.add(weightspliced);
    		
    	}
    	return splicedList;
    }
    
    public static ArrayList<double[][]> MutateWeights(ArrayList<double[][]> type1, Random rand){
    	ArrayList<double[][]> splicedList = new ArrayList<double[][]>();
    	for(int i = 0; i < type1.size(); i++) {
    		double[][] weight1 = type1.get(i);
    		
    		int indexOneLength = weight1.length;
    		int indexTwoLength = weight1[0].length;
    		double[][] weightspliced = new double[indexOneLength][indexTwoLength];
    		for(int k = 0; k < indexOneLength; k++) {
    			for(int m = 0; m < indexTwoLength; m++) {
    				double choice1 = weight1[k][m];
    				int chooser = rand.nextInt(100);
    				
    				if(chooser > 90) {
    					weightspliced[k][m] = choice1-choice1/100;
    				}
    				else if(chooser < 10) {
    					weightspliced[k][m] = choice1+choice1/100;
    				}
    				else {
    					weightspliced[k][m] = choice1;
    				}
    			}
    		}
    		splicedList.add(weightspliced);
    		
    	}
    	return splicedList;
    }
}

