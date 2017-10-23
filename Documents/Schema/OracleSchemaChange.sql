-- 2017.05.11 지오펜스 노드 사용여부 필드 추가
-- ㄴ 측위정보 보정을 위하여 사용되던 노드를 지오펜스 단위로 관리 하기 위해 사용함
ALTER TABLE "PNTBIZ"."TB_GEOFENCING" ADD (IS_NODE_ENABLE CHAR(1) DEFAULT 'N');
COMMENT ON COLUMN "PNTBIZ"."TB_GEOFENCING"."IS_NODE_ENABLE" IS '노드활성화여부(Y/N)';

-- 2017.08.03 jhlee : FloorCode 필드 추가
-- 비콘모델에서 건물(BUILDING)에 대한 UUID 매핑데이터가 필요하여 수정
ALTER TABLE "PNTBIZ"."TB_FLOOR_CODE" ADD NODE_FIELD VARCHAR2(50);
COMMENT ON COLUMN "PNTBIZ"."TB_FLOOR_CODE"."NODE_FIELD" IS '노드 필드';
