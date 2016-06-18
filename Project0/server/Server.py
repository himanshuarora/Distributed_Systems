#!/usr/bin/python

import socket
import threading
import sys

# ThreadedServer Class for multiple connections
class ThreadedServer(object):
    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sock.bind((self.host, self.port))

# Listen on port
    def listen(self,path):
        self.sock.listen(5)
        while True:
            client, address = self.sock.accept()
            client.settimeout(60)
            threading.Thread(target = self.listenToClient,args = (client,address,path)).start()

# main logic written here
    def listenToClient(self, client, address, path):
        size = 1024
        f = open(path, 'r')
        fread = f.read()
        if fread == '':
            print "Empty file can't proceed"
            exit()
        while True:
            try:
# Receiving message from client & Sending appropriate output
                data = client.recv(size)
                if data == 'LINE\n':
                    x = f.readline()
                    if x == '':
                        f.close()
                        f = open(path, 'rw')
                        x = f.readline()
                    z = x.upper()
                    client.send(z)
                else:
                    client.send('Error')
            except:
                client.close()
                return False

if __name__ == "__main__":
    port_num = int(sys.argv[2])
    path = str(sys.argv[1])
    print "Creating server at port :", port_num
    ThreadedServer('',port_num).listen(path)



