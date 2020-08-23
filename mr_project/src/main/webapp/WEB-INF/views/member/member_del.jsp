<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원탈퇴</title>
<link rel="stylesheet" type="text/css" href="./css/admin.css" />
<link rel="stylesheet" type="text/css" href="./css/member.css" />
<script src="./js/jquery.js"></script>
<script>
 function check(){
	 if($.trim($("#passwd1").val())==""){
		 alert("비밀번호를 입력하세요!");
		 $("#passwd1").val("").focus();
		 return false;
	 }
 }
</script>
</head>
<body>
 <div id="del_wrap">
  <h2 class="del_title">회원탈퇴</h2>
  <form method="post" action="member_del_ok.do" onsubmit="return check()">
    <table id="del_t">
     <tr>
      <th>회원아이디</th>
      <td>
      ${d_id}
      </td>
     </tr>
     
     
     <tr>
      <th>비밀번호</th>
      <td>
      <input type="password" name="passwd1" id="passwd1" size="14" 
      			class="input_box" />
      </td>
     </tr>

    </table>
    
    <div id="del_menu">
     <input type="submit" value="탈퇴" class="input_button" />
     <input type="reset" value="취소" class="input_button"
     onclick="$('#passwd1').focus();" />
    </div>
  </form>
 </div>
</body>
</html>