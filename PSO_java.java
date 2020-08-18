import java.util.*;
import java.io.*;
import java.util.Map.Entry;
import java.util.Collection;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

class PSO_java{
	// Function to be optimized:
	public static double ProblemFunc(double[] buffer){
		double val = 0;
		for(int i=0;i<buffer.length;i++) {
			val = val + Math.pow(buffer[i], 2);
		}
		return val;
	} 

public static void main(String[] args) {
	double INF = Double.NEGATIVE_INFINITY;
	double maxVal = INF;
	double weight = 0.9, r1 = 0.0 , r2 = 0.0;	// Inertia weight
	double p1 = 2.05, p2 = 2.05;
	double posMin = -5.12, posMax = 5.12;
	double velocityMin = 0 , velocityMax = 1;
	double weightMin = 0.4 , weightMax = 0.9;
	double phiVal = p1 + p2;
	double chiVal = 2.0/Math.abs(2.0-phiVal-Math.sqrt(Math.pow(phiVal, 2)-4*phiVal));
    
    String[] str = new String[100000];
    
    int lenn = 0;
	
	//int no_p = 100, no_s = 1000 , no_d = 2;

    //Input taken from user
    
    Scanner sc = new Scanner(System.in);
    
    System.out.printf("\nEnter Number of Particle: ");
    int no_p = sc.nextInt();
    
    System.out.printf("\nEnter Number of Timesteps: ");
    int no_s = sc.nextInt();
    
    System.out.printf("\nEnter Number of Dimensions: ");
    int no_d = sc.nextInt();

    Random rd = new Random();
	double[] pOptimumVal = new double[no_p];		
	double[] gOptimumPrevious = new double[no_s];  // Previous fitness stats
	double[] gOptimumPos = new double[no_d];		// Best particle position
	double[] M = new double[no_p];

	double[][] pOptimumPos = new double[no_p][no_d];  // Individual Particle position
	double[][] R = new double[no_p][no_d];
	double[][] V = new double[no_p][no_d];

	// Initialisation
	for(int i=0; i<no_p; i++) {
		pOptimumVal[i] = INF;
	}
	// Populate particle velocity table with random value between (0 - (max-min+1)) range
	for(int i=0; i<no_p; i++){  
            for(int j=0; j<no_d; j++){
                R[i][j] = posMin + (posMax-posMin)*rd.nextDouble();
                V[i][j] = velocityMin + (velocityMax-velocityMin)*rd.nextDouble();
                // Global best error checkpoint
                if(rd.nextDouble() < 0.5){
                    V[i][j] = -V[i][j];
                    R[i][j] = -R[i][j];
                }
            }
        }
    
        for(int i=0; i<no_p; i++){
            M[i] = ProblemFunc(R[i]);
            M[i] = -M[i];
        }
        // Updation and finding optimum position
        for(int j=0; j<no_s; j++){ 
            for(int p=0; p<no_p;p++){         
                for(int i=0; i<no_d; i++){    
                    R[p][i] = R[p][i] + V[p][i];

                    if(R[p][i] > posMax)          { R[p][i] = posMax;}
                    else if(R[p][i] < posMin)     { R[p][i] = posMin;}
                }           
            }   
            // Iterating through all particle and calculating the fitness stats
            for(int p=0; p<no_p; p++){ 

                M[p] = ProblemFunc(R[p]);
                M[p] = -M[p];
            
                if(M[p] > pOptimumVal[p]){
                
                     pOptimumVal[p] = M[p];
                     for(int i=0; i<no_d; i++){
                        pOptimumPos[p][i] = R[p][i];
                     }
                 }
            
                if(M[p] > maxVal){
        
                    maxVal = M[p];          
                    for(int i=0; i<no_d; i++){
                       gOptimumPos[i] =  R[p][i];
                    }
                }
            
            }
            // Finally, (Previous) Current fitness stats is set.
            gOptimumPrevious[j] = maxVal;
        
            weight = weightMax - ((weightMax-weightMin)/no_s) * j;
            // Updation of velocity : Cognitive and social constant
            for(int p=0; p<no_p; p++){
                for(int i=0; i<no_d; i++){
                    
                    r1 = rd.nextDouble();
                    r2 = rd.nextDouble();
                    // Formula : v(next)=w()*v(curr)+p1*r1(pos(individual particle pos)−x(particle position))+p2*r2(pos(best opt pos)−x(particle position))
                    V[p][i] = chiVal * weight * (V[p][i] + r1 * p1 * (pOptimumPos[p][i] - R[p][i]) + r2*p2 *(gOptimumPos[i] - R[p][i]));
                
                 
                    if      (V[p][i] > velocityMax) { V[p][i] = velocityMax; }        
                    else if (V[p][i] < velocityMin) { V[p][i] = velocityMin; }
                }
            }
            
            // Store global optimum for every step
            str[lenn++] = "At i:" + j + " Global Optimum Value " + maxVal;

        }   
    
    
        //TO PRINT THE OUTPUT INTO THE SYSTEM
        
        /*for(int i = 0; i < lenn; i++)
            System.out.println(str[i]);*/
    
    
        //TO PRINT THE OUTPUT INTO THE TEXTFILE
        
        File file = new File ("Output_File.txt");
        try
        {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println ("INPUT:\n");
            printWriter.println ("Enter Number of Particle: " + no_p + "\n");
            printWriter.println ("Enter Number of Timesteps: " + no_s + "\n");
            printWriter.println ("Enter Number of Dimensions: " + no_d + "\n");
            printWriter.println ("OUTPUT:\n");
            for(int i = 0; i < lenn; i++)
                printWriter.println(str[i]);
            printWriter.close();       
        }
        catch (FileNotFoundException ex)  
        {
            System.err.print("Something went wrong");
        }
    
    }
}
