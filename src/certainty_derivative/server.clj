(ns certainty-derivative.server
  (:require [certainty-derivative.generator :as gen]
            [certainty-derivative.loader.read :as read]
            [certainty-derivative.viewer.format :as format]
            [certainty-derivative.viewer.sort :as sort]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :as middle]
            [ring.util.response :as res]))

(defn init []
  (gen/generate-test-data 30))

(def records
  (atom (read/read-files "./resources/001.txt" "./resources/002.txt" "./resources/003.txt")))

#_(defn add-record! [input-record]
    (swap! records conj (xform/parse-row input-record)))

(def json-response
  {"gender" {:description "Females first, then sorted by last name"
             :records (->> @records
                           sort/by-gender-and-last-name
                           (map format/json-format))}
   "birthdate" {:description "Sorted by date of birth"
                :records (->> @records
                              sort/by-date-of-birth
                              (map format/json-format))}
   "name" {:description "Sorted by last name, descending"
           :records (->> @records
                         sort/by-last-name
                         reverse
                         (map format/json-format))}})

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
  (POST "/records" [input-record]
        (res/response {:description "Success! Probably!"
                       :record input-record})))

(def app
  (-> (handler/api app-routes)
      (middle/wrap-json-body {:keywords? true})
      (middle/wrap-json-response)))
