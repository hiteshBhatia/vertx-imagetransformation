import vertx
import os

from core.event_bus import EventBus


def myHandler(message):
    finalPath = message.body['destinationPath']
    command= ("convert -scale 10% -scale 1000% " + message.body['originalPath'] + " " +  finalPath)
    os.system(command)

    EventBus.send("image.processing.completed",{'updatedName':message.body['updatedName'],'name':message.body['name']})

    print finalPath +" ::Finished"
    print "------------------------------------------"

EventBus.register_handler('image.transform.pixelate', handler=myHandler)