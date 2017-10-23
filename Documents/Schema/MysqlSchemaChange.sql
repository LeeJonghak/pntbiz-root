-- 작성 요청 : 최근 변경사항을 제일 위에 쓸것

-- 2017.9.26 Socket Notificiation 로그 설정
CREATE TABLE `pntbiz_beacon`.`TB_LOG_NOTIFICATION` (
  `logNum` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '로그일련번호',
  `comNum` INT UNSIGNED NOT NULL COMMENT '사이트번호',
  `beaconNum` BIGINT UNSIGNED NOT NULL COMMENT '비콘번호',
  `interfaceCommandType` CHAR(2) NULL COMMENT '이벤트타입',
  `beaconInfo` TEXT NULL COMMENT '비콘정보 JSON',
  `eventZoneInOutState` TEXT NULL COMMENT '이벤트 발생 ZONE 정보',
  `regDate` INT NULL COMMENT '등록일',
  PRIMARY KEY (`logNum`)  COMMENT '',
  INDEX `IX_TB_LOGNOTIFICATION_1` (`comNum` ASC, `beaconNum` ASC, `interfaceCommandType` ASC, `regDate` ASC)  COMMENT '',
  INDEX `IX_TB_LOGNOTIFICATION_2` (`comNum` ASC, `interfaceCommandType` ASC, `regDate` ASC)  COMMENT '')
COMMENT = 'Socket.io Event Notification 로그 정보';

-- 20179.24 Geofence Group Index 변경
-- 동일 Geofence Group 명을 다른 사이트(회사)에서 등록 시 오류가 발생하여 해결함
ALTER TABLE `pntbiz_beacon`.`TB_GEOFENCING_GROUP`
DROP INDEX `IX_fcGroupName` ,
ADD UNIQUE INDEX `IX_fcGroupName` (`fcGroupName` ASC, `comNum` ASC)  COMMENT '';

-- 2017.9.22 Geofence 로그에 비인가 출입 여부 필드 추가
ALTER TABLE `pntbiz_beacon`.`TB_LOG_PRESENCE_GEOFENCE`
ADD COLUMN `permitted` CHAR(1) NULL DEFAULT 0 COMMENT '비인가 출입 여' AFTER `floor`;

-- 2017.9.22 Floor 출입 로그 테이블 추가
CREATE TABLE `TB_LOG_PRESENCE_FLOOR` (
  `logNum` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '로그일련번호',
  `SUUID` char(36) NOT NULL COMMENT '사이트UUID',
  `UUID` char(36) NOT NULL COMMENT 'UUID',
  `majorVer` mediumint(5) NOT NULL COMMENT 'MajorVersion',
  `minorVer` mediumint(5) NOT NULL COMMENT 'minorVersion',
  `floor` varchar(5) DEFAULT NULL COMMENT '층',
  `permitted` char(1) DEFAULT '0' COMMENT '비인가 출입 여부',
  `beaconName` varchar(100) DEFAULT NULL COMMENT '비콘명',
  `targetName` varchar(100) DEFAULT NULL COMMENT '타켓명',
  `logDesc` varchar(100) DEFAULT NULL COMMENT '비고',
  `inDate` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '층 입장시간',
  `outDate` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '충 퇴장시간',
  `regDate` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '등록일',
  PRIMARY KEY (`logNum`),
  KEY `IX_logPresenceFloorRegdate` (`regDate`),
  KEY `IX_logPresenceFloorBeaconName` (`SUUID`,`regDate`),
  KEY `IX_logPresenceFloorTargetName` (`SUUID`,`regDate`)
) ENGINE=InnoDB AUTO_INCREMENT=280352 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='로그-프레즌스층';

