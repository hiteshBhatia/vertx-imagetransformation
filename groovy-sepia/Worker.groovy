String original = "/home/hitesh/Desktop/im/46.jpg"
String destination = "/home/hitesh/Desktop/im/462.jpg"
String processString = "/usr/bin/convert $original -sepia-tone 80% $destination"

ProcessBuilder pb = new ProcessBuilder("convert", "-sepia-tone", "80%", original, destination);
Process p = pb.start()
p.waitFor()

def transformImage(String original, String destination) {
    ProcessBuilder pb = new ProcessBuilder("convert", "-sepia-tone", "80%", original, destination);
    Process p = pb.start()
    p.waitFor()

}

println "Finish"




def eb = vertx.eventBus

def queueConf = [
        "address": "vert-x.process.queue",
        "persistor_address": 'vertx.mongopersistor',
        "collection": "process.queue"]

container.with {
    deployModule('vertx.mongo-persistor-v1.2', ["db_name": "queue_list"], 1) {
        println "Mongo in place."
        deployModule('vertx.work-queue-v1.2', queueConf, 1) {
            deployVerticle('StaticData.groovy', ["persistor_address": "vertx.mongopersistor",
                    "collection": "process.queue"], 1, {})
        }
    }
}
