<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/admin.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/member.css" />
<!-- <script src="./js/jquery.js"></script> -->
<script src="http://code.jquery.com/jquery-latest.js"></script>

<script>
 function check(){
	 if($.trim($("#id").val())==""){
		 alert("로그인 아이디를 입력하세요!");
		 $("#id").val("").focus();
		 return false;
	 }
	 if($.trim($("#pwd").val())==""){
		 alert("비밀번호를 입력하세요!");
		 $("#pwd").val("").focus();
		 return false;
	 }
 }
 
 /*비번찾기 공지창*/
 function pwd_find(){
	 window.open("pwd_find.do","비번찾기","width=450,height=500");
	 //자바 스크립트에서 window객체의 open("공지창경로와 파일명","공지창이름","공지창속성")
	 //메서드로 새로운 공지창을 만듬.폭이 400,높이가 400인 새로운 공지창을 만듬.단위는 픽셀
 }
</script>

<!-- 카카오 로그인 버튼을 생성  -->
<script src = "//developers.kakao.com/sdk/js/kakao.min.js"></script>


</head>
<body>
 <div id="login_wrap">
  <h2 class="login_title">로그인</h2>
  <form method="post" action="member_login_ok.do"
  		onsubmit="return check()">
   <table id="login_t">
    <tr>
     <th>아이디</th>
     <td>
      <input name="id" id="id" size="20" class="input_box" />
     </td>
    </tr>
    
    <tr>
     <th>비밀번호</th>
     <td>
     <input type="password" name="pwd" id="pwd" size="20" class="input_box"/>
     </td>
    </tr>
   </table>
    <div id="login_menu">
    <input type="submit" value="로그인" class="input_button" />
    <input type="reset" value="취소" class="input_button"
    		onclick="$('#id').focus();" />
    <input type="button" value="회원가입" class="input_button"
    		onclick="location='member_join.do'" />
    <input type="button" value="비번찾기" class="input_button"
    		onclick="pwd_find()" />
    </div>

	<div>
<!--     <a href = "https://kauth.kakao.com/oauth/authorize?client_id=369b216cafcc89156aa8d4dd04ab8675&redirect_uri=369b216cafcc89156aa8d4dd04ab8675&response_type=code"> -->
    <a href = "https://kauth.kakao.com/oauth/authorize?client_id=369b216cafcc89156aa8d4dd04ab8675&redirect_uri=http://localhost/join3/kakao_login_ok.do&response_type=code">
        로그인
    </a>
	</div>


 	<div id="kakao-login_menu">
		<a id="kakao-login-btn" onclick="location='kakao_login_ok.do'"></a>
		<a href="http://developers.kakao.com/logout"></a>

<script type='text/javascript'>
	Kakao.init('54aecdbad390640bb2ddaecda41426d8'); //자바스크립트 키
			 
	//카카오 로그인 버튼을 생성
	Kakao.Auth.createLoginButton({ 
		container: '#kakao-login-btn', 
		success: function(authObj) { 
			Kakao.API.request({
				url: '/v1/user/me',
				success: function(res) {
					//console.log(res.id);//콘솔 로그에 id 출력(id는 res안에 있기 때문에  res.id 로 불러온다)
			        //console.log(res.kaccount_email);//콘솔 로그에 email 출력
			        //var kakaonickname = res.properties.nickname;  //카카오톡 닉네임을 변수에 저장 (닉네임 값을 다른페이지로 넘겨 출력하기 위해서)
			        // window.location.replace("http://localhost/join3/kakao_login_ok.do?kakaonickname="+kakaonickname);
			        window.location.replace("http://localhost/join3/kakao_login_ok.do");
			        //로그인 결과 페이지로 닉네임 값을 넘겨서 출력시킨다.
			        alert('카카오 로그인')
				}
			})
		},
		fail: function(error) {
		alert(JSON.stringify(error));
		}
	});
</script>

  	</div>
    
  </form>
 </div>
</body>
</html>


 
