package order_system;

/**
 * Created by wonhyuk on 2015. 12. 29..
 */


public class Queue {
	private Order[] container;
	private int head, tail;
	private int size = 20;  // default size
	private int cnt_cng;
	public Queue(){
		container = new Order[size];
		head = 0;
		tail = 0;
		cnt_cng = 0;
	}

	private int dec_size(){
		int toDec = size/4;
		Order[] newContainer = new Order[size - toDec];
		System.arraycopy( container, 0, newContainer, 0, size - size/3 );
		size -= toDec;
		container = newContainer;
		System.out.println("size after decrease : " + container.length);
		return size;
	}

	private int inc_size(){
		int toInc = size/4;
		Order[] newContainer = new Order[size + toInc];
		System.arraycopy( container, 0, newContainer, 0, size - size/3 );
		size += toInc;
		container = newContainer;
		System.out.println("size after decrease : " + container.length);
		return size;
	}

	private int clean_half(){
		Order[] newContainer = new Order[size/2];
		System.arraycopy( container, size/2, newContainer, 0, size/2 );
		size /= 2;
		tail -= size;
		container = newContainer;
		System.out.println("size after decrease : " + container.length);
		return size;
	}

	public int push_back(Order order){
		tail += 1;
		if(tail > size - size/3 && cnt_cng >15)
			inc_size();
		container[tail-1] = order;
		System.out.println(order.get_order_id() + " 주문의 " + order.get_product_id() + " 상품이 잘 등록되었습니다");
		return size;
	}

	public Order pop_front(){
		Order toRet = container[head];
		if(toRet == null)
			return null;
		head += 1;
		if(head > size/2 && cnt_cng > 15)
			clean_half();
		if(tail <= size - size/3 && cnt_cng > 15)
			dec_size();
		return toRet;
	}
}