-- 2017.09.15 외부 연동 호출 결과 로그 Table 추가
CREATE TABLE `pntbiz_beacon`.`TB_LOG_INTERFACE` (
  `logNum` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '',
  `interfaceNum` BIGINT(20) UNSIGNED NOT NULL COMMENT '',
  `comNum` INT(10) UNSIGNED NOT NULL COMMENT '',
  `target` VARCHAR(100) NULL COMMENT '',
  `requestMessage` TEXT NULL COMMENT '',
  `responseCode` VARCHAR(10) NULL COMMENT '',
  `responseMessage` TEXT NULL COMMENT '',
  `regDate` INT(10) UNSIGNED NOT NULL COMMENT '',
  PRIMARY KEY (`logNum`)  COMMENT '')
COMMENT = '외부연동 호출 로그';


-- 2017.08.30 외부 연동 설정 Table 추가
CREATE TABLE `pntbiz_beacon`.`TB_CONFIG_INTERFACE` (
  `interfaceNum` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '인터페이스 고유키(PK)',
  `comNum` INT(10) UNSIGNED NOT NULL COMMENT '회사 고유',
  `interfaceBindingType` CHAR(2) NULL DEFAULT 'FC' COMMENT '인터페이스 바인딩 대상 구분 ',
  `InterfaceCommandType` CHAR(2) NULL COMMENT '인터페이스 유형 ',
  `bindingZoneId` VARCHAR(20) NULL COMMENT '인터페이스 바인딩 대상 고유',
  `targetInfo` TEXT NULL COMMENT '인터페이스 대상 정보 - Protocol URL Port 등',
  `headers` TEXT NULL COMMENT '인터페이스 메시지 헤더 정',
  `bodyMetaData` TEXT NULL COMMENT '인터페이스 메시지 바디 구성을 위한 메타정보',
  `modDate` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '수정일시',
  `regDate` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '생성일시',
  PRIMARY KEY (`interfaceNum`))
ENGINE=InnoDB DEFAULT CHARSET=utf8
COMMENT = '연동 Interface 설정';

