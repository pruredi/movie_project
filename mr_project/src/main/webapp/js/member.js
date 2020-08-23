function id_check() {
	var join_id = $("#join_id").val()
	if($("#join_id").val()==""){
		var newtext='<font color="red">ID를 입력하세요.</font>';
		$("#idcheck").text('');
		$("#idcheck").show();
		$("#idcheck").append(newtext);//아이디 영역에 경고문자 추가
		$("#join_id").val("").focus();
		return false;
	} else if($.trim($("#join_id").val()).length < 4){
		var newtext='<font color="red">아이디는 4자 이상이어야 합니다.</font>';
		$("#idcheck").text('');
		$("#idcheck").show();
		$("#idcheck").append(newtext);//아이디 영역에 경고문자 추가
		$("#join_id").val("").focus();
		return false;
	} else if($.trim($("#join_id").val()).length >12){
		var newtext='<font color="red">아이디는 12자 이하이어야 합니다.</font>';
		$("#idcheck").text('');
		$("#idcheck").show();
		$("#idcheck").append(newtext);//아이디 영역에 경고문자 추가
		$("#join_id").val("").focus();
		return false;
	} else {	
	    $.ajax({
	        type:"POST",
	        url:"member_idcheck.do",
	        data: {"join_id":join_id},        
	        success: function (data) { 
	        	//alert("return success="+data);
	      	  if(data==1){	//중복 ID
	      		var newtext='<font color="red">중복 아이디입니다.</font>';
	      			$("#idcheck").text('');
	        		$("#idcheck").show();
	        		$("#idcheck").append(newtext);
	          		$("#join_id").val('').focus();
	          		return false;
		     
	      	  }else{	//사용 가능한 ID
	      		var newtext='<font color="blue">사용가능한 아이디입니다.</font>';
	      		$("#idcheck").text('');
	      		$("#idcheck").show();
	      		$("#idcheck").append(newtext);
	      		$("#passwd1").focus();
	      	  }  	    	  
	        }
	        , error:function(e){
	    		  alert("data error"+e);
	    	  }
	      });//$.ajax			
	};
}



$(document).ready(function(){

		// 주민번호  뒷자리로 포커스 이동
		$("#join_date").keyup(function(){
			
			if($("#join_date").val().length == 6)
				$("#join_num").focus();
		});
		
		
		// 도메인 선택
		$("#mail_list").change(function(){
			if($("#mail_list").val()==""){		// 직접 입력 선택
				$("#emaildomain").attr("readOnly", false);
				$("#emaildomain").val("").focus();
			}else{							// 도메인을 선택
				$("#emaildomain").val($("#mail_list").val());
				$("#emaildomain").attr("readOnly","readOnly");				
			}
		});
		
		
		// 유효성 검사
		$("form").submit(function(){
			
			if($("#join_id").val() == ""){
				alert("ID를 입력하세요.");
				$("#join_id").focus();
				return false;
			}
			if($("#passwd1").val()==""){
				alert("비밀번호를 입력하세요.");
				$("#passwd1").focus();
				return false;
			}
			if($("#passwd2").val()==""){
				alert("비밀번호 확인을 입력하세요.");
				$("#passwd2").focus();
				return false;
			}
			
			//비번 틀릴시
			if($("#passwd1").val() != $("#passwd2").val()){
				alert("비밀번호와 비밀번호 확인을 동일하게 입력하세요.");
				$("#passwd1").focus();
				return false;
			}
			
			if($("#join_name").val()==""){
				alert("이름을 입력하세요.");
				$("#join_name").focus();
				return false;
			}
			if($("#join_date").val()==""){
				alert("주민번호 앞자리를 입력하세요.");
				$("#join_date").focus();
				return false;
			}
			if($("#join_date").val().length != 6){
				alert("주민번호 앞자리를 6자리로 입력하세요.");
				$("#join_date").val("").focus();
				return false;
			}
			// isNaN() : 문자가 포함되면 true를 리턴하는 함수
			if(isNaN($("#join_date").val())){
				alert("주민번호 앞자리는 숫자만 입력하세요.");
				$("#join_date").val("").focus();
				return false;
		    }
			if($("#join_num").val()==""){
				alert("주민번호 뒷자리를 입력하세요.");
				$("#join_num").focus();
				return false;
			}
			if($("#join_num").val().length != 7){
				alert("주민번호 뒷자리 7자리를 입력하세요.");
				$("#join_num").val("").focus();
				return false;
			}
			// isNaN() : 문자가 포함되면 true를 리턴하는 함수
			if(isNaN($("#join_num").val())){
				alert("주민번호 뒷자리는 숫자만 입력하세요.");
				$("#join_num").val("").focus();
				return false;
		    }
			// 우편번호

			if($("#addr_num").val()==""){
				alert("우편번호를 입력하세요.");
				$("#addr_num").focus();
				return false;
			}
			// 주소
			if($("#addr1").val()==""){
				alert("주소를 입력하세요.");
				$("#addr1").focus();
				return false;
			}
			if($("#addr2").val()==""){
				alert("나머지 주소를 입력하세요.");
				$("#addr2").focus();
				return false;
			}
			
			
			
			if($("#tel2").val() != ""){
				if($("#tel2").val().length < 3){
					alert("전화번호 중간자리는 3자리 이상을 입력하세요.");
					$("#tel2").val("").focus();
					return false;
				}
			}
			if(isNaN($("#tel2").val())){
				alert("전화번호 중간자리는 숫자만 입력하세요.");
				$("#tel2").val("").focus();
				return false;
			}
			if($("#tel3").val() != ""){
				if($("#tel3").val().length < 4){
					alert("전화번호 끝자리는 4자리를 입력하세요.");
					$("#tel3").val("").focus();
					return false;
				}
			}
			if(isNaN($("#tel3").val())){
				alert("전화번호 끝자리는 숫자만 입력하세요.");
				$("#tel3").val("").focus();
				return false;
			}
			
			
			if($("#tel2").val().length >= 3){
				if($("#tel3").val().length < 4){
					alert("전화번호 끝자리는 4자리를 입력하세요.");
					$("#tel3").val("").focus();
					return false;
				}
			}
			if($("#tel3").val().length == 4){
				if($("#tel2").val().length < 3){
					alert("전화번호 중간자리는 3자리 이상을 입력하세요.");
					$("#tel2").val("").focus();
					return false;
				}
			}
			
		
			if($("#phone1").val()==""){
				alert("핸드폰 앞자리를 선택 하세요.");
				return false;
			}
			if($("#phone2").val()==""){
				alert("핸드폰 중간자리를 입력하세요.");
				$("#phone2").focus();
				return false;
			}
			if(isNaN($("#phone2").val())){
				alert("핸드폰 중간자리는 숫자만 입력하세요.");
				$("#phone2").val("").focus();
				return false;
			}
			if($("#phone3").val()==""){
				alert("핸드폰 끝자리를 입력하세요.");
				$("#phone3").focus();
				return false;
			}
			if(isNaN($("#phone3").val())){
				alert("핸드폰 끝자리는 숫자만 입력하세요.");
				$("#phone3").val("").focus();
				return false;
			}

			if($("#emailid").val()==""){
				alert("EMail주소를 입력하세요.");
				$("#emailid").focus();
				return false;
			}
			if($("#emaildomain").val()==""){
				alert("도메인을 입력하세요.");
				$("#emaildomain").focus();
				return false;
			}

			
	});	// submit() end	
		
}); // ready() end