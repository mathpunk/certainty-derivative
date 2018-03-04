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
  (is (= 200 (get (app birthdate-request) :status)))
  (is (= 200 (get (app gender-request) :status)))
  #_(is (= 200 (get (app post-request) :status)))
  )

(deftest test-endpoints-content-type
  (is (string/includes? (-> (app birthdate-request)
                            :headers
                            (get "Content-Type")) "application/json"))
  (is (string/includes? (-> (app gender-request)
                            :headers
                            (get "Content-Type")) "application/json"))
  (is (string/includes? (-> (app name-request)
                            :headers
                            (get "Content-Type")) "application/json"))
  #_(is (string/includes? (-> (app post-request)
                              :headers
                              (get "Content-Type")) "application/json")) )
