package es.udc.cartolab.gvsig.users.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FakeRequestsThread extends Thread {

    @Override
    public void run() {
	while(DBSession.isActive()){
	    PreparedStatement st;
	    try {
		Thread.sleep(30000); // wait 30 seconds
		String fakeQuery = "SELECT 1=1";
		st = DBSession.getCurrentSession().getJavaConnection().prepareStatement(fakeQuery);
		st.execute();
	    } catch (SQLException e) {
		e.printStackTrace();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}

    }

}
