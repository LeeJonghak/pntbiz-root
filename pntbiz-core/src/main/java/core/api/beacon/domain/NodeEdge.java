package core.api.beacon.domain;

/**
 * TB_NODE_EDGE
 *
 * @author nohsoo 2015-03-10
 */
public class NodeEdge {

    /**
     * Node Edge 일련번호
     */
    private Long edgeNum;

    /**
     * 업체번호
     */
    private Integer comNum;

    /**
     * 층번호
     */
    private String floor;

    /**
     * 연결 시작점 NodeID
     */
//    private Integer startPoint;

    /**
     * 연결 끝점 NodeID
     */
//    private Integer endPoint;
    
    private String edge;

    private String type;

    public Long getEdgeNum() {
        return edgeNum;
    }

    public void setEdgeNum(Long edgeNum) {
        this.edgeNum = edgeNum;
    }

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

//    public Integer getStartPoint() {
//        return startPoint;
//    }
//
//    public void setStartPoint(Integer startPoint) {
//        this.startPoint = startPoint;
//    }
//
//    public Integer getEndPoint() {
//        return endPoint;
//    }
//
//    public void setEndPoint(Integer endPoint) {
//        this.endPoint = endPoint;
//    }

	public String getEdge() {
		return edge;
	}

	public void setEdge(String edge) {
		this.edge = edge;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
