package framework.web.util;

public class PagingParam {

	private int			page = 1;
	private int			pageSize = 10; // 한페이지에 보여줄 row수
	private int			blockSize = 10; // 한페이지 보여줄 page의 갯수
	@SuppressWarnings("unused")
	private int			firstItemNo;
	@SuppressWarnings("unused")
	private int			lastItemNo;

	public void initPage(int pageSize, int blockSize)
	{
		this.setPageSize(pageSize);
		this.setBlockSize(blockSize);
	}
	public int getPage() {
		if(page < 1) {
			return 1;
		} else {
			return page;
		}
	}
	public void setPage(int page) {
		this.page = (page < 1) ? 1 : page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = (pageSize < 1) ? this.pageSize : pageSize;
	}
	public void setPageSizeZero() {
		this.pageSize = 0;
	}
	public int getBlockSize() {
		return blockSize;
	}
	public void setBlockSize(int blockSize) {
		this.blockSize = (blockSize < 1) ? this.blockSize : blockSize;
	}
	public int getFirstItemNo() {
		// oracle
		//return (this.page - 1) * this.pageSize + 1;
		// mysql
		return (this.page - 1) * this.pageSize;
	}
	public void setFirstItemNo(int firstItemNo) {
		this.firstItemNo = firstItemNo;
	}
	public int getLastItemNo() {
		// oracle
		//return this.page * this.pageSize;
		// mysql
		return this.pageSize;
	}
	public void setLastItemNo(int lastItemNo) {
		this.lastItemNo = lastItemNo;
	}
}
