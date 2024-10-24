<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<HTML>
 <HEAD>
  <TITLE> 회원 로그인 </TITLE>
  <style>
header {
	width:100%
	height:100px;
	text-align:center;
}
nav {
	width:15%;
	height:300px;
	float:left;
	--background-color:blue;
}
section { 
	width: 70%;	
	height: 400px;
	float: left; 
	--background: olivedrab; 
}
aside { 
	width: 15%; 
	height: 300px; 
	float: left; 
	--background: orange; 
		}
footer {
	width: 100%;
	height: 100px; 
	clear: both; 
	--background: plum; 
	text-align:center;
		}

</style>
<script>
// 아이디, 비밀번호 유효성검사
function check() {
	// 이름으로 객체찾기
	let memberid = document.getElementsByName("memberid");
	let memberpwd = document.getElementsByName("memberpwd");
	//alert(memberid[0].value);
	
	
	if(memberid[0].value == "") {
		alert("아이디를 입력해주세요");
		memberid[0].focus();
		return;
	} else if(memberpwd[0].value == "") {
		alert("비밀번호를 입력해주세요");
		memberpwd[0].focus();
		return;
	}
	
	var fm = document.frm;
	fm.action = "<%=request.getContextPath()%>/member/memberLoginAction.aws";	// 가상경로지정 action은 처리하는 의미
	fm.method = "post";
	fm.submit();
	
	return
}
</script>
 </HEAD>
 <BODY>
<header>회원 로그인</header>
<nav></nav>
<section>
	 <article>
		<form name="frm">
			<table border=1 style="width:500px">
				<tr>
					<td>아이디</td>
					<td><input type="text" name="memberid" maxlength="20" style="width:150px" placeholder="아이디를 입력하세요"></td>
				</tr>
				<tr>
					<td>비밀번호</td>
					<td><input type="password" name="memberpwd" maxlength="20" style="width:150px" placeholder="비밀번호를 입력하세요"></td>
				</tr>
				<tr>
					<td colspan=2 style="text-align:center">
					<input type="button" name="btn" value="로그인" onclick = "check();">
					</td>
				</tr>
			</table>
		</form>
	</article>
</section>
<aside>
</aside>
<footer>
made by kis
</footer>
 </BODY>
</HTML>
