<%@page import="java.util.ArrayList"%>
<%@page import="java.util.*"%>
<%@page import="mvc.vo.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
ArrayList<BoardVo> alist = (ArrayList<BoardVo>)request.getAttribute("alist");

PageMaker pm = (PageMaker)request.getAttribute("pm");

String keyword = pm.getScri().getKeyword();
String searchType = pm.getScri().getSearchType();

String param = "keyword=" + keyword + "%searcType=" + searchType + "";

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글목록</title>
<link href="../css/style2.css" rel="stylesheet">
</head>
<body>
<header>
	<h2 class="mainTitle">글목록</h2>
	<form class="search" name = "frm" action = "<%=request.getContextPath()%>/board/boardList.aws">
		<select name = "searchType">
			<option value = "subject">제목</option>
			<option value = "writer">작성자</option>
		</select>
		<input type="text" name = "keyword">
		<button type = "submit" class="btn">검색</button>
	</form>
</header>

<section>	
	<table class="listTable">
		<tr>
			<th>No</th>
			<th>제목</th>
			<th>작성자</th>
			<th>조회</th>
			<th>추천</th>
			<th>날짜</th>
		</tr>
	   <% 
	   int num = 1;
	   for(BoardVo bv : alist) { %>
		<tr>
			<td><%=bv.getOriginbidx() %></td>
			<td class="title"><a href="<%=request.getContextPath()%>/board/boardContents.aws?bidx=<%=bv.getBidx()%>"><%=bv.getSubject()%></a></td>
			<td><%=bv.getWriter() %></td>
			<td><%=bv.getViewcnt() %></td>
			<td><%=bv.getRecom() %></td>
			<td><%=bv.getWriteday().substring(0, 10) %></td>
		</tr>
		<% 
	 	 num++;
	 	  }%>
	</table>
	
	<div class="btnBox">
		<a class="btn aBtn" href="<%=request.getContextPath()%>/board/boardWrite.aws">글쓰기</a>
	</div>
	
	<div class="page">
		<ul>
		<%if(pm.isPrev() == true) {%>
			<li><a href = "<%=request.getContextPath() %>/board/boardList.aws?page=<%=pm.getStartPage() - 1%>&<%=param%>">◀</a></li>
		<%} %>
		<% for(int i = pm.getStartPage(); i <= pm.getEndPage(); i++) {%>
			<li <%if(i == pm.getScri().getPage()) {%>class = "on" <%} %>>
			<a href = "<%=request.getContextPath() %>/board/boardList.aws?page=<%=i%>"><%=i %></a></li>
		<%} %>
		<%if(pm.isNext() == true && pm.getEndPage() > 0) {%>
			<li><a href = "<%=request.getContextPath() %>/board/boardList.aws?page=<%=pm.getEndPage() + 1%>&<%=param%>">▶</a></li>
		<%} %>
		</ul>
	</div>
</section>

</body>
</html>