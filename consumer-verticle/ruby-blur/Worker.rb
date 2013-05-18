require "vertx"
include Vertx

id = Vertx::EventBus.register_handler('image.transform.blur') do |message|
    finalPath = message.body['destinationPath']
    command="convert #{message.body['originalPath']} -blur 8x8 #{finalPath}"
    system "#{command}"

    Vertx::EventBus.send("image.processing.completed",{
     "name"=>message.body['name'],
     "updatedName"=>message.body['updatedName']
    })

    puts "#{finalPath} :: Finished"
    puts "------------------------------------------"
end