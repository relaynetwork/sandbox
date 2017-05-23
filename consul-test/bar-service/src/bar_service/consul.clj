(ns bar-service.consul
  (:require
   [cheshire.core :as json]
   [org.httpkit.client :as http]
   [clojure.tools.logging    :as log]))


(comment

  [@(http/get "http://localhost:8500/v1/agent/services")]

  [@(http/get "http://localhost:8500/v1/catalog/services")]

  [@(http/put "http://localhost:8500/v1/agent/service/register"
              {:headers {"Content-type" "application/json"}
               :body (json/generate-string
                      {"ID" "bar-service"
                       "Name" "bar"
                       "Tags" [
                               "primary"
                               "v1"
                               "bar"]
                       "Address" "127.0.0.1"
                       "Port" 8988
                       "EnableTagOverride" false
                       "Check" {
                                ;; "DeregisterCriticalServiceAfter": "90m"
                                ;; "Script": "/usr/local/bin/check_redis.py"
                                ;; "HTTP": "http://localhost:5000/health"
                                ;; "Interval": "10s"
                                ;; "TTL": "15s"
                                }})})]

)