drop table joindb purge;

/* 화원가입 */
CREATE TABLE joindb (
	code NUMBER(20) NOT NULL, 
	join_id VARCHAR2(20) NOT NULL, /* 회원아이디 */
	passwd1 VARCHAR2(10) NOT NULL, /* 회원비번 */
	passwd2 VARCHAR2(10), /* 회원비번확인 */
	join_name VARCHAR2(10) NOT NULL, /* 성명 */
	join_date VARCHAR2(7) NOT NULL, /* 생년월일 */
	join_num VARCHAR2(8) NOT NULL, /* 주민번호 뒷자리 */
	addr_num VARCHAR2(10), /* 우편번호 */
	addr1 VARCHAR2(50), /* 주소 */
	addr2 VARCHAR2(50), /* 나머지 주소 */
	tel VARCHAR2(20), /* 집전화번호 */
	phone VARCHAR2(20), /* 휴대전화번호 */
	email VARCHAR2(30), /* 이메일 */
	join_joindate date, /* 가입 날짜 */
	join_deldate date, /* 탈퇴 날짜 */	
	join_delcont  VARCHAR2(10) /* 탈퇴회원여부 */
);


create sequence join_code_seq 
increment by 1 start with 1 nocache;


insert into joindb (
	code,
	join_id,passwd1,passwd2,
	join_name,join_date,join_num,
	addr_num,addr1,addr2,
	tel,phone,email,
	join_joindate,join_deldate,join_delcont)
values(join_code_seq .nextval,
	'33333','33333','22222',
	'33333','123456','1234567',
	'18577','경기 화성시 팔탄면 3.1만세로771번길 8','7777',
	'02-7777-7777','010-7777-7777','7777@naver.com',
	sysdate,sysdate,1);

update joindb set
      passwd2='22222'
      where join_id='33333';

-- joindb.sql
select * from tab;
select * from seq;
select * from joindb;
select join_code_seq.nextval from dual;


select * from joindb where join_id='11111' and join_delcont=1;
