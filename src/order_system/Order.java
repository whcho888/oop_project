package order_system;
import order_system.Queue;
import order_system.Csv;

import java.io.IOException;

/**
 * Created by wonhyuk on 2015. 12. 29..
 */
public class Order  {
	private String order_id;
	private String product_id;
	private Boolean isProcessed;

	private Boolean isFlushedOnce;
	private int lineNum;

	public String get_order_id(){
		return order_id;
	}
	public String get_product_id(){
		return product_id;
	}

	public Order(String __order_id__, String __product_id__) throws IOException {
		order_id = __order_id__;
		product_id = __product_id__;
		isProcessed = false;
		isFlushedOnce = false;
	}

	public void add_product(String __product_id__){
		product_id = __product_id__;
	}

	public void set_processed() throws IOException {
		if(Operator.csv.get_field(order_id, "product_id").equals("true")) {
			System.out.println(order_id + " 는 이미 배송완료 되었습니다.");
			return;
		}
		if(isFlushedOnce)
				Operator.csv.set_processed(order_id);
		else {
			isProcessed = true;
			flush_to_csv();
			System.out.println(order_id + " 주문의 " + product_id + " 상품 배송이 잘 완료 되었습니다");
		}
	}

	public int flush_to_csv() throws IOException {
		if(product_id.equals(""))
			return -1;		// not writing

		String[] toWrite = {order_id, product_id, isProcessed.toString()};
		lineNum = Operator.csv.insert(toWrite);
		isFlushedOnce = true;
		return lineNum;
	}
}
