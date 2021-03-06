
package mz.ex.ws;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 3.1.10
 * 2017-05-02T17:44:46.263-03:00
 * Generated source version: 3.1.10
 * 
 */
 
public class BookRepoPortTypeServer{

    protected BookRepoPortTypeServer() throws Exception {
        System.out.println("Starting Server");
        Object implementor = new mz.ex.ws.BookRepoPortTypeImpl();
        String address = "http://localhost:9090/BookRepoPort";
        Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws Exception { 
        new BookRepoPortTypeServer();
        System.out.println("Server ready..."); 
        
        Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);
    }
}
 
 