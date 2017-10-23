package core.common.enums;

/**
 * Created by ucjung on 2017-06-15.
 */
public enum ZoneInOutStateType {
    IN_PROCESSING(11),       // stay 시간 및 leave 시간 등에 따른 Zone In 처리중
    IN(10),
    STAY_PROCESSING(21),
    STAY(22),
    STAYING(20),
    OUT_PROCESSING(31),     // stay 시간 및 leave 시간 등에 따른 Zone Out 처리중
    OUT(30),
    DONE(99);               // 별도의 처리를 하지 않을 경우


    private Integer value;
    ZoneInOutStateType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
