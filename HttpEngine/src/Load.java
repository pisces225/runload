import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import java.io.*;



public abstract class Load {
	Properties task_prop = null;
	String name = null;
	int id = 0;
	Future[] fs = null;
	private ExecutorService es;
	protected static Logger logger = Logger.getLogger("task");
	
	public Load(){
		try {
			FileHandler handler = new FileHandler("./test.log");
			handler.setLevel(Level.INFO);			
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	void initLoad(String loadName,Properties prop){
		setName(loadName);
		setId(Integer.parseInt(prop.getProperty(loadName)));
		task_prop = new Properties();
		String numThread = prop.getProperty(String.valueOf(getId())+".thread", String.valueOf(10));
		task_prop.setProperty("thread", numThread);
		String duration = prop.getProperty(String.valueOf(getId())+".thread", String.valueOf(10));
		task_prop.setProperty("duration", duration);
		String rampup = prop.getProperty(String.valueOf(getId())+".thread", String.valueOf(100));
		task_prop.setProperty("rampup", rampup);
		String minThink = prop.getProperty(String.valueOf(getId())+".thread", String.valueOf(1));
		task_prop.setProperty("minThink", minThink);
		String maxThink = prop.getProperty(String.valueOf(getId())+".thread", String.valueOf(10));
		task_prop.setProperty("maxThink", maxThink);
	}
	
	void setId(int id){
		this.id=id;
	}
	int getId(){
		return id;
	}
	
	public abstract LoadResult doTask();
	
	void setName(String name){
		this.name = name;
	}
	
	String getName(){
		return this.name;
	}
	
	protected void setTask_prop(Properties prop) {
		this.task_prop = prop;
	}
	
	protected String strParameter(String paraName) {
		return task_prop.containsKey(paraName) ? task_prop
				.getProperty(paraName) : null;
	}

	protected String strParameter(String paraName, String defaultValue) {
		return task_prop.containsKey(paraName) ? task_prop
				.getProperty(paraName) : defaultValue;
	}

	protected int intParameter(String paraName, int defaultValue) {
		String v = task_prop
				.getProperty(paraName, String.valueOf(defaultValue));
		return Integer.parseInt(v);
	}

	protected int intParameter(String paraName) {
		return intParameter(paraName, 0);
	}

	protected boolean boolParameter(String paraName, boolean defaultValue) {
		if (task_prop.containsKey(paraName)) {
			if ("true".equalsIgnoreCase(task_prop.getProperty(paraName))) {
				return true;
			}
			return false;
		}
		return defaultValue;
	}

	protected boolean boolParameter(String paraName) {
		return boolParameter(paraName, false);
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
	public void startLoad(){
		String tester = strParameter("tester");
		logger.info(tester);
		
		String duration = strParameter("duration");
		final long testTime = convertDuration(duration);
		System.out.println(testTime);
		Thread timer = null;
		if(testTime > 0){
			timer = new Thread(){
				public void run() {
					try {
						for (int i = 0; i < 10; i++) {
							Thread.sleep(testTime * 100L);
						}
						stopLoad();
					} catch (InterruptedException e) {
						System.err.println("Test Duration Timer was stopped in force");
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.start();
		}
		int rampup = intParameter("rampup");
		int numThread = intParameter("thread");
		System.out.println(numThread);
		fs = new Future[numThread];
		es = Executors.newFixedThreadPool(numThread);
		for (int i = 1; i <= numThread; i++) {
			final String threadId = "T" + i;
			Runnable r = new Runnable() {
				public void run() {
					System.out.println("Start Thread "+threadId);
					doTask();
				}
			};
			try {
				fs[i - 1] = es.submit(r);
			} catch (RejectedExecutionException e) {
				
				break;
			}
			if (rampup > 0 && numThread > 1 && numThread != i) {
				try {
					Thread.sleep(rampup / numThread * 1000L);
				} catch (Exception e) {
					
				}
			}
		}
		for (int i = 0; i < numThread; i++) {
			try {
				if (fs[i] != null && !fs[i].isCancelled()
						&& !fs[i].isDone()) {
					fs[i].get();
				}
			} catch (Exception e) {
				
			}
		}
		if (timer != null) {
			timer.interrupt();
		}
		if (!es.isShutdown() || !es.isTerminated()) {
			es.shutdownNow();
		}

	}
	
	public void stopLoad(){
		int p = 0;
		if(fs != null){
			for(Future ft:fs){
				logger.info("Force stopping task "+(++p));
				ft.cancel(true);
			}
		}
	}
}
