CORRER mvn install. SE GENERARAN:
	wsdl en target/generated/wsdl/
	war en target/
	clases cliente y servidor en /mz/ex/ws/ (NO SOURCE FOLDER SINO ROOT DEL PROYECTO)
	DEPOSITAR EL war EN $TOMCAT/webapps (SIENDO $TOMCAT EL DIRECTORIO DE INSTALACION DE TOMCAT)

PARA DEBUGGEAR: 
	LEVANTAR TOMCAT EN MODO DEBUG CORRIENDO "%TOMCAT_HOME%/bin/catalina.bat jpda start" (
		SI OCURRE UN ERROR, CORRER LOS SIGUIENTES COMANDOS E INTENTAR NUEVAMENTE: 
			"set JPDA_ADDRESS=8000"
			"set JPDA_TRANSPORT=dt_socket").
	DESDE ECLIPSE IR A DEBUG > DEBUG CONFIGURATIONS... > REMOTE JAVA APPLICATION > NEW.
	ASIGNAR BREAKPOINTS EN EL PROYECTO SERVER.
	CORRER ALGUN PROYECTO CLIENTE (COMO CxfMavenSslUsernametokenClient) EN MODO DEBUG.