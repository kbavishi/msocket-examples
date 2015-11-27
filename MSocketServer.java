import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import edu.umass.cs.msocket.MServerSocket;
import edu.umass.cs.msocket.MSocket;
import edu.umass.cs.msocket.mobility.MobilityManagerServer;
import edu.umass.cs.msocket.common.policies.NoProxyPolicy;

public class MSocketServer {

private static final String LOCALHOST = "127.0.0.1";
private static final int LOCAL_PORT = 5454;

public static void main(String[] args) throws IOException {
      String serverName = args[0];
      MServerSocket mserv = null;
      try {
	      System.out.println("Trying to get a new socket");
	      mserv = new MServerSocket(serverName, new NoProxyPolicy(),
					new InetSocketAddress(LOCALHOST, LOCAL_PORT), 0);
      } catch (Exception e) {
	      // We sometimes get GNS connect timeout errors.
	      // Exit in that case.
	      System.out.println("Got an exception. Cleanup quietly");
	      e.printStackTrace();
	      System.exit(1);
      }
      MSocket msocket = mserv.accept();
      OutputStream outstream = msocket.getOutputStream();
      InputStream inpstream = msocket.getInputStream();
      boolean done = false;
      byte[] byteArray = new byte[1000];
      while(!done) {
	      outstream.write( new String("hello world from server").getBytes() );
	      inpstream.read(byteArray);
	      System.out.println(new String(byteArray));
	      try {
		      Thread.sleep(2000);
	      } catch (InterruptedException e) {
		      e.printStackTrace();
	      }
      }
      msocket.close();
      mserv.close();
      MobilityManagerServer.shutdownMobilityManager();
}

}
