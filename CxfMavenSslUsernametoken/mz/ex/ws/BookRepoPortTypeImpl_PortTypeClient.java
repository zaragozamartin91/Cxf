
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package mz.ex.ws;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

/**
 * This class was generated by Apache CXF 3.1.10
 * 2017-04-25T16:58:54.695-03:00
 * Generated source version: 3.1.10
 * 
 */
public class BookRepoPortTypeImpl_PortTypeClient {

    public static void main(String[] args) throws Exception {
        QName serviceName = new QName("http://ws.ex.mz/", "BookRepoService");
        QName portName = new QName("http://ws.ex.mz/", "BookRepoPort");

        Service service = Service.create(serviceName);
        service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,
                        "http://localhost:9090/BookRepoPort"); 
        mz.ex.ws.BookRepoPortTypeImpl_PortType client = service.getPort(portName,  mz.ex.ws.BookRepoPortTypeImpl_PortType.class);
        
        // Insert code to invoke methods on the client here
    }

}