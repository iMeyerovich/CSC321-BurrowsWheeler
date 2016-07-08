/**
 * @author Ivan Meyerovich
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BWTBackward {
	@SuppressWarnings("unused")
	private int charAmt;
	private String inFile;
	private String outFile;
	
	public BWTBackward(int charAmt, String inFile, String outFile) throws IOException{
		this.charAmt = charAmt;
		this.inFile = inFile;
		this.outFile = outFile;
		FileProcessor();
	}
	
	/**
	 * Takes input file and removes [charAmt] length segments to send to the unBWToutput
	 * Note: given the way I set up my locator values in BWTForward, the <> characters are
	 * used to determine the length of String removed. I am aware that this is not a universally
	 * applicable method, but MobyDick.txt has no < or > characters native to it and this
	 * assignment is due in a few hours...
	 * @throws IOException
	 */
	private void FileProcessor() throws IOException{
		long start = System.currentTimeMillis();
		
		InputStream in;
		Reader reader;
		Reader buffer = null;
		
		try{
			in = new FileInputStream(inFile);
			reader = new InputStreamReader(in); // buffer for efficiency
			buffer = new BufferedReader(reader);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		PrintWriter writer = new PrintWriter(outFile, "ASCII");
		String holder = "x";
		int r;
        while ((r = buffer.read()) != -1) {
            char ch = (char) r;
            holder = holder + Character.toString(ch);
            if(Character.toString(ch).equals(">"))
            {
            	unBWToutput(holder.substring(1), writer);
            	holder = "x";
            }
        }
        
        writer.close();
        
        double totaltime = (double) System.currentTimeMillis()-start;
        System.out.format("Reverse BWT Runtime: %.4f"+"s", totaltime/1000);
	}
	
	/**
	 * uBWToutput reverses the BWT process to output the original String
	 * @param holder - current String segment being worked on
	 * @param writer
	 */
	private void unBWToutput(String holder, PrintWriter writer) {
		List<String> sortunBWT = new ArrayList<String>();
		
		String requiredString = holder.substring(holder.indexOf("<") + 1, holder.indexOf(">"));
		int locatorVal = Integer.parseInt(requiredString);
		
		String holder2 = holder.substring(0, holder.indexOf("<"));
		
		for(int i = 0; i < holder2.length(); i++){ //Initialize Strings
			sortunBWT.add(Character.toString(holder2.charAt(i)));
		}
		Collections.sort(sortunBWT);
		
		for(int i = 0; i < holder2.length()-1; i++){
			for(int j = 0; j < holder2.length(); j++){
				String temp = Character.toString(holder2.charAt(j)) + sortunBWT.get(j);
				sortunBWT.set(j, temp);
			}
			Collections.sort(sortunBWT);
		}
		
		String output = sortunBWT.get(locatorVal);
		
		writer.print(output);
	}
}
