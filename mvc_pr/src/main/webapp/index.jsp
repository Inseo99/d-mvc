<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
//System.out.println("안녕하세요");
//out.println("웹페이지에서 안녕하세요");

String msg = "";


if (session.getAttribute("msg") != null) {
	msg = (String)session.getAttribute("msg");
}
session.setAttribute("msg", "");

int midx = 0;
String memberId = "";
String memberName = "";
String alt = "";
String logMsg = "";

if (session.getAttribute("midx") != null) {
	
	midx = (int)session.getAttribute("midx");
	memberId = (String)session.getAttribute("memberid");
	memberName = (String)session.getAttribute("memberName");
	
	alt = memberName + "로그인되었습니다.";
	logMsg = "<a href = '"+ request.getContextPath() +"/member/memberLogout.aws'>로그아웃</a>";
} else {
	alt = "로그인하세요";
	logMsg = "로그인";
}

%>

<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="./css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
<%
	if (!msg.equals("")) {
%>
	alert('<%=msg%>');
<%
	}
%>
</script>
</head>
<body>
<%=alt %>
<%=logMsg %>
<hr>
<div class="main">환영합니다. 메인페이지입니다.</div>
<div>
<a href = "<%=request.getContextPath()%>/member/memberJointeacher.aws">회원가입 페이지가기</a>
</div>
<div>
<a href = "<%=request.getContextPath()%>/member/memberLogin.aws">회원 로그인하기</a>
</div>
<div>
<a href = "<%=request.getContextPath()%>/member/memberList.aws">회원 목록 가기</a>
</div>
<div>
<a href = "<%=request.getContextPath()%>/board/boardList.aws">게시판 목록 가기</a>
</div>
</body>
</html>