(ns certainty-derivative.server-test
  (:require [certainty-derivative.server :refer :all]
            [clojure.set :refer [subset?]]
            [ring.mock.request :as mock]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as string]))

(deftest hello-testing
  (is (= (app (mock/request :get "/"))
         {:status  200
          :headers {"Content-Type" "text/plain"}
          :body    "Hello world"})))

(def post-request (mock/request :post "/records"))
(def gender-request (mock/request :get "/records/gender"))
(def birthdate-request (mock/request :get "/records/birthdate"))
(def name-request (mock/request :get "/records/name"))

(deftest test-endpoints-status
  (is (= 200 (get (app gender-request) :status)))
  #_(is (= 200 (get (app birthdate-request) :status)))
  #_(is (= 200 (get (app gender-request) :status))))

(deftest test-endpoints-content-type
  (is (string/includes? (-> (app gender-request)
                            :headers
                            (get "Content-Type"))
                        "application/json")))

#_(deftest your-json-handler-test
    (is (= (your-handler (-> (mock/request :post "/api/endpoint")
                             (mock/json-body {:foo "bar"})))
           {:status  201
            :headers {"content-type" "application/json"}
            :body    {:key "your expected result"}})))
