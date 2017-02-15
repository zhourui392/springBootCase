package cn.zz.user.common.util.page;

public class PageQuery {
	
	public static int DEFAULT_PAGE_SIZE = 2;
	
	public PageQuery(){
		this.pageIndex = 1;
	}
	protected Integer pageIndex;
	protected int limit = DEFAULT_PAGE_SIZE;
	public int getPageOffset() {
		return (this.pageIndex-1)<0?0:(this.pageIndex-1)*this.limit;
	}
	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }
	@Override
	public String toString() {
		return "PageQuery [pageIndex=" + pageIndex + ", limit=" + limit + "]";
	}
    
}
