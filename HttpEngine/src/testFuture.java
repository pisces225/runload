import java.util.concurrent.*;

public class testFuture implements Runnable{
	
	public void run(){
		while(true){
		System.out.println("test test");

		}
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		testFuture tf = new testFuture();
		ExecutorService service = Executors.newFixedThreadPool(2);
		
		Future task = service.submit(new Callable<String>() {
			public String call() throws Exception {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "future thread";
			}
		});
		System.out.println("begin doing task");
		for(int i =0;i<3;i++){
			service.submit(new testFuture());
		}
		Thread.sleep(3000);
		String obj = (String)task.get();
		System.out.println(obj);
		service.shutdown();
		
	}
}
