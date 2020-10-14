package cn.hoob.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.*;

/*****
 * 慎用直连，rdd中大量数据查询，直接爆炸，改用连接池
 *
 * ******/
public class MySQLUtlis implements Serializable {

	private static final String url;
	private static final String user;
	private static final String password;
	private static final boolean isUseConnectionPools;
	private static final int minPoolSize;
	private static final int maxPoolSize;
	private static final int checkoutTimeout;
	private static final int maxStatements;
	private static final int acquireIncrement;
	private static final int maxIdleTime;
	private static final ComboPooledDataSource ds;



	static {

		url = SysUtils.getSysparamString("rs.jdbc.url");
		user = SysUtils.getSysparamString("rs.jdbc.user");//"appuser";
		password = SysUtils.getSysparamString("rs.jdbc.password");//"appuser";
		isUseConnectionPools = SysUtils.getSysparamBoolean("rs.jdbc.isUseConnectionPools","true");//"true";
		minPoolSize = SysUtils.getSysparamInteger("rs.jdbc.minPoolSize","10");
		maxPoolSize = SysUtils.getSysparamInteger("rs.jdbc.maxPoolSize","200");
		checkoutTimeout = SysUtils.getSysparamInteger("rs.jdbc.checkoutTimeout","5000");
		maxStatements = SysUtils.getSysparamInteger("rs.jdbc.maxStatements","200");
		maxIdleTime = SysUtils.getSysparamInteger("rs.jdbc.maxIdleTime","18000");
		acquireIncrement=SysUtils.getSysparamInteger("rs.jdbc.acquireIncrement","5");
		if (isUseConnectionPools) {
			ds = new ComboPooledDataSource();
			try {
				ds.setDriverClass("com.mysql.jdbc.Driver");
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
			ds.setUser(user);
			ds.setPassword(password);
			ds.setJdbcUrl(url);
			ds.setMinPoolSize(minPoolSize);
			ds.setMaxPoolSize(maxPoolSize);
			ds.setMaxStatements(maxStatements);
			ds.setAcquireIncrement(acquireIncrement);
			ds.setCheckoutTimeout(checkoutTimeout);
			ds.setPreferredTestQuery("SELECT 1");
			ds.setIdleConnectionTestPeriod(18000);
			ds.setTestConnectionOnCheckout(true);
			ds.setMaxIdleTime(maxIdleTime);
		} else {
			ds = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();

			}
		}

	}
	/**
	 * 从连接次中获取连接
	 * **/
	private static Connection getConnection() throws SQLException {
		if (isUseConnectionPools) {
			return ds.getConnection();
		}

		return DriverManager.getConnection(url, user, password);
	}

