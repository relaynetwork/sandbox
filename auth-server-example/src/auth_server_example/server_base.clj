(ns auth-server-example.server-base
  (:require
   [ring.adapter.jetty             :refer :all]
   [ring.util.response             :refer :all]
   [ring.middleware.params         :refer [wrap-params]]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [ring.middleware.session        :refer [wrap-session]]
   [ring.middleware.json           :refer [wrap-json-body wrap-json-response]]
   [buddy.auth.backends.session    :refer [session-backend]]
   [buddy.auth                     :refer [authenticated? throw-unauthorized]]
   [buddy.auth.middleware          :refer [wrap-authentication wrap-authorization]]
   [buddy.auth.accessrules         :as access :refer [wrap-access-rules restrict]]
   [taoensso.carmine.ring          :refer [carmine-store]]
   [compojure.core                 :refer :all]
   [compojure.route                :as r]))

(defonce server (atom nil))

(defn not-authorized []
  (-> (response nil)
      (status 401)))

(defn require-login [request]
  (if (authenticated? request)
    (access/success)
    (access/error (not-authorized))))

(defn restricted [handler]
  (restrict handler {:handler require-login}))

(defn mk-handler [opts]
  (-> (:routes opts)
      (wrap-authentication (session-backend))
      (wrap-session {:cookie-name "id"
                     :cookie-attrs {:secure true :http-only true}
                     :store (carmine-store
                             {:host "127.0.0.1" :port 6379 :db 1}
                             {:expiration-secs 60 :key-prefix ""})})
      (wrap-json-body {:keywords? true})
      wrap-json-response
      wrap-params))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn start-server [opts]
  (if @server
    :already-started
    (reset! server (run-jetty (mk-handler opts) opts))))
