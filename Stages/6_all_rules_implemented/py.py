import threading
import time

class Timeout_Error(Exception):
    pass

def timer():
  time.sleep(120)
  print('120 seconds timout')

def after_timeout():
  raise Timeout_Error

try:
    t = threading.Thread(target=timer)
    t.daemon = True
    t.start()
    threading.Timer(2, after_timeout).start()
except Timeout_Error:
    print('test')
except:
    print(0)