-- 2017.08.24
ALTER TABLE `TB_BEACON` DROP `restrictedZoneType`;
ALTER TABLE `TB_BEACON` ADD `restrictedZonePermitted` CHAR(1)  NULL  DEFAULT NULL  COMMENT '0: 비인가구역, 1:인가구역'  AFTER `externalAttribute`;
DROP TABLE IF EXISTS TB_BEACON_RESTRICTED_ZONE;
DROP TABLE IF EXISTS TB_BEACON_EXTERNAL_LOG;
DROP TABLE IF EXISTS TB_BEACON_RESTRICTED_ZONE_LOG;
DROP TABLE IF EXISTS TB_BEACON_RESTRICTED_ZONE;
DROP TABLE IF EXISTS TB_BEACON_EXTERNAL_LOG;
DROP TABLE IF EXISTS TB_BEACON_RESTRICTED_ZONE_LOG;
CREATE TABLE `TB_BEACON_RESTRICTED_ZONE` (
	`beaconNum` bigint(11) unsigned NOT NULL COMMENT '비콘 번호',
	`zoneType` char(1) NOT NULL DEFAULT 'F' COMMENT 'F:층코드(floor),G:지오펜스식별번호(fcNum)',
	`zoneId` varchar(20) NOT NULL DEFAULT '' COMMENT '층코드',
	`permitted` char(1) NOT NULL DEFAULT '0' COMMENT '0: 비인가구역, 1:인가구역',
	`additionalAttribute` text COMMENT '추가속성',
	`startDate` int(10) DEFAULT NULL COMMENT '시작시간',
	`endDate` int(10) DEFAULT NULL COMMENT '종료시간',
	`modDate` int(10) unsigned DEFAULT NULL COMMENT '수정시간',
	`regDate` int(10) unsigned NOT NULL COMMENT '등록시간',
	PRIMARY KEY (`beaconNum`,`zoneType`,`zoneId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `TB_BEACON_EXTERNAL_LOG` (
	`logNum` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '자동증가값',
	`type` char(1) NOT NULL DEFAULT 'A' COMMENT 'A:할당,U:할당해제',
	`beaconNum` bigint(11) unsigned NOT NULL COMMENT '비콘 번호',
	`externalId` varchar(100) DEFAULT NULL COMMENT '외부연동ID',
  `barcode` VARCHAR(200)  NULL  DEFAULT NULL  COMMENT '바코드',
  `externalAttribute` text COMMENT '외부데이터',
	`regDate` int(10) unsigned NOT NULL COMMENT '등록시간',
	PRIMARY KEY (`logNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `TB_BEACON_RESTRICTED_ZONE_LOG` (
	`logNum` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '자동증가값',
	`type` varchar(10) NOT NULL DEFAULT '' COMMENT 'A:할당,U:할당해제',
	`refLogNum` bigint(11) unsigned DEFAULT NULL COMMENT '외부정보 로그 고유번호',
	`beaconNum` bigint(11) unsigned NOT NULL COMMENT '비콘 번호',
	`zoneType` char(1) NOT NULL DEFAULT '' COMMENT 'F:층코드(floor),G:지오펜스식별번호(fcNum)',
	`zoneId` varchar(20) NOT NULL DEFAULT '' COMMENT '층코드,지오펜스식별번호',
	`permitted` char(1) NOT NULL DEFAULT '0' COMMENT '0: 비인가구역, 1:인가구역',
	`additionalAttribute` text COMMENT '추가속성',
	`startDate` int(10) DEFAULT NULL COMMENT '시작시간',
	`endDate` int(10) DEFAULT NULL COMMENT '종료시간',
	`regDate` int(10) unsigned NOT NULL COMMENT '등록시간',
	PRIMARY KEY (`logNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 2017.08.23 출입통제, 바코드 관련 필드 추가
ALTER TABLE `TB_BEACON` ADD `barcode` VARCHAR(200)  NULL  DEFAULT NULL  COMMENT '바코드'  AFTER `field5`;
ALTER TABLE `TB_BEACON` ADD `restrictedZoneType` VARCHAR(10)  NULL  DEFAULT NULL  COMMENT '종류(인가:permitted, 줄입금지:permitted)'  AFTER `externalAttribute`;

-- 2017.08.17 층코드 테이블에 노드필드 컬럼 추가
ALTER TABLE `TB_FLOOR_CODE` ADD `nodeField` VARCHAR(50) NULL DEFAULT NULL COMMENT '노드필드' AFTER `typeCode`;

-- 2017.08.14 비콘 외부정보 관련 테이블 수정
DROP TABLE IF EXISTS `TB_BEACON_EXTERNAL`;
DROP TABLE IF EXISTS `TB_BEACON_EXTERNAL_RESTRICTED_ZONE`;
ALTER TABLE `TB_BEACON` ADD `externalId` VARCHAR(100)  NULL  COMMENT '외부연동키'  AFTER `field5`;
ALTER TABLE `TB_BEACON` ADD `externalAttribute` TEXT  NULL  COMMENT '외부데이터'  AFTER `externalId`;

CREATE TABLE `TB_BEACON_EXTERNAL_LOG` (
	`logNum` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '자동증가값',
	`type` varchar(10) DEFAULT NULL COMMENT 'assign:할당,unassign:할당해제',
	`beaconNum` bigint(11) unsigned NOT NULL COMMENT '비콘 번호',
	`externalId` varchar(100) DEFAULT NULL COMMENT '외부연동ID',
	`externalAttribute` text COMMENT '외부데이터',
	`regDate` int(10) unsigned NOT NULL COMMENT '등록시간',
	PRIMARY KEY (`logNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `TB_BEACON_RESTRICTED_ZONE` (
	`zoneNum` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '자동증가값',
	`beaconNum` bigint(11) unsigned NOT NULL COMMENT '비콘 번호',
	`zoneType` varchar(10) NOT NULL DEFAULT 'permitted' COMMENT '종류(인가:permitted, 줄입금지:permitted)',
	`additionalAttribute` text COMMENT '추가속성',
	`floor` varchar(20) DEFAULT NULL COMMENT '층코드',
	`fence` bigint(11) DEFAULT NULL COMMENT '펜스번호',
	`regDate` int(10) unsigned NOT NULL COMMENT '등록시간',
	`modDate` int(10) unsigned DEFAULT NULL COMMENT '수정시간',
	PRIMARY KEY (`zoneNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `TB_BEACON_RESTRICTED_ZONE_LOG` (
	`logNum` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '자동증가값',
	`type` varchar(10) NOT NULL DEFAULT '' COMMENT 'assign:할당,unassign:할당해제',
	`refLogNum` bigint(11) unsigned DEFAULT NULL COMMENT '외부정보 로그 고유번호',
	`beaconNum` bigint(11) unsigned NOT NULL COMMENT '비콘 번호',
	`zoneType` varchar(10) DEFAULT NULL COMMENT '종류(인가:permitted 줄입금지:permitted)',
	`additionalAttribute` text COMMENT '추가속성',
	`floor` varchar(20) DEFAULT NULL COMMENT '층코드',
	`fence` bigint(11) DEFAULT NULL COMMENT '펜스번호',
	`regDate` int(10) unsigned NOT NULL COMMENT '등록시간',
	PRIMARY KEY (`logNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 2017.08.09 노드테이블에 구역정보 컬럼 추가, 지오펜스의 노드사용여부 옵션
ALTER TABLE `TB_NODE` ADD `areaNum` BIGINT(11)  NULL  DEFAULT NULL  COMMENT '구역 고유번호'  AFTER `type`;
ALTER TABLE `TB_NODE` ADD `areaName` VARCHAR(50)  NULL  DEFAULT NULL  COMMENT '구역명'  AFTER `areaNum`;

-- 2017.07.24 비콘 외부정보 설정
CREATE TABLE `TB_BEACON_EXTERNAL` (
  `externalNum` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '자동증가값',
  `beaconNum` bigint(11) unsigned NOT NULL COMMENT '비콘번호',
  `externalId` varchar(100) NOT NULL COMMENT '외부연동키',
  `jsonAttribute` text COMMENT '외부정보 ',
  `assignDate` int(10) unsigned NOT NULL COMMENT '등록시간',
  `unassignDate` int(10) unsigned DEFAULT NULL COMMENT '제거시간(null이면 할당상태, 아니면 제거상태)',
  `modDate` int(10) unsigned DEFAULT NULL COMMENT '수정시간',
  PRIMARY KEY (`externalNum`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- 2017.07.24 비콘 출입통제구역 설정
CREATE TABLE `TB_BEACON_EXTERNAL_RESTRICTED_ZONE` (
  `efNum` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '자동증가값',
  `externalNum` bigint(11) unsigned NOT NULL COMMENT '외부연동 번호',
  `type` varchar(10) NOT NULL DEFAULT 'permitted' COMMENT '종류(인가:permitted, 줄입금지:permitted)',
  `additionalAttribute` text COMMENT '추가속성',
  `floor` varchar(20) DEFAULT NULL COMMENT '층코드',
  `fence` bigint(11) DEFAULT NULL COMMENT '펜스번호',
  `regDate` int(10) unsigned NOT NULL COMMENT '등록시간',
  `modDate` int(10) unsigned DEFAULT NULL COMMENT '수정시간',
  PRIMARY KEY (`efNum`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- 2017.05.11 지오펜스 노드 사용여부 필드 추가
-- ㄴ 측위정보 보정을 위하여 사용되던 노드를 지오펜스 단위로 관리 하기 위해 사용함
ALTER TABLE `pntbiz_beacon`.`TB_GEOFENCING`
ADD COLUMN `isNodeEnable` CHAR(1) NULL DEFAULT 'N' COMMENT 'Node 사용여부 (Y : N)' AFTER `field5`;
