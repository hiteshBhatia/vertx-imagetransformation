import org.vertx.groovy.core.eventbus.EventBus

EventBus eb = vertx.eventBus

def queueConf = [
        "address": "vert-x.process.queue",
        "persistor_address": 'vertx.mongopersistor',
        "collection": "process.queue",
        "processorHandler": "queue.processor"
]

container.with {
//    deployVerticle("groovy-sepia/Worker.groovy",null,1){
//        println "verticle sepia-groovy deployed"
//    }

    Map map = [
            "original": "/home/hitesh/Projects/vertx/ImageTransformations/images/am2.jpg",
            "destination": "/home/hitesh/Projects/vertx/ImageTransformations/updatedImages",
            "updatedName": "hello1.jpg"
    ]

    deployVerticle("python-pixelate/Worker.py", map, 1) {
        println "verticle python-pixelate deployed"
        eb.send("image.transform.pixelate",map)
    }

//    deployWorkerVerticle("ruby-blur/Worker.rb",null,1){
//        println "verticle ruby-blur deployed"
//    }
}


def queueItemHandler = { item ->
    println "---------------------"
    println item.properties
    println "---------------------"
    item.reply([:])
}

//Register processor handler on eventbus
eb.registerHandler(queueConf['processorHandler'], queueItemHandler) { message ->
    println "Consumer Registered..."
};

//Register with queue for messages
eb.send("${queueConf['address']}.register", ["processor": queueConf['processorHandler']]) {
    println "Consumer running...."
}
