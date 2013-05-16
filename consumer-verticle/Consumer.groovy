import org.vertx.groovy.core.eventbus.EventBus

EventBus eb = vertx.eventBus

def queueConf = [
        "address": "vert-x.process.queue",
        "persistor_address": 'vertx.mongopersistor',
        "collection": "process.queue",
        "processorHandler": "queue.processor"
]



def queueItemHandler = { item ->
    println "---------------------"
    println item.properties
    println "---------------------"
    item.reply([:])
}

// Register processor handler on eventbus
eb.registerHandler(queueConf['processorHandler'], queueItemHandler) { message ->
    println "Consumer Registered..."
};

// Register with queue for messages
eb.send("${queueConf['address']}.register", ["processor": queueConf['processorHandler']]) {
    println "Consumer running...."
}



