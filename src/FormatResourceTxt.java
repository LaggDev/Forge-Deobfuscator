import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class FormatResourceTxt {
	public static void main(String[] args) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\ddude\\Desktop\\forge obfuscation.txt"));
			String line = in.readLine();
			ArrayList<String> list = new ArrayList<String>();
			while(line!=null) {
				Pattern p = Pattern.compile("(.*_.*_.*)\\s+(.*)");
				Matcher m = p.matcher(line);
				m.matches();
				list.add(m.group(1)+":"+m.group(2));
				line = in.readLine();
			}
			in.close();
			BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\ddude\\Desktop\\forge obfuscation2.txt"));
			for(int i = 0; i<list.size(); i++) {
				out.write(list.get(i) + "\r\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
