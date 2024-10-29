<%@page import="mvc.vo.BoardVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
BoardVo bv = (BoardVo)request.getAttribute("bv"); // 강제형변환 양쪽형을 맞춰준다

String memberName = "";
int midx = 0;
if(session.getAttribute("memberName") != null) {
	memberName = (String)session.getAttribute("memberName");
}
if(session.getAttribute("midx") != null) {
	midx = (int)session.getAttribute("midx");
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
function commentDel(cidx) {
	
	let ans = confirm("삭제하시겠습니까?");
	
	if (ans == true) {
		
		$.ajax({
			type : "get",	// 전송방식
			url : "<%=request.getContextPath()%>/comment/commentDeleteAction.aws?cidx=" + cidx,
			dataType : "json",		// json 타입은 문서에서 {"키값" : "vlaue값" , "키값" : "value값2"}
			success : function(result) {	// 결과가 넘어와서 성공했을 때 받는 영역
				// alert("전송성공");				
				// alert(result.value);
				$.boardCommentList();
			},
			error : function() {		// 결과가 실패했을 때 받는 영역
				alert("전송실패");
			}			
		});
	}
	
	return;
}

//jquery로 만드는 함수
$.boardCommentList = function(){
	
	$.ajax({
		type : "get",	// 전송방식
		url : "<%=request.getContextPath()%>/comment/commentList.aws?bidx=<%=bv.getBidx()%>",
		dataType : "json",		// json 타입은 문서에서 {"키값" : "vlaue값" , "키값" : "value값2"}
		success : function(result) {	// 결과가 넘어와서 성공했을 때 받는 영역
			// alert("전송성공");	
		var strTr = "";
		
		$(result).each(function(){
			
			var btnn = "";
			if (this.midx == "<%=midx%>"){	// 현재로그인 사람과 댓글쓴 사람의 번호가 같을때만 나타남				
				if (this.delyn == 'N') {
					btnn = "<button class = 'btn' type = 'button' onclick = 'commentDel(" + this.cidx + ")'>삭제</button>";
					
				}				
			}
			
			strTr = strTr + "<tr>"
			+ "<td>" + this.cidx + "</td>"
			+ "<td>" + this.cwriter + "</td>"
			+ "<td class='content'>" + this.ccontents + "</td>"
			+ "<td>" + this.writeday + "</td>"
			+ "<td>" + btnn + "</td>"
			+ "</tr>"
			
		});
		
		var str = "<table class='replyTable'>"
		+ "<tr>" 
		+ "<th>번호</th>"
		+ "<th>작성자</th>"
		+ "<th>내용</th>"
		+ "<th>날짜</th>"
		+ "<th>DEL</th>"
		+ "</tr>" + strTr + "</table>"
		
		$("#commentListView").html(str);
		
		},
		error : function() {		// 결과가 실패했을 때 받는 영역
			// alert("전송실패");
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
		
		let loginCheck = "<%=midx%>";
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
					"midx" : "<%=midx%>"
					},
			dataType : "json",		// json 타입은 문서에서 {"키값" : "vlaue값" , "키값" : "value값2"}
			success : function(result) {	// 결과가 넘어와서 성공했을 때 받는 영역
				// alert("전송성공");				
				// var str = "("+result.value+")";
		 		alert("댓글이 등록되었습니다.");
		 		if(result.value == 1) {
		 			$("#contents").val("");
		 		}
		 		
		 		$.boardCommentList();
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
		<input type="text" id="cwriter" name="cwriter" value="<%=memberName%>" readonly="readonly" style="width:100%;border:0px;">
		</p>	
		<input type="text" id="ccontents" name="ccontents"style="width:75%;">		
		<button type="button" id="cmtbtn" class="replyBtn">댓글쓰기</button>
	</form>	
		
		<div id = "commentListView"></div>
	</article>
</body>
</html>