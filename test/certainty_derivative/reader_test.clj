(ns certainty-derivative.reader-test
  (:require  [certainty-derivative.reader :refer :all]
             [certainty-derivative.generator :refer [generate-test-data]]
             [clojure.spec.alpha :as s]
             [clojure.string :as string]
             [clojure.test :refer [deftest testing is]]
             [clojure.string :as string]))

(def example-first-name "Thomas")
(def example-last-name "Henderson")
(def example-favorite-color "purple")
(def example-gender "m")
(def example-date-of-birth "1976-03-23")

(def example-space-row (string/join " " [example-last-name
                                         example-first-name
                                         example-gender
                                         example-favorite-color
                                         example-date-of-birth]))

(def example-comma-row (string/join ", " [example-last-name
                                          example-first-name
                                          example-gender
                                          example-favorite-color
                                          example-date-of-birth]))

(def example-pipe-row (string/join " | " [example-last-name
                                          example-first-name
                                          example-gender
                                          example-favorite-color
                                          example-date-of-birth]))

(deftest test-format-classification
  (is (= :space (detect-delimiter example-space-row)))
  (is (= :pipe (detect-delimiter example-pipe-row)))
  (is (= :comma (detect-delimiter example-comma-row))))

(deftest test-row-parsing
  (is (s/valid? :certainty-derivative.input/row (parse-row example-space-row)))
  (is (s/valid? :certainty-derivative.input/row (parse-row example-comma-row)))
  (is (s/valid? :certainty-derivative.input/row (parse-row example-pipe-row))))

(deftest test-file-reading
  (do
    (generate-test-data 100)
    (is (= 100 (count (read-file "./resources/001.txt"))))
    (is (= 300 (count (read-files "./resources/001.txt"
                                  "./resources/002.txt"
                                  "./resources/003.txt"))))))

(deftest test-file-parsing
  (do
    (generate-test-data 100)
    (let [data (read-files "./resources/001.txt"
                           "./resources/002.txt"
                           "./resources/003.txt")]
      (is (every? #(s/valid? :certainty-derivative.input/row %) data)))))
