(ns auth-server-example.server-base
  (:require
   [clojure.tools.logging          :as log]
   [ring.adapter.jetty             :refer :all]
   [ring.util.response             :refer :all]
   [ring.middleware.params         :refer [wrap-params]]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [ring.middleware.session        :refer [wrap-session]]
   [ring.middleware.json           :refer [wrap-json-body wrap-json-response]]
   [ring.middleware.session.store  :as    session-store]
   [buddy.auth.backends            :as    backends]
   [buddy.auth                     :refer [authenticated? throw-unauthorized]]
   [buddy.auth.middleware          :refer [wrap-authentication wrap-authorization]]
   [buddy.auth.accessrules         :as access :refer [wrap-access-rules restrict]]
   [taoensso.carmine.ring          :refer [carmine-store]]
   [compojure.core                 :refer :all]
   [compojure.route                :as r]))

(defonce server (atom nil))
(defonce session-store (atom nil))

(defn not-authorized []
  (-> (response nil)
      (status 401)))

(defn require-login [request]
  (if (authenticated? request)
    (access/success)
    (access/error (not-authorized))))

(defn restricted [handler]
  (restrict handler {:handler require-login}))

(defn redis-config [opts]
  (merge
   {:host "127.0.0.1" :port 6379 :db 1}
   (-> opts :auth :store :redis)))

(defn session-config [opts]
  (merge
   {:expiration-secs 120 :key-prefix ""}
   (-> opts :auth :store :params)))

(defn delete-session [session-key]
  (-> @session-store (session-store/delete-session session-key)))

(defn wrap-session-store [handler opts]
  (reset! session-store (carmine-store (redis-config opts) (session-config opts)))
  (-> handler
      (wrap-session {:cookie-name "id"
                     :cookie-attrs {:secure true :http-only true}
                     :store        @session-store})))

(defn mk-backend [opts]
  (cond (= :session (-> opts :auth :type))
        (backends/session)

        (= :token (-> opts :auth :type))
        (backends/jws {:secret   (-> opts :auth :api-key-secret)
                       ;; :on-error authentication-error
                       })

        :otherwise
        (log/errorf "No valid backend type configured for service auth")))

(defn wrap-service-auth [handler opts]
  (let [backend (mk-backend opts)
        handler (-> (:routes opts) (wrap-authentication backend))]
    (if (= :session (-> opts :auth :type))
      (-> handler (wrap-session-store opts))
      handler)))

(defn mk-handler [opts]
  (let [handler (if (:require-auth? opts)
                  (-> (:routes opts) (wrap-service-auth opts))
                  (:routes opts))]
    (-> handler
        (wrap-json-body {:keywords? true})
        wrap-json-response
        wrap-params)))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn start-server [opts]
  (if @server
    :already-started
    (reset! server (run-jetty (mk-handler opts) opts))))
