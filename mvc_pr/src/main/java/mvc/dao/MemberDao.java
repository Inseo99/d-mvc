package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.MemberVo;

public class MemberDao {	// MVC방식으로 가기전에 첫번째 model1 방식
	
	private Connection conn;	// 전역변수로 사용 페이지 어느 곳에서도 사용할 수 있다.
	private PreparedStatement pstmt;
	
	// 생성자를 통해서 db연결해서 메소드 사용
	public MemberDao() {
		Dbconn dbconn = new Dbconn();	// DB 객체 생성
		conn = dbconn.getConnection();	// 메소드 호출해서 연결객체를 가져온다.
	}
	
	public int memberInsert(String memberId, String memberPwd, 
			String memberName, String memberGender, String memberBirth, 
			String memberAddr, String memberPhone, String memberEmail, String memberInHobby) {
	
		int value = 0; // 메소드 지역변수 결과값을 담는다
			String sql = "";
			pstmt = null; // 구문클래스 선언
		try{
			sql = "insert into member(memberid,memberpwd,membername,membergender,memberbirth,memberaddr,memberphone,memberemail,memberhobby) values(?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);		
			pstmt.setString(1,memberId);			// 문자형 메소드 사용
			pstmt.setString(2,memberPwd);			// 문자형 메소드 사용 숫자형 setInt(번호, 값);
			pstmt.setString(3,memberName);
			pstmt.setString(4,memberGender);
			pstmt.setString(5,memberBirth);
			pstmt.setString(6,memberAddr);
			pstmt.setString(7,memberPhone);
			pstmt.setString(8,memberEmail);
			pstmt.setString(9,memberInHobby);
			value = pstmt.executeUpdate();		// 구문객체 실행하면 성공시 1 실패시 0리턴
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally{					//try를 했던 catch를 했던 꼭 실행해야하는 영역
			// 객체 사라지게하고
			// db연결 끊기
			try{
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	// 로그인을 통해서 회원정보를 담아오는 메소드이다.
	public MemberVo memberLoginCheck(String memberId, String memberPwd) {
		MemberVo mv = null;
		
		String sql = "select * from member where memberid = ? and memberpwd = ?";
		ResultSet rs = null;	// db에서 결과 데이터를 받아오는 전용클래스
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPwd);
			rs = pstmt.executeQuery();	
			
			if (rs.next() == true) {	// 커서가 이동해서 데이터 값이 있으면 if (rs.next())와 같은 표현
				String memberid = rs.getString("memberid");	// 결과값에서 아이디값을 뽑는다
				int midx = rs.getInt("midx");				// 결과갑에서 회원번호를 뽑는다
				String membername = rs.getString("membername");
				String membergender = rs.getString("membergender");
				
				mv = new MemberVo();	// 화면에 가지고 갈 데이터를 담을 vo객체생성
				mv.setMemberid(memberid);	// 옮겨담는다.
				mv.setMidx(midx);
				mv.setMembername(membername);	
				mv.setMembergender(membergender);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try{
				rs.close();
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return mv;
	}
	
	public ArrayList<MemberVo> memberSelectAll() {
		
		ArrayList<MemberVo> alist = new ArrayList<MemberVo>();
		String sql = "SELECT * FROM member WHERE delyn = 'N' ORDER BY midx DESC";
		ResultSet rs = null;	// db값을 가져오기위한 전용클래스
		MemberVo mv = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
			
			while(rs.next()) {	// 커서가 다음으로 이동해서 첫 글이 있느냐 물어보고 true면 진행
				int midx = rs.getInt("midx");
				String memberId = rs.getString("memberid");
				String memberName = rs.getString("membername");
				String writeday = rs.getString("writeday");
				
				mv = new MemberVo();	// 첫행부터 mv에 옮겨담기
				mv.setMidx(midx);
				mv.setMemberid(memberId);
				mv.setMembername(memberName);
				mv.setWriteday(writeday);
				
				alist.add(mv);		// ArrayList객체에 하나씩 추가한다.
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try{
				
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}	
		
		return alist;
	}
	
	public int memberIdCheck(String memberId) {
		String sql = "SELECT COUNT(*) AS cnt FROM member WHERE memberid = ?";
		ResultSet rs = null;	// db에서 결과 데이터를 받아오는 전용클래스
		int cnt = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery();	
			
			if (rs.next()) {
				cnt = rs.getInt("cnt");
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try{
				rs.close();
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return cnt;
	}
	
}
