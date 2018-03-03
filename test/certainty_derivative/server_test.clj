(ns certainty-derivative.server-test
  (:require [certainty-derivative.server :refer :all]
            [clojure.set :refer [subset?]]
            [ring.mock.request :as mock]
            [clojure.test :refer [deftest testing is]]))

(deftest hello-testing
  (is (= (handler (mock/request :get "/"))
         {:status  200
          :headers {"Content-Type" "text/plain"}
          :body    "Hello world"})))

(def post-record-request (mock/request :post "/records"))
;; => Post a single data line in any of the 3 formats
(def get-gender-request (mock/request :get "/records/gender"))
;; => Return records sorted by gender
(def get-gender-request (mock/request :get "/records/birthdate"))
;; => Return records sorted by birthdate
(def get-gender-request (mock/request :get "/records/name"))
;; => Return records sorted by name

(defn submap? [m1 m2]
  (subset? (set m1) (set m2)))

(deftest test-endpoints-return-json
  (is (submap? {:status 200
                :headers {"Content-Type" "application/json"}}
               (handler get-gender-request))))

#_(deftest your-json-handler-test
    (is (= (your-handler (-> (mock/request :post "/api/endpoint")
                             (mock/json-body {:foo "bar"})))
           {:status  201
            :headers {"content-type" "application/json"}
            :body    {:key "your expected result"}})))
