#!/usr/bin/python

import socket
import sys
import time

# reading CLI arguments
port_num = int(sys.argv[2])
path = str(sys.argv[1])

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect the socket to the port where the server is listening
server_address = ('catserver', port_num)
#print >>sys.stderr, 'connecting to %s port %s' % server_address
try:
    sock.connect(server_address)

    f = open(path,'rw')
    fdata = f.read()
    # verifying if file exists
    if fdata == '':
        print 'Empty file cant operate'
        exit()
    fdata = fdata.upper()
    # repeating the loop for 11 times total 0-30 Sec
    for i in range(11):
        time.sleep(3)
        # Sending the data to client
        sock.send("LINE\n")
        res = ''
        # Receiving the single line from Server
        while True:
            y = sock.recv(1)
            if y == '\n':
                break
            else:
                res = res + y
        if res in fdata:
            print 'OK'
        else:
            print 'MISSING'

    sock.close()
except:
    print "Couldn't Find Server"

