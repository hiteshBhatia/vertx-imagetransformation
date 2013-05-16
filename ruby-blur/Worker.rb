origin  = "/home/hitesh/Desktop/im/46.jpg"
destination = "/home/hitesh/Desktop/im/463.jpg"
command="convert #{origin} -blur 8x8 #{destination}"
puts "#{command}"
system "ll"
system "#{command}"
puts "OK"

