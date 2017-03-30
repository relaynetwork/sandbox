(ns auth-server-example.core
  (:require
   [clojure.tools.logging           :as log]
   [auth-server-example.server-base :refer [start-server stop-server restricted not-authorized]]
   [ring.util.response              :refer :all]
   [compojure.core                  :refer :all]
   [compojure.route                 :as r]))

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

(defn sensitive-content [req]
  (response
   {:secret-message "drink your ovaltine"}))

(defn non-sensitive-content [req]
  (response
   {:message "everyone can see this!"}))

(defroutes main-routes
  (POST "/session"          [] login)
  (GET  "/public/stuff"     [] non-sensitive-content)
  (GET  "/restricted/thing" [] (restricted sensitive-content))
  (r/not-found nil))

;; This obviously would get pulled from edn/config files per env...
(def server-config
     {:port          8282
      :join?         false
      :routes        main-routes
      :require-auth? true
      :auth          {:type :session}})

(defn -main [& args]
  (start-server server-config)
  (log/infof "server is running on port %s" (:port server-config)))

(comment

  (do
    (stop-server)
    (start-server {:port 8282 :join? false :routes main-routes}))
)