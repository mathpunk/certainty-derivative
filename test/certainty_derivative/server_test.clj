(ns certainty-derivative.server-test
  (:require [certainty-derivative.server :refer :all]
            [certainty-derivative.record.example :refer [example-comma-row
                                                         example-space-row
                                                         example-pipe-row]]
            [clojure.set :refer [subset?]]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [clojure.test :refer [deftest testing is]]
            [clojure.string :as string]))

(deftest test-there-is-a-landing-page
  (is (= 200 (get (app (mock/request :get "/")) :status))))

(def post-request (mock/request :post "/records"))
(def gender-request (mock/request :get "/records/gender"))
(def birthdate-request (mock/request :get "/records/birthdate"))
(def name-request (mock/request :get "/records/name"))

(deftest test-endpoints-status
  (is (= 200 (get (app gender-request) :status)))
  (is (= 200 (get (app birthdate-request) :status)))
  (is (= 200 (get (app gender-request) :status)))
  (is (= 200 (get (app post-request) :status))))

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
  (is (string/includes? (-> (app post-request)
                            :headers
                            (get "Content-Type")) "application/json")))

(deftest test-get-records
  (let [body (-> (app name-request)
                 :body
                 (json/decode true))
        records (body :records)]
    (testing "structure" 
      (is (contains? body :description ))
      (is (contains? body :records ))
      (is (every? #(= #{:last-name
                        :first-name
                        :gender
                        :favorite-color
                        :date-of-birth} (set (keys %))) records)))
    (testing "ordering"
      (let [actual-name-seq (map :last-name records)
            expected-name-seq (reverse (sort (map :last-name records)))]
        (is (= actual-name-seq expected-name-seq))))))

(deftest test-put-records-client-perspective
  (let [response (app (-> (mock/request :post "/records")
                          (mock/json-body example-pipe-row)))
        body (json/decode (response :body) true)]
    (= 200 (response :status))
    (= {"Content-Type" "application/json"} (response :headers))
    (is (contains? body :record))
    ))

