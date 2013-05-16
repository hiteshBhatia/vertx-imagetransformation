def eb = vertx.eventBus

def queueConf = [
        "address": "vert-x.process.queue",
        "persistor_address": 'vertx.mongopersistor',
        "collection": "process.queue"]

container.with {
    deployModule('vertx.mongo-persistor-v1.2', ["db_name": "queue_list"], 1) {
        println "Mongo in place."
        deployModule('vertx.work-queue-v1.2', queueConf, 1) {
            println "Queue started..."
        }
    }
}
