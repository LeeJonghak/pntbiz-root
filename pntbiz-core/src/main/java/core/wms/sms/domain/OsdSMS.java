package core.wms.sms.domain;

public class OsdSMS {
	/**
	 * SMS DB filed comment
	 */
	/*
	ColumnName	Data	Type	Property	Decription 
	CMP_MSG_ID	VARCHAR2(20)	PRIMARY_KEY	EC	고객측에서 unique 20자리 값으로 설정 일반적으로 처리 일시 기준 Ex.‘20070614120051301100’ 
	CMP_USR_ID	VARCHAR2(5)	NOT NULL	EC	00000' 서비스코드 스탠다드네트웍스에서 지정 (Default 00000 ) 
	ODR_FG	CHAR(1)	NOT NULL	EC	'2'	메시지 Follow Control Mode	(Default 2) 
	SMS_GB	CHAR(1)	NOT NULL	EC	'1'	전송 위치 구분 
	USED_CD	CHAR(3)	NOT NULL	EC	'00'	사용 구분 
	MSG_GB	CHAR(1)	NOT NULL	EC	'A'	메시지 종류 구분	(A.SMS , B.URL , C.해외SMS , D.해외URL, M.LMS)
	WRT_DTTM	CHAR(14)	C	메시지 작성시간	(Ex. ‘20070614100621’) 
	SND_DTTM	CHAR(14)	NOT NULL	EC	메시지를 전송할 시간	즉시전송:현재시간, 예약발송:예약시간	(Ex. ‘20070614100621’)
	REG_SND_DTTM	CHAR(14)	A	스탠다드네트웍스가 통신사로 메시지를 보낸 시간
	REG_RCV_DTTM	CHAR(14)	A	휴대폰 가입자가 메시지를 받은 시간
	CMP_SND_DTTM	CHAR(14)	A	업체에서 스탠다드네트웍스로 전송한 시간
	CMP_RCV_DTTM	CHAR(14)	A	업체에서 결과를 통보 받은 시간
	SND_PHN_ID	VARCHAR2(15)	C	보내는 사람 휴대폰 번호(빈칸도 가능하나 권장하지 않음) 
	RCV_PHN_ID	VARCHAR2(15)	NOT NULL	EC	받는 사람 휴대폰 번호 
	CALLBACK	VARCHAR2(15)	EC	회신번호 (‘*’,’#’,’번호’ 만 가능) KT, LGT URL SMS전송일 경우 필수입력 사항임 (단, LGT는 반드시 3자리 이상 입력) 
	SND_MSG	VARCHAR2(2000)	NOT NULL	EC	메시지 내용
	EXPIRE_VAL	NUMBER (7)	C	0	유효시간 (디폴드 값 0) 
	SMS_ST	CHAR(1)	NOT NULL	EC	0’ 메시지의 내부 처리 상태 (상태값 참조) 반드시 최초 입력 시 ‘0’으로 셋팅할 것
	RSLT_VAL	NUMBER (4)	C	99 전송결과 (디폴트 값 99, 상태값 참조 )
	RSRVD_ID	VARCHER2(20)	C	특수 예비 의미용 필드
	RSRVD_WD	VARCHER2(35)	C	특수 예비 의미용 필드
	ASSIGN_CD	CHAR(1)	C	이통사 지정 정보
	RCV_MNO_CD	VARCHER2(5)	A	실제 전송 성공한 이동통신사 (ex. SKT, KT, LGT) 
	SND_GB	CHAR(1)	C	‘N’이통사별 전송 기능을 사용할 것인지 사용하지 않을 것인지의 정보 지정 (‘Y’ ‘y’ 값일경우만 )
	SND_SKT_FG	CHAR(1)	C	이통사 SKT 지정 (‘Y’ ‘y’ 값일경우만 ),‘y’일 경우 SKT 번호가 아닌 수신번호는 SMS를 전송하지 않는다.
	SND_KTF_FG	CHAR(1)	C	이통사 KT 지정 (‘Y’ ‘y’ 값일경우만 ) y’일 경우 KT 번호가 아닌 수신번호는 SMS를 전송하지 않는다.
	SND_LGT_FG	CHAR(1)	C	이통사 LGT 지정 (‘Y’ ‘y’ 값일경우만 ) y’일 경우 LGT 번호가 아닌 수신번호는 SMS를 전송하지 않는다.
	NAT_CD	CHAR(3)	C	‘82’국가코드
	MSG_ID	VARCHAR(20)	A	서버에서 생성하는 MSG 고유ID
	AUTH_SEQ	VARCAHR(5)	A	SMS메시지를 전송하는 데 사용 된 AUTH_SEQ 기록
	SUBJECT	VARCHER2(40)	C	LMS 메시지 제목	제목을 넣지 않는 경우 “제목없음” 으로 전송됨	특수문자 사용 불가, 한글 영문만 사용가능
	READ_REPLY_VAL	NUMBER (4)	A	핸드폰에서 읽음결과 (디폴트 값 99, 성공 -100, 실패 97 ), KT로 전송 시에만 사용
	READ_REPLY_DTTM	CHAR(14)	A	핸드폰에서 읽은 시간 KT로 전송 시에만 사용
	*/	
	private String cmpMsgID;
	private final String cmpUsrID = "00000";
//	private String odrFg;
//	private String smsGb;
//	private String userCD;
//	private String msgGb;
	private String wrtDttm;
	private String sndDttm;
//	private String regSndDttm;
//	private String regRcvDttm;
//	private String cmpSndDttm;
//	private String cmpRcvDttm;
	private String sndPhnID;
	private String rcvPhnID;
	private String callback;
	private String sndMsg;
//	private String expireVal;
//	private String smsSt;
//	private String rsltVal;
//	private String rsrvdID;
//	private String rsrvdWd;
//	private String assignCD;
//	private String rcvMnoCD;
//	private String sndGb;
//	private String sndSktFg;
//	private String sndKtfFg;
//	private String sndLgtFg;
//	private String natCD;
//	private String msgID;
//	private String authSeq;
//	private String subject;
//	private String readReplyVal;
//	private String readReplyDttm;
	
	public String getCmpMsgID() {
		return cmpMsgID;
	}
	public void setCmpMsgID(String cmpMsgID) {
		this.cmpMsgID = cmpMsgID;
	}
	public String getWrtDttm() {
		return wrtDttm;
	}
	public void setWrtDttm(String wrtDttm) {
		this.wrtDttm = wrtDttm;
	}
	public String getSndDttm() {
		return sndDttm;
	}
	public void setSndDttm(String sndDttm) {
		this.sndDttm = sndDttm;
	}
	public String getSndPhnID() {
		return sndPhnID;
	}
	public void setSndPhnID(String sndPhnID) {
		this.sndPhnID = sndPhnID;
	}
	public String getRcvPhnID() {
		return rcvPhnID;
	}
	public void setRcvPhnID(String rcvPhnID) {
		this.rcvPhnID = rcvPhnID;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public String getSndMsg() {
		return sndMsg;
	}
	public void setSndMsg(String sndMsg) {
		this.sndMsg = sndMsg;
	}
	public String getCmpUsrID() {
		return cmpUsrID;
	}
	
}