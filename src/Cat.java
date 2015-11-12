import java.net.InetAddress;
import java.net.UnknownHostException;


public class Cat{

  public static void main(String[] args) {
	  try {
		InetAddress addr = InetAddress.getByName(args[0]);
		ReverseConnection r=  new ReverseConnection(addr,Integer.parseInt(args[1]));
		r.run();
	} catch (UnknownHostException e) {
		e.printStackTrace();
	}
	
 }

  

}