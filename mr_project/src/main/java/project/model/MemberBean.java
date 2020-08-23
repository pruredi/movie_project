package project.model;

public class MemberBean {

	private int code;				/* 회원번호 */
	private String join_id;			/* 회원아이디 */
	private String passwd1;			/* 회원비번 */
	private String passwd2;			/* 회원비번확인 */
	private String join_name;		/* 성명 */
	private String join_date;		/* 생년월일 */
	private String join_num;		/* 주민번호 뒷자리 */
	private String addr_num;		/* 우편번호 */
	private String addr1;			/* 주소 */
	private String addr2;			/* 나머지 주소 */
	private String tel;				/* 집전화번호 */
	private String phone;			/* 휴대전화번호 */
	private String email;			/* 이메일 */
	private String join_joindate;		/* 가입 날짜 */
	private String join_deldate;		/* 탈퇴 날짜 */
	private String join_delcont;	/* 탈퇴회원여부 */
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getJoin_id() {
		return join_id;
	}
	public void setJoin_id(String join_id) {
		this.join_id = join_id;
	}
	public String getPasswd1() {
		return passwd1;
	}
	public void setPasswd1(String passwd1) {
		this.passwd1 = passwd1;
	}
	public String getPasswd2() {
		return passwd2;
	}
	public void setPasswd2(String passwd2) {
		this.passwd2 = passwd2;
	}
	public String getJoin_name() {
		return join_name;
	}
	public void setJoin_name(String join_name) {
		this.join_name = join_name;
	}
	public String getJoin_date() {
		return join_date;
	}
	public void setJoin_date(String join_date) {
		this.join_date = join_date;
	}
	public String getJoin_num() {
		return join_num;
	}
	public void setJoin_num(String join_num) {
		this.join_num = join_num;
	}
	public String getAddr_num() {
		return addr_num;
	}
	public void setAddr_num(String addr_num) {
		this.addr_num = addr_num;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJoin_joindate() {
		return join_joindate;
	}
	public void setJoin_joindate(String join_joindate) {
		this.join_joindate = join_joindate;
	}
	public String getJoin_deldate() {
		return join_deldate;
	}
	public void setJoin_deldate(String join_deldate) {
		this.join_deldate = join_deldate;
	}
	public String getJoin_delcont() {
		return join_delcont;
	}
	public void setJoin_delcont(String join_delcont) {
		this.join_delcont = join_delcont;
	}
}
