import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileUtils {
	public static String read(String path){
			File file                     = new File(path);
			StringBuilder fileContents    = new StringBuilder((int)file.length());
			Scanner scanner               = null;
			String lineSeparator          = System.getProperty("line.separator");

			try {
				scanner = new Scanner(file);
				while(scanner.hasNextLine()) {
					fileContents.append(scanner.nextLine());
					if (scanner.hasNextLine()) {
						fileContents.append(lineSeparator);
					}
				}
				return fileContents.toString();
			}

			catch (FileNotFoundException e) {
				System.out.println("file not found!");
			}

			finally {
				if (scanner != null) {
					scanner.close();
				}
			}

			return null;
	}
}
