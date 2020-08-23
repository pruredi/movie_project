<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 수정</title>

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
//우편번호, 주소 Daum API
function openDaumPostcode() {
	new daum.Postcode({
		oncomplete : function(data) {				
			document.getElementById('addr_num').value = data.zonecode;
			document.getElementById('addr1').value = data.address;				
		}
	}).open();
}
</script>

<script src="<%=request.getContextPath() %>/js/member.js"></script>

</head>
<body>
  <div id="join_wrap">
  <h2 class="join_title">회원수정</h2>
  <form name="f" method="post" action="member_edit_ok.do"
  		onsubmit="return edit_check()">
   <!-- 이진파일을 업로드 할려면 enctype 속성을 지정 -->
   <table id="join_t">
    <tr>
     <th>회원아이디</th>
     <td>
      ${mbbe.join_id}
     </td>
    </tr>
    
    <tr>
     <th>회원비번</th>
     <td>
      <input type="password" name="passwd1" id="passwd1" size="14"
      class="input_box" />
     </td>
    </tr>
    
    <tr>
     <th>회원비번확인</th>
     <td>
      <input type="password" name="passwd2" id="passwd2" size="14"
      class="input_box" />
     </td>
    </tr>
    
    <tr>
     <th>성명</th>
     <td>
      <input name="join_name" id="join_name" size="14" class="input_box"
      value="${mbbe.join_name}" />
     </td>
    </tr>
    
    <tr>
     <th>주민등록 번호</th>
     <td>
      <input name="join_date" id="join_date" size="7" class="input_box" maxlength="6" value="${mbbe.join_date}"/>
     -
      <input type="password" name="join_num" id="join_num" size="8" maxlength="7" value="${mbbe.join_num}"
      class="input_box" />
     </td>
    </tr>
    
    <tr>
     <th>우편번호</th>
     <td>
      <input name="addr_num" id="addr_num" size="5" class="input_box"
      		readonly onclick="post_search()" value="${mbbe.addr_num}"/>
      <input type="button" value="우편번호검색" class="input_button"
      		onclick="openDaumPostcode()" />
     </td>
    </tr>
    
    <tr>
     <th>주소</th>
     <td>
      <input name="addr1" id="addr1" size="50" class="input_box"
      readonly value="${mbbe.addr1}" onclick="post_search()" />
     </td>
    </tr>
    
    <tr>
     <th>나머지 주소</th>
     <td>
      <input name="addr2" id="addr2" size="37" 
      value="${mbbe.addr2}" class="input_box" />
     </td>
    </tr>
    
    <tr>
     <th>집전화번호</th>
     <td>
      <%@ include file="../include/tel_number.jsp"%>
      <select name="tel1" >      
      <c:forEach var="t" items="${tel}" begin="0" end="16">
      	<option value="${t}" <c:if test="${tel1 == t}">${'selected'}
          </c:if>>${t}
        </option>
      </c:forEach>        
      </select>-
      <input name="tel2" id="tel2" size="4" maxlength="4" class="input_box" value="${tel2}"/>-
      <input name="tel3" id="tel3" size="4" maxlength="4" class="input_box" value="${tel3}"/>
     </td>
    </tr>
    
    <tr>
     <th>휴대전화번호</th>
     <td>
     <%@ include file="../include/phone_number.jsp" %>
     <select name="phone1">
      <c:forEach var="p" items="${phone}" begin="0" end="5">
       <option value="${p}" <c:if test="${phone1 == p}">${'selected'}
          </c:if>>${p}
        </option>
      </c:forEach>
     </select>-
     <input name="phone2" id="phone2" size="4" maxlength="4" class="input_box" value="${phone2}"/>-
     <input name="phone3" id="phone3" size="4" maxlength="4" class="input_box" value="${phone3}"/>
     </td>
    </tr>
    
    <tr>
     <th>전자우편</th>
     <td>
      <input name="emailid" id="emailid" size="10" class="input_box" value="${emailid}"/>@
      <input name="emaildomain" id="emaildomain" size="20" class="input_box" readonly value="${emaildomain}" />
      
      <!--readonly는 단지 쓰기,수정이 불가능하고 읽기만 가능하다 //-->
      <select name="mail_list" onchange="domain_list()">
      <option value="">=이메일선택=</option>
      <option value="daum.net" 
      		<c:if test="${emaildomain == 'daum.net'}">${'selected'}
            </c:if>>daum.net</option>
      <option value="nate.com" 
      		<c:if test="${emaildomain == 'nate.com'}">${'selected'}
            </c:if>>nate.com</option>
      <option value="naver.com" 
      		<c:if test="${emaildomain == 'naver.com'}">${'selected'}
            </c:if>>naver.com</option>
      <option value="hotmail.com" 
            <c:if test="${emaildomain == 'hotmail.com'}">${'selected'}
            </c:if>>hotmail.com</option>
      <option value="gmail.com" 
            <c:if test="${emaildomain == 'gmail.com'}">${'selected'}
            </c:if>>gmail.com</option>
      <option value="0">직접입력</option>
     </select> 
     </td>
    </tr>
    

   </table>
   
   <div id="join_menu">
    <input type="submit" value="회원수정" class="input_button" />
    <input type="reset" value="수정취소" class="input_button" 
    onclick="$('#passwd1').focus();" />
   </div>
  </form>
 </div>
</body>
</html>