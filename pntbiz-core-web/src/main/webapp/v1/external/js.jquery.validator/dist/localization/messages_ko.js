(function( factory ) {
	if ( typeof define === "function" && define.amd ) {
		define( ["jquery", "../jquery.validate"], factory );
	} else {
		factory( jQuery );
	}
}(function( $ ) {

/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: KO (Korean; 한국어)
 */
$.extend($.validator.messages, {
	required: "필수 항목입니다. ",
	remote: "항목을 수정하세요. ",
	email: "유효하지 않은 E-Mail주소입니다. ",
	url: "유효하지 않은 URL입니다. ",
	date: "올바른 날짜를 입력하세요. ",
	dateISO: "올바른 날짜(ISO)를 입력하세요. ",
	number: "유효한 숫자가 아닙니다. ",
	digits: "숫자만 입력 가능합니다. ",
	creditcard: "신용카드 번호가 바르지 않습니다. ",
	equalTo: "같은 값을 다시 입력하세요. ",
	extension: "올바른 확장자가 아닙니다. ",
	maxlength: $.validator.format("{0}자를 넘을 수 없습니다. "),
	minlength: $.validator.format("{0}자 이상 입력하세요. "),
	rangelength: $.validator.format("문자 길이가 {0} 에서 {1} 사이의 값을 입력하세요. "),
	range: $.validator.format("{0} 에서 {1} 사이의 값을 입력하세요. "),
	max: $.validator.format("{0} 이하의 값을 입력하세요. "),
	min: $.validator.format("{0} 이상의 값을 입력하세요. "),
	maxselect: $.validator.format("{0}개 이하로 선택하세요. "),
	minselect: $.validator.format("{0}개 이상 선택하세요. "),
	// jhlee 2015-01-19 addtional-methods 
	alphanumeric: '영문/숫자로만 입력하세요. ',
	alpha: '영문으로만 입력하세요. ',
	hangle: '한글로 입력하세요. ',
	uuid: '다음 입력 형식을 따라 주세요(허용문자:0~9, a~f). xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxx ',
	filesize: '파일허용 용량을 넘었습니다. ',
//	pwd: '영문,숫자,특수문자(!,@,#,$,%,^,&,*,?,_,~) 사용하여 주세요. 단, 영문 숫자는 반드시 포함되어야 합니다. ',
	pwd: '영문, 숫자는 반드시 포함되어야 합니다. ',
	// nsyun 2015-11-26
	userid: '5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다. ',
	userpw: '6~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요. ',
	// jhlee 2015-02-26 공통메세지 추가
	error: "에러가 발생하였습니다. ",
	dupSuccess: "사용할 수 있는 정보입니다. ",
	dupFail: "이미 등록된 정보입니다. ",
	regSuccess: "등록 되었습니다. ",
	regFail: "등록이 실패하였습니다. ",
	regError: "등록 오류입니다. ",
	regConfirm: "등록하시겠습니까? ", 
	regLimit: "더 이상 등록 할 수 없습니다. ",
	// jhlee 2016-08-12 콘텐츠 타입 항목
    regComboboxAddError: "항목을 추가하세요.",
    regArrayInputError: "모든 항목을 입력하세요. ",
	modSuccess: "수정 되었습니다. ",
	modFail: "수정이 실패하였습니다. ",
	modError: "수정 오류입니다. ",
	modConfirm: "수정하시겠습니까? ", 
	modLimit: "더 이상 수정 할 수 없습니다. ",
	delSuccess: "삭제 되었습니다. ",
	delFail: "삭제가 실패하였습니다. ",
	delError: "삭제 오류입니다. ",
	delConfirm: "삭제하시겠습니까?  ", 
	delLimit: "더 이상 삭제 할 수 없습니다. ",
	deploySuccess: "배포 되었습니다. ",
	deployFail: "배포가 실패하였습니다. ",
	deployError: "배포 오류입니다. ",
	deployConfirm: "배포하시겠습니까?  ", 
	deployLimit: "더 이상 배포 할 수 없습니다. ",
	infoFail: "정보가 없습니다. ",
	infoError: "정보를 불러오는데 실패하였습니다. ",
	addConfirm: "추가 하시겠습니까?", 
	addSuccess: "추가 되었습니다. ",
	addFail: "추가가 실패하였습니다. ",
	addError: "추가 오류입니다. ", 
	addLimit: "더 이상 추가 할 수 없습니다. ",
	saveConfirm: "저장 하시겠습니까? ", 
	searchFail: "찾을 수 없습니다.",
	start: "시작 하였습니다.",
	end: "종료 되었습니다.",
	syncSuccess: "동기화 되었습니다. ",
	syncFail: "동기화가 실패하였습니다. ",
	syncError: "동기화 오류입니다. ",
	syncConfirm: "동기화 하겠습니까? ",
	// jhlee 2015-01-19 보안성심사 추가
	noChangePasswd: "기존 비밀번호와 동일 합니다. 다른 비밀번호를 입력하세요.",
    itsmeSuccess: "본인 확인 되었습니다.",
    itsmeFail: "본인 확인이 실패하였습니다.",
    itsmeError: "본인 확인 처리중 오류가 발생하였습니다.",
    // custom message
    redrawConfirm: "다시 그리시겠습니까? ",
    fenceDrawError: "펜스를 그려주세요. ",
    fenceCompleteError: "펜스를 완성해 주세요. ",
    nodeNextError: "연결할 다음 노드를 선택하세요. ",
    nodeRemoveError: "연결을 제거할 다음 노드를 선택하세요. ",
    nodeSameError: "같은 노드는 연결하실 수 없습니다. ",
    keyboardMoveError: "키보드 이동(A,S,D,W) ",
    sdateChooseError: "시작일을 선택하세요. ",
    edateChooseError: "종료일을 선택하세요. ",
    dateChooseError: "시작일,종료일이 옳바르지 않습니다.",
    phoneNumberChooseError: "휴대폰번호를 선택하세요. ",
    targetBeaconChooseError: "대상자(비콘)을 선택하세요. ",
    excelDataCreate: "엑셀데이터가 생성되었습니다. ",
    // jhlee 2016-03-11 그외 추가
    loginFail: "아이디 또는 비밀번호가 잘못되었습니다. 다시 시도 하세요.",
    // jhlee 2016-03-11 OTP
    otpPasswordChange: "비밀번호가 변경되었습니다. 변경된 비밀번호로 로그인 하세요.",
    otpAccountLocked: "비밀번호 5회 오류로 계정이 잠겼습니다. OTP 인증 후 사용 가능 합니다. ",
    otpAccountExpired: "계정이 만료되었습니다. OTP 인증 후 사용 가능 합니다. ",
    otpAuthCodeExpired: "인증코드 유효시간이 만료되었습니다. ",
    otpAuthCodeSend: "OTP 인증코드가 발급되었습니다. 인증코드 입력 후 OTP 인증을 하세요. ",
    otpAuthCodeSendFail: "OTP 인증코드 발급이 실패하였습니다. ",
    otpAuth: "인증되었습니다. 비밀번호를 변경하세요. ",
    otpAuthFail: "인증이 실패하였습니다. ",    
    otpAuthCodeExpiredTime: "OTP 인증코드 만료시간 ",
    otpAuthCodeMin: "분",
    otpAuthCodeSec: "초",
    // yjhwang 2016-08-10 콘텐츠리스트-매핑
    mappingContentsError: "콘텐츠를 선택해주세요.",
    mappingTypeError: "매핑 타입을 선택해주세요.",
    mappingFcEventTypeError: "서브 매핑 타입을 선택해주세요.",
    mappingReferenceNoError: "매핑 대상을 선택해주세요.",
    mappingSuccess: "매핑 완료되었습니다. ",
    mappingFail: "매핑에 실패하였습니다. ",
    mappingError: "매핑 오류입니다. ",
	// jhlee 2016-08-12 콘텐츠 타입 항목
    addItemError: "항목을 추가하세요.",
	zoneTypeChange: $.validator.format("아래의 모든 구역을 {0} 구역으로 변경하시겠습니까?"),
	permitted: "인가",
	restricted : "비인가",
	changeFail: "변경에 실패하였습니다.",
	duplicateAttributeKey: $.validator.format("{0}번째 속성의 key값은 {1}번째 속성과 중복됩니다."),
	inputAttribute: $.validator.format("{0}번째 속성의 {1}를 입력하여주세요."),
	inputAttributes: $.validator.format("{0}번째 속성의 {1}와 {2}를 입력하여주세요."),
	startNendDateValidation: "종료일은 시작일보다 커야 합니다.",
	locationChange : "위치 변경",
	floorIn : "층 진입",
	floorStay : "층 머무름",
	floorOut : "층 퇴장",
	geofenceIn : "지오펜스 진입",
	geofenceStay : "지오펜스 머무름",
	geofenceOut : "지오펜스 퇴장",
	restrictionIn : "비인가구역 진입",
	restrictionStay : "비인가구역 머무름",
	restrictionOut : "비인가구역 퇴장",
});

}));