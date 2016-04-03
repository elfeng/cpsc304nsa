package queries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;

import main.AttrType;
import main.Pair;
import tables.Table;

public class Aggregation extends Query {
	
	
	public Aggregation(Connection con) {
		super(con);
	}
	
	public void aggregate() {
		Table table = selectTable();
		
		int aggrTypeInt = selectGeneric("select type of aggregation:", Arrays.asList("average", "count", "min", "max"));
		String aggrType = "";
		switch (aggrTypeInt) {
		case 1: aggrType = "AVG"; break;
		case 2: aggrType = "COUNT"; break;
		case 3: aggrType = "MIN"; break;
		case 4: aggrType = "MAX"; break;
		default:
			System.out.println("invalid input");
		}

		Pair<AttrType, String> aggrAttr = selectAttr(table.getAttrs(), "select attribute to aggregate by:");
		
		String query = "SELECT " + aggrType + "(" + aggrAttr.getRight() + ") FROM " + table.getName() + ";";
		runQuery(query, Arrays.asList(new Pair<AttrType, String>(aggrAttr.getLeft(), aggrType + "(" + aggrAttr.getRight() + ")")));
	}
	
	public JTable countSuspiciousDataByCountry() {
		String[] columnNames = {"country", "suspicious_data_count"};
		ArrayList<List<Object>> data = new ArrayList<List<Object>>();

		String query = 
				"SELECT country, COUNT(data_id)\n"
				+ "FROM data, location\n"
				+ "WHERE data.lat = location.lat AND data.lng = location.lng AND suspicious = 1\n"
				+ "GROUP BY country;";
		
        ResultSet rs = null;
        Statement statement = null; 
        try {           
            statement = con.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
            	data.add(Arrays.asList(
            		rs.getString("country"),
            		rs.getInt("COUNT(data_id)")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Object[][] dataArray = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            List<Object> row = data.get(i);
            dataArray[i] = row.toArray(new Object[row.size()]);
        }

		return new JTable(dataArray, columnNames);
		}
	
	
	@Override
	public JTable doQuery(String arg) {
		return countSuspiciousDataByCountry();
	}

}
