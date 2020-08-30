package project.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;

import project.kakao.kakao;
import project.model.MemberBean;
import project.service.MemberServiceImpl;


@Controller
public class MemberAction {

	@Autowired
	private MemberServiceImpl memberService;

	// ID중복검사 ajax함수로 처리부분
	@RequestMapping(value = "/member_idcheck.do", method = RequestMethod.POST)
	public String member_idcheck(String join_id, Model model) throws Exception {
		System.out.println("join_id:"+join_id);
		int result = memberService.checkMemberId(join_id);
		model.addAttribute("result", result);

		return "member/idcheckResult";
	}

	/* 로그인 폼 뷰 */
	@RequestMapping(value = "/member_login.do")
	public String member_login() {
		System.out.println("member_login");
		return "member/member_login";
	}

	/* 비번찾기 폼 */
	@RequestMapping(value = "/pwd_find.do")
	public String pwd_find() {
		return "member/pwd_find";
		// member 폴더의 pwd_find.jsp 뷰 페이지 실행
	}

	/* 회원가입 폼 */
	@RequestMapping(value = "/member_join.do")
	public String member_join() {
		System.out.print("member_join.do");
		return "member/member_join";
	}


	/* 비번찾기 완료 */
	@RequestMapping(value = "/pwd_find_ok.do", method = RequestMethod.POST)
	public String pwd_find_ok(@ModelAttribute MemberBean mem,
							HttpServletResponse response, Model model)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		MemberBean member = memberService.findpwd(mem);

		if (member == null) {// 값이 없는 경우

			return "member/pwdResult";

		} else {
			
			/* 임시비밀번호 생성 */
			String temPasswd2 = "";
			 int d = 0;
			  for (int i = 1; i <= 5; i++){
			    Random r = new Random();
			    d = r.nextInt(9);
			    temPasswd2 += Integer.toString(d);
			  }	// 무작위 임시비밀번호 생성
			 
			  member.setPasswd2(temPasswd2);
			  memberService.tem_pwd(member);
			  //임시비번 업데이트


			// Mail Server 설정
			String charSet = "utf-8";
			String hostSMTP = "smtp.naver.com";
			String hostSMTPid = "chlchdyd@naver.com";
			String hostSMTPpwd = "Pr84528542@"; // 비밀번호 입력해야함

			// 보내는 사람 EMail, 제목, 내용
			String fromEmail = "chlchdyd@naver.com";
			String fromName = "관리자";
			String subject = "비밀번호 찾기";

			// 받는 사람 E-Mail 주소
			String mail = member.getEmail();

			try {
				HtmlEmail email = new HtmlEmail();
				email.setDebug(true);
				email.setCharset(charSet);
				email.setSSL(true);
				email.setHostName(hostSMTP);
				email.setSmtpPort(587);

				email.setAuthentication(hostSMTPid, hostSMTPpwd);
				email.setTLS(true);
				email.addTo(mail, charSet);
				email.setFrom(fromEmail, fromName, charSet);
				email.setSubject(subject);
				
				email.setHtmlMsg("<p align = 'center'>비밀번호 찾기</p><br>"
						+ "<div align = 'center'>비밀번호 2번째 비번" + member.getPasswd2() + "</div>"
				+ "<div align = 'center'>비밀번호 2번째 비번" + temPasswd2 + "</div>");

				//member.getJoin_pwd() - 임시비번 설정
				
				email.send();
			} catch (Exception e) {
				System.out.println(e);
			}

			model.addAttribute("pwdok", "등록된 email을 확인 하세요~!!");
			return "member/pwd_find";

		}

	}


	
	/* 회원 가입 저장 */
	@RequestMapping(value = "/member_join_ok.do", method = RequestMethod.POST)
	public String member_join_ok(MemberBean member,
								 HttpServletRequest request,
								 Model model) throws Exception {
		
		System.out.println("member_join_ok.do");
		
		int result=0;
		
		String tel1 = request.getParameter("tel1");
		String tel2 = request.getParameter("tel2");
		String tel3 = request.getParameter("tel3");
		String tel = tel1 + "-" + tel2 + "-" + tel3;
		
		String phone1 = request.getParameter("phone1");
		String phone2 = request.getParameter("phone2");
		String phone3 = request.getParameter("phone3");
		String phone = phone1 + "-" + phone2 + "-" + phone3;
		
		String emailid = request.getParameter("emailid");
		String emaildomain = request.getParameter("emaildomain");
		String email = emailid + "@" + emaildomain;
		
		
		member.setTel(tel);
		member.setPhone(phone);
		member.setEmail(email);
		
		memberService.insertMember(member);
		
		return "redirect:member_login.do";
	}


	
	/* 로그인 인증 */
	@RequestMapping(value = "/member_login_ok.do")
	public String member_login_ok(@RequestParam("id") String id, 
			                      @RequestParam("pwd") String pwd,
			                      HttpSession session, 
			                      Model model) throws Exception {
		System.out.println("/member_login_ok.do");
		int result=0;		
		MemberBean mb = memberService.userCheck(id);
		
		if (mb == null) {	// 등록되지 않은 회원일때
			
			result = 1;
			model.addAttribute("result", result);
			
			System.out.println("등록되지 않은 회원일때");
			return "member/loginResult";
			
		} else {			// 등록된 회원일때
			if (mb.getPasswd1().equals(pwd)) {// 비번이 같을때
				if(mb.getPasswd1().equals(pwd) == mb.getPasswd2().equals(pwd)) {
				session.setAttribute("id", id);

				String join_name = mb.getJoin_name();
				
				model.addAttribute("join_name", join_name);
				
				return "member/main";
				
				} else {
					System.out.println("// 비밀번호가 서로 다를때 /member/pwd_change");
					
					session.setAttribute("id", id);

					String join_id = mb.getJoin_id();
					String join_name = mb.getJoin_name();
					
					model.addAttribute("join_id", join_id);
					model.addAttribute("join_name", join_name);
					
					return "member/pwd_change";		
				}
				
			} else if(mb.getPasswd2().equals(pwd)) {// 비밀번호 2번째가 같을때
				System.out.println("// 비밀번호 2번째로 로그인 했을때 /member/pwd_change");
				
				session.setAttribute("id", id);

				String join_id = mb.getJoin_id();
				String join_name = mb.getJoin_name();
				
				model.addAttribute("join_id", join_id);
				model.addAttribute("join_name", join_name);
				
				return "member/pwd_change";			
				
			} else {// 비번이 다를때
				result = 2;
				model.addAttribute("result", result);
				
				return "member/loginResult";				
			}
		}
		// 탈퇴한 회원 일때
		// 추가예정

	}
	
	/* 임시비밀번호 바꾸기 */
	@RequestMapping(value = "/pwd_change_ok.do")
	public String pwd_change_ok(MemberBean member,
								HttpServletRequest request, 
								HttpSession session, 
								Model model
								) throws Exception {
		System.out.println("/pwd_change_ok.do");
		
		String id = (String) session.getAttribute("id");
		
		MemberBean mb = this.memberService.userCheck(id);		
		
		member.setJoin_id(id);
		
		memberService.pwdupdateMember(member);// 수정 메서드 호출
		
		model.addAttribute("join_name", mb.getJoin_name());

		return "member/main";
		}
		
	
	/* 회원정보 수정 폼 */
