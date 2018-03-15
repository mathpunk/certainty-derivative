(ns certainty-derivative.server-test
  (:require [certainty-derivative.loader.transform :as xform]
            [certainty-derivative.record.example
             :refer
             [example-pipe-row example-space-row]]
            [certainty-derivative.server :refer :all]
            [cheshire.core :as json]
            [clojure.string :as string]
            [clojure.test :refer [deftest is testing]]
            [ring.mock.request :as mock]))

(deftest test-there-is-a-landing-page
  (is (= 200 (get (app (mock/request :get "/")) :status))))

(def gender-request (mock/request :get "/records/gender"))
(def birthdate-request (mock/request :get "/records/birthdate"))
(def name-request (mock/request :get "/records/name"))

(deftest test-get-endpoints-status
  (is (= 200 (get (app gender-request) :status)))
  (is (= 200 (get (app birthdate-request) :status)))
  (is (= 200 (get (app gender-request) :status))))

(deftest test-get-endpoints-content-type
  (is (string/includes? (-> (app birthdate-request)
                            :headers
                            (get "Content-Type")) "application/json"))
  (is (string/includes? (-> (app gender-request)
                            :headers
                            (get "Content-Type")) "application/json"))
  (is (string/includes? (-> (app name-request)
                            :headers
                            (get "Content-Type")) "application/json")))

(deftest test-get-records-structure
  (let [body (-> (app name-request)
                 :body
                 (json/decode true))
        records (body :records)]
    (is (contains? body :description))
    (is (every? #(contains? % :last-name) records))
    (is (every? #(contains? % :first-name) records))
    (is (every? #(contains? % :favorite-color) records))
    (is (every? #(contains? % :gender) records))
    (is (every? #(contains? % :date-of-birth) records))))

(defn ascending?
  "Given a sequence of comparable things, returns true if the sequence is increasing. Not required to be monotonic, i.e., a run of logically equivalent items does not itself falsify this predicate."
  [sequence]
  (let [pairs (partition 2 1 sequence)
        comparisons (map (fn [pair] (apply compare pair)) pairs)]
    (every? #(>= 0 %) comparisons)))

(defn make-comparable
  "Given a date formatted as mm/dd/yyyy, return a representation that can be compared."
  [date-string]
  (vec (map #(Integer. %)
            (vector (last (string/split date-string #"/"))
                    (first (string/split date-string #"/"))
                    (second (string/split date-string #"/"))))))

(deftest test-get-records-order
  (testing "name ordering: last-name, descending"
    (let [records-served (-> (app name-request)
                             :body
                             (json/decode true)
                             :records)]
      (is (ascending? (reverse (map :last-name records-served))))))
  (testing "dob ordering: ascending"
    (let [records-served (-> (app birthdate-request)
                             :body
                             (json/decode true)
                             :records)
          date-strings (map :date-of-birth records-served)]
      (is (ascending? (map make-comparable date-strings)))))
  (testing "gender ordering: female first, otherwise ascending by last-name"
    (let [records-served (-> (app gender-request)
                             :body
                             (json/decode true)
                             :records)
          gender-partitions (partition-by #(= "f" (get % :gender)) records-served)]
      (is (every? #(ascending? (map :last-name %)) gender-partitions)))))

(deftest test-posting-records
  (let [new-record (xform/parse-row example-space-row)]
    (swap! records (fn [records] (remove #(= new-record %) records))) ;; In case record already exists, as in re-running tests. Mocking, sierra/component, or with-redefs might be preferable.
    (testing "record shouldn't exist before post"
      (is (nil? (some #{new-record} @records))))
    (testing "record should exist after post" 
      (app (-> (mock/request :post "/records")
               (mock/json-body example-space-row)))
      (is (some #{new-record} @records)))))


(deftest test-post-records-client-perspective
  (let [response (app (-> (mock/request :post "/records")
                          (mock/json-body example-pipe-row)))
        headers (response :headers)
        body (json/decode (response :body) true)
        parsed-record (body :record)]
    (is (string/includes? (get headers "Content-Type")  "application/json"))
    (is (= 200 (response :status)))
    ;; Note: The way I understand https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html, the status code would be 201 if (and only if) the record was assigned a URI. Since assigning such a URI was not a part of the requirements, I'm returning 200 instead. However, it seems friendly to return a parsed representation of the record in the response body.
    (is (contains? body :record))
    (is (= "Thomas" (parsed-record :first-name)))
    (is (= "Henderson" (parsed-record :last-name)))
    (is (= "m" (parsed-record :gender)))
    (is (= "purple" (parsed-record :favorite-color)))
    (is (= "03/23/1976" (parsed-record :date-of-birth)))))
