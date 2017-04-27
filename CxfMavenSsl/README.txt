EN ESTE EJEMPLO SE CREA UN SERVICIO Y CLIENTE USANDO CXF EN EL QUE LA COMUNICACION SERA DEL TIPO HTTPS Y BASIC_AUTH. 

SIGUIENDO ALGUNOS PASOS DE CONFIGURACION DE SSL DE: http://web-gmazza.rhcloud.com/blog/entry/ssl-for-web-services

------------------------------------------------------------------------------------------------------------------

DEPOSITAR EL KEYSTORE cert/serverKeystore.jks EN $TOMCAT/conf/
(EL CERFICADO TIENE UN PERIODO DE VENCIMIENTO. EN CASO DE VENCER VOLVERLO A GENERAR CON: 
	keytool -genkeypair -keyalg RSA -validity 730 -alias myserverkey -keystore serverKeystore.jks -dname "cn=localhost" -keypass password -storepass password
	NOTAR QUE EL keypass Y EL storepass DEBEN SER IGUALES.)

AGREGAR AL ARCHIVO $TOMCAT/conf/server.xml :
	<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol" maxThreads="150" SSLEnabled="true" keystoreFile="conf/serverKeystore.jks" keystorePass="password">
		<SSLHostConfig>
			<Certificate certificateKeystoreFile="conf/serverKeystore.jks" certificateKeystorePassword="password" type="RSA" />
		</SSLHostConfig>
	</Connector>

AGREGAR A $TOMCAT/conf/tomcat-users.xml :
	<role rolename="mywsrole"/>
	<user username="alice"  password="clarinet"  roles="mywsrole"/>
	<user username="bob"    password="trombone"  roles="mywsrole"/>
	<user username="chuck"  password="harmonica" roles="mywsrole"/>

CORRER mvn install Y DEPOSITAR EL war en $TOMCAT/webapps

CORRER BookRepoClient PARA PROBAR EL SERVICIO.

PARA DEBUGGEAR: 
	LEVANTAR TOMCAT EN MODO DEBUG CORRIENDO "%TOMCAT_HOME%/bin/catalina.bat jpda start" (
		SI OCURRE UN ERROR, CORRER LOS SIGUIENTES COMANDOS E INTENTAR NUEVAMENTE: 
			"set JPDA_ADDRESS=8000"
			"set JPDA_TRANSPORT=dt_socket").
	DESDE ECLIPSE IR A DEBUG > DEBUG CONFIGURATIONS... > REMOTE JAVA APPLICATION > NEW.
	ASIGNAR BREAKPOINTS EN EL PROYECTO SERVER.
	CORRER ALGUN PROYECTO CLIENTE (COMO CxfMavenSslUsernametokenClient) EN MODO DEBUG.
