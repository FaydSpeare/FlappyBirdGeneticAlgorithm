package NeuralNetwork;

import java.util.ArrayList;
import java.util.Random;

public class NeuralNetwork {

	private int numOfInputs, numOfOutputs;

	private int[] layers;

	private double LR = 0.05;

	private ArrayList<double[][]> outputs = new ArrayList<>();
	private ArrayList<double[][]> errorOutputs = new ArrayList<>();
	
	public ArrayList<double[][]> weights = new ArrayList<>();
	private ArrayList<double[][]> updateWeights = new ArrayList<>();
	
	// expected
	private double[][] trainingResults;
	private double[][] trainingData;

	public NeuralNetwork(int layers[]) {
		this.numOfInputs = layers[0];
		this.numOfOutputs = layers[layers.length -1];
		this.layers = layers;
		
		//Initialize outputs
		for(int i = 0; i< layers.length; i++) {
			double[][] j = new double[layers[i]][1];
			outputs.add(j);
			errorOutputs.add(j);
		}
		
		//Initialize weights
		for(int i = 0; i< layers.length-1; i++ ) {
			double[][] j = initWeights(layers[i+1],layers[i]);
			weights.add(j);
			updateWeights.add(j);
		}
	}

	public void query(double[] input) {
		
		double[][] query = new double[input.length][1];
		int number = 0;
		for(double i: input) {
			query[number][0] = i;
			number++;
		}

		Matrix.print(query);
		outputs.set(0, query);
		for(int layer = 0; layer < weights.size(); layer++) {
			 double[][] out = matrixSigmoid(Matrix.add(Matrix.multiply(weights.get(layer), outputs.get(layer)),1));
			 outputs.set(layer+1, out);
		}
		System.out.println("\n\nquery: ");
		System.out.print("(");
		for(double i: input) {
			System.out.print(i);
			if(i != input[input.length-1]) {
				System.out.print(", ");
			}
		}
		System.out.print(") ");
		System.out.print("returned:");
		Matrix.print(outputs.get(outputs.size()-1));
		
	}
	
	public double[][] queryOutput(double[] input) {
		double[][] query = new double[input.length][1];
		int number = 0;
		for(double i: input) {
			query[number][0] = i;
			number++;
		}
		outputs.set(0, query);
		for(int layer = 0; layer < weights.size(); layer++) {
			 double[][] out = matrixSigmoid(Matrix.add(Matrix.multiply(weights.get(layer), outputs.get(layer)),1));
			 outputs.set(layer+1, out);
		}
		return (outputs.get(outputs.size()-1));
	}

	// Training

	public void train(int n) {
		
		
		for(int i = 0; i < n; i++) {
			// set data and numOfOutputs
			double[][] data = new double[numOfInputs][1];
			int number = 0;
			for(double num: trainingData[i% trainingData.length]) {
				data[number][0] = num;
				number++;
			}
			outputs.set(0, data);

			
			double[][] ans = new double[numOfOutputs][1];
			int number2 = 0;
			for(double num: trainingResults[i% trainingResults.length]) {
				ans[number2][0] = num;
				number2++;
			}
			
			
			for(int layer = 0; layer < weights.size(); layer++) {
				 double[][] out = matrixSigmoid(Matrix.add(Matrix.multiply(weights.get(layer), outputs.get(layer)),1));
				 outputs.set(layer+1, out);
			}
			
			double[][] LastError = Matrix.multiply(Matrix.subtract(outputs.get(outputs.size()-1), ans), -1);
			errorOutputs.set(errorOutputs.size()-1, LastError);
			
			for(int m = 0; m < outputs.size()-1; m++) {
				double[][] error = Matrix.multiply(Matrix.transpose(weights.get(weights.size()-1-m)), Matrix.ElementMult(matrixSigmoidPrime(outputs.get(outputs.size()-1-m)), errorOutputs.get(errorOutputs.size()-1 - m)));
				errorOutputs.set(errorOutputs.size()-2-m, error);
				double[][] update = Matrix.ElementMult(Matrix.ElementMult(errorOutputs.get(errorOutputs.size()-1 - m), matrixSigmoidPrime(outputs.get(outputs.size()-1-m))), Matrix.transpose(outputs.get(outputs.size()-2-m)));
				updateWeights.set(updateWeights.size()-1-m, update);
			}
			for(int k = 0; k < weights.size(); k++) {
				weights.set(k, Matrix.add(weights.get(k), Matrix.multiply(updateWeights.get(k), LR)));
				
			}
			
			if(i % 2500 == 0) {
				//System.out.println(Matrix.Sum(Matrix.multiply(errorOutputs.get(errorOutputs.size()-1),-1)));
			}
		}
		System.out.println("\n\nTrained "+n+" times...   LearningRate = "+LR+"..." );
	}
	
	public void setTrainingData(double[][] data, double[][] dataAnswers) {
		trainingResults = dataAnswers;
		trainingData = data;
	}

	/// Sum weights
	public double[][] sumWeights(double[][] weights, double[][] inputs){
		//Matrix.print(Matrix.multiply(weights, inputs));
		return matrixSigmoid(Matrix.add(Matrix.multiply(weights, inputs),1));
	}

	/// Sigmoid Function
	
	public double sigmoid(double n) {
		return (1/(1+Math.exp(-n)));
	}
	
	/// Sigmoid Derivative function
	
	private double sigmoidPrime(double n) {
		return n*(1-n);
	}
	
	/// Element-wise application of sigmoid function
	
	private double[][] matrixSigmoid(double[][] matrix){
		int n = matrix.length;
		int m = matrix[0].length;
        double[][] b = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
            	b[i][j] = sigmoid(matrix[i][j]);
            }
        }
        return b;
	}
	
	private double[][] matrixSigmoidPrime(double[][] matrix){
		int n = matrix.length;
		int m = matrix[0].length;
        double[][] b = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
            	b[i][j] = sigmoidPrime(matrix[i][j]);
            }
        }
        return b;
	}
	
	/// Creates weights in a row*column matrix ~ Normal Distribution
	
	private double[][] initWeights(int rows, int columns){
		double[][] matrix = new double[rows][columns];
		Random Random = new Random();
		for(int i = 0; i<matrix.length;i++) {
			for(int j = 0; j<matrix[0].length;j++) {
				matrix[i][j] = Random.nextGaussian();
			}
		}
		return matrix;
	}

}

