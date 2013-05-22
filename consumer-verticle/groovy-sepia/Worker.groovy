def eb = vertx.eventBus


def transformImage = { message ->
    String finalPath = message.body['destinationFilePath']
    ProcessBuilder pb = new ProcessBuilder("convert", "-sepia-tone", "80%", message.body['originalFilePath'], finalPath );
    Process p = pb.start()
    p.waitFor()
    eb.send("image.processing.completed",["name":message.body['name'],"updatedName":message.body['updatedName']])
    println "${finalPath} :: Finished"
    println "------------------------------------------"
}

eb.registerHandler("image.transform.sepia",transformImage){
}
