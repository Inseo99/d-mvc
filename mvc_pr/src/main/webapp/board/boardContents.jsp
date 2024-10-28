<%@page import="mvc.vo.BoardVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
BoardVo bv = (BoardVo)request.getAttribute("bv"); // 강제형변환 양쪽형을 맞춰준다

String memberName = "";
if(session.getAttribute("memberName") != null) {
	memberName = (String)session.getAttribute("memberName");
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<!-- jquery CDN주소 -->
<link href="../css/style2.css" rel="stylesheet">
<script> 
//jquery로 만드는 함수
$.boardCommentList = function(){
	
	$.ajax({
		type : "get",	// 전송방식
		url : "<%=request.getContextPath()%>/comment/commentList.aws?bidx=<%=bv.getBidx()%>",
		dataType : "json",		// json 타입은 문서에서 {"키값" : "vlaue값" , "키값" : "value값2"}
		success : function(result) {	// 결과가 넘어와서 성공했을 때 받는 영역
			alert("전송성공");							
		},
		error : function() {		// 결과가 실패했을 때 받는 영역
			alert("전송실패");
		}			
	});		
	
}

$(document).ready(function() {	// cdn주소 필요
	
	$.boardCommentList();
	
	$("#btn").click(function() {
		// alert("추천버튼 클릭")
		
		$.ajax({
			type : "get",	// 전송방식
			url : "<%=request.getContextPath()%>/board/boardRecom.aws?bidx=<%=bv.getBidx()%>",
			dataType : "json",		// json 타입은 문서에서 {"키값" : "vlaue값" , "키값" : "value값2"}
			success : function(result) {	// 결과가 넘어와서 성공했을 때 받는 영역
				// alert("전송성공");				
				var str = "추천("+result.recom+")";
		
				$("#btn").val(str);
			},
			error : function() {		// 결과가 실패했을 때 받는 영역
				alert("전송실패");
			}			
		});
		
	});
	
	$("#cmtbtn").click(function() {
		
		let loginCheck = "<%=session.getAttribute("midx")%>";
		if (loginCheck == "" || loginCheck == "null" || loginCheck == null) {
			alert("로그인을 해주세요.");
			return;
		}
		
		let cwriter = $("#cwriter").val();
		let ccontents = $("#ccontents").val();
		
		if (cwriter == "") {
			alert("작성자를 입력해주세요.");
			$("#cwriter").focus();
			return;
			
		} else if (ccontents == "") {
			alert("내용을 입력해주세요.");
			$("#ccontents").focus();
			return;
		}
		
		$.ajax({
			type : "post",	// 전송방식
			url : "<%=request.getContextPath()%>/comment/commentWriteAction.aws",
			data : {"cwriter" : cwriter, 
					"ccontents" : ccontents, 
					"bidx" : "<%=bv.getBidx()%>", 
					"midx" : "<%=session.getAttribute("midx")%>"
					},
			dataType : "json",		// json 타입은 문서에서 {"키값" : "vlaue값" , "키값" : "value값2"}
			success : function(result) {	// 결과가 넘어와서 성공했을 때 받는 영역
				// alert("전송성공");				
				var str = "("+result.value+")";
		 		alert(str);
			},
			error : function() {		// 결과가 실패했을 때 받는 영역
				alert("전송실패");
			}			
		});		
	});	
});


</script>
</head>
<body>
	<header>
		<h2 class="mainTitle">글내용</h2>
	</header>

	<article class="detailContents">
		<h2 class="contentTitle"><%=bv.getSubject() %>
			(조회수:<%=bv.getViewcnt() %>) <input type="button" id="btn"
				value="추천(<%=bv.getRecom()%>)">
		</h2>
		<p class="write"><%=bv.getWriter()%>
			(<%=bv.getWriteday() %>)
		</p>
		<hr>
		<div class="content">
			<%=bv.getContents() %>
		</div>
		<% if (bv.getFilename() == null || bv.getFilename().equals("")) {}else{ %>
		<img src="/images/<%=bv.getFilename() %>">
		<%} %>
		
	</article>

	<div class="btnBox">
		<a class="btn aBtn"
			href="<%=request.getContextPath()%>/board/boardDownload.aws?filename=<%=bv.getFilename()%>">다운</a>
		<a class="btn aBtn"
			href="<%=request.getContextPath()%>/board/boardModify.aws?bidx=<%=bv.getBidx()%>">수정</a>
		<a class="btn aBtn" 
			href="<%=request.getContextPath()%>/board/boardDelete.aws?bidx=<%=bv.getBidx()%>">삭제</a> 
		<a class="btn aBtn"
			href="<%=request.getContextPath()%>/board/boardReply.aws?bidx=<%=bv.getBidx()%>">답변</a>
		<a class="btn aBtn"
			href="<%=request.getContextPath()%>/board/boardList.aws">목록</a>
	</div>

	<article class="commentContents">
		<form name="frm">
		<p class="commentWriter" style="width:100px;">
		<input type="text" id="cwriter" name="cwriter" value="<%=memberName%>" readonly="readonly" style="width:100px;border:0px;">
		</p>	
		<input type="text" id="ccontents" name="ccontents">
		<button type="button" id="cmtbtn" class="replyBtn">댓글쓰기</button>
	</form>


		<table class="replyTable">
			<tr>
				<th>번호</th>
				<th>작성자</th>
				<th>내용</th>
				<th>날짜</th>
				<th>DEL</th>
			</tr>
			<tr>
				<td>1</td>
				<td>홍길동</td>
				<td class="content">댓글입니다</td>
				<td>2024-10-18</td>
				<td>sss</td>
			</tr>
		</table>
	</article>

</body>
</html>