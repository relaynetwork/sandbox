(ns auth-server-example.core
  (:require
   [clojure.tools.logging              :as log]
   [com.relaynetwork.service-base.core :refer :all]
   [ring.util.response                 :refer :all]
   [compojure.core                     :refer :all]
   [compojure.route                    :as r]
   [clojure.java.io                    :as io]
   [cheshire.core                      :as json]))


;;;;;;;; BS Login functions
;;;;;;;; The only valid user/pass is jc/kool
(defn user-valid? [uname pword]
  (and (= "jc" uname)
       (= "kool" pword)))

(defn login [req]
  (let [uname (-> req :body :username)
        pword (-> req :body :password)]
    (if (user-valid? uname pword)
      (-> (response {:authenticated true})
          (assoc :session {:identity uname}))
      (not-authorized))))

;;;;;;;; logout route to destroy session

(defn logout [req]
  (when-let [session-key (:session/key req)]
    (delete-session session-key))
  (response
   {:success true}))


;;;;;;;; Restricted route that requires authentication

(defn sensitive-content [req]
  (response
   {:secret-message "drink your ovaltine"}))

;;;;;;;; Public API w/ no authentication required

(defn non-sensitive-content [req]
  (response
   {:message "everyone can see this!"}))

;;;;;;;; simple routing table

(defroutes main-routes
  (POST   "/session"          [] login)
  (DELETE "/session"          [] logout)
  (GET    "/public/stuff"     [] non-sensitive-content)
  (GET    "/restricted/thing" [] (restricted sensitive-content))
  (r/not-found nil))


(defn server-config [config-file]
  (-> (format "config/%s.json" config-file)
      io/resource
      io/reader
      slurp
      (json/parse-string true)
      (assoc :routes main-routes)))

(defn -main [& args]
  (let [config-file (or (first args) "server-session")
        config      (server-config config-file)]
    (start-server config)
    (log/infof "server config: %s" config)
    (log/infof "server is running on port %s" (:port config))))

(comment

  (mk-jwt {:user "me"} (-> server-config :auth :api-key-secret))

  (do
    (stop-server)
    (start-server (server-config "server-session")))
  )