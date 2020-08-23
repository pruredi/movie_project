<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 수정</title>
<link rel="stylesheet" type="text/css" href="./css/admin.css" />
<link rel="stylesheet" type="text/css" href="./css/member.css" />
<script src="./js/jquery.js"></script>
<script>
 function check(){
	 if($.trim($("#passwd2").val())==""){
		 alert("비밀번호를 입력하세요!");
		 $("#passwd2").val("").focus();
		 return false;
	 }
 }
</script>
</head>


<body>
<c:if test="${sessionScope.id == null }"> 
  <script>
   alert("다시 로그인 해주세요!");
   location.href="<%=request.getContextPath()%>/member_login.do";
  </script>
</c:if>

<c:if test="${sessionScope.id != null }">  
 <div id="pwd_change_wrap">
  <h2 class="pwd_change_title">비밀번호 수정</h2>
   <form method="post" action="pwd_change_ok.do"> 
   	<input type="hidden" name="join_id" value="${join_id}">
    <table id="pwd_change_t">
     <tr>
     <th colspan="2">${join_name}님 로그인을 환영합니다</th>
    </tr>
     <tr>
     	<th colspan="2">새로운 비밀번호를 입력해주세요</th>
     </tr>
     <tr>
      <th>회원아이디</th>
      <td>
      ${join_id}
      </td>
     </tr>
     
     
     <tr>
      <th>비밀번호</th>
      <td>
      <input type="password" name="passwd2" id="passwd2" size="14"
      class="input_box" />
      </td>
     </tr>

    </table>
    
    <div id="pwd_edit_menu">
     <input type="submit" value="수정" class="input_button" />
     <input type="reset" value="취소" class="input_button"
     onclick="$('#passwd2').focus();" />
     <input type="reset" value="로그아웃" class="input_button"
     onclick="location='member_logout.do'" />
    </div>
    
   </form>
 </div>
</c:if>



</body>
</html>