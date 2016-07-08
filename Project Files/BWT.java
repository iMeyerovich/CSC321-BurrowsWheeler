import java.io.IOException;

/**
 * @author Ivan Meyerovich
 */

public class BWT {

	public static void main(String[] args) throws IOException {
		String direction = args[0];
		String cA = args[1];
		int charAmt = Integer.parseInt(cA);
		String inFile = args[2];
		String outFile = args[3];
		
		if(direction.equals("+")){
			BWTForward Forward = new BWTForward(charAmt, inFile, outFile);
		}else if(direction.equals("-")){
			BWTBackward Backward = new BWTBackward(charAmt, inFile, outFile);
		}else{
			System.out.println("\"" + direction + "\" is not a valid direction.");
		}	
	}
}
