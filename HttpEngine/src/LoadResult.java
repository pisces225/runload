


public class LoadResult {
	
	private boolean succeed = true;
	private String errorCode = "000000";
	private String errorMsg = "";
	private Throwable exception = null;
	
	public final static LoadResult SUCCEED = new LoadResult();
	public final static LoadResult FAILED = new LoadResult(false,"-1","UNKNOWN REASON");
	
	public LoadResult(){
		
	}
	public LoadResult(boolean res, String errCode, String errMsg){
		this.succeed = res;
		this.errorCode = errCode;
		this.errorMsg = errMsg;
	}
	
	public LoadResult(boolean res, Throwable excep){
		this(res,"-1","unknown");
		this.exception = excep;
	}
	
	public LoadResult(boolean res, String errCode, Throwable excep){
		this(res, errCode, "UNKNOWN");
		this.exception = excep;
	}
	
	public LoadResult(boolean res, String errCode, String errMsg, Throwable excep){
		this(res,errCode,errMsg);
		this.exception = excep;
	}
	
	public boolean isSucceed(){
		return succeed;
	}
	
	public String getErrorCode(){
		return errorCode;
	}
	
	public String getErrorMsg(){
		return errorMsg;
	}
	

}
