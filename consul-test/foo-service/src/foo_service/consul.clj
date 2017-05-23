(ns foo-service.consul
  (:require
   [cheshire.core :as json]
   [org.httpkit.client :as http]
   [clojure.tools.logging    :as log]))

(defn register-service [ip port]
  @(http/put "http://localhost:8500/v1/agent/service/register"
             {:headers {"Content-type" "application/json"}
              :body (json/generate-string
                     {"ID" "foo-service"
                      "Name" "foo"
                      "Tags" ["rn"]
                      "Address" ip
                      "Port" port
                      "EnableTagOverride" false})}))

(defn deregister-service []
  @(http/put "http://localhost:8500/v1/agent/service/deregister/foo-service"
             {:headers {"Content-type" "application/json"}}))

(comment

  [@(http/get "http://localhost:8500/v1/agent/services")]

  [@(http/get "http://localhost:8500/v1/catalog/services")]

  [@(http/put "http://localhost:8500/v1/agent/service/register"
              {:headers {"Content-type" "application/json"}
               :body (json/generate-string
                      {"ID" "foo-service"
                       "Name" "foo"
                       "Tags" [
                               "rn-proto-services"
                               "primary"
                               "v1"]
                       "Address" "127.0.0.1"
                       "Port" 8987
                       "EnableTagOverride" false
                       "Check" {
                                ;; "DeregisterCriticalServiceAfter": "90m"
                                ;; "Script": "/usr/local/bin/check_redis.py"
                                ;; "HTTP": "http://localhost:5000/health"
                                ;; "Interval": "10s"
                                ;; "TTL": "15s"
                                }})})]

)