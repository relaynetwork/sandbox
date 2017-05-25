(ns foo-service.core
  (:require
   [clojure.tools.logging :as log]
   [ring.adapter.jetty    :refer [run-jetty]]
   [ring.middleware.json  :refer [wrap-json-response wrap-json-body]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [webster.core          :refer [wrap-mount]])
  (:gen-class))

(defonce server (atom nil))

(def svc-cfg
     {:ID            "foo-service"
      :Name          "foo"
      :Tags          ["rn"]
      :Address       "127.0.0.1"
      :Port          8989
      :url-prefix    "/foo"
      :api-ns-prefix "foo-service.web.api"})

(defn start-service [cfg]
  (reset! server
          (run-jetty
           (-> {:status 404}
               (wrap-mount cfg)
               (wrap-keyword-params)
               (wrap-params)
               (wrap-json-response)
               (wrap-json-body))
           {:port (:Port cfg) :join? false}))

  (log/infof "foo-service online"))

(defn stop-service []
  (when-let [s @server]
    (.stop s))
  (reset! server nil))

(defn -main [ip port & args]
  (register-shutdown)
  (start-service (assoc svc-cfg
                   :Address ip
                   :Port (Integer/parseInt port))))

(comment

  (do
    (stop-service)
    (start-service svc-cfg))
)
