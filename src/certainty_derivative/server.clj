(ns certainty-derivative.server
  (:require [compojure.core :refer :all]
            [ring.util.response :as res]
            [certainty-derivative.viewer.format :as format]
            [compojure.handler :as handler]
            [ring.middleware.json :as middle]
            [certainty-derivative.generator :as gen]
            [certainty-derivative.loader.read :as read]
            [compojure.route :as route]
            [clojure.java.io :as io]))

(defn init []
  (gen/generate-test-data 100))

(def records
  (read/read-files "./resources/001.txt" "./resources/002.txt" "./resources/003.txt"))


(defroutes app-routes
  (GET "/favicon.ico" [] "")
  (GET "/" [] (-> "Hello world"
                  res/response
                  (res/content-type "text/plain")))
  (GET "/records/gender" []
       (res/response {:description "Records sorted by, females first, then by last name"
                      :records (map format/json-format records)})))

(def app
  (-> (handler/api app-routes)
      (middle/wrap-json-body {:keywords? true})
      (middle/wrap-json-response)))

;; • POST /records - Post a single data line in any of the 3 formats supported by your existing code
;; • GET /records/gender - returns records sorted by gender
;; • GET /records/birthdate - returns records sorted by birthdate
;; • GET /records/name - returns records sorted by name

;; It's your choice how you render the output from these endpoints as long as it well structured data. These endpoints should return JSON.
