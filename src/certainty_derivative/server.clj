(ns certainty-derivative.server
  (:require [certainty-derivative.generator :as gen]
            [certainty-derivative.loader.read :as read]
            [certainty-derivative.loader.transform :as xform]
            [certainty-derivative.viewer.format :as format]
            [certainty-derivative.viewer.sort :as sort]
            [hiccup.core :refer [ html ]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [ring.middleware.json :as json]
            [ring.middleware.params :as params]
            [ring.util.response :as res]))

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
  (html [:html
         [:title "Certainty Derivative"]
         [:body [:h2 "Routes available:"]
          [:ul
           [:li
            [:a {:href "/records/gender"} "/records/gender"] " ('f' first, ascending by last name)"]
           [:li
            [:a {:href "/records/birthdate"} "/records/birthdate"]]
           [:li
            [:a {:href "/records/name"} "/records/name"] " (descending)"]]]]))

(defroutes app-routes
  (GET "/favicon.ico" [] "")
  (GET "/" [] (-> landing-page
                  res/response
                  (res/content-type "text/html")))
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
