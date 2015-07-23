import java.util.Properties;
import java.io.*;

public class RunLoad {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties prop = new Properties();  
	    FileInputStream fis = null;
		try {
			fis = new FileInputStream("E://sample.properties");
			prop.load(fis);
			prop.list(System.out);
			String tester = prop.getProperty("tester");
			System.out.println(tester);
			  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}

}
