import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deobfuscator {
	
	public static void main(String[] args) throws IOException {
		while(true) {
			Deobfuscator deobfuscator = new Deobfuscator();
			System.out.println("Please enter the path to a file or directory, and hit enter:");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String filePath = in.readLine();
			File f = new File(filePath);
			if(f.exists()) {
				deobfuscator.run(f);
				break;
			} else {
				System.out.println("That is an invalid file path! Try again.");
			}
		}
	}
	
	public void run(File base) {
		List<File> translators = getFiles(new File("./resources"),(File f) -> f.getName().endsWith(".csv"));
		Map<String,String> map = createMap(translators);
		List<File> toFix = getFiles(base, (File f) -> f.getName().endsWith(".java"));
		for(File f : toFix) {
			fix(f,map);
		}
	}
	
	public void fix(File f, Map<String,String> map) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line;
			Pattern p = Pattern.compile("((func|field)_\\d+_[a-zA-Z]+)");
			List<String> newLines = new ArrayList<String>();
			while((line = in.readLine())!=null) {
				String newLine = line;
				Matcher m = p.matcher(line);
				while(m.find()) {
					if(map.containsKey(m.group(1))) {
						newLine = newLine.replaceAll(m.group(1), map.get(m.group(1)));
					}
				}
				newLines.add(newLine);
			}
			in.close();
			f.delete();
			f.createNewFile();
			PrintWriter out = new PrintWriter(new FileWriter(f));
			for(String s : newLines) {
				out.println(s);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Map<String,String> createMap(List<File> translators) {
		Map<String,String> map = new HashMap<String,String>();
		for(File f : translators) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(f));
				String line;
				Pattern p = Pattern.compile("(.*),(.*),.*,.*");
				while((line = in.readLine())!=null) {
					Matcher m = p.matcher(line);
					if(m.find()) {
						map.put(m.group(1), m.group(2));
					}
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public List<File> getFiles(File base, SearchQuery search) {
		List<File> found = new ArrayList<File>();
		if(!base.isDirectory()) {
			if(search.search(base)) {
				found.add(base);
			}
		} else {
			for(File f : base.listFiles()) {
				found.addAll(getFiles(f,search));
			}	
		}
		return found;
	}
	
	public interface SearchQuery {
		abstract boolean search(File f);
	}
}	
