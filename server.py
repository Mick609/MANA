import socket
import threading

BUFFER_SIZE = 48
TCP_IP = '10.3.141.51'
# TCP_IP = '172.20.10.2'
TCP_PORT = 8004
CLIENTS = {}


def client(conn):
    while True: 
        data = conn.recv(BUFFER_SIZE)
        print data
        if not data:
            break
        # broadcast
        print CLIENTS
        for client in CLIENTS.values():
            try:
                client.send(data)
            except socket.error, e:
                print 'A socket error'
                del CLIENTS[client.fileno()]
            except IOError, e:
                if e.errno == errno.EPIPE:
                    print 'EPIPE error'
                else:
                    print 'Other error'

def listener():
    print "Start server ",TCP_IP,":",TCP_PORT
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((TCP_IP, TCP_PORT))
    s.listen(5)
    while True:
        conn, addr = s.accept()
        CLIENTS[conn.fileno()] = conn
        threading.Thread(target=client, args=(conn,)).start()

     # the connection is closed: unregister
    del CLIENTS[conn.fileno()]

if __name__ == '__main__':
    listener()