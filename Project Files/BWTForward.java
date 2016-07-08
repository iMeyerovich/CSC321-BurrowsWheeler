import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ivan Meyerovich
 */

public class BWTForward {
	private int charAmt;
	private String inFile;
	private String outFile;
	private int max;
	private char maxChar;
	
	public BWTForward(int charAmt, String inFile, String outFile) throws IOException{
		this.charAmt = charAmt;
		this.inFile = inFile;
		this.outFile = outFile;
		FileProcessor();
	}
	
	/**
	 * Takes input file and removes [charAmt] length segments to send to the BWToutput
	 * @throws IOException
	 */
	private void FileProcessor() throws IOException{
		long start = System.currentTimeMillis();
		
		InputStream in;
		Reader reader;
		Reader buffer = null;
		
		try{
			in = new FileInputStream(inFile);
			reader = new InputStreamReader(in);
			buffer = new BufferedReader(reader);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		PrintWriter writer = new PrintWriter(outFile, "ASCII");
		String holder = "x";
		int counter = 0;
		int r;
        while ((r = buffer.read()) != -1) {
            char ch = (char) r;
            holder += Character.toString(ch);
            counter++;
            if(counter == charAmt) //charAmt length segments
            {
            	BWToutput(holder.substring(1), writer);
            	holder = "x";
            	counter = 0;
            }
        }
        if((r = buffer.read()) == -1){ //For the final potential short segment
        	BWToutput(holder.substring(1), writer);
        }
        
        writer.close();
        
        double totaltime = (double) System.currentTimeMillis()-start;
        System.out.format("BWT Runtime: %.4f"+"s%n", totaltime/1000);
        System.out.println("Longest run: "+max+", for the character \""+maxChar+"\"");
	}
	
	/**
	 * BWToutput Processes the the input String with the Burrows-Wheeler Transform
	 * and Writes the BWT section to an output file.
	 * @param holder - current String segment being transformed
	 * @param writer - As it writes one segment at a time, the Writer is passed
	 */
	private void BWToutput(String holder, PrintWriter writer){
		List<String> sortBWT = new ArrayList<String>(); 
		sortBWT.add(holder);
		
		for(int i = 0; i < holder.length()-1; i++){
			String temp = sortBWT.get(i); //Grab existing String
			temp = temp.substring(1) + temp.charAt(0); //move first char to end
			sortBWT.add(temp);
		}
		
		Collections.sort(sortBWT);
		
		int locatorVal=-1;
		for(int i = 0; i < holder.length(); i++){
			if(sortBWT.get(i).equals(holder)){
				locatorVal = i;
				break;
			}
		}
		
		String output = Character.toString(sortBWT.get(0).charAt(holder.length()-1));
		for(int i = 1; i < holder.length(); i++){
			output += Character.toString(sortBWT.get(i).charAt(holder.length()-1));
		}
		
		MaxCharLocator(output);
		
		output += "<"+locatorVal+">"; //Best way I could come up with including the location value
		writer.print(output);
	}
	
	/**
	 * Courtesy of "justhalf" @
	 * http://stackoverflow.com/questions/19579568/most-repeating-character-in-a-string
	 * @param output
	 */
	private void MaxCharLocator(String output){
		char[] array = output.toCharArray();
		int count = 1;
		for(int i=1; i<array.length; i++){ // Start from 1 since we want to compare it with the char in index 0
		    if(array[i]==array[i-1]){
		        count++;
		    } else {
		        if(count>max){  // Record current run length, is it the maximum?
		            max=count;
		            maxChar=array[i-1];
		        }
		        count = 1; // Reset the count
		    }
		}
		if(count>max){
		    max=count; // This is to account for the last run
		    maxChar=array[array.length-1];
		}
	}
}
