import java.io.File;
import java.io.FileNotFoundException;
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
			String fileName = scanUser.next();
			File file = new File(fileName); //File of copied comment section containing all guesses so far
			try {
				scanFile = new Scanner(file);
			}
			catch (FileNotFoundException e) {} 
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
		
			System.out.println("Comments from file:\n");
			while (scanFile.hasNextLine()) 
			{
				String line = scanFile.nextLine();
				//Filter out replies and likes which will contain numbers
				if(line.contains("Reply") || line.contains("Replies") || line.length() <= 2)
				{
					line = "";
				}
				//Remove all nonnumerics
				String digits = line.replaceAll("[^0-9]", "");
				//Print out list of users with guesses and also add guesses to list
				if(!digits.isEmpty())
				{
					System.out.println(line);
					numbersList.add(Integer.parseInt(digits));
				}
			}
		
			//sort to ascending order
			Collections.sort(numbersList);
			System.out.println("\nGuesses:\n");
			System.out.println(numbersList+"\n");
			
			int bestNumber=1;    //number you should guess to have best chance of getting closest without going over
			int gap = 0;       //number of possible guesses that bestNumber could win with
			
			for(int i = 0; i < numbersList.size()-1; i++)
			{
				//Check each guess with the next largest guess to determine the gap in between
				System.out.println("Evaluating: "+numbersList.get(i+1));
				if(numbersList.get(i+1)-numbersList.get(i) > gap)
				{
					//if new largest gap is found, update gap and bestNumber
					int oldGap = gap;
					gap = numbersList.get(i+1)-numbersList.get(i);
					System.out.println(numbersList.get(i+1)+"-"+numbersList.get(i)+"="+gap);
					System.out.println("old gap: "+oldGap+" was less than new gap: "+gap);
					
					
					bestNumber = numbersList.get(i)+1;
					System.out.println("new best number: "+bestNumber);
				}
			}
			
			//give user result
			System.out.println("\nFinished evaluating guesses... In order to maximize chance of getting closest without going over, you should pick: "+bestNumber);
			
			//calculate chance of winning
			float chanceOfWin = (float)gap/maxGuess;
			chanceOfWin *=100;
			
			System.out.println("Picking "+bestNumber + " will give you "+gap+"/"+maxGuess+", or "+chanceOfWin+ "% chance of winning! Good luck!!");
	}

}
