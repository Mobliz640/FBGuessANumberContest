import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
		
		Scanner scanFile = null;
		Scanner scanUser = new Scanner(System.in);
		while(scanFile == null)
		{
			System.out.println("Please specify complete path of input guesses. Example: \"C:\\Users\\Scott\\Desktop\\guesses.txt\"");
			String fileName = scanUser.nextLine();
			File file = new File(fileName); //File of copied comment section containing all guesses so far
			try {
				scanFile = new Scanner(file, StandardCharsets.UTF_8);
			}
			catch (FileNotFoundException e) {
				System.out.println("File not found!");
			} catch (IOException e) {
				System.out.println("IOException!");
				return;
			} 
		}
		
		int minGuess = 0;
		int maxGuess = 0;
		while(!(minGuess < maxGuess))
		{
			System.out.println("Lowest integer you can guess?");
			minGuess = scanUser.nextInt();
			System.out.println("Highest integer you can guess?");
			maxGuess = scanUser.nextInt();
		}
		
		List<Integer> numbersList = new ArrayList<Integer>(); //List to store all numbers once file is stripped of nonnumerics	    
		
			System.out.println("\nComments from file:");
			while (scanFile.hasNextLine()) 
			{
				String line = scanFile.nextLine();
				//Filter out replies
				if(line.contains("Reply") || line.contains("Replies"))
				{
					line = "";
				}
				//Filter out all nonnumeric characters
				String digits = line.replaceAll("[^0-9]", "");
				//Print out list of guesses, and add guesses to numbersList
				if(!digits.isEmpty())
				{
					System.out.println(line);
					numbersList.add(Integer.parseInt(digits));
				}
			}
		
			//sort to ascending order
			Collections.sort(numbersList);
			System.out.println("\nGuesses:");
			System.out.println(numbersList+"\n");
			
			int bestNumber=1;      //number you should guess to have best chance of getting closest to the winning number without going over
			int secondBestNumber = 1;  //number you should guess to have best chance of getting closest to the winning number without going over
			int bestGap = 0;       //number of possible winning numbers that bestNumber could win with
			int secondBestGap = 0;  //number of possible winning numbers that secondBestNumber could win with
			
			//check the lowest guess against minGuess bound
			System.out.println("Evaluating bound: "+minGuess);
			if(numbersList.get(0)-minGuess > bestGap)
			{
				//if new largest gap is found, update both bestGap/secondBestGap and bestNumber/secondBestNumber
				secondBestGap = bestGap;
				bestGap = numbersList.get(0)-minGuess;
				System.out.println(numbersList.get(0)+"-"+minGuess+"="+bestGap);
				System.out.println("old best gap: "+secondBestGap+" was less than new best gap: "+bestGap);
				
				secondBestNumber = bestNumber;
				bestNumber = minGuess;
				System.out.println("new best number: "+bestNumber);
			}
			else if(numbersList.get(0)-minGuess > secondBestGap)
			{
				//new second largest gap was found, update secondBestGap and secondBestNumber
				int oldGap = secondBestGap;
				secondBestGap = numbersList.get(0)-minGuess;
				secondBestNumber = minGuess;
				System.out.println(numbersList.get(0)+"-"+minGuess+"="+secondBestGap);
				System.out.println("old second best gap: "+oldGap+" was less than new second best Gap: "+secondBestGap);
				System.out.println("new second best number: "+secondBestNumber);
			}
			
			//check the highest guess against maxGuess bound
			System.out.println("Evaluating bound: "+maxGuess);
			if(maxGuess-numbersList.get(numbersList.size()-1) > bestGap)
			{
				//if new largest gap is found, update both bestGap/secondBestGap and bestNumber/secondBestNumber
				secondBestGap = bestGap;
				bestGap = maxGuess-numbersList.get(numbersList.size()-1);
				System.out.println(maxGuess+"-"+numbersList.get(numbersList.size()-1)+"="+bestGap);
				System.out.println("old best gap: "+secondBestGap+" was less than new best gap: "+bestGap);
				
				secondBestNumber = bestNumber;
				bestNumber = numbersList.get(numbersList.size()-1)+1;
				System.out.println("new best number: "+bestNumber);
			}
			else if(maxGuess-numbersList.get(numbersList.size()-1) > secondBestGap)
			{
				//new second largest gap was found, update secondBestGap and secondBestNumber
				int oldGap = secondBestGap;
				secondBestGap = maxGuess-numbersList.get(numbersList.size()-1);
				secondBestNumber = numbersList.get(numbersList.size()-1)+1;
				System.out.println(maxGuess+"-"+numbersList.get(numbersList.size()-1)+"="+secondBestGap);
				System.out.println("old second best gap: "+oldGap+" was less than new second best gap: "+secondBestGap);
				System.out.println("new second best number: "+secondBestNumber);
			}
			
			for(int i = 0; i < numbersList.size()-1; i++)
			{
				//Check each guess with the next largest guess to determine the gap in between
				System.out.println("Evaluating: "+numbersList.get(i+1));
				if(numbersList.get(i+1)-numbersList.get(i) > bestGap)
				{
					//if new largest gap is found, update both bestGap/secondBestGap and bestNumber/secondBestNumber
					secondBestGap = bestGap;
					bestGap = numbersList.get(i+1)-numbersList.get(i);
					System.out.println(numbersList.get(i+1)+"-"+numbersList.get(i)+"="+bestGap);
					System.out.println("old best gap: "+secondBestGap+" was less than new best gap: "+bestGap);
					
					secondBestNumber = bestNumber;
					bestNumber = numbersList.get(i)+1;
					System.out.println("new best number: "+bestNumber);
				}
				else if(numbersList.get(i+1)-numbersList.get(i) > secondBestGap)
				{
					//new second largest gap was found, update secondBestGap and secondBestNumber
					int oldGap = secondBestGap;
					secondBestGap = numbersList.get(i+1)-numbersList.get(i);
					secondBestNumber = numbersList.get(i)+1;
					System.out.println(numbersList.get(i+1)+"-"+numbersList.get(i)+"="+secondBestGap);
					System.out.println("old second best gap: "+oldGap+" was less than new second best gap: "+secondBestGap);
					System.out.println("new second best number: "+secondBestNumber);
				}
			}
					
			//calculate chance of winning
			float bestChanceOfWin = (float)bestGap/maxGuess;
			bestChanceOfWin *=100;
			
			float secondBestChanceOfWin = (float)secondBestGap/maxGuess;
			secondBestChanceOfWin *=100;
			
			int totalGap = bestGap + secondBestGap;
			float totalChanceOfWin = bestChanceOfWin + secondBestChanceOfWin;
			
			//give user result
			System.out.println("\nFinished evaluating guesses...\n\nIn order to maximize the chance of getting closest without going over, you should pick: "+bestNumber);
			
			System.out.println("Picking "+bestNumber + " will give you "+bestGap+"/"+maxGuess+", or "+bestChanceOfWin+ "% chance of winning!");
			
			System.out.println("\nFor a second number, you should pick: "+secondBestNumber);
			
			System.out.println("Picking "+secondBestNumber + " will give you "+secondBestGap+"/"+maxGuess+", or "+secondBestChanceOfWin+ "% chance of winning!");
			
			System.out.println("\nBy picking both numbers, you will have "+totalGap+"/"+maxGuess+", or "+totalChanceOfWin+ "% chance of winning!\nGood luck!!");
			
	}

}
