def eb = vertx.eventBus


def queueConf = [
        "address": "vert-x.process.queue",
        "persistor_address": 'vertx.mongopersistor',
        "collection": "messages.queue"]

container.with {
    deployModule('vertx.mongo-persistor-v1.2', ["db_name": "queue_list_1"], 1) {
        println "Mongo in place."

        deployModule('vertx.work-queue-v1.2', queueConf, 1) {
            println "Queue started..."
            startHttpServer()

            sendMessagesToQueue(eb)
        }
    }
}

def periodicHandler = { reply ->
 sendMessagesToQueue(eb)
}

timerID = vertx.setPeriodic(100, periodicHandler)




def sendMessagesToQueue(eb){
    eb.send('logs',['msg':"This is a message"])
}



def startHttpServer() {
    def server = vertx.createHttpServer()

    // Serve the static resources
    server.requestHandler { req ->
        if (req.uri == '/') req.response.sendFile('index.html')
        if (req.uri == '/vertxbus.js') req.response.sendFile('vertxbus.js')
    }

    vertx.createSockJSServer(server).bridge(prefix: '/eventbus', [[:]])

    server.listen 8181

    println "Server started at http://localhost:8181/"
}
