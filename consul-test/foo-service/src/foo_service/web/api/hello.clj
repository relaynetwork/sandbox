(ns foo-service.web.api.hello
  (:require
   [webster.core :refer [register-api]]
   [clojure.tools.logging    :as log]))

(register-api)

(defn ^{:api :get} do-something [params]
  (def params params)
  {:hello "from the foo service"})