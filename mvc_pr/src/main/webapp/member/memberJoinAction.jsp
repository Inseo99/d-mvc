<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="mv" class="mvc.vo.MemberVo" scope="page"/>
<%@ page import = "mvc.dao.MemberDao" %>>
<jsp:setProperty name="mv" property="*"/>
 
<%
// 필요가 없어짐	

String[] memberHobby = request.getParameterValues("memberHobby");
String memberInHobby="";
for(int i=0;i<memberHobby.length;i++){
	memberInHobby = memberInHobby + memberHobby[i] + ",";
		
}

MemberDao md = new MemberDao();
int value = md.memberInsert(mv.getMemberid(), mv.getMemberpwd(), mv.getMembername() ,mv.getMembergender(), mv.getMemberbirth(), mv.getMemberaddr(), mv.getMemberphone(), mv.getMemberemail(), memberInHobby);

String pageUrl = "";
String msg = "";
if(value == 1) {							// index.jsp파일은 web.xml웹설정파일에 기본등록되어있음 - 생략가능
	msg = "회원가입되었습니다.";
	pageUrl = request.getContextPath() + "/";	//request.getContextPath() : 프로젝트 이름
	response.sendRedirect(pageUrl);		// 전송방식 sendRedirect는 요청받으면 다시 그쪽으로 가라고 지시하는 방법
} else {
	msg = "회원가입 오류발생하였습니다.";
	pageUrl = request.getContextPath() + "/member/memberjointeacher.jsp";
	response.sendRedirect(pageUrl);
}
%>
    <script>
    alert('<%=msg%>');
<%--     // alert(<%=value%>); --%>
     //자바스크립트로 페이지 이동시킨다. document 객체안에 location객체안에 주소속성에 담아서
     document.location.href = "<%=pageUrl%>";
     
    </script>

