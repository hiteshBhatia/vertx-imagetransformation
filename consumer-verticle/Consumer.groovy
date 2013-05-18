import org.vertx.groovy.core.eventbus.EventBus

EventBus eb = vertx.eventBus
String webroot = "/home/hitesh/Projects/vertx/ImageTransformations/queue-verticle/web"
String images = "images"
String updatedImages = "updatedImages"


def queueConf = [
        "address": "vert-x.process.queue",
        "persistor_address": 'vertx.mongopersistor',
        "collection": "process.queue",
        "processorHandler": "queue.processor"
]

container.with {
    deployWorkerVerticle("groovy-sepia/Worker.groovy", null, 1) {
        println "verticle sepia-groovy deployed"
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
    def finalMap = createTransformationMapFromInputData(initialMap,webroot,images,updatedImages)
    eb.send("image.transform.${initialMap.transformation}", finalMap)
    item.reply([:])
}

private Map createTransformationMapFromInputData(Map initialMap,String webroot,String images,String updatedImages) {
    List nameList = initialMap.name.tokenize(".")
    String updatedName = "${nameList.first()}-${initialMap.transformation}.${nameList.last()}"
    String originalPath = "${webroot}/${images}/${initialMap.name}"
    String destinationPath = "${webroot}/${updatedImages}/${updatedName}"

    [
            "name": initialMap.name,
            "webroot": webroot,
            "images": images,
            "updatedImages": updatedImages,
            "updatedName": updatedName,
            "originalPath": originalPath,
            "destinationPath": destinationPath
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
