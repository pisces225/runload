import java.util.Properties;
import java.util.regex.*;
import java.io.*;

public class RunLoad {
	public static long convertDuration1(String duration){
		long time = 0L;
		String regex_day = "\\d+[d|D]";
		String regex_hour = "\\d+[h|H]";
		String regex_minute = "\\d+[m|M]";
		String regex_second = "\\d+[s|S]";
		Pattern p_d= Pattern.compile(regex_day);
		Matcher m_d = p_d.matcher(duration);
		if(m_d.find()){
			System.out.println(m_d.group());
			time += Integer.parseInt(m_d.group().substring(0, m_d.group().length()-1)) * 3600 * 24;
		}
		Pattern p_h= Pattern.compile(regex_hour);
		Matcher m_h = p_h.matcher(duration);
		if(m_h.find()){
			System.out.println(m_h.group());
			time += Integer.parseInt(m_h.group().substring(0, m_h.group().length()-1)) * 3600;
		}
		Pattern p_m= Pattern.compile(regex_minute);
		Matcher m_m = p_m.matcher(duration);
		if(m_m.find()){
			System.out.println(m_m.group());
			time += Integer.parseInt(m_m.group().substring(0, m_m.group().length()-1)) * 60;
		}
		Pattern p_s= Pattern.compile(regex_second);
		Matcher m_s = p_s.matcher(duration);
		if(m_s.find()){
			System.out.println(m_s.group());
			time += Integer.parseInt(m_s.group().substring(0, m_s.group().length()-1));
		}
		return time;
	}
	
	public static long convertDuration(String duration){
		long time = 0;
		String regex = "\\d+";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(duration);
		while(mat.find()){
			String val = mat.group();
			if(mat.end() < duration.length())
				switch(duration.charAt(mat.end())){
				case 'h':;
				case 'H': time += Integer.parseInt(val) * 3600;break;
				case 'd':;
				case 'D': time += Integer.parseInt(val) * 3600 * 24;break;
				case 'm':;
				case 'M': time += Integer.parseInt(val) * 60; break;
				case 's':;
				case 'S': time += Integer.parseInt(val);break;
				default : time += Integer.parseInt(val);break;
				}
			else{
				time += Integer.parseInt(val);
			}
		}

		return time;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties prop = new Properties();  
	    FileInputStream fis = null;
		try {
			fis = new FileInputStream("E://sample.properties");
			prop.load(fis);
//			prop.list(System.out);
			String tester = prop.getProperty("tester");
			System.out.println(tester);
			String duration = prop.getProperty("duration");
//			String duration = "1233d44h33m33321s";
			long testTime = convertDuration(duration);
			System.out.println(testTime);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}

}
