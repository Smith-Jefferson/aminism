package com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtil {

	public static void free(ResultSet resultset,Statement st,Connection conn) throws SQLException{

		try{
			if(resultset!=null)  
	            resultset.close();  
	        } catch (SQLException e) {  
	           
	            e.printStackTrace();  
	        }  
		finally  
        {  
            try  
            {  
                if(st!=null)  
                    st.close();  
            } catch (SQLException e) {  
               
                e.printStackTrace();  
            }  
            finally  
            {  
                if(conn!=null)
                	conn.close();
            }  
        }  
		
	}
}
