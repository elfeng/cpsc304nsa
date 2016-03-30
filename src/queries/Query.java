package queries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import cpsc304nsa.tables.*;
import cpsc304nsa.main.AttrType;
import cpsc304nsa.main.Pair;;

public class Query {
	private BufferedReader br;
	protected Connection con;
	public Query(Connection con) {
		this.con = con;
		br = new BufferedReader(new InputStreamReader(System.in));
	}

	protected Table selectTable() {
		Table table = null;
		System.out.println("select a table:\n"
				+ "\t1. Call\n"
				+ "\t2. CommLog\n"
				+ "\t3. Data\n"
				+ "\t4. Device\n"
				+ "\t5. Location\n"
				+ "\t6. Photo\n"
				+ "\t7. Text\n"
				+ "\t8. Transaction\n"
				+ "\t9. User");
		try {
			int input = Integer.parseInt(br.readLine());
			switch (input) {
			case 1: table = new Call(); break;
			case 2: table = new CommLog(); break;
			case 3: table = new Data(); break;
			case 4: table = new Device(); break;
			case 5: table = new Location(); break;
			case 6: table = new Photo(); break;
			case 7: table = new Text(); break;
			case 8: table = new Transaction(); break;
			case 9: table = new User(); break;
			default: 
				System.out.println("invalid input");
				table = selectTable();
				break;
			}
			
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
		return table;
	}

	protected Pair<AttrType, String> selectAttr(Table t) {
		Pair<AttrType, String> result = null;
		
		System.out.println("select an attribute:");
		int i;
		for (i = 0; i < t.getAttrs().size(); i++) {
			System.out.println("\t" + (i + 1) + ". " + t.getAttrs().get(i).getRight());
		}
		System.out.println("\t" + (i + 1) + ". done");
		
		try {
			int input = Integer.parseInt(br.readLine());
			if (input == (i + 1)) {
				return null;
			} else if (input > t.getAttrs().size() ||
				input <= 0) {
				System.out.println("invalid input");
				result = selectAttr(t);
			} else {
			result = t.getAttrs().get(input - 1);
			}
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
		return result;
	}
	
	protected String enterInput() {
		String result = "";
		System.out.println("enter value:");
		
		try {
			result = br.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
		return result;
	}
	
	protected String attrEqualsValString(Pair<AttrType, String> attr, String val) {
		String result = "";
		switch (attr.getLeft()) {
		case INT: 
			result = attr.getRight() + "=" + val;
			break;
		case BOOL:
			result = attr.getRight() + " IS " + val;
			break;
		case FLOAT: 
			result = "CAST(" + attr.getRight() + " AS DECIMAL) = CAST(" + val + " AS DECIMAL)";
			break;
		case STRING: 
			result = attr.getRight() + "=\"" + val + "\"";
			break;
		case DATETIME: 
			// TODO
			result = attr.getRight() + " BETWEEN '" + val + "' AND '" + val + "'";
			break;
		default:
			System.out.println("something went wrong");
			break;
		}
		
		return result;
	}
	
	protected void runQuery(String query, List<Pair<AttrType, String>> attrs)  {      
        ResultSet rs = null;
        Statement statement = null; 
         
        try {           
            statement = con.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
            	for (int i = 0; i < attrs.size(); i++) {
            		Pair<AttrType, String> attr = attrs.get(i);
            		switch (attr.getLeft()) {
            		case INT: 
            			System.out.print(attr.getRight() + ": " + rs.getInt(attr.getRight()));
            			break;
            		case BOOL:
	        			System.out.print(attr.getRight() + ": " + rs.getBoolean(attr.getRight()));
	        			break;
            		case FLOAT: 
	        			System.out.print(attr.getRight() + ": " + rs.getFloat(attr.getRight()));
	        			break;
            		case STRING: 
	        			System.out.print(attr.getRight() + ": " + rs.getString(attr.getRight()));
	        			break;
            		case DATETIME: 
	        			System.out.print(attr.getRight() + ": " + rs.getDate(attr.getRight()));
	        			break;
            		default:
	        			System.out.println("something went wrong");
            			break;
            		}
            		if (i + 1 < attrs.size()) {
            			System.out.print(", ");
            		}
            	}
        		System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
