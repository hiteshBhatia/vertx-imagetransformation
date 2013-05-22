def eb = vertx.eventBus

Map queueConf = [
        "address": "vert-x.process.queue",
        "persistor_address": 'vertx.mongopersistor',
        "collection": "process.queue"]


container.deployVerticle('StaticData.groovy', queueConf, 1) {
    println "Producer Started..."
}






