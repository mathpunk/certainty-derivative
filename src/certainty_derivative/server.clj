(ns certainty-derivative.server
  (:require [compojure.core :refer :all]
            [ring.util.response :as res]
            [certainty-derivative.viewer.format :as format]
            [compojure.handler :as handler]
            [ring.middleware.json :as middle]
            [certainty-derivative.generator :as gen]
            [certainty-derivative.loader.read :as read]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [certainty-derivative.viewer.sort :as sort]))

(defn init []
  (gen/generate-test-data 30))

(def records
  (read/read-files "./resources/001.txt" "./resources/002.txt" "./resources/003.txt"))

(def json-response
  {"gender" {:description "Females first, then sorted by last name"
             :records (->> records
                           sort/by-gender-and-last-name
                           (map format/json-format))}
   "birthdate" {:description "Sorted by date of birth"
                :records (->> records
                              sort/by-date-of-birth
                              (map format/json-format))}
   "name" {:description "Sorted by last name, descending"
           :records (->> records
                         sort/by-last-name
                         reverse
                         (map format/json-format))}})

(defroutes app-routes
  (GET "/favicon.ico" [] "")
  (GET "/" [] (-> "Hello world"
                  res/response
                  (res/content-type "text/plain")))
  (GET "/records/gender" []
       (res/response (json-response "gender")))
  (GET "/records/birthdate" []
       (res/response (json-response "birthdate")))
  (GET "/records/name" []
       (res/response (json-response "name"))))

(def app
  (-> (handler/api app-routes)
      (middle/wrap-json-body {:keywords? true})
      (middle/wrap-json-response)))

;; • POST /records - Post a single data line in any of the 3 formats supported by your existing code
