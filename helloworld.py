def test_create(name,msg):
  desktop_path = "D:\JenkinsTest"
  full_path = desktop_path + name + ".txt"
  file = open(full_path,'w')
  file.write(msg)
  file.close()
  print("OK!!!!!!")

test_create("hello","hello world")
