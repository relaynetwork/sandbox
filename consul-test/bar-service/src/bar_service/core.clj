(ns bar-service.core
  (:require
   [clojure.tools.logging :as log]
   [ring.adapter.jetty    :refer [run-jetty]]
   [ring.middleware.json  :refer [wrap-json-response wrap-json-body]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [webster.core          :refer [wrap-mount]])
  (:gen-class))

(defonce server (atom nil))

(defn start-service []
  (reset! server
          (run-jetty
           (-> {:status 404}
               (wrap-mount
                {:url-prefix "/bar"
                 :api-ns-prefix "bar-service.web.api"})
               (wrap-keyword-params)
               (wrap-params)
               (wrap-json-response)
               (wrap-json-body))
           {:port 8988 :join? false}))
  (log/infof "bar-service online"))

(defn stop-service []
  (when-let [s @server]
    (.stop s))
  (reset! server nil))

(defn -main [& args]
  (start-service))

(comment

  (do
    (stop-service)
    (start-service))
)
