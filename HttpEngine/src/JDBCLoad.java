import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;

import oracle.ucp.UniversalConnectionPoolAdapter;
import oracle.ucp.UniversalConnectionPoolException;
import oracle.ucp.jdbc.*;
import oracle.ucp.admin.*;


public class JDBCLoad extends RunLoad {
	UniversalConnectionPoolManager mgr;
	PoolDataSource pds;
	DataSource ds;
	
	@Override
	public void doTask() {
//		System.out.println("run into do task");
		// TODO Auto-generated method stub
		if(ds != null){
			while(true){
				try {
	//				System.out.println("getting a connection");
					
					Connection conn = ds.getConnection();
					Statement stmt = conn.createStatement();
					long start = new Date().getTime();
	//				System.out.println("executing query");
					ResultSet st = stmt.executeQuery("select * from largeTable");
					int count = 0;
					while(st.next()){
						count++;
					}
					long end = new Date().getTime();
	//				System.out.println("count:"+count);
	//				System.out.println("fetchsize:"+ size);
					System.out.println("end : "+end+" - start-- "+start);
					System.out.println("uptime:"+(end-start)/100);
					st.close();
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			System.out.println("ds is null");
		}
	}
	
	public JDBCLoad(){
		//initTask();
		createDataSource();
	}
	
	public void createDataSource(){
		PoolDataSource pds = null;
		try {
			pds = PoolDataSourceFactory.getPoolDataSource();
			pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
			pds.setURL("jdbc:oracle:thin:@//slc07imk.us.oracle.com:1521/orcl");
			pds.setUser("nodejs");
			pds.setPassword("nodejs");
			pds.setMinPoolSize(50);
			pds.setMaxPoolSize(300);
			pds.setInitialPoolSize(50);
			pds.setInactiveConnectionTimeout(10);
			ds = pds;
			System.out.println("get data source");
		} catch (Exception e) {
			logger.info("Failed to create database connection pool");
		}
	}
	
	public void initTask(){
		try {
			mgr = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
			pds = PoolDataSourceFactory.getPoolDataSource();
			pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
			pds.setConnectionPoolName("UCP_Name");

			mgr.createConnectionPool((UniversalConnectionPoolAdapter)pds);
			mgr.startConnectionPool("UCP_Name");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UniversalConnectionPoolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
