package order_system;
/**
 * Created by wonhyuk on 2015. 12. 29..
 */

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public class Csv  {
	private int MAX = 987654321;
	private String csvPath;
	private CSVReader csvToRead;
	private CSVWriter csvToWrite;
	private HashMap<String, Integer> mem_order_ids;
	private int numLine;

	private String[] columns = {"order_id", "product_id", "isProcessed"};

	public Csv(String __csvPath__) throws IOException {
		mem_order_ids = new HashMap<String, Integer>();
		numLine = 1;
		csvPath = __csvPath__;
		if(!(new File(csvPath)).exists()) {
			write_fields();
		}
		else{
			numLine = init_mem_order_id();
		}
	}

	private int get_inc_numLine(){
		++numLine;
		return numLine-1;
	}

	private void write_fields() throws IOException {
		init_writer();
		csvToWrite.writeNext(columns);
		close_writer();
	}

	protected int get_field_idx(String field){
		for(int i =0; i < columns.length; i++)
			if(columns[i].compareToIgnoreCase(field)==0)
				return i;

		System.out.println("[Fatal Error]: Field name is not Found.");
		return MAX;   // not found
	}

	public List<String> get_all_order_ids() throws IOException {
		init_reader();
		List<String> toRet = new ArrayList<String>();
		List<String[]> all = csvToRead.readAll();
		for(String[] line : all)
			toRet.add(line[get_field_idx("order_id")]);
		close_reader();
		return toRet;
	}

	public Set<String> get_all_uniq_order_ids() throws IOException {
		init_reader();
		Set<String> toRet = new HashSet<String>();
		List<String[]> all = csvToRead.readAll();
		for(String[] line : all)
			toRet.add(line[get_field_idx("order_id")]);
		close_reader();
		return toRet;
	}

	private int init_mem_order_id() throws IOException {
		init_reader();
		List<String> all_order_ids = get_all_order_ids();
		for(String order_id : all_order_ids)
			mem_order_ids.put(order_id, get_inc_numLine());
		close_reader();
		return all_order_ids.size()+1;
	}

	protected void init_reader() throws FileNotFoundException {
		csvToRead = new CSVReader(new FileReader(csvPath), ',', '"', 1);
	}

	protected void close_reader() throws IOException {
		csvToRead.close();
	}

	protected void init_writer() throws IOException {
		csvToWrite = new CSVWriter(new FileWriter(csvPath));
	}

	protected void close_writer() throws IOException {
		csvToWrite.close();
	}

	protected void init_appender() throws IOException {
		csvToWrite = new CSVWriter(new FileWriter(csvPath, true));
	}


	protected void close_appender() throws IOException {
		csvToWrite.close();
	}

	public String[] readOrderLine(String order_id) throws IOException {
		init_reader();
		List<String[]> all = csvToRead.readAll();
		close_reader();
		if(all == null)
			return null;
		else {
			if (mem_order_ids.get(order_id) == null) {
				System.out.println("Order ID 를 다시 확인해 주세요.");
				return null;
			} else
				return all.get(mem_order_ids.get(order_id) - 1); // Check when not Found
		}
	}

	protected String get_field(String order_id, String field) throws IOException {
		return readOrderLine(order_id)[get_field_idx(field)];
	}

	public int insert(String[] fields) throws IOException {
		init_appender();
		csvToWrite.writeNext(fields);
		mem_order_ids.put(fields[get_field_idx("order_id")], get_inc_numLine());   // if Key Exists, It will be updated to new one
		close_appender();
		return mem_order_ids.get(fields[get_field_idx("order_id")]);
	}

	private String[] change_field(String[] line, String field, String value){
		line[get_field_idx(field)] = value;
		return line;
	}

	public void set_processed(String order_id) throws IOException {
		insert(change_field(readOrderLine(order_id), "isProcessed", "True"));
	}
}
