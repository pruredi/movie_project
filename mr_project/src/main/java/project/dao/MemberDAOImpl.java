package project.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import project.model.MemberBean;

@Repository
public class MemberDAOImpl {
	
	@Autowired
	private SqlSession sqlSession;	

	/* 회원저장 */
	public void insertMember(MemberBean member) throws Exception {
		System.out.println("dao - insertMember");
		sqlSession.insert("member_join", member);
	}

	/* 아이디 중복 체크 */
	public int checkMemberId(String id) throws Exception {
		int re = -1;	// 사용 가능한 ID
		MemberBean mb = (MemberBean) sqlSession.selectOne("login_check", id);
		if (mb != null)
			re = 1; 	// 중복id
		return re;
	}
	
	/* 비번 검색 */
	public MemberBean findpwd(MemberBean pm) throws Exception {
		return (MemberBean) sqlSession.selectOne("pwd_find", pm);
	}

	/* 로그인 인증 체크 */
	public MemberBean userCheck(String id) throws Exception {
		System.out.println("dao - userCheck");
		return (MemberBean) sqlSession.selectOne("login_check", id);
	}

	/* 회원수정 */
	public void updateMember(MemberBean member) throws Exception {
		System.out.println("dao - updateMember");
		sqlSession.update("member_edit", member);
	}
	
	/* 회원삭제 */
	public void deleteMember(MemberBean delm) throws Exception {
		System.out.println("dao - deleteMember");
		sqlSession.update("member_delete", delm);
	}
	
	/* 임시비밀번호수정 */
	public void pwdupdateMember(MemberBean member) throws Exception {
		System.out.println("dao - pwdupdateMember");
		sqlSession.update("pwd_edit", member);
	}
	
	/* 임시비밀번호생성 */
	public void tem_pwd(MemberBean member) throws Exception {
		System.out.println("dao - tem_pwd");
		sqlSession.update("tem_pwd", member);
	}
	
	
	
}





