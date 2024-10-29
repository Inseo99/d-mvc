package mvc.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.print.DocFlavor.STRING;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;
import mvc.vo.CommentVo;
import mvc.vo.Criteria;
import mvc.vo.MemberVo;
import mvc.vo.SearchCriteria;

public class CommentDao {
	
	private Connection conn;	// 전역적으로 쓴다. 연결객체를...
	private PreparedStatement pstmt;	// 쿼리를 실행하기 위한 구문객체
	
	public CommentDao() {	// 생성자를 만드는 이유 : DB연결하는 Dbconn 객체 생성할려고...생성해야 mysql 접속하니까
		Dbconn db = new Dbconn();
		this.conn = db.getConnection();
	}
	
	public ArrayList<CommentVo> commentSelectAll(int bidx) {
		
		ArrayList<CommentVo> alist = new ArrayList<CommentVo>();
		  
		String sql = "SELECT * FROM comment WHERE delyn = 'N' AND bidx = ? ORDER BY cidx DESC"; 
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, bidx);
			
			rs = pstmt.executeQuery();
		  
		  
		while(rs.next()) { // 커서가 다음으로 이동해서 첫 글이 있느냐 물어보고 true면 진행 int originbidx =
			 int cidx = rs.getInt("cidx"); 
			 String ccontents = rs.getString("ccontents"); 
			 String cwriter = rs.getString("cwriter"); 
			 String writeday = rs.getString("writeday");
			 String delyn = rs.getString("delyn");
			 int midx = rs.getInt("midx");
				  
			 CommentVo cv = new CommentVo();
				  
			 cv.setCidx(cidx);
			 cv.setCcontents(ccontents);
			 cv.setCwriter(cwriter);
			 cv.setWriteday(writeday);
			 cv.setDelyn(delyn);
			 cv.setMidx(midx);
				  				  
			 alist.add(cv); // ArrayList객체에 하나씩 추가한다. 
			}
		  
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{	// 각 개체도 소멸시키고 DB연결을 끊는다.
				pstmt.close();
				conn.close();
				rs.close();
			} catch(Exception e) {
				e.printStackTrace();
			}		
		}	  
		return alist;
		 
	}
	
	// 게시판 전체 갯수 구하기
	public int commentInsert(CommentVo cv) {
		
		int value = 0;
		
		
		  String csubject = cv.getCsubject();
		  String ccontents = cv.getCcontents();
		  String cwriter = cv.getCwriter();
		  int bidx = cv.getBidx();
		  int midx = cv.getMidx();
		  String cip = cv.getCip();
		  
		  String sql = "INSERT INTO comment(csubject, ccontents, cwriter, bidx, midx, cip) VALUE(null, ?, ?, ?, ?, ?)"; 
		  
		  try { 
		  pstmt = conn.prepareStatement(sql);
		  pstmt.setString(1, ccontents);
		  pstmt.setString(2, cwriter);
		  pstmt.setInt(3, bidx);
		  pstmt.setInt(4, midx);  
		  pstmt.setString(5, cip);

		  value = pstmt.executeUpdate();
		  
		  } catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try{	// 각 개체도 소멸시키고 DB연결을 끊는다.
					pstmt.close();
					conn.close();
				} catch(Exception e) {
					e.printStackTrace();
				}		
			}		
			return value;
	}
	
	public int commentDelete(int cidx) {

		
		int value = 0;
		String sql = "UPDATE comment SET delyn = 'Y' WHERE cidx = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cidx);
			
			value = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{	// 각 개체도 소멸시키고 DB연결을 끊는다.
				pstmt.close();
				conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}		
		}		
		return value;
	}
}



















