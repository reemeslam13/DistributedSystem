# information
* To run in UDP method, 3 arguments needed:  'udp' hostname portnumber

* for AMQP method, 4 arguments needed: 'AMQP' hostname queueName push|pull

Queue properties are in config.properties file

* for gRPC method, 3 arguments needed: 'gRPC' hostname portnumber

* for REST method, 1 argument  needed: 'Rest' (URI is in config.properties file)

* Otherwise Local method will be used 

* all case insensitive *

** log file for client: clientlogfile.log

** log file for server: serverlogfile.log
