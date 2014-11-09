Overview
------------

SSH2 Sampler is for Apache JMeter that executes commands (eg, iostat) and scripts(multiple commands) over an SSH session, and returns the output. 
This plugin is inspired from https://github.com/yciabaud/jmeter-ssh-sampler plugin. One main change is that it uses ganymed-ssh2 in place of Jsch for SSH operations.


Installation
------------

1. Build this project with maven
2. Place the ApacheJMeter_ssh2-1.0.0.jar file into JMeter's lib/ext directory
3. Place ganymed-ssh2-261.jar into JMeter's lib directory
4. Run JMeter, and find "SSH2 Command"/"SSH2 Script" sampler in the Samplers category 

Usage
------------

Using the plugin is simple (assuming familiarity with SSH and JMeter):

### SSH2 Command / SSH2 Script

1. Create a new Test Plan
2. Add a Thread Group
3. Add a Sampler > SSH2 Command / SSH2 Script
4. Specify the host to connect to, port, username and password (unencrypted) or a key file, and a command/script to execute (such as date)
5. Add a Listener > View Results Tree
6. Run the test 



Dependencies
------------

Maven retrieves the following dependencies:

* SSH functionality is provided by the ganymed-ssh2 library
* JMeter 2.9+ is capable of running this plugin 