	public static Integer getSeriesId(String contentId) throws SQLException {
		
		Integer id = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			pstmt = connection.prepareStatement("select id  from series_id_contentid where contentId=?");

			pstmt.setString(1, contentId);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}

		}
		return id;
	}



	/**
	 * 执行SQL
	 * **/
	public static boolean executeSQL(String sql,String ...str) throws SQLException {
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		boolean flag=false;
		try {
			connection = getConnection();
			pstmt = connection.prepareStatement(sql);
			if(str!=null&&str.length>0){
				for(int i=1;i<=str.length;i++){
					pstmt.setString(i,str[i-1]);
				}
			}
			flag = pstmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return flag;
	}

	
    /**
     * 批量更新用户数据推荐数据
     * @throws SQLException 
     * **/
	public static void excuteBatchReUser(Dataset<Row> data){ 
		data.foreachPartition(partition->{
			Connection connection = getConnection();
			PreparedStatement pstmt=connection.prepareStatement("INSERT INTO `user_recommendation` "
					+ "(`contentIds`, `userId`,updateTime) VALUES (?,?,now()) ON DUPLICATE KEY "
					+ "UPDATE `contentIds` = values(`contentIds`),`updateTime` = values(`updateTime`)");
			connection.setAutoCommit(false);
			int i=1;
			while(partition.hasNext()){
				Row row=partition.next();
				pstmt.setString(1,row.getAs("contentIds"));
				pstmt.setString(2,row.getAs("userId"));
				pstmt.addBatch();
				i++;
				if(i%200==0){
					pstmt.executeBatch();   //批量执行sql，避免因此单次的insert操作建立多个Connection浪费资源
					connection.commit();
				}
			}
			connection.setAutoCommit(true);
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
		});
	}
	/*****/
	public static void excuteBatchReUserneNew(Dataset<Row> data){
		data.foreachPartition(partition->{
			//一个分区一个连接
			Connection connection =DriverManager.getConnection(SysUtils.getSysparamString("rs.jdbc.url"),
					SysUtils.getSysparamString("rs.jdbc.user"),
					SysUtils.getSysparamString("rs.jdbc.password"));
			PreparedStatement pstmt=connection.prepareStatement("INSERT INTO `user_recommendation` "
					+ "(`contentIds`, `userId`,updateTime) VALUES (?,?,NOW())" +
					" ON DUPLICATE KEY UPDATE `contentIds` = values(`contentIds`),`updateTime` = values(`updateTime`)");
			connection.setAutoCommit(false);
			int i=1;
			while(partition.hasNext()){
				Row row=partition.next();
				pstmt.setString(1,row.getAs("contentIds"));
				pstmt.setString(2,row.getAs("userId"));
				pstmt.addBatch();
				i++;
				if(i%200==0){
					pstmt.executeBatch();   //批量执行sql，避免因此单次的insert操作建立多个Connection浪费资源
					connection.commit();
				}
			}
			pstmt.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
		});
	}
	 /**
     * 批量更新内容相似性推荐数据
     * @throws SQLException 
     * **/
	public static void excuteBatchReSimilarity(Dataset<Row> data){ 
		data.foreachPartition(partition->{
			Connection connection = getConnection();
			PreparedStatement pstmt=connection.prepareStatement("INSERT INTO `similar_recommendation` "
					+ "(`contentIds`, `contentId`,updateTime) VALUES (?,?,now()) ON DUPLICATE KEY "
					+ "UPDATE `contentIds` = values(`contentIds`),`updateTime` = values(`updateTime`)");
			connection.setAutoCommit(false);
			int i=1;
			while(partition.hasNext()){
				Row row=partition.next();
				pstmt.setString(1,row.getAs("contentIds"));
				pstmt.setString(2,row.getAs("contentId"));
				pstmt.addBatch();
				i++;
				if(i%200==0){
					pstmt.executeBatch();   //批量执行sql，避免因此单次的insert操作建立多个Connection浪费资源
					connection.commit();
				}
			}
			pstmt.executeBatch(); 
			connection.commit();
			connection.setAutoCommit(true);
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
		});
	}
	 /**
     * 批量更新内容相似性推荐数据
     * @throws SQLException 
     * **/
	public static void excuteBatchReTopN(Dataset<Row> data){ 
		data.foreachPartition(partition->{
			Connection connection = getConnection();
			PreparedStatement pstmt=connection.prepareStatement("INSERT INTO `topn_recommendation`"
					+ " (`playCount`, `programType`, `contentId`) VALUES (?,?,?) ON DUPLICATE KEY"
					+ " UPDATE `playCount` = values(`playCount`)");
			connection.setAutoCommit(false);
			int i=1;
			while(partition.hasNext()){
				Row row=partition.next();
				pstmt.setInt(1,row.getAs("playCount"));
				pstmt.setString(2,row.getAs("programType"));
				pstmt.setString(3,row.getAs("contentId"));
				pstmt.addBatch();
				i++;
				if(i%200==0){
					pstmt.executeBatch();   //批量执行sql，避免因此单次的insert操作建立多个Connection浪费资源
					connection.commit();
				}
			}
			pstmt.executeBatch(); 
			connection.commit();
			connection.setAutoCommit(true);
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
		});
	}
	/**
	 * 更新参数ALS模型最优参数
	 * @throws SQLException
	 * **/
	public static boolean updateALSBestParam( int rank,int  maxIter,double regParam)
			throws SQLException{

		Connection connection = null;
		PreparedStatement pstmt = null;
		boolean flag=false;
		try {

			connection = getConnection();
			pstmt = connection.prepareStatement("INSERT INTO `model_params` ( " +
					"`maxIter`, `modelName`, `rank`, `regParam`) " +
					"VALUES ( ?,'ALS', ?, ?) ON DUPLICATE KEY"+
					" UPDATE `maxIter` = values(`maxIter`) ," +
					"`rank` = values(`rank`) ," +
					"`regParam` = values(`regParam`) ");

			pstmt.setInt(1,maxIter);
			pstmt.setInt(2,rank);
			pstmt.setDouble(3,regParam);

			flag = pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return flag;
	}
	//获取ALS模型参数
	public static Integer getALSMaxIter() throws SQLException {

		Integer maxIter = 10;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			pstmt = connection.prepareStatement("select maxIter  from model_params where modelName='ALS'");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				maxIter = rs.getInt("maxIter");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}

		}
		return maxIter;
	}
	//获取ALS模型参数
	public static Integer getALSRank() throws SQLException {

		Integer rank = 10;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			pstmt = connection.prepareStatement("select rank  from model_params where modelName='ALS'");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				rank = rs.getInt("rank");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}

		}
		return rank;
	}
	//获取ALS模型参数
	public  static Double getALSRegParam() throws SQLException {

		double regParam = 0.75;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			pstmt = connection.prepareStatement("select regParam  from model_params where modelName='ALS'");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				regParam = rs.getDouble("regParam");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}

		}
		return regParam;
	}
	//释放连接回连接池
	public static void close(Connection conn, PreparedStatement pst, ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pst!=null){
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//测试本地redis服务是否正常
	public static void main(String[] args) throws Exception {
		
	}
}
