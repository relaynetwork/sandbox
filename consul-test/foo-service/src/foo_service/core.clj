(ns foo-service.core
  (:require
   [foo-service.consul :as consul]
   [clojure.tools.logging :as log]
   [ring.adapter.jetty    :refer [run-jetty]]
   [ring.middleware.json  :refer [wrap-json-response wrap-json-body]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [webster.core          :refer [wrap-mount]])
  (:gen-class))

(defonce server (atom nil))

(defn start-service [ip port]
  (reset! server
          (run-jetty
           (-> {:status 404}
               (wrap-mount
                {:url-prefix "/foo"
                 :api-ns-prefix "foo-service.web.api"})
               (wrap-keyword-params)
               (wrap-params)
               (wrap-json-response)
               (wrap-json-body))
           {:port port :join? false}))
  (consul/register-service ip port)
  (log/infof "foo-service online"))

(defn stop-service []
  (when-let [s @server]
    (.stop s))
  (reset! server nil))

(defn -main [ip port & args]
  (start-service ip port))

(comment

  (do
    (stop-service)
    (start-service))
)
