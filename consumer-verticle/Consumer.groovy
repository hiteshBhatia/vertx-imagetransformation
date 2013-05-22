import org.vertx.groovy.core.eventbus.EventBus

EventBus eb = vertx.eventBus


def queueConf = [
        "address": "vert-x.process.queue",
        "persistor_address": 'vertx.mongopersistor',
        "collection": "process.queue",
        "processorHandler": "queue.processor"
]

container.with {
    deployWorkerVerticle("groovy-sepia/Worker.groovy", null, 1) {
        println "verticle groovy-sepia deployed"
    }

    deployWorkerVerticle("python-pixelate/Worker.py", null, 1) {
        println "verticle python-pixelate deployed"
    }

    deployWorkerVerticle("ruby-blur/Worker.rb", null, 1) {
        println "verticle ruby-blur deployed"
    }
}

// Settings for the queue
def queueItemHandler = { item ->
    Map initialMap = item.body.document
    def finalMap = createTransformationMapFromInputData(initialMap)
    eb.send("image.transform.${initialMap.transformation}", finalMap)
    item.reply([:])
}

//private Map createTransformationMapFromInputData(Map initialMap,String webroot,String images,String updatedImages) {
private Map createTransformationMapFromInputData(Map initialMap){
    List nameList = initialMap.name.tokenize(".")
    String updatedName = "${nameList.first()}-${initialMap.transformation}.${nameList.last()}"
    String originalFilePath = "${initialMap.originalPath}/${initialMap.name}"
    String updatedFilePath = "${initialMap.updatedPath}/${updatedName}"

    [
            "name": initialMap.name,
            "updatedName": updatedName,
            "originalFilePath": originalFilePath,
            "destinationFilePath": updatedFilePath
    ]

}

//Register processor handler on eventbus
eb.registerHandler(queueConf['processorHandler'], queueItemHandler) { message ->
    println "Consumer Registered..."
};

//Register with queue for messages
eb.send("${queueConf['address']}.register", ["processor": queueConf['processorHandler']]) {
    println "Consumer running...."
}
