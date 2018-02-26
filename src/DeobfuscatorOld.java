import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class DeobfuscatorOld {
	
	public static String translate(String identifier) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\ddude\\Desktop\\forge obfuscation2.txt"));
			String line = in.readLine();
			while(line!=null) {
				String[] split = line.split(":");
				if(split[0].equals(identifier)) {
					return split[1];
				}
				line = in.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/*TODO ERROR*/" + identifier;
	}
	
	public static ArrayList<File> findfiles(File folder) {
		File[] files = folder.listFiles();
		ArrayList<File> list = new ArrayList<File>();
		for(int i = 0; i<files.length; i++) {
			if(files[i].isDirectory()) {
				ArrayList<File> sublist = findfiles(files[i]);
				for(int j = 0; j<sublist.size(); j++) {
					list.add(sublist.get(j));
				}
			} else {
				list.add(files[i]);
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		try {
			File f = new File("C:\\Users\\ddude\\Desktop\\orange's simple mods\\src\\main\\java\\com\\orangemarshall");
			ArrayList<File> files = findfiles(f);
			for(int i = 0; i<files.size(); i++) {
				//System.out.println(files.get(i).getName());
				
				BufferedReader in = new BufferedReader(new FileReader(files.get(i)));
				ArrayList<String> list = new ArrayList<String>();
				String line = in.readLine();
				while(line!=null) {
					Pattern p = Pattern.compile("(func_\\d+_[a-zA-Z]+|field_\\d+_[a-zA-Z]+)");
					Matcher m = p.matcher(line);
					int count = 0;
					while(m.find()) {
						System.out.println(m.group(1) + " to -> " + translate(m.group(1)));
						System.out.println("old string: " + line);
						line = line.substring(0, m.start(1)-count) + translate(m.group(1)) + line.substring(m.end(1)-count,line.length());
						System.out.println("new string: " + line);
						count+=m.group(1).length()-translate(m.group(1)).length();
					}
					list.add(line);
					line = in.readLine();
				}
				in.close();
				
				BufferedWriter out = new BufferedWriter(new FileWriter(files.get(i)));
				for(int j = 0; j<list.size(); j++) {
					out.write(list.get(j) + "\r\n");
				}
				out.close();
				
			}
			
			
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}
}
