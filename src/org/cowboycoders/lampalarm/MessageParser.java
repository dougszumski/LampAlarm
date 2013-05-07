package org.cowboycoders.lampalarm;

import net.wimpi.modbus.*;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.io.*;
import net.wimpi.modbus.net.*;
import net.wimpi.modbus.util.*;

public class MessageParser {
	
	/* The important instances of the classes mentioned before */
	SerialConnection con = null; //the connection
	ModbusSerialTransaction trans = null; //the transaction
	ReadInputRegistersRequest req = null; //the request
	ReadInputRegistersResponse res = null; //the response
	
	/* Variables for storing the parameters */
	private String portname= "Test" ; //the name of the serial port to be used
	private int unitid = 0; //the unit identifier we will be talking to
	private int ref = 0; //the reference, where to start reading from
	private int count = 0; //the count of IR's to read
	private int repeat = 1; //a loop for repeating the transaction
	
	public String parseString(String message)
	{
		//2. Set master identifier
		//ModbusCoupler.createModbusCoupler(null);
		ModbusCoupler.getReference().setUnitID(1);      

		//3. Setup serial parameters
		SerialParameters params = new SerialParameters();
		params.setPortName(portname);
		params.setBaudRate(9600);
		params.setDatabits(8);
		params.setParity("None");
		params.setStopbits(1);
		params.setEncoding("ascii");
		params.setEcho(false);
		
		//4. Open the connection
		con = new SerialConnection(params);
		try {
			con.open();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//5. Prepare a request
		req = new ReadInputRegistersRequest(ref, count);
		req.setUnitID(unitid);
		req.setHeadless();

		//6. Prepare a transaction
		trans = new ModbusSerialTransaction(con);
		trans.setRequest(req);
		
		//7. Execute the transaction repeat times
		int k = 0;
		do {
		  try {
			trans.execute();
		} catch (ModbusIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModbusSlaveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModbusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  res = (ReadInputRegistersResponse) trans.getResponse();
		  for (int n = 0; n < res.getWordCount(); n++) {
		    System.out.println("Word " + n + "=" + res.getRegisterValue(n));
		  }
		  k++;
		} while (k < repeat);

		//8. Close the connection
		con.close();  
		
		return message;
	}
}
