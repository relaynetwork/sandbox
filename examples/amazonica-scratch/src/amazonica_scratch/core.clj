(ns amazonica-scratch.core
  (:require
   [clojure.tools.nrepl.server :refer [start-server stop-server]]
   [cider.nrepl                :refer [cider-nrepl-handler]]
   [clojure.tools.logging      :as log]
   [schema.core                :as s]))

(defonce config (atom {:nrepl {:port 4445}}))
(defonce nrepl-server (atom nil))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))



(defn -main [& args]
  (reset! nrepl-server
          (start-server :port (-> config deref :nrepl :port)
                        :handler cider-nrepl-handler))
  (log/infof "nrepl initialized."))
