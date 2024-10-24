package mvc.dbcon;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dbconn {
	private Connection conn;	// 멤버변수는 선언만해도 자동초기화 됨
	private String url="jdbc:mysql://127.0.0.1/aws0822?serverTimezone=UTC";
	private String user="root";
	private String password="1234";

	public Connection getConnection() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// System.out.println("객체생성확인 ==>" + conn);
		return conn;	// 연결객체가 생겨났을때의 객체정보를 담고있는 객체참조변수
						// null값이면 연결이 되지 않았다라는 뜻
	}
	
}
