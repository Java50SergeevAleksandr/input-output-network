package telran.employees;

import java.util.*;

import telran.employees.service.*;
import telran.net.ApplProtocol;
import telran.net.TcpServer;
import telran.view.SystemInputOutput;

public class CompanyServerAppl {
	private static final String DEFAULT_FILE_NAME = "employees";

	public static void main(String[] args) throws Exception {
		String fileName = args.length > 0 ? args[0] : DEFAULT_FILE_NAME;
		Company company = new CompanyImpl();
		company.restore(fileName);
		ApplProtocol protocol = new CompanyProtocol(company);
		TcpServer tcpServer = new TcpServer(CompanyApi.SERVER_PORT, protocol);
		Thread threadServer = new Thread(tcpServer);
		threadServer.start();
		SystemInputOutput io = new SystemInputOutput();
		io.readString("Enter shutdown command for exit", "no shutdown command",
				new HashSet<String>(List.of("shutdown")));
		tcpServer.shutdown();
		company.save(fileName);
	}

}