//	@RequestMapping(value = "/member_edit.do", method = RequestMethod.POST)
	@RequestMapping(value = "/member_edit.do")
	public String member_edit(HttpSession session, Model m) throws Exception {
		System.out.println("/member_edit.do");
		
		String id = (String) session.getAttribute("id");
		
		MemberBean mbbe = memberService.userCheck(id);
		
		String tel = mbbe.getTel();
		

		if(tel.length() > 5) {
			StringTokenizer st01 = new StringTokenizer(tel, "-");
			// java.util 패키지의 StringTokenizer 클래스는 첫번째 전달인자를
			// 두번째 -를 기준으로 문자열을 파싱해준다.
			String tel1 = st01.nextToken();// 첫번째 전화번호 저장
			String tel2 = st01.nextToken(); // 두번째 전번 저장
			String tel3 = st01.nextToken();// 세번째 전번 저장
			
			// m.addAttribute("mbbe", mbbe);
			m.addAttribute("tel1", tel1);
			m.addAttribute("tel2", tel2);
			m.addAttribute("tel3", tel3);
		}

		
		String phone = mbbe.getPhone();
		StringTokenizer st02 = new StringTokenizer(phone, "-");
		// java.util 패키지의 StringTokenizer 클래스는 첫번째 전달인자를
		// 두번째 -를 기준으로 문자열을 파싱해준다.
		String phone1 = st02.nextToken();// 첫번째 전화번호 저장
		String phone2 = st02.nextToken(); // 두번째 전번 저장
		String phone3 = st02.nextToken();// 세번째 전번 저장
		
		
		String email = mbbe.getEmail();
		StringTokenizer st03 = new StringTokenizer(email, "@");
		String emailid = st03.nextToken();// 첫번째 전화번호 저장
		String emaildomain = st03.nextToken(); // 두번째 전번 저장
		
		
		m.addAttribute("mbbe", mbbe);
		m.addAttribute("phone1", phone1);
		m.addAttribute("phone2", phone2);
		m.addAttribute("phone3", phone3);
		m.addAttribute("emailid", emailid);
		m.addAttribute("emaildomain", emaildomain);
		// 따로 따로 해 놓아야 view 페이지에서 출력이 가능하다.
		
		return "member/member_edit";
	}


	
	
	/* 회원정보 수정 */
	@RequestMapping(value = "/member_edit_ok.do", method = RequestMethod.POST)
	public String member_edit_ok(MemberBean member,
								 HttpServletRequest request, 
								 HttpSession session, 
								 Model model
								 ) throws Exception {
		System.out.println("/member_edit_ok.do");
		
		String id = (String) session.getAttribute("id");

		
		String tel1 = request.getParameter("tel1").trim();
		String tel2 = request.getParameter("tel2").trim();
		String tel3 = request.getParameter("tel3").trim();
		String tel = tel1 + "-" + tel2 + "-" + tel3;
		
		String phone1 = request.getParameter("phone1").trim();
		String phone2 = request.getParameter("phone2").trim();
		String phone3 = request.getParameter("phone3").trim();
		String phone = phone1 + "-" + phone2 + "-" + phone3;
		
		String emailid = request.getParameter("emailid").trim();
		String emaildomain = request.getParameter("emaildomain").trim();
		String email = emailid + "@" + emaildomain;
		
		MemberBean editm = this.memberService.userCheck(id);		
		
		member.setJoin_id(id);
		member.setTel(tel);
		member.setPhone(phone);
		member.setEmail(email);
		
		
		memberService.updateMember(member);// 수정 메서드 호출
		
		model.addAttribute("join_name", member.getJoin_name());
		
		return "member/main";
	}

	
	
	
	/* 회원정보 삭제 폼 */
	@RequestMapping(value = "/member_del.do")
	public String member_del(HttpSession session, Model dm) throws Exception {
		System.out.println("/member_del.do");
		
		String id = (String) session.getAttribute("id");
		MemberBean deleteM = memberService.userCheck(id);
		dm.addAttribute("d_id", id);
		dm.addAttribute("d_name", deleteM.getJoin_name());
		
		return "member/member_del";
	}

	/* 회원정보 삭제 완료 */
	@RequestMapping(value = "/member_del_ok.do", method = RequestMethod.POST)
	public String member_del_ok(@RequestParam("passwd1") String passwd1, 
							    HttpSession session) throws Exception {
		System.out.println("/member_del_ok.do");
		
		String id = (String) session.getAttribute("id");
		MemberBean member = this.memberService.userCheck(id);

		if (!member.getPasswd1().equals(passwd1)) {

			return "member/deleteResult";
			
		} else {// 비번이 같은 경우
			
			
			MemberBean delm = new MemberBean();
			delm.setJoin_id(id);

			memberService.deleteMember(delm);// 삭제 메서드 호출

			session.invalidate();	// 세션만료

			return "redirect:member_login.do";
		}
	}

	// 로그아웃
	@RequestMapping("member_logout.do")
	public String logout(HttpSession session) {
		session.invalidate();

		return "member/member_logout";
	}
	
	
	
	
	
	
	
	
	
	

	//카카오 로그인
	//컨트롤을 세션에 저장하기
	//@RequestMapping(value = "/kakao_login_ok.do", produces = "application/json")
	@RequestMapping(value = "/kakao_login_ok.do", produces = "application/json")
    public String kakaoLogin(
    		@RequestParam("code") String code, 	
    		Model model, HttpSession session) {
		
		System.out.println("/kakao_login_ok.do");
        //카카오 홈페이지에서 받은 결과 코드
        System.out.println("code - " + code);
        
        //카카오 rest api 객체 선언
//        kakao kr = new kakao();
//        System.out.println("kr - " + kr);
        
        //결과값을 node에 담아줌
        JsonNode node = kakao.getAccessToken(code);
        System.out.println("node - " + node);
        
        
        // 로그인 페이지로 넘어가기 위한 임시 id 주입
		session.setAttribute("id", code);
		
        //사용자 정보 요청
        JsonNode userInfo = kakao.getKakaoUserInfo(code);

        System.out.println("userInfo - " + userInfo);

        // Get id
 		String id = userInfo.path("id").asText();
 		String nickname = null;
 		String account_email = userInfo.path("account_email").asText();
 		String birthday = userInfo.path("birthday").asText();

        // 유저정보 카톡에서 가져오기 Get properties
		JsonNode properties = userInfo.path("properties");
		
		if (properties.isMissingNode()) {
			System.out.println("nickname : ");
		} else {
			nickname = properties.path("nickname").asText();
			System.out.println("nickname : " + nickname);
			System.out.println("account_emaile : " + account_email);
			System.out.println("birthday : " + birthday);
		}        

        return "member/main";
    }


	    

	
	
	
	
	


	
	
	//카카오 로그아웃
    @RequestMapping("kakao_logout.do")
    public String kakao_logout(HttpSession session, HttpServletRequest request) {
    	System.out.println("kakao_logout.do");
        
        session.invalidate(); //세션 초기화
        
        return "member/member_logout";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //리뷰 메인 페이지 뷰
	@RequestMapping(value = "/review_main.do")
	public String review_main() {
		System.out.println("review_main");
		return "review/review_main";
	}
	
	
	
	
}


