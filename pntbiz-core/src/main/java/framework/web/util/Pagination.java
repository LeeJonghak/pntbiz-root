package framework.web.util;

public class Pagination {

	private int totalRows = 0;
	private int currentPage = 1;
	private int pageSize = 10;
	private int blockSize = 10;	
	private int totalPages;
	private int totalBlocks;
	private int startPageNum;
	private int endPageNum;
	private int currentBlock;

	private String amp = "";
	
	// for design
//	public String firstLink = "<img src='/images/contents/page_01_on.gif' alt='이전페이지' />";
//	public String firstOffLink = "<img src='/images/contents/page_01.gif' alt='이전페이지' />";
//	public String prevLink = "<img src='/images/contents/page_02_on.gif' alt='이전' />";	
//	public String prevOffLink = "<img src='/images/contents/page_02.gif' alt='이전' />";	
//	
//	public String nextLink = "<img src='/images/contents/page_03_on.gif' alt='다음' />";	
//	public String nextOffLink = "<img src='/images/contents/page_03.gif' alt='다음' />";	
//	public String lastLink = "<img src='/images/contents/page_04_on.gif' alt='다음페이지' />";	
//	public String lastOffLink = "<img src='/images/contents/page_04.gif' alt='다음페이지' />";	
	
	// for text	
	public String firstLink = "&lt;&lt;";
	public String firstOffLink = "<li class=\"disabled\"><a href=\"#\">&lt;&lt;</a></li>";
	public String prevLink = "&lt;";	
	public String prevOffLink = "<li class=\"disabled\"><a href=\"#\">&lt;</a></li>";
	
	public String nextLink = "&gt;";
	public String nextOffLink = "<li class=\"disabled\"><a href=\"#\">&gt;</a></li>";
	public String lastLink = "&gt;&gt;";
	public String lastOffLink = "<li class=\"disabled\"><a href=\"#\">&gt;&gt;</a></li>";
	
	//public String delimiter = "|";
	public String delimiter = "";
	
	// current Page Wrapper
	public String preWrap = "<li class=\"active\">";
	public String postWrap = "</li>";
	
	public String linkPage = "";
	public String queryString = "";
	
	// result temp object
	public StringBuffer pageString = new StringBuffer();
	
	public Pagination(int currentPage, int totalRows)
	{
		this.currentPage = currentPage;
		this.totalRows = totalRows;		
		initialize();
	}
	
	public Pagination(int currentPage, int totalRows, int pageSize , int blockSize)
	{
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.blockSize = blockSize;
		this.totalRows = totalRows;	
		initialize();
	} 
	
	public void initialize()
	{	
		this.totalPages = (int)Math.ceil((double)this.totalRows / this.pageSize);
		this.totalBlocks = (int)Math.ceil((double)this.totalPages / this.blockSize);
		this.currentBlock = (int)Math.ceil((double)((this.currentPage - 1) / this.blockSize)) + 1;
		// 기존
		//this.startPageNum = ((this.currentBlock - 1) * this.pageSize) + 1;
		//this.endPageNum = this.startPageNum + this.pageSize;
		// mysql
		this.startPageNum = ((this.currentBlock - 1) * this.blockSize) + 1;
		this.endPageNum = this.startPageNum + this.blockSize;	
	}
	
	public void prePrint()
	{
		// set first block link
		if(this.currentBlock > 1)
			pageString.append("<li><a href=\"" + this.linkPage + "?" + this.queryString + this.amp + "page=" + (((this.currentBlock - 2) * this.blockSize) + 1) + "\">" + this.firstLink + "</a></li>");
		else
			pageString.append(this.firstOffLink + " ");			
		
		// set prev page link
		if(this.currentPage > 1)
			pageString.append("<li><a href=\"" + this.linkPage + "?" + this.queryString + this.amp + "page=" + (this.currentPage  - 1) + "\">" + this.prevLink + "</a></li>");
		else
			pageString.append(this.prevOffLink + " ");		
	}
	
	public void postPrint()
	{
		// set next page link
		if(this.currentPage < this.totalPages )
			pageString.append("<li><a href=\"" + this.linkPage + "?" + this.queryString + this.amp + "page=" + (this.currentPage + 1) + "\">" + this.nextLink + "</a></li>");
		else
			pageString.append(this.nextOffLink + " ");
		
		// set last page link
		if(this.currentBlock < this.totalBlocks)
			pageString.append("<li><a href=\"" + this.linkPage + "?" + this.queryString + this.amp + "page=" + ((this.currentBlock * this.blockSize) + 1) + "\">" + this.lastLink + "</a></li>");
		else
			pageString.append(this.lastOffLink);
	}
	
	public void printList()
	{	
		for(int i = startPageNum ; i <= endPageNum ; i++)
		{
			if(i > this.totalPages || i == endPageNum)
				break;
			else if(i > startPageNum)
				pageString.append(this.delimiter);
			
			if(i == this.currentPage)			
				pageString.append(this.preWrap + "<a href=\"#\">" + i + "</a>" + this.postWrap);
			else
				pageString.append("<li><a href=\"" + this.linkPage + "?" + this.queryString + this.amp + "page=" + i + "\">" + i + "</a></li>");
		}
	}
	
	public String print()
	{
		if(!this.queryString.equals(""))
			this.amp = "&";		
		//if(this.totalPages > 1)
		//{
		//	this.prePrint();
		//	this.printList();
		//	this.postPrint();
		//}		
		this.prePrint();
		this.printList();
		this.postPrint();
		
		return(pageString.toString());
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
}
