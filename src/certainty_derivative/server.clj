(ns certainty-derivative.server
  (:require [compojure.core :refer :all]
            [ring.util.response :as res]
            [compojure.route :as route]))

(defroutes app
  (GET "/" [] (-> "Hello world"
                  res/response
                  (res/content-type "text/plain"))))

;; • POST /records - Post a single data line in any of the 3 formats supported by your existing code
;; • GET /records/gender - returns records sorted by gender
;; • GET /records/birthdate - returns records sorted by birthdate
;; • GET /records/name - returns records sorted by name

;; It's your choice how you render the output from these endpoints as long as it well structured data. These endpoints should return JSON.
