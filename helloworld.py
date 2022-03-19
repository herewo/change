def test_create(name,msg):
  desktop_path = 'D:\Jenkins\workspace'
  full_path = desktop_path + name + ".txt"
  file = open(full_path,'w')
  file.write(msg)
  file.close()
test_create("hello","hello world")
