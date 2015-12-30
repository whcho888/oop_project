package order_system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

/**
 * Created by wonhyuk on 2015. 12. 29..
 */
public class Operator {
	private static Queue q;
	public static Csv csv;
	public Operator() throws IOException {
		q = new Queue();
		csv = new Csv("/Users/wonhyuk/oop_project/mydb.csv");
	}

	public static void main(String [] args) throws IOException {
		Operator program = new Operator();
		program.operator();
	}

	private String wait_cmd(){
		try{
			BufferedReader br =
				new BufferedReader(new InputStreamReader(System.in));

			String input;

			while((input=br.readLine())!=null){
				return input;
			}

		}catch(IOException io){
			io.printStackTrace();
		}
		return "";
	}
	public void init_queue() throws IOException {
		Set<String> orders = csv.get_all_uniq_order_ids();
		for(String order : orders)
			if(csv.get_field(order, "isProcessed").equalsIgnoreCase("false"))
				q.push_back(new Order(order, csv.get_field(order, "product_id")));
	}

	public void operator() throws IOException {
		init_queue();
		while(true){
			String[] cmd = wait_cmd().split(" ");
			switch(cmd[0]){
				case "order":
					if(cmd.length < 3)
						System.out.println("[Fatal Error]: order cmd need two parameter. \n" + usage);
					Order order = new Order(cmd[1], cmd[2]);
					q.push_back(order);
					break;
				case "process":
					Order processed = q.pop_front();
					if(processed!=null) {
						processed.flush_to_csv();
						System.out.println(processed.get_order_id() + " 주문이 " + " 잘 처리 되어 배송 대기중 입니다.");
					}
					else
						System.out.println("Queue is Empty!");
					break;
				case "find":
					if(cmd.length < 2)
						System.out.println("[Fatal Error]: find cmd need two parameter. \n" + usage);
					String[] info = csv.readOrderLine(cmd[1]);
					System.out.println("order_id: " + info[0] + ", product_id: " + info[1] + ", isProcessed: " + info[2]);
					break;
				case "list":
					System.out.println(csv.get_all_uniq_order_ids());
					break;
				case "logi":
					Order toLogi = new Order(cmd[1], csv.get_field(cmd[1], "product_id"));
					if(toLogi == null)
						break;
					else
						toLogi.set_processed();
					break;
				case "help":
					System.out.println(usage);
					break;
				default:
					System.out.println("[Fatal Error]: No matching command.. please type help to see usage");
			}

		}
	}

	private String usage = "1. order [order_id] [product_id] : 주문을 Q에 넣기\n2. process : deQ 처리됨 (이 상태에서 파일로 저장됨)\n"
		+ "3. 파일은 csv 형태(마지막 필드는 process 처리 여부 T/F)\n4. find [order ID] : ID 주문에 대한 상세정보 보여줌\n"
		+ "5. list : 주문 ID 100개 정도 나열\n6. logi [order ID] : process 된 주문을 배송처리";
}
