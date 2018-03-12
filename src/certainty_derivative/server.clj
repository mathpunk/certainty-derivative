(ns certainty-derivative.server
  (:require [certainty-derivative.generator :as gen]
            [certainty-derivative.loader.read :as read]
            [certainty-derivative.viewer.format :as format]
            [certainty-derivative.viewer.sort :as sort]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :as json]
            [ring.middleware.params :as params]
            [ring.util.response :as res]
            [certainty-derivative.loader.transform :as xform]
            [ring.mock.request :as mock]))


(defn init []
  (gen/generate-test-data 30))

(def records
  (atom (read/read-files "./resources/001.txt"
                         "./resources/002.txt"
                         "./resources/003.txt")))

#_(defn add-record! [input-record]
    (swap! records conj (xform/parse-row input-record)))

(defn prepare-records [ordering]
  (->> @records
       ordering
       (map format/json-format)))

(def json-response
  {"gender" {:description "Females first, then sorted by last name"
             :records (prepare-records sort/by-gender-and-last-name)}
   "birthdate" {:description "Sorted by date of birth"
                :records (prepare-records sort/by-date-of-birth)}
   "name" {:description "Sorted by last name, descending"
           :records (prepare-records (comp reverse sort/by-last-name))}})

(def landing-page
  "Routes available:
  - /records/gender ('f' first)
  - /records/birthdate
  - /records/name (reverse sort)")


(defroutes app-routes
  (GET "/favicon.ico" [] "")
  (GET "/" [] (-> landing-page
                  res/response
                  (res/content-type "text/plain")))
  (GET "/records/gender" []
       (res/response (json-response "gender")))
  (GET "/records/birthdate" []
       (res/response (json-response "birthdate")))
  (GET "/records/name" []
       (res/response (json-response "name")))
  (POST "/records" [req]
        (res/response {:description "Success! Probably!"
                       :record req})))

(def app
  (-> (handler/api app-routes)
      (json/wrap-json-body {:keywords? true})
      (json/wrap-json-response)
      (params/wrap-params)))


;; REPL testing
;; ==================
(require '[ring.mock.request :as mock]
         '[certainty-derivative.record.example :refer :all]
         '[clojure.string :as string])

example-comma-row

(app (-> (mock/request :post "/records")
         (mock/json-body [example-comma-row])))


;; (detect-delimiter example-comma-row)

;; (tokenize-row example-comma-row)



