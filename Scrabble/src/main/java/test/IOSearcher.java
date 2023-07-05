package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

// define the file searcher interface
interface FileSearcher {
	public boolean search(String word, String...fileNames);	
	public void stop();

}
public class IOSearcher implements FileSearcher{

	boolean stopMe;
	
	public IOSearcher() {
		stopMe=false;
	}
	
	public boolean search(String word, String...fileNames){
		word=word.toLowerCase();
		// boolean found=false;
		// try {
		// 	for(int i=0;!stopMe && i<fileNames.length && !found; i++) {
		// 		Scanner s=new Scanner(new File(fileNames[i]));
		// 		while(s.hasNext() && !found && !stopMe){
		// 			String w = s.next();
		// 			w = w.toLowerCase();
		// 			if(w.equals(word))
		// 				found=true;
		// 		}
		// 		s.close();
		// 	}
		// }catch(Exception e) {}
		
		// return found;

		for (String fileName : fileNames) 
		{
			// BufferedReader is typically more efficient for reading large amounts of text than Scanner
			// The FileReader provides the location of the file to read the data, 
			// while the BufferedReader is used to read the file more efficiently.
			// The BufferedReader closes automatically when the try block is exited
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
            {
                String line = br.readLine();
                while ((line != null) && (!stopMe)) 
                {
                    if (line.contains(word)) 
                    {
                        return true;
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
	}
	
	public void stop() {
		stopMe=true;
	}
}
