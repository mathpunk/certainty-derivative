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

(defn add-record! [record]
  (swap! records conj record))

(def json-response
  {"gender" {:description "Females first, then sorted by last name"
             :records (map format/json-format (sort/by-gender-and-last-name @records))}
   "birthdate" {:description "Sorted by date of birth"
                :records (map format/json-format (sort/by-date-of-birth @records))}
   "name" {:description "Sorted by last name, descending"
           :records (map format/json-format (reverse (sort/by-last-name @records)))}})

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
  (POST "/records" req
        (let [input-record (req :body)
              parsed-record (xform/parse-row input-record)]
          (add-record! parsed-record)
          (res/response {:description "Success!"
                         :record (format/json-format parsed-record)}))))

(def app
  (-> (handler/api app-routes)
      (json/wrap-json-body {:keywords? true})
      (json/wrap-json-response)
      (params/wrap-params)))
