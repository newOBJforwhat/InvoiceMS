package Model;
/**
 * 记录发票的操作结果
 * @author ctk
 *
 */

import Common.Annotation.ATable;
import Common.Annotation.PrimaryKey;

@ATable(name = "InvoiceItem")
public class InvoiceItem {
	@PrimaryKey
	private long id;
	private String solve;//json字符串，记录处理人;格式[id....]
	private String advice;//json字符串，表示处理意见;格式{"id":"意见"}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSolve() {
		return solve;
	}
	public void setSolve(String solve) {
		this.solve = solve;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
}
