package mvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import mvc.dao.BoardDao;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;
import mvc.vo.PageMaker;
import mvc.vo.SearchCriteria;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Servlet implementation class BoardController
 */
@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String location;	// 멤버변수(전역) 초기화 => 이동할 페이지
	
	public BoardController(String location) {
		this.location = location;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String paramMethod = "";	// 전송방식이 sendRedirect면 S foward방식이면 F
		String url = "";
		
		if (location.equals("boardList.aws")) {	// 가상 경로
			// System.out.println("boardList.aws");
			
			String page = request.getParameter("page");
			if (page == null) page = "1";
			int pageInt = Integer.parseInt(page);
			
			String searchType = request.getParameter("searchType");
			String keyword = request.getParameter("keyword");
			
			if(keyword == null) keyword = "";
			
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(pageInt);		
			scri.setSearchType(searchType);
			scri.setKeyword(keyword);
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);	// <--------- PageMaker에 SearchCriteria 담아서 가지고 다닌다.
			
			BoardDao bd = new BoardDao();
			// 페이징 처리하기 위한 전체 데이터 갯수 가져오기
			int boardCnt = bd.boardTatalCount(scri);
			// System.out.println("게시물수는? " + boardCnt);
			
			pm.setTotalCount(boardCnt);			// <--------- PageMaker에 전체게시물수를 담아서 페이지계산
			
			ArrayList<BoardVo> alist = bd.boardSelectAll(scri);
			
			request.setAttribute("alist", alist);	// 화면까지 가지고 가기위해 request객체에 담는다.
			request.setAttribute("pm",pm);			// forward방식으로 넘기기 때문에 공유가 가능하다.
			
			// System.out.println(alist);
			
			paramMethod = "F";
			url = "/board/boardList.jsp";	// 실제 내부 경로

		} else if (location.equals("boardWrite.aws")) {	// 가상 경로
			// System.out.println("boardWrite.aws");
			
			paramMethod = "F";	// 포워드 방식은 내부에서 내부에서 공유하는 것이기때문에 내부에서 활동하고 이동한다.
			url = "/board/boardWrite.jsp";	// 실제 내부 경로

		} else if (location.equals("boardWriteAction.aws")) {	// 가상 경로
			// System.out.println("boardWriteAction.aws");	

	        String savePath = "D:\\dev\\eclipse-workspace\\d-mvc\\mvc_pr\\src\\main\\webapp\\images\\";   // 저장될 위치
	        // System.out.println(savePath);
	        int fsize = (int) request.getPart("filename").getSize();
	        // System.out.println(fsize);
	         
	        String originFileName = "";
	        if (fsize != 0) {
	           Part filePart = (Part)request.getPart("filename");	// 넘어온 멀티파트 파일을 Part클래스로 담는다.
	           // System.out.println(filePart);
	            
	           originFileName = getFileName(filePart);	// 파일이름 추출
	           // System.out.println(originFileName);
	           
	           // System.out.println(savePath + originFileName);	
	           
	           File file = new File(savePath + originFileName);	// 파일 객체 생성
	           InputStream is = filePart.getInputStream();	// 파일 읽어들이는 스트림 생성
	           FileOutputStream fos = null;
	            
	           fos = new FileOutputStream(file);	// 파일 작성 및 완성하는 스트림 생성
	            
	           int temp = -1;
	            
	           while((temp = is.read()) != -1) {	// 반복문을 돌려서 읽어드린 데이터를 output에 작성한다
	           	fos.write(temp);
	           }
	           is.close();	// input 스트림 객체 소멸
	           fos.close();	// Output 스트림 객체 소멸
	        } else {
	        	originFileName = "";
	        }
			
			// 1. 파라미터 값을 넘겨받는다.
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			
			HttpSession session = request.getSession();	// 세션 객체를 불러와서
			int midx = Integer.parseInt(session.getAttribute("midx").toString());	// 로그인할때 담았던 세션변수 midx값을 꺼낸다.
			
			// String ip = request.getRemoteAddr();
			String ip = "";
			
			try {
				ip = getUserIp(request);
				// System.out.println(ip);
				String serverip = InetAddress.getLocalHost().getHostAddress();
				// System.out.println(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPassword(password);
			bv.setMidx(midx);
			bv.setFilename(originFileName);
			bv.setIp(ip);
			
			// 2. DB처리한다.
			BoardDao bd = new BoardDao();
			
			int value = bd.boardInsert(bv);
			
			paramMethod = "S";
			// 3. 처리 후 이동한다. sendRedirect			
			if(value == 2) {	// 입력성공				
				url = request.getContextPath() + "/board/boardList.aws";					
			} else {	// 입력실패
				url = request.getContextPath() + "/board/boardWrite.aws";	
			}			
		} else if (location.equals("boardContents.aws")) {
			// System.out.println("boardContents.aws");
			// 1. 넘어온 값 받기
			String bidx = request.getParameter("bidx");
			// System.out.println("bidx:" + bidx);
			int bidxInt = Integer.parseInt(bidx);
			
			// 2. 처리하기
			BoardDao bd = new BoardDao();	// 객체생성
			int value = bd.boardViewCntUpdate(bidxInt);
			
			BoardVo bv = bd.boardSelectOne(bidxInt);	// 생성한 메소드 호출 (해당되는 bidx의 게시물 데이터 가져옴)
			request.setAttribute("bv", bv);	// 포워드 방식이라 같은 영역안에 있어서 공유해서 jsp페이지에서 꺼내쓸 수 있다.
						
			// 3. 이동해서 화면 보여주기
			paramMethod = "F";
			url = "/board/boardContents.jsp";			
		} else if (location.equals("boardModify.aws")) {
			// System.out.println("boardModify.aws");
			
			String bidx = request.getParameter("bidx");
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(Integer.parseInt(bidx));
			request.setAttribute("bv", bv);
			
			paramMethod = "F";
			url = "/board/boardModify.jsp";
		
		} else if (location.equals("boardModifyAction.aws")) {
			//System.out.println("boardModifyAction.aws");
			
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			String bidx = request.getParameter("bidx");

			BoardDao bd = new BoardDao();
			int bidxInt = Integer.parseInt(bidx);
			BoardVo bv = bd.boardSelectOne(bidxInt);
			
			paramMethod = "S";
			// 비밀번호 체크
			if (password.equals(bv.getPassword())) {	// 비밀번호 같으면
				
				BoardDao bd2 = new BoardDao();
				BoardVo bvnew = new BoardVo();

				bvnew.setSubject(subject);
				bvnew.setContents(contents);
				bvnew.setWriter(writer);
				bvnew.setPassword(password);
				bvnew.setBidx(bidxInt);
				
				int value = bd2.boardUpdate(bvnew); 
				
				if(value == 1) {	// 입력성공				
					url = request.getContextPath() + "/board/boardContents.aws?bidx=" + bidx;					
				} else {	// 입력실패
					url = request.getContextPath() + "/board/boardModify.aws?bidx=" + bidx;	
				}
			} else {	// 비밀번호 다르면				
				response.setContentType("text/html; charset=UTF-8");  // 응답 콘텐츠 타입 설정
	            PrintWriter out = response.getWriter();  // PrintWriter 객체 가져오기
	            
	            out.println("<script>");
	            out.println("alert('비밀번호가 다릅니다.');");
	            out.println("location.href='" + request.getContextPath() + "/board/boardModify.aws?bidx=" + bidx + "';");
	            out.println("</script>");
	            out.flush();
			}

		} else if (location.equals("boardRecom.aws")) {
			
			String bidx = request.getParameter("bidx");
			int bidxInt = Integer.parseInt(bidx);
			
			BoardDao bd = new BoardDao();	// 객체생성
			int recom = bd.boardRecomUpdate(bidxInt);
			
			PrintWriter out = response.getWriter();
			out.println("{\"recom\" :\""+ recom +"\"}");
						
			// paramMethod = "S";
			// url = request.getContextPath() + "/board/boardContents.aws?bidx=" + bidx;
		} else if (location.equals("boardDelete.aws")) {
			String bidx = request.getParameter("bidx");
			
			request.setAttribute("bidx", bidx);
			
			paramMethod = "F";
			url = "/board/boardDelete.jsp";
		} else if (location.equals("boardDeleteAction.aws")) {
			
			String bidx = request.getParameter("bidx");
			String password = request.getParameter("password");
			// System.out.println(bidx + password);
			
			// 처리하기
			BoardDao bd = new BoardDao();
			int value = bd.boardDelete(Integer.parseInt(bidx), password);
			System.out.println(value);
			paramMethod = "S";
			
			if (value == 1) {
				url = request.getContextPath() + "/board/boardList.aws";
			} else {
				response.setContentType("text/html; charset=UTF-8");  // 응답 콘텐츠 타입 설정
	            PrintWriter out = response.getWriter();  // PrintWriter 객체 가져오기
	            
	            out.println("<script>");
	            out.println("alert('비밀번호가 다릅니다.');");
	            out.println("location.href='" + request.getContextPath() + "/board/boardDelete.aws?bidx=" + bidx + "';");
	            out.println("</script>");
	            out.flush();
			}			
		} else if (location.equals("boardReply.aws")) {
			
			String bidx = request.getParameter("bidx");
			
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(Integer.parseInt(bidx));
			
			int originbidx= bv.getOriginbidx();
			int depth= bv.getDepth();
			int level_= bv.getLevel_();
			
			request.setAttribute("bidx", Integer.parseInt(bidx));
			request.setAttribute("originbidx", originbidx);
			request.setAttribute("depth", depth);
			request.setAttribute("level_", level_);
			
			paramMethod = "F";
			url = "/board/boardReply.jsp";
			
		} else if (location.equals("boardReplyAction.aws")) {
			// System.out.println("boardReplyAction.aws");	

	        String savePath = "D:\\dev\\eclipse-workspace\\d-mvc\\mvc_pr\\src\\main\\webapp\\images\\";   // 저장될 위치
	        // System.out.println(savePath);
	        int fsize = (int) request.getPart("filename").getSize();
	        // System.out.println(fsize);
	         
	        String originFileName = "";
	        if (fsize != 0) {
	           Part filePart = (Part)request.getPart("filename");	// 넘어온 멀티파트 파일을 Part클래스로 담는다.
	           System.out.println(filePart);
	            
	           originFileName = getFileName(filePart);	// 파일이름 추출
	           System.out.println(originFileName);
	           
	           System.out.println(savePath + originFileName);	
	           
	           File file = new File(savePath + originFileName);	// 파일 객체 생성
	           InputStream is = filePart.getInputStream();	// 파일 읽어들이는 스트림 생성
	           FileOutputStream fos = null;
	            
	           fos = new FileOutputStream(file);	// 파일 작성 및 완성하는 스트림 생성
	            
	           int temp = -1;
	            
	           while((temp = is.read()) != -1) {	// 반복문을 돌려서 읽어드린 데이터를 output에 작성한다
	           	fos.write(temp);
	           }
	           is.close();	// input 스트림 객체 소멸
	           fos.close();	// Output 스트림 객체 소멸
	        } else {
	        	originFileName = "";
	        }
			
			// 1. 파라미터 값을 넘겨받는다.
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			String bidx = request.getParameter("bidx");
			// System.out.println(bidx);
			String originbidx = request.getParameter("originbidx");
			// System.out.println(originbidx);
			String depth = request.getParameter("depth");
			// System.out.println(depth);
			String level_ = request.getParameter("level_");
			// System.out.println(level_);
			
			HttpSession session = request.getSession();	// 세션 객체를 불러와서
			int midx = Integer.parseInt(session.getAttribute("midx").toString());	// 로그인할때 담았던 세션변수 midx값을 꺼낸다.
			
			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPassword(password);
			bv.setMidx(midx);
			bv.setFilename(originFileName);
			bv.setBidx(Integer.parseInt(bidx));
			bv.setOriginbidx(Integer.parseInt(originbidx));
			bv.setDepth(Integer.parseInt(depth));
			bv.setLevel_(Integer.parseInt(level_));
			
			BoardDao bd = new BoardDao();
			int maxbidx = bd.boardReply(bv);
			
			paramMethod = "S";
			if(maxbidx != 0) {	// 입력성공				
				url = request.getContextPath() + "/board/boardContents.aws?bidx=" + maxbidx;					
			} else {	// 입력실패
				url = request.getContextPath() + "/board/boardReply.aws?bidx=" + bidx;	
			}
		}
		
		if (paramMethod.equals("F")) {
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request, response);		
		} else if (paramMethod.equals("S")) {
			response.sendRedirect(url);
		}
		
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	public String getFileName(Part filePart) {
		      
		for(String filePartData : filePart.getHeader("Content-Disposition").split(";")) {
		// System.out.println(filePartData);
		         
			if(filePartData.trim().startsWith("filename")) {
				return filePartData.substring(filePartData.indexOf("=") + 1).trim().replace("\"","");
		    }
		}
		return null;
	}
	
public String getUserIp(HttpServletRequest request) throws Exception {
		
        String ip = null;
        // HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

        ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
		
        if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
        	InetAddress address = InetAddress.getLocalHost();
        	ip = address.getHostAddress();
        	
        }
        
		return ip;
	}
	
}